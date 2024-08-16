package platformer;
import java.util.LinkedList;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MapHandler {
	public LinkedList<MapObject> map = new LinkedList<MapObject>();
	
	
	public void addMapObject(MapObject mapobject) {
		map.add(mapobject);
	}
	
	public void removeMapObject(MapObject mapobject) {
		map.remove(mapobject);
	}
	
	public void clearMap() {
		map.clear();
	}
	
	
	public void tick() {
		for (int i = 0; i < map.size(); i++) {
			map.get(i).tick();
		}
	}
	
	public void render(Graphics g, Graphics2D g2d) {
		for (int i = 0; i < map.size(); i++) {
			MapObject obj = map.get(i);
			if (obj.x + (int)Game.cameraX < 0 && obj.x + obj.xSize + (int)Game.cameraX + Window.windowX < 0) { 
				continue;
			}
			if (obj.x + (int)Game.cameraX > Window.windowX && obj.x + obj.xSize + (int)Game.cameraX > 0) {
				continue;
			}
			if (obj.y + (int)Game.cameraY < 0 && obj.y + obj.ySize + (int)Game.cameraY + Window.windowY < 0) {
				continue;
			}
			if (obj.y + (int)Game.cameraY > Window.windowY && obj.y + obj.ySize + (int)Game.cameraY > 0) {
				continue;
			}
			
			map.get(i).render(g, g2d);
			
			if (Game.drawHitboxes) {
				g.setColor(new Color(0, 0, 255));
				g2d.setStroke(new BasicStroke(5));
				g2d.draw(map.get(i).getHitbox());
			}
		}
	}
	
	public LinkedList<GroundObject> getGroundObjects() {
		LinkedList<GroundObject> output = new LinkedList<GroundObject>();
		for (int i = 0; i < map.size(); i++) {
			if (GroundObject.class.isAssignableFrom(map.get(i).getClass())) {
				output.add((GroundObject) map.get(i));
			}
		}
		
		return output;
	}
	
	public LinkedList<KillObject> getKillObjects() {
		LinkedList<KillObject> output = new LinkedList<KillObject>();

		for (int i = 0; i < map.size(); i++) {
			if (KillObject.class.isAssignableFrom(map.get(i).getClass())) {
				output.add((KillObject) map.get(i));
			}
		}
		
		//System.out.println(output.size());

		return output;

	}
	
	public boolean touchingGroundObject(Rectangle rectangle) {
		LinkedList<GroundObject> level = getGroundObjects();
		
		for (int i = 0; i < level.size(); i++) {
			if (level.get(i).getHitbox().intersects(rectangle)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean touchingKillObject(Rectangle rectangle) {
		LinkedList<KillObject> level = getKillObjects();
		
		for (int i = 0; i < level.size(); i++) {
			if (level.get(i).getHitbox().intersects(rectangle)) {
				return true;
			}
		}
		
		return false;

	}
	
	public GroundObject getGroundObjectTouching(Rectangle rectangle) {
		LinkedList<GroundObject> level = getGroundObjects();
		
		for (int i = 0; i < level.size(); i++) {
			if (level.get(i).getHitbox().intersects(rectangle)) {
				return level.get(i);
			}
		}
		
		return null;
	}
	
	public KillObject getKillObjectTouching(Rectangle rectangle) {
		LinkedList<KillObject> level = getKillObjects();
		
		for (int i = 0; i < level.size(); i++) {
			if (level.get(i).getHitbox().intersects(rectangle)) {
				return level.get(i);
			}
		}
		
		return null;

	}
	
	
	
}
