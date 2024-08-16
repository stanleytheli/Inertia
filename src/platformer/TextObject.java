package platformer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class TextObject extends MapObject {

	public String message;
	public Color defaultColor = new Color(210, 210, 210);
	private int alpha;
	
	public TextObject(int x, int y, String message) {
		super(x, y, 0, 0, new Color(210, 210, 210));
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

		g.setFont(new Font("Arial", Font.PLAIN, 32));
		
		g.setColor(new Color(0, 0, 0, alpha));
		g.drawString(message, x + xSize/2 - g.getFontMetrics().stringWidth(message)/2 - 2, y);
		g.drawString(message, x + xSize/2 - g.getFontMetrics().stringWidth(message)/2, y - 2);
		
		g.setColor(new Color(255, 255, 255, alpha));
		g.drawString(message, x + xSize/2 - g.getFontMetrics().stringWidth(message)/2 + 1, y);
		g.drawString(message, x + xSize/2 - g.getFontMetrics().stringWidth(message)/2, y + 1);

		g.setColor(new Color(255, 255, 255, alpha));
		g.drawString(message, x + xSize/2 - g.getFontMetrics().stringWidth(message)/2, y );
	}

	public double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	}
	public double distanceToPlayer() {
		return distance(x, y, Game.player.x + 35, Game.player.y + 35);
	}

	@Override
	public String getName() {
		return "text" + ":" + message;
	}
}
