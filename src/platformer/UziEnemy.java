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

public class UziEnemy extends PistolEnemy {

	public int magSize = 3;
	public int shotsUntilReload = magSize;
	
	public int shotTimer = 0;
	public int shotTime = 30;
	
	public int reloadTimer = 60;
	public int reloadTime = 120;
	
	public UziEnemy(int x, int y) {
		super(x, y);
	}
	
	@Override
	public void render(Graphics g, Graphics2D g2d) {
		
		g.setColor(color);
		g.fillRect(x, y, xSize, ySize);
		
		//for x: subtract 35, *-1, add 35, subtract width

		if (distanceToPlayer() < range && canSeePlayer()) {

			if (player.x > x) {
				g2d.rotate(angleToPlayer + gunAngleOffset, x+60, y+35);
				
				g.setColor(Color.black);
				g.fillRect(x + 35 - (int)gunRecoilOffset, y + 25, 50, 20);
				g.fillRect(x + 15 - (int)gunRecoilOffset, y + 29, 90, 12);
				g.fillRect(x + 15 - (int)gunRecoilOffset, y + 29, 10, 35);
				g.fillRect(x + 53 - (int)gunRecoilOffset, y + 25, 15, 60);
				
				g2d.rotate(-(gunAngleOffset + angleToPlayer), x+60, y +35);
				
			} else {
				g2d.rotate(angleToPlayer + gunAngleOffset + Math.PI, x + 10, y + 35);
				
				g.setColor(Color.black);
				g.fillRect(x - 15 + (int)gunRecoilOffset, y + 25, 50, 20);
				g.fillRect(x - 45 + (int)gunRecoilOffset, y + 29, 90, 12);
				g.fillRect(x + 45 + (int)gunRecoilOffset, y + 29, 10, 35);
				g.fillRect(x + 2 + (int)gunRecoilOffset, y + 25, 15, 60);

				
				g2d.rotate(- (angleToPlayer + gunAngleOffset + Math.PI), x + 10, y + 35);
			}
			
		} else {
			
			g2d.rotate(Math.PI/2, x+60, y+35);

			g.setColor(Color.black);
			g.fillRect(x + 15, y + 15, 50, 20);
			g.fillRect(x - 15, y + 19, 90, 12);
			g.fillRect(x + 75, y + 19, 10, 35);
			g.fillRect(x + 32, y + 15, 15, 60);

			g2d.rotate(-Math.PI/2, x+60, y+35);

		}
		
	}
	
	@Override
	public String getName() {
		return "uziEnemy";
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
			reloadTimer++;
			
			if (reloadTimer > reloadTime - (Math.round((2*Math.PI) / 0.3)) && reloadTimer < reloadTime) {
				gunAngleOffset += 0.3;
			} else {
				gunAngleOffset = 0;
			}

			
			if (reloadTimer > reloadTime) {
				shotTimer++;
				
				if (shotTimer > shotTime) {
					shoot();
					
					shotTimer = 0;
					
					shotsUntilReload--;
					
					if (shotsUntilReload == 0) {
						shotsUntilReload = magSize;
						reloadTimer = 0;
					}
				}
				
			}
		} else {
			if (Game.gameTicks % 3 == 0) {
				reloadTimer--;
			}
			
			if (reloadTimer < (int)(0.5 * (double)reloadTime)) {
				reloadTimer = (int)(0.5 * (double)reloadTime);
			}
		}
	}


}
