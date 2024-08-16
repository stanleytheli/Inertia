package platformer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class EnemyRocket extends EnemyBullet {

	public Player player = Game.player;
	public double angleToPlayer = 0;
	public double killRadius = 200;
	
	public double xTrue, yTrue;
	
	public EnemyRocket(int x, int y, double angle) {
		super(x, y, angle);
		xTrue = x;
		yTrue = y;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void tick() {
		xTrue += Math.round(20 * Math.cos(angle));
		yTrue += Math.round(20 * Math.sin(angle));
		
		x = (int)xTrue;
		y = (int)yTrue;
		
		ticks++;
		
		if (ticks > lifespan) {
			Game.entityhandler.removeEntity(this);
		}
		if (Game.maphandler.touchingGroundObject(getHitbox())) {
			explode();
		}
	}
	
	@Override
	public void render(Graphics g, Graphics2D g2d) {
		
		/*g.setColor(new Color(0, 55, 0));
		
		g2d.setStroke(new BasicStroke(20));
		g.drawLine(x, y, (x - (int)(40 * Math.cos(angle))), (y - (int)(40 * Math.sin(angle))));
		g2d.setStroke(new BasicStroke(10));
		g.drawLine(x, y, (x - (int)(60 * Math.cos(angle))), (y - (int)(60 * Math.sin(angle))));
		
		g2d.setStroke(new BasicStroke(20));
		g.drawLine((x - (int)(70 * Math.cos(angle))), (y - (int)(70 * Math.sin(angle))), (x - (int)(71 * Math.cos(angle))), (y - (int)(71 * Math.sin(angle))));*/

		g.setColor(new Color(0, 55, 0));

		g2d.rotate(angle + Math.PI, x, y);

		g.fillRect(x - 10, y - 10, 40, 20);
		g.fillOval(x - 20, y - 10, 20, 20);
		g.fillRect(x - 4, y - 4, 50, 8);
		g.fillRect(x + 40, y - 10, 20, 20);

		//g2d.setStroke(new BasicStroke(20));
		//g.drawLine(x, y, x - 40, y);
		//g2d.setStroke(new BasicStroke(10));
		//g.drawLine(x, y, x - 60, y);
		
		//g2d.setStroke(new BasicStroke(20));
		//g.drawLine(x - 70, y, x - 80, y);
		
		g2d.rotate(-angle - Math.PI, x, y);

		
		/*
		g2d.setStroke(new BasicStroke(10));
		g.setColor(new Color(255, 55, 55, 25));
		g.drawOval(x - (int)killRadius, y - (int)killRadius, 2*(int)killRadius, 2*(int)killRadius);*/
	}

	@Override
	public void generateDeathParticle(double xVelocity, double yVelocity) {
		int size = 20;
		double startBoost = 0.5;
		Game.entityhandler.addEntity(new FlamingParticle(
				(int)(x + Math.random()*xSize + (int)(xVelocity*startBoost)), (int)(y + Math.random()*ySize + (int)(yVelocity*startBoost)), //x and y position
				size, size, //size
				new Color(0, 55, 0), //color
				30, //lifespan
				(int)(xVelocity), //x velocity
				(int)(yVelocity) //y velocity
				));		
	}
	
	public void generateExplosionShrapnel() {
		int particles = (int)(3 + Math.random() * 2);
		double angle = Math.random() * 6.28;
		double angleStep = 6.28 / particles;
			
		for (int i = 0; i < particles; i++) {
			double particleVelocity = 25 + 10*Math.random();
			double angleOffset = 0.7 * (2*(Math.random()-0.5));
			
			generateDeathParticle(particleVelocity * Math.cos(angle+angleOffset) / 2, particleVelocity * Math.sin(angle+angleOffset) - 5);
			angle += angleStep;
		}
	}
	
	public void explode() {
		Game.entityhandler.addEntity(new ExplosionParticle(x - 75, y - 75, 150, 150, 20, 30));
		Game.entityhandler.addEntity(new ExplosionParticle(x - 50, y - 50, 100, 100, 6, 45));		
		
		generateExplosionShrapnel();

		if (!Game.lockPlayer) {			
			if (player.x < x) {
				angleToPlayer = Math.PI + Math.atan(((double)player.y - (double)y)/((double)player.x - (double)x));
			} else {
				angleToPlayer = Math.atan(((double)player.y - (double)y)/((double)player.x - (double)x));
			}

			double explosionVelocity = 50;
			explosionVelocity -= Math.sqrt(distanceToPlayer());
			
			if (explosionVelocity > 0) {
				Game.xVelocity += Math.cos(angleToPlayer) * explosionVelocity;
				if (Game.yVelocity < 0) {
					Game.yVelocity = Math.sin(angleToPlayer) * explosionVelocity;
				} else {
					Game.yVelocity += Math.sin(angleToPlayer) * explosionVelocity;
				}
			}
		}
		
		if (distanceToPlayer() < killRadius) {
			Game.killPlayer();
		}
		
		destroy();
	}
	
	@Override
	public void deathAnimation() {
	}
	
	@Override
	public void kill() {
		explode();
	}
	
	public void destroy() {
		Game.entityhandler.removeEntity(this);
	}

	@Override
	public double distanceToPlayer() {
		return distance(x + 5, y + 5, Game.player.x + 35, Game.player.y + 35);
		
	}
}
