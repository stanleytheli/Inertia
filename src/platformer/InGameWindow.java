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

public class InGameWindow {
	
	public LinkedList<WindowComponent> components = new LinkedList<WindowComponent>();
	
	public Color color = new Color(145, 145, 145);
	public Color color2 = new Color (87, 87, 87);
	public Color textColor = new Color(255, 255, 255);
	
	public int x, y, xSize, ySize, displayX, displayY, backgroundAlpha;
	public boolean shown, centered = true;
	public String title;
	
	public WindowButton closebutton;
	
	public InGameWindow(int x, int y, int xSize, int ySize, String title) {
		this.x = x;
		this.y = y;
		this.xSize = xSize;
		this.ySize = ySize;
		this.title = title;
		
		displayX = x;
		displayY = 0 - ySize;
		
		closebutton = new WindowButton(x + xSize - 52, y + 3, 50, 40, "X", this, color, new Color(0, 0, 0, 0), new Color(255, 55, 55), false);
		
		closebutton.setActionListener(new WindowActionListener() {

			@Override
			public void actionOccured(WindowComponent source) {
				source.parent.shown = false;
			}
			
		});
		components.add(closebutton);
		
	}
	
	public void tick() {
		
		for (int i = 0; i < components.size(); i++) {
			components.get(i).tick();
		}

		
	}
	
	public void render(Graphics g, Graphics2D g2d) {
		
		g.setColor(new Color(0, 0, 0, backgroundAlpha));
		g.fillRect(0, 0, Window.windowX, Window.windowY);
		
		if (displayY <= 0 - ySize) {
			return;
		}
		
		
		if (centered) {
			g.translate(Window.windowX/2 - 500, Window.windowY/2 - 500);
		}

		g.setColor(color);
		g.fillRect(displayX, displayY, xSize, ySize);
		
		g2d.setStroke(new BasicStroke(5));
		g.setColor(color2);
		g.drawRect(displayX, displayY, xSize, ySize);
		
		g.fillRect(displayX, displayY, xSize, 50);
		
		g.setColor(textColor);
		g.setFont(new Font("Arial", Font.PLAIN, 40));
		g.drawString(title, displayX+5, displayY+37);
		
		g.drawString(title, displayX+6, displayY+37);
		g.drawString(title, displayX+4, displayY+37);
		g.drawString(title, displayX+5, displayY+38);
		g.drawString(title, displayX+5, displayY+36);
		
		g.translate(displayX - x, displayY - y);
		for (int i = 0; i < components.size(); i++) {
			components.get(i).shown = true;
			components.get(i).active = true;
			components.get(i).render(g, g2d);
		}
		g.translate(- (displayX - x), - (displayY - y));
		
		if (centered) {
			g.translate(-(Window.windowX/2 - 500), -(Window.windowY/2 - 500));
		}

	}
	
	
	public void addComponent(WindowComponent component) {
		components.add(component);
	}
	
	
}
