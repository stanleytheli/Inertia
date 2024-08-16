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

public class BasicEnemy extends Enemy {
	
	public int jumpTime = -1; //time between jumps, -1 = no jumping

	public int jumpTimer = (int)(Math.random() * (double)jumpTime);
	
	public int switchMovementTimer = 0;
	public int switchMovementTime = 60; //time between switching movement directions
	
	public MapHandler maphandler = Game.maphandler;

	
	public boolean movementDirection = false;
	
	
	public BasicEnemy(int x, int y) {
		super(x, y);
		
	}

	public BasicEnemy(int x, int y, int xSize, int ySize, Color color) {
		super(x, y, xSize, ySize, color);
	}

	@Override
	public void behavior() {
		if (movementDirection) {
			
			if (maphandler.touchingGroundObject(new Rectangle(x + xSize, y + ySize, xSize, 150)) && !maphandler.touchingKillObject(new Rectangle(x, y + ySize, xSize, 150))) { //detect if there's ground in front
				xVelocity += 1; //if there is, keep going
			} else {
				xVelocity = 0; //if not, stop,
				
			}
		} else {
			
			if (maphandler.touchingGroundObject(new Rectangle(x - xSize, y + ySize, xSize, 150)) && !maphandler.touchingKillObject(new Rectangle(x, y + ySize, xSize, 150))) {
				xVelocity -= 1;
			} else {
				xVelocity = 0;
				
			}
		}
		if (midairTime < 2) {
			jumpTimer++;
			
			if (jumpTimer > jumpTime && jumpTime != -1) {
				yVelocity = -15;
				jumpTimer = 0;
			}
		}
				
	}
	
	public void postPhysicsBehavior() {
		if (Math.abs(xVelocity) < 1) {
			switchMovementTimer++;
			
			if (switchMovementTimer > switchMovementTime) {
				switchMovementTimer = 0;
				movementDirection = !movementDirection;
			}

		} else {
			switchMovementTimer = 0;
		}
	}

	@Override
	public String getName() {
		return "basicEnemy";
	}
	
	@Override
	public void render(Graphics g, Graphics2D g2d) {
		g.setColor(color);
		g.fillRect(x, y, xSize, ySize);
		
		if (movementDirection) {

			g2d.rotate(0.7, x + 60, y + 40);
			
			g.setColor(new Color(0, 0, 0));
			g.fillRect(x + 48, y + 45, 29, 10);
			g.fillRect(x + 55, y + 45, 15, 25);
			g.setColor(new Color(150, 150, 150));
			g.fillPolygon(
					new int[] {x + 55, x + 70, x + 70, x + 55},
					new int[] {y + 45, y + 45, y + 18, y + 12},
					4);
			
			g2d.rotate(-0.7, x + 60, y + 40);

		} else {
			//for x: 70 - x_original - width

			g2d.rotate(-0.7, x + 10, y + 40);

			g.setColor(new Color(0, 0, 0));
			g.fillRect(x - 7, y + 45, 29, 10);
			g.fillRect(x, y + 45, 15, 25);
			g.setColor(new Color(150, 150, 150));
			g.fillPolygon(
					new int[] {x + 15, x, x, x + 15},
					new int[] {y + 45, y + 45, y + 18, y + 12},
					4);
		
			g2d.rotate(0.7, x + 10, y + 40);

		}
	}



}
