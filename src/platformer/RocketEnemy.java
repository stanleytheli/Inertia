package platformer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class RocketEnemy extends PistolEnemy {

	public RocketEnemy(int x, int y) {
		super(x, y, 200);
	}

	@Override
	public void render(Graphics g, Graphics2D g2d) {
		g.setColor(color);
		g.fillRect(x, y, xSize, ySize);
		
		g.setColor(new Color(0, 77, 0));

		if (distanceToPlayer() < range && canSeePlayer()) {

			g2d.rotate(angleToPlayer, x+35, y+35);
			
			if (player.x > x) {
				g2d.rotate(gunAngleOffset, x+35, y+35);
				
				g.fillRect((int)(x - 15 - gunRecoilOffset), y + 20, 110, 30);
				g.fillRect((int)(x + 90 - gunRecoilOffset), y + 15, 12, 40);
				
				g.fillRect((int)(x + 60 - gunRecoilOffset), y + 45, 12, 20);
				
				g2d.rotate(-gunAngleOffset, x+35, y+35);
			} else {
				//for x: subtract 35, *-1, add 35, subtract width

				g2d.rotate(-(gunAngleOffset + Math.PI), x+35, y+35);

				g.fillRect((int)(x - 25 + gunRecoilOffset), y + 20, 110, 30);
				g.fillRect((int)(x - 32 + gunRecoilOffset), y + 15, 12, 40);
				
				g.fillRect((int)(x - 2 + gunRecoilOffset), y + 45, 12, 20);

				g2d.rotate(gunAngleOffset + Math.PI, x+35, y+35);

			}
			
			g2d.rotate(-angleToPlayer, x+35, y+35);

		} else {
			g2d.rotate(Math.PI/2, x+35, y+35);
			
			g.fillRect((int)(x - 25 + gunRecoilOffset), y, 110, 30);
			g.fillRect((int)(x - 32 + gunRecoilOffset), y - 5, 12, 40);
			
			g.fillRect((int)(x - 2 + gunRecoilOffset), y + 25, 12, 20);

			g2d.rotate(-Math.PI/2, x+35, y+35);

		}
	}

	public void shoot() {
		int xCoordinate = (int)(x + 35 + (90 * Math.cos(angleToPlayer)));
		int yCoordinate = (int)(y + 40 + (90 * Math.sin(angleToPlayer)));
		gunRecoilOffset = 70;

		Game.entityhandler.addEntity(new EnemyRocket(xCoordinate, yCoordinate, angleToPlayer));

	}
	
	@Override
	public String getName() {
		return "rocketEnemy";
	}
	
}
