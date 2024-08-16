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

public class DoorObject extends MapObject {
	
	public Color closedColor = new Color(0, 0, 0);
	public Color openColor = new Color(55, 255, 55); //55, 255, 55

	public int colorChangeProgress = 0;
	public double colorChangeSpeed = 25; //frames it takes to change color
	
	private int rChange = (int)((double)(openColor.getRed() - closedColor.getRed())/colorChangeSpeed);
	private int gChange = (int)((double)(openColor.getGreen() - closedColor.getGreen())/colorChangeSpeed);
	private int bChange = (int)((double)(openColor.getBlue() - closedColor.getBlue())/colorChangeSpeed);

	public DoorObject(int x, int y) {
		super(x, y, 125, 225, new Color(0, 0, 0));
		customColor = false;
	}
	
	public DoorObject(int x, int y, int xSize, int ySize) { //xSize and ySize parameters don't matter here
		super(x, y, 125, 225, new Color(0, 0, 0));
		customColor = false;
	}


	@Override
	public void tick() {
		
		if (Game.enemiesLeft == 0) {
			if (colorChangeProgress < colorChangeSpeed) {
				color = new Color(color.getRed() + rChange, color.getGreen() + gChange, color.getBlue() + bChange);
				colorChangeProgress++;
			} else {
				color = openColor;
			}
		} else {
			color = closedColor;
		}
		
	}
	
	@Override
	public void render(Graphics g, Graphics2D g2d) {
		g.setColor(color);
		g.fillRect(x, y, xSize, ySize);
		
		if (Game.enemiesLeft > 0) {
			/*g.setColor(new Color(255, 55, 55));
			g.setFont(new Font("Arial", Font.BOLD, 100));
			g.drawString(String.valueOf(Game.enemiesLeft), (x + xSize/2) - (g.getFontMetrics().stringWidth(String.valueOf(Game.enemiesLeft))/2), (y + ySize/2) + 40);*/
			
			g.setColor(new Color(255, 55, 55));
			g.fillRect(x + xSize/2 - 40, y + ySize/2 - 25 + 20, 80, 50);
			g2d.setStroke(new BasicStroke(15));
			g.drawOval(x + xSize/2 - 25, y + ySize/2 - 30, 50, 50);
			
			g.setColor(new Color(0, 0, 0));
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString(String.valueOf(Game.enemiesLeft), (x + xSize/2) - (g.getFontMetrics().stringWidth(String.valueOf(Game.enemiesLeft))/2), (y + ySize/2) + 34);
		}
	}
	
	@Override
	public String getName() {
		return "door";
	}
	
}
