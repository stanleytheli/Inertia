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

public class WindowButton extends WindowComponent {

	public Color color = new Color(145, 145, 145);
	public Color color2 = new Color (87, 87, 87);
	
	public Color hoverColor = new Color(165, 165, 165);

	public Color displayColor = color;
	
	public int colorChangeProgress = 0;
	public double colorChangeSpeed = 15; //frames it takes to change color
	
	private int rChange = (int)Math.round((double)(hoverColor.getRed() - color.getRed())/colorChangeSpeed);
	private int gChange = (int)Math.round((double)(hoverColor.getGreen() - color.getGreen())/colorChangeSpeed);
	private int bChange = (int)Math.round((double)(hoverColor.getBlue() - color.getBlue())/colorChangeSpeed);
	
	public boolean border = true;
	
	public String text;
	
	public WindowActionListener actionlistener;

	public WindowButton(int x, int y, int xSize, int ySize, String text, InGameWindow parent) {
		super(x, y, xSize, ySize, parent);
		this.text = text;
	}

	public WindowButton(int x, int y, int xSize, int ySize, String text, InGameWindow parent, Color color, Color color2, Color hovercolor) {
		super(x, y, xSize, ySize, parent);
		
		this.color = color;
		this.color2 = color2;
		this.hoverColor = hovercolor;
		this.text = text;
		
		rChange = (int)Math.round((double)(hoverColor.getRed() - color.getRed())/colorChangeSpeed);
		gChange = (int)Math.round((double)(hoverColor.getGreen() - color.getGreen())/colorChangeSpeed);
		bChange = (int)Math.round((double)(hoverColor.getBlue() - color.getBlue())/colorChangeSpeed);
	}

	public WindowButton(int x, int y, int xSize, int ySize, String text, InGameWindow parent, boolean border) {
		super(x, y, xSize, ySize, parent);
		this.text = text;
		this.border = border;
	}
	public WindowButton(int x, int y, int xSize, int ySize, String text, InGameWindow parent, Color color, Color color2, Color hovercolor, boolean border) {
		super(x, y, xSize, ySize, parent);
		
		this.color = color;
		this.color2 = color2;
		this.hoverColor = hovercolor;
		this.text = text;
		this.border = border;
		
		rChange = (int)Math.round((double)(hoverColor.getRed() - color.getRed())/colorChangeSpeed);
		gChange = (int)Math.round((double)(hoverColor.getGreen() - color.getGreen())/colorChangeSpeed);
		bChange = (int)Math.round((double)(hoverColor.getBlue() - color.getBlue())/colorChangeSpeed);
	}

	@Override
	public void tick() {
		
		int X = 500;
		int Y = 500;
		
		if (shown) {
			if (Game.mouseX + X > x && Game.mouseX + X < x + xSize && Game.mouseY + Y > y && Game.mouseY + Y < y + ySize) {
				if (Game.mouseDown && Game.newClick && active) {
					actionlistener.actionOccured(this);
				}
				
				if (colorChangeProgress < colorChangeSpeed) {
					displayColor = new Color(displayColor.getRed() + rChange, displayColor.getGreen() + gChange, displayColor.getBlue() + bChange);
					colorChangeProgress++;
				} else {
					displayColor = hoverColor;
				}
	
			} else {
				
				if (colorChangeProgress > 0) {
					displayColor = new Color(displayColor.getRed() - rChange, displayColor.getGreen() - gChange, displayColor.getBlue() - bChange);
					colorChangeProgress--;
				} else {
					displayColor = color;
					colorChangeProgress = 0;
				}
			}
		}
	}

	@Override
	public void draw(Graphics g, Graphics2D g2d) {
	
		g.setColor(displayColor);
		g.fillRect(x, y, xSize, ySize);
		
		g.setColor(color2);
		
		/*
		g2d.setStroke(new BasicStroke(5));
		g.drawRect(x, y, xSize, ySize);
		*/
				
		int X = 500;
		int Y = 500;
		
		if (Game.mouseX + X > x && Game.mouseX + X < x + xSize && Game.mouseY + Y > y && Game.mouseY + Y < y + ySize && Game.mouseDown) {
			g.drawRect(x+2, y+2, xSize-4, ySize-4);
		} else {
			g.drawRect(x, y, xSize, ySize);
		}
		
		if (border) {
			if (Game.mouseX + X > x && Game.mouseX + X < x + xSize && Game.mouseY + Y > y && Game.mouseY + Y < y + ySize) {
				g.setColor(Color.white);
				
				if (Game.mouseDown) {
					g.drawRect(x-3, y-3, xSize+6, ySize+6);
				} else {
					g.drawRect(x-5, y-5, xSize+10, ySize+10);
				}
			}
		}
		
		g.setColor(new Color(255, 255, 255));
		g.setFont(new Font("Arial", Font.PLAIN, 32));
		
		
		g.drawString(text, (x + xSize/2) - g.getFontMetrics().stringWidth(text)/2, (y + ySize/2) + 12);

		g.drawString(text, (x + xSize/2) - g.getFontMetrics().stringWidth(text)/2 + 1, (y + ySize/2) + 12);
		g.drawString(text, (x + xSize/2) - g.getFontMetrics().stringWidth(text)/2 - 1, (y + ySize/2) + 12);
		g.drawString(text, (x + xSize/2) - g.getFontMetrics().stringWidth(text)/2, (y + ySize/2) + 12 + 1);
		g.drawString(text, (x + xSize/2) - g.getFontMetrics().stringWidth(text)/2, (y + ySize/2) + 12 - 1);

	}
	
	public void setActionListener(WindowActionListener actionListener) {
		actionlistener = actionListener;
	}
	
}
