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


public class ShotgunEnemy extends PistolEnemy {
	
	public double spread = 0.3;
	
	public ShotgunEnemy(int x, int y) {
		super(x, y, 150);
	}
	
	@Override
	public void shoot() {
		
		for (double i = -spread; i <= spread; i += spread) {
			int xCoordinate = (int)(x + 35 + (110 * Math.cos(angleToPlayer + i/2)));
			int yCoordinate = (int)(y + 40 + (110 * Math.sin(angleToPlayer + i/2)));
			
			Game.entityhandler.addEntity(new EnemyBullet(xCoordinate, yCoordinate, angleToPlayer + i));


		}
		
		gunRecoilOffset = 50;
		
		//Game.entityhandler.addEntity(new EnemyBullet(xCoordinate, yCoordinate, angleToPlayer + 0.3));
		//Game.entityhandler.addEntity(new EnemyBullet(xCoordinate, yCoordinate, angleToPlayer - 0.3));

	}

	@Override
	public void render(Graphics g, Graphics2D g2d) {
		g.setColor(color);
		g.fillRect(x, y, xSize, ySize);
		
		if (distanceToPlayer() < range && canSeePlayer()) {
			g2d.rotate(angleToPlayer, x+35, y+47);

			if (player.x > x) {
				
				g2d.rotate(gunAngleOffset, x+35, y+47);
				
				g.setColor(new Color(90, 50, 0));
				
				g.fillRect((int)(x + 20 - (int)gunRecoilOffset), (int)(y) + 40, 100, 15);
				g.fillRect((int)(x - 5 - (int)gunRecoilOffset), (int)(y) + 45, 40, 20);
				
				g.setColor(new Color(117, 66, 0));
				
				g.fillRect((int)(x + 45 - (int)gunRecoilOffset), (int)(y) + 48, 50, 12);
				
				g2d.rotate(-gunAngleOffset, x+35, y+47);

				
			} else {
				
				g2d.rotate(-(gunAngleOffset + Math.PI), x+35, y+47);
				
				g.setColor(new Color(90, 50, 0));
				
				g.fillRect((int)(x - 50 + (int)gunRecoilOffset), (int)(y) + 40, 100, 15);
				g.fillRect((int)(x + 35 + (int)gunRecoilOffset), (int)(y) + 45, 40, 20);
				
				g.setColor(new Color(117, 66, 0));
				
				g.fillRect((int)(x - 25 + (int)gunRecoilOffset), (int)(y) + 48, 50, 12);
				
				g2d.rotate((gunAngleOffset + Math.PI), x+35, y+47);


			}

			g2d.rotate(-(angleToPlayer), x+35, y+47);

		} else {
			g2d.rotate(Math.PI/2, x+35, y+47);
			
			g.setColor(new Color(90, 50, 0));
			
			g.fillRect((int)(x - 50), (int)(y) + 20, 100, 15);
			g.fillRect((int)(x + 35), (int)(y) + 25, 40, 20);
			
			g.setColor(new Color(117, 66, 0));
			
			g.fillRect((int)(x - 25), (int)(y) + 28, 50, 12);
			
			g2d.rotate(-Math.PI/2, x+35, y+47);

		}
	}

	@Override
	public String getName() {
		return "shotgunEnemy";
	}


}
