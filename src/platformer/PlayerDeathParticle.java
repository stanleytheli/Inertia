package platformer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PlayerDeathParticle extends Entity {
	
	public int size, alpha;
	public int initialSize = 70;
	public int finalSize;
	public int targetX, targetY;
	public double angle;
	public double movementDelay = Game.cameraMovementDelay - 1;
	
	public PlayerDeathParticle(double angle) {
		super(Game.player.x + 35, Game.player.y + 35);
		
		alpha = 75 + (int)(Math.random() * 75);
		
		initialSize = 70;
		finalSize = (int)(50 + Math.random() * 50);
		size = initialSize;
		
		double radius = 25 + Math.random() * 100;
		targetX = (int)(x + radius * Math.cos(angle));
		targetY = (int)(y + radius * Math.sin(angle));
	}

	@Override
	public void tick() {
		if (Game.deathAnimationTimer == 0) {
			Game.entityhandler.removeEntity(this);
		}
		
		int stageOneEnd = Game.deathAnimationTime/2;

		if (Game.deathAnimationTimer == stageOneEnd) {
			targetX = Game.spawnX + 35;
			targetY = Game.spawnY + 35;
		}
		
		double factor = 2 * Math.abs(Game.deathAnimationTimer - stageOneEnd)/(double)Game.deathAnimationTime; //animation distance from middle
		size = (int)Math.round(initialSize * factor + finalSize * (1 - factor));
		
		if (Game.deathAnimationTimer > stageOneEnd) {
			if (magnitude(targetX - x, targetY - y) > 10) {
				x += 10 * (targetX - x)/magnitude(targetX - x, targetY - y);
				y += 10 * (targetY - y)/magnitude(targetX - x, targetY - y);
				
				x += (targetX - x)/movementDelay;
				y += (targetY - y)/movementDelay;	
			} else {
				x = targetX;
				y = targetY;
			}
		} else {
			x += (targetX - x)/movementDelay;
			y += (targetY - y)/movementDelay;	
		}
				
	}

	@Override
	public void render(Graphics g, Graphics2D g2d) {
		g.setColor(new Color(Game.player.playerColor.getRed(), Game.player.playerColor.getGreen(), Game.player.playerColor.getBlue(), alpha));
		g.fillRect(x - size/2, y - size/2, size, size);
		
	}

}
