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

public class WindowBooleanButton extends WindowButton {

	public boolean value = false;
	
	public WindowBooleanButton(int x, int y, int xSize, int ySize, String text, InGameWindow parent) {
		super(x, y, xSize, ySize, text, parent);
	}
	
	public WindowBooleanButton(int x, int y, int xSize, int ySize, String text, InGameWindow parent, Color color, Color color2, Color color3) {
		super(x, y, xSize, ySize, text, parent, color, color2, color3);
	}

	public void setValue(boolean newValue) {
		value = newValue;
	}
	public boolean getValue() {
		return value;
	}
	
	@Override
	public void draw(Graphics g, Graphics2D g2d) {
		
		int X = 500;
		int Y = 500;
	
		g.setColor(displayColor);
		g.fillRect(x, y, xSize, ySize);
		
		g.setColor(color2);
		g2d.setStroke(new BasicStroke(5));
		
		if (Game.mouseX + X > x && Game.mouseX + X < x + xSize && Game.mouseY + Y > y && Game.mouseY + Y < y + ySize && Game.mouseDown) {
			g.drawRect(x+2, y+2, xSize-4, ySize-4);
		} else {
			g.drawRect(x, y, xSize, ySize);
		}
		
		if (Game.mouseX + X > x && Game.mouseX + X < x + xSize && Game.mouseY + Y > y && Game.mouseY + Y < y + ySize) {
			g.setColor(Color.white);
			
			if (Game.mouseDown) {
				g.drawRect(x-3, y-3, xSize+6, ySize+6);
			} else {
				g.drawRect(x-5, y-5, xSize+10, ySize+10);
			}
		}
		

		g.setColor(new Color(255, 255, 255));
		g.setFont(new Font("Arial", Font.PLAIN, 32));
		
		int stringWidth = g.getFontMetrics().stringWidth(text);
		
		if (value) {
			g.fillRect((x + xSize/2) - stringWidth/2 + stringWidth - 12, (y + ySize/2) - 16, 32, 32);
		}
		g.drawRect((x + xSize/2) - stringWidth/2 + stringWidth - 12, (y + ySize/2) - 16, 32, 32);
		
		
		g.drawString(text, (x + xSize/2) - (stringWidth + 15 + 32)/2, (y + ySize/2) + 12);
		
		g.drawString(text, (x + xSize/2) - (stringWidth + 15 + 32)/2 + 1, (y + ySize/2) + 12);
		g.drawString(text, (x + xSize/2) - (stringWidth + 15 + 32)/2 - 1, (y + ySize/2) + 12);
		g.drawString(text, (x + xSize/2) - (stringWidth + 15 + 32)/2, (y + ySize/2) + 12 + 1);
		g.drawString(text, (x + xSize/2) - (stringWidth + 15 + 32)/2, (y + ySize/2) + 12 - 1);
 		
	}


}
