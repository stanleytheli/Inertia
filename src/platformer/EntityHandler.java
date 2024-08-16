package platformer;

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
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class EntityHandler {
	public List<Entity> entities = new LinkedList<Entity>();
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}
	
	public void clearEntities() {
		entities.clear();
	}
	
	public void tick() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).tick();
		}
		
	}
	
	public void render(Graphics g, Graphics2D g2d) {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(g, g2d);
			
			if (Game.drawHitboxes) {
				if (Enemy.class.isAssignableFrom(entities.get(i).getClass())) {
					g.setColor(new Color(0, 0, 255));
					g2d.setStroke(new BasicStroke(5));
					g2d.draw(((Enemy)entities.get(i)).getHitbox());
				} else if (Player.class.isAssignableFrom(entities.get(i).getClass())) {
					g.setColor(new Color(0, 0, 255));
					g2d.setStroke(new BasicStroke(5));
					g2d.draw(((Player)entities.get(i)).getHitbox());
				} else if (GrapplingHook.class.isAssignableFrom(entities.get(i).getClass())) {
					g.setColor(new Color(0, 0, 255));
					g2d.setStroke(new BasicStroke(5));
					g2d.draw(((GrapplingHook)entities.get(i)).getHitbox());
				}
			}
		}
	}
	
	public List<Enemy> getEnemies() {
		
		List<Enemy> output = new LinkedList<Enemy>();
		
		for (int i = 0; i < entities.size(); i++) {
			if (Enemy.class.isAssignableFrom(entities.get(i).getClass())) {
				output.add((Enemy)entities.get(i));
			}
			
		}
		
		return output;
		
	}
	
	public boolean touchingEnemy(Rectangle rectangle) {
		List<Enemy> enemies = getEnemies();
		
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getHitbox().intersects(rectangle)) {
				return true;
			}
		}
		
		return false;

	}
	
	public boolean touchingCountedEnemy(Rectangle rectangle) {
		
		List<Enemy> enemies = getEnemies();
		
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getEnemyValue() != 0 && enemies.get(i).getHitbox().intersects(rectangle)) {
				return true;
			}
		}
		
		return false;

	}
	
	public Enemy getCountedEnemyTouching(Rectangle rectangle) {
		List<Enemy> enemies = getEnemies();
		
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getEnemyValue() != 0 && enemies.get(i).getHitbox().intersects(rectangle)) {
				return enemies.get(i);
			}
		}
		
		return null;
	}
	
	public List<Enemy> getEnemiesTouching(Rectangle rectangle) {
		List<Enemy> output = new LinkedList<Enemy>();
		List<Enemy> enemies = getEnemies();
		
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getHitbox().intersects(rectangle)) {
				output.add(enemies.get(i));
			}
		}
		
		return output;
	}
	
	public int countAliveEnemies() {
		
		List<Enemy> enemies = getEnemies();
		int output = 0;
		
		for (int i = 0; i < enemies.size(); i++) {
			output += enemies.get(i).getEnemyValue();
		}
		
		return output;
	}
	
}
