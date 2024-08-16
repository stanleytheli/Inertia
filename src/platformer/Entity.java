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

public abstract class Entity {

	public int x, y;
	
	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g, Graphics2D g2d);
	
	public boolean hasLineOfSight(double startX, double startY, double endX, double endY) {
		
		double currentX = startX;
		double currentY = startY;
		
		double precision = 4;
		double totalDistance = Math.sqrt(((startX-endX)*(startX-endX))+((startY-endY)*(startY-endY)));
		
		double rayCastSteps = Math.round(totalDistance/precision);
		
		double xAdd = (endX-startX)/rayCastSteps;
		double yAdd = (endY-startY)/rayCastSteps;
		
		for (int i = 0; i < rayCastSteps; i++) {
			currentX += xAdd;
			currentY += yAdd;

			if (Game.maphandler.touchingGroundObject(new Rectangle((int)currentX, (int)currentY, 1, 1))) {
				return false;
			}
		}
		
		return true;
	}

	public double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	}
	
	public double magnitude(double x, double y) {
		return Math.sqrt(x * x + y * y);
	}
	
	public boolean canSeePlayer() {
		return hasLineOfSight(x + 35, y + 35, Game.player.x + 35, Game.player.y + 35);
		/*return hasLineOfSight(x, y, Game.player.x, Game.player.y) ||
				hasLineOfSight(x + 70, y, Game.player.x + 70, Game.player.y) ||
				hasLineOfSight(x, y + 70, Game.player.x, Game.player.y + 70) ||
				hasLineOfSight(x + 70, y + 70, Game.player.x + 70, Game.player.y + 70);*/
	}
	
	public double angleToPlayer() {
		return (Game.player.x < x ? Math.PI : 0) + Math.atan(((double)Game.player.y - (double)y)/((double)Game.player.x - (double)x));
	}
	
	public double distanceToPlayer() {
		return distance(x, y, Game.player.x, Game.player.y);
	}

	public String getName() {
		return "null";
	}
	
	public static int randomint(int min, int max) {
		return (int)(Math.random() * (max - min + 1) + min);
	}
	
	public static int max(int a, int b) {
		return (a > b) ? a : b;
	}
	public static double max(double a, double b) {
		return (a > b) ? a : b;
	}
	
	public static Color invertColor(Color i) {
		return new Color(255 - i.getRed(), 255 - i.getGreen(), 255 - i.getBlue(), i.getAlpha());
	}
	
	public static int min(int a, int b) {
		return (a < b) ? a : b;
	}
	public static double min(double a, double b) {
		return (a < b) ? a : b;
	}
}
