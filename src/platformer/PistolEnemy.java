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

public class PistolEnemy extends Enemy {
	
	public int jumpTime = -1; //-1 = no jumping
	public int jumpTimer = (int)(Math.random() * (double)jumpTime);
	
	public int shotTime = 120;
	public int shotTimer = (int)(0.5 * (double)shotTime);
	
	public int range = 750;
	
	public double gunAngleOffset = 0;
	public double gunRecoilOffset = 0;
	
	public double angleToPlayer = 0;
	
	public Player player = Game.player;
	public MapHandler map = Game.maphandler;
	
	public PistolEnemy(int x, int y) {
		super(x, y);
	}
	
	public PistolEnemy(int x, int y, int shotTime) {
		super(x, y);
		this.shotTime = shotTime;
		shotTimer = (int)(0.5 * (double)shotTime);
	}

	@Override
	public void behavior() {
		gunRecoilOffset *= 0.95;
		if (gunRecoilOffset < 1) {
			gunRecoilOffset = 0;
		}
		
		if (midairTime < 2) {
			jumpTimer++;
			
			if (jumpTimer > jumpTime && jumpTime != -1) {
				yVelocity = -15;
				jumpTimer = 0;
			}
		}

		
		angleToPlayer = angleToPlayer();
		
		if (distanceToPlayer() < range && canSeePlayer()) {
			shotTimer++;
			
			if (shotTimer > shotTime - (Math.round((2*Math.PI) / 0.3)+10) && shotTimer < shotTime - 10) {
				gunAngleOffset += 0.3;
			} else {
				gunAngleOffset = 0;
			}

			if (shotTimer > shotTime) {
				shoot();
				
				shotTimer = 0;
			}
		} else {
			if (Game.gameTicks % 3 == 0) {
				shotTimer--;
			}
			
			if (shotTimer < (int)(0.5 * (double)shotTime)) {
				shotTimer = (int)(0.5 * (double)shotTime);
			}
		}
	}

	@Override
	public void postPhysicsBehavior() {
		
	}
	
	@Override
	public void render(Graphics g, Graphics2D g2d) {
		g.setColor(color);
		g.fillRect(x, y, xSize, ySize);
		
		if (canSeePlayer() && distanceToPlayer() < range) {
			if (player.x > x) {
				g2d.rotate(angleToPlayer + gunAngleOffset, x + 35, y + 40);

				g.setColor(new Color(0, 0, 0));
				g.fillRect(x + 35 - (int)gunRecoilOffset, y + 35, 60, 15);
				g2d.setStroke(new BasicStroke(15));
				g.drawLine(x + 37 - (int)gunRecoilOffset, y + 45, x + 27 - (int)gunRecoilOffset, y + 65);
				
				g2d.rotate(-(angleToPlayer + gunAngleOffset), x + 35, y + 40);

			
			} else {
				g2d.rotate(angleToPlayer - Math.PI - gunAngleOffset, x + 35, y + 40);
				
				g.setColor(new Color(0, 0, 0));
				g.fillRect(x - 22 + (int)gunRecoilOffset, y + 35, 60, 15);
				g2d.setStroke(new BasicStroke(15));
				g.drawLine(x + 35 + (int)gunRecoilOffset, y + 45, x + 45 + (int)gunRecoilOffset, y + 65);
				
				g2d.rotate(-(angleToPlayer - Math.PI - gunAngleOffset), x + 35, y + 40);
			}
		} else {
			g2d.rotate(Math.PI/2, x + 35, y + 40);
			
			g.setColor(new Color(0, 0, 0));
			g.fillRect(x - 22, y + 15, 60, 15);
			g2d.setStroke(new BasicStroke(15));
			g.drawLine(x + 35, y + 25, x + 45, y + 45);
			
			g2d.rotate(-Math.PI/2, x + 35, y + 40);
		}
	}
	
	public void shoot() {
		int xCoordinate = (int)(x + 35 + (90 * Math.cos(angleToPlayer)));
		int yCoordinate = (int)(y + 40 + (90 * Math.sin(angleToPlayer)));
		gunRecoilOffset = 30;

		Game.entityhandler.addEntity(new EnemyBullet(xCoordinate, yCoordinate, angleToPlayer));

	}
	
	@Override
	public String getName() {
		return "pistolEnemy";
	}

}
