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

public class SniperEnemy extends PistolEnemy {


	public SniperEnemy(int x, int y) {
		super(x, y, 135);
	}
	
	@Override
	public void render(Graphics g, Graphics2D g2d) {
		if (canSeePlayer()) {
			g.setColor(new Color(255, 0, 0, 120 + shotTimer)); //draw laser
			g2d.setStroke(new BasicStroke(1 + (int)(shotTimer/13)));
			g.drawLine(x + 35, y + 35, player.x + 35, player.y + 35);
		}

		g.setColor(color);
		g.fillRect(x, y, xSize, ySize);
		
		g.setColor(Color.black);
		
		if (canSeePlayer()) {
			

			g2d.rotate(angleToPlayer, x+35, y+47);
			
			g.setColor(color.black); //draw gun
			
			//for x: 70 - x_original - width

			if (player.x > x) {
	
				g.fillRect((int)(x + 30 - (int)gunRecoilOffset), (int)(y + 35), 120, 10);
				g.fillRect((int)(x + 10 - (int)gunRecoilOffset), (int)(y) + 40, 80, 15);
				g.fillRect((int)(x - 5 - (int)gunRecoilOffset), (int)(y) + 45, 40, 20);
				g.fillRect((int)(x + 68 - (int)gunRecoilOffset), (int)(y) + 48, 10, 30);
				
				g.fillRect((int)(x + 60 - (int)gunRecoilOffset), (int)(y + 28), 15 ,15);
				g.fillRect((int)(x + 40 - (int)gunRecoilOffset), (int)(y + 15), 65,15);
				

			} else {
				g2d.rotate(Math.PI, x+35, y+47);
				
				g.fillRect((int)(x - 80 + (int)gunRecoilOffset), (int)(y + 35), 120, 10);
				g.fillRect((int)(x - 20 + (int)gunRecoilOffset), (int)(y) + 40, 80, 15);
				g.fillRect((int)(x + 35 + (int)gunRecoilOffset), (int)(y) + 45, 40, 20);
				g.fillRect((int)(x - 7 + (int)gunRecoilOffset), (int)(y) + 48, 10, 30);
				
				g.fillRect((int)(x - 5 + (int)gunRecoilOffset), (int)(y + 28), 15 ,15);
				g.fillRect((int)(x - 35 + (int)gunRecoilOffset), (int)(y + 15), 65,15);

				g2d.rotate(-Math.PI, x+35, y+47);
			}
			
			g2d.rotate(-angleToPlayer, x+35, y+47);

		} else {
			g2d.rotate(Math.PI/2, x+35, y+47);
			
			g.fillRect((int)(x - 80), (int)(y + 15), 120, 10);
			g.fillRect((int)(x - 20), (int)(y) + 20, 80, 15);
			g.fillRect((int)(x + 35), (int)(y) + 25, 40, 20);
			g.fillRect((int)(x - 7), (int)(y) + 28, 10, 30);
			
			g.fillRect((int)(x - 5), (int)(y + 8), 15 ,15);
			g.fillRect((int)(x - 35), (int)(y - 5), 65,15);

			g2d.rotate(-Math.PI/2, x+35, y+47);

		}
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
		
		if (canSeePlayer()) {
			shotTimer++;
			
			if (shotTimer > shotTime - (Math.round((2*Math.PI) / 0.3)+10) && shotTimer < shotTime - 10) {
				gunAngleOffset += 0.3;
			} else {
				gunAngleOffset = 0;
			}

			if (shotTimer > shotTime) {
				Game.killPlayer();
				
				shotTimer = 0;
			}
		} else {
			shotTimer = 0;
		}
	}

	@Override
	public String getName() {
		return "sniperEnemy";
	}

}
