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

public class EnemyBullet extends Enemy {

	public double angle;
	public int lifespan = 300;
	public int ticks = 0;
	
	public double speed = 10;
	
	public double xTrue, yTrue;
	
	public EnemyBullet(int x, int y, double angle) {
		super(x, y, 1, 1, new Color(255, 55, 55));
		xTrue = x;
		yTrue = y;
		this.angle = angle;
	}
	
	public EnemyBullet(int x, int y, double angle, double speed) {
		super(x, y, 1, 1, new Color(255, 55, 55));
		xTrue = x;
		yTrue = y;
		this.angle = angle;
		this.speed = speed;
	}


	@Override
	public void behavior() {
		
	}

	@Override
	public void postPhysicsBehavior() {
		
	}
	
	@Override
	public void tick() {
		xTrue += (speed * Math.cos(angle));
		yTrue += (speed * Math.sin(angle));
		
		x = (int)xTrue;
		y = (int)yTrue;
		
		ticks++;
		
		if (ticks > lifespan) {
			Game.entityhandler.removeEntity(this);
		}
		if (Game.maphandler.touchingGroundObject(getHitbox())) {
			kill();
		}
	}
	
	@Override
	public Rectangle getHitbox() {
		return new Rectangle(x - 7, y - 7, 15, 15);
	}
	
	@Override
	public void render(Graphics g, Graphics2D g2d) {
		
		g.setColor(new Color(255, 55, 55));
		
		g2d.setStroke(new BasicStroke(15));
		g.drawLine(x, y, (x - (int)(50 * Math.cos(angle))), (y - (int)(50 * Math.sin(angle))));
		
		//g.setColor(Color.blue);
		//g2d.fill(getHitbox());
	}
	
	public void generateDeathExplosion() {
		Game.entityhandler.addEntity(new ExplosionParticle(
				x - 13, y - 13, //x and y
				40, 40, //size
				color,
				8,
				14
				));
	}

	@Override
	public void generateDeathSplatter(double angle) {
		int size = randomint(11, 15);
		Game.entityhandler.addEntity(new EnemyBloodSplatter(
				x + xSize/2 - 9, y + ySize/2 - 9,
				size, size,
				angle,
				randomint(3,4)
				));
	}
	
	@Override
	public void deathAnimation() {
		int particles = 3;
		double angle = Math.random() * 6.28;
		double angleStep = 6.28 / particles;
		
		generateDeathExplosion();
		
		for (int i = 0; i < particles; i++) {
			double particleVelocity = 12;
			generateDeathParticle(particleVelocity * Math.cos(angle) / 2, particleVelocity * Math.sin(angle) - 6);
			angle += angleStep;
		}
		
		boolean splatter = true;
		
		if (splatter) {
			particles = 5;
			angle = Math.random() * 6.28;
			angleStep = 6.28 / particles;
			
			for (int i = 0; i < particles; i++) {
				generateDeathSplatter(angle);
				angle += angleStep;
			}
		}

	}

	@Override
	public int getEnemyValue() {
		return 0; // :(
	}
	
}
