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


public class SignObject extends MapObject {

	public String message;
	public Color defaultColor = new Color(210, 210, 210);
	private int alpha;
	
	public SignObject(int x, int y, String message) {
		super(x, y, 75, 85, new Color(210, 210, 210));
		this.message = message;
		customColor = false;
	}
	
	
	@Override
	public void tick() {
		if (distanceToPlayer() < 120) {
			alpha += 10;
		} else {
			alpha -= 10;
		}
		alpha = Math.min(255, Math.max(0, alpha));
	}
	
	@Override
	public void render(Graphics g, Graphics2D g2d) {
		g.setColor(new Color(199, 127, 32));
		
		int supportWidth = (int) (0.2 * xSize);
		
		g.fillRect(x, y, xSize, (int)(ySize/1.65) + 1);
		g.fillRect(x + xSize/2 - supportWidth/2, y + ySize/2, supportWidth, ySize/2 + 1);

		g.setColor(new Color(105, 66, 17));
		
		g.fillRect(x + 7, y + 11, xSize - 14, 10);
		g.fillRect(x + 7, y + 29, xSize - 14, 10);

		g.setFont(new Font("Arial", Font.PLAIN, 32));
		
		
		g.setColor(new Color(0, 0, 0, alpha));
		g.drawString(message, x + xSize/2 - g.getFontMetrics().stringWidth(message)/2 - 2, y - 42);
		g.drawString(message, x + xSize/2 - g.getFontMetrics().stringWidth(message)/2, y - 42 - 2);
		
		g.setColor(new Color(255, 255, 255, alpha));
		g.drawString(message, x + xSize/2 - g.getFontMetrics().stringWidth(message)/2 + 1, y - 42);
		g.drawString(message, x + xSize/2 - g.getFontMetrics().stringWidth(message)/2, y - 42 + 1);

		g.setColor(new Color(255, 255, 255, alpha));
		g.drawString(message, x + xSize/2 - g.getFontMetrics().stringWidth(message)/2, y - 42);
	}

	public double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	}
	public double distanceToPlayer() {
		return distance(x + 37, y + 43, Game.player.x + 35, Game.player.y + 35);
	}

	@Override
	public String getName() {
		return "sign" + ":" + message;
	}

}
