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
import java.util.LinkedList;

public abstract class WindowComponent {
	
	public InGameWindow parent;
	public boolean shown, active = false;
	
	public int x, y, xSize, ySize;
	
	public WindowComponent(int x, int y, int xSize, int ySize, InGameWindow parent) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.xSize = xSize;
		this.ySize = ySize;
	}
	
	public abstract void tick();
	public abstract void draw(Graphics g, Graphics2D g2d);
	
	public void render(Graphics g, Graphics2D g2d) {
		if (shown) {
			draw(g, g2d);
		}
	}
	
	public void show() {
		shown = true;
	}
	public void hide() {
		shown = false;
	}
	
	public void activate() {
		active = true;
	}
	public void deactivate() {
		active = false;
	}
	

}
