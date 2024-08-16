package platformer;

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

public abstract class MapObject {
	
	public int x, y, xSize, ySize;
	public Color color = new Color(210, 210, 210);
	public boolean customColor = false;
	
	public MapObject(int x, int y, int xSize, int ySize, Color color) {
		this.x = x;
		this.y = y;
		this.xSize = xSize;
		this.ySize = ySize;
		this.color = color;
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g, Graphics2D g2d) {
		g.setColor(color);
		g.fillRect(x, y, xSize, ySize);
	}
	
	public Rectangle getHitbox() {
		return new Rectangle(x, y, xSize, ySize);
	}
	
	public String getName() {
		return "null";
	}
}
