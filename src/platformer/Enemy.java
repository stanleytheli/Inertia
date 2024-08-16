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

public abstract class Enemy extends Entity {

	Color color = new Color(255, 55, 55);
	int xSize = 70, ySize = 70;
	//public boolean active = false;
	
	public int midairTime = 0;
	
	public double yVelocity = 0;
	public double xVelocity = 0;
	
	public double gravity = Game.gravity;
	public MapHandler maphandler = Game.maphandler;
	
	public Enemy(int x, int y) {
		super(x, y);
	}
	
	public Enemy(int x, int y, int xSize, int ySize, Color color) {
		super(x, y);
		this.xSize = xSize;
		this.ySize = ySize;
		//this.active = active;
	}

	public abstract void behavior();
	public abstract void postPhysicsBehavior();
	
	public void tick() { //the tick() method basically involves physics
		if (Game.lockEnemies) {
			return;
		}
		
		behavior(); //How the enemy moves
		
		xVelocity *= 0.8;
		
		//physics calculations
		if (!touchingLevel()) {
			yVelocity += gravity;
			midairTime++;	
		}
		 
		y += yVelocity;
		
		if (touchingLevel()) {
			if (yVelocity < 0) {
				
				while (touchingLevel()) {
					y += 1;
				}
				
				yVelocity = gravity;

			} else {
				while (touchingLevel()) {
					y -= 1;
				}
				yVelocity = 0;
				midairTime = 0;
			}

		}
		
		x += xVelocity;
		
		if (touchingLevel()) {
			
			if (xVelocity > 0) {
				while (touchingLevel()) {
					x -= 1;
				}
			} else {
				while (touchingLevel()) {
					x += 1;
				}
			}
			xVelocity = 0;
			
		}
		
		if (maphandler.touchingKillObject(getHitbox())) {
			kill();
		}

		postPhysicsBehavior();
		
	}

	public boolean touchingLevel() {
		return maphandler.touchingGroundObject(getHitbox());
	}
	public boolean touchingLevel(Rectangle rect) {
		return maphandler.touchingGroundObject(rect);
	}
	
	@Override
	public void render(Graphics g, Graphics2D g2d) {
		g.setColor(color);
		g.fillRect(x, y, xSize, ySize);
	}
	
	public Rectangle getHitbox() {
		return new Rectangle(x, y, xSize, ySize);
	}
	
	public void generateDeathParticle(double xVelocity, double yVelocity) {
		int size = 25;
		Game.entityhandler.addEntity(new GravityParticle(
				(int)(x + (Math.random() * xSize)), (int)(y + (Math.random() * ySize)), //x and y position
				size, size, //size
				color, //color
				30, //lifespan
				(int)(xVelocity), //x velocity
				(int)(yVelocity) //y velocity
				));		
	}
	
	public void generateDeathExplosion() {
		int size = 40;
		Game.entityhandler.addEntity(new ExplosionParticle(
				x + xSize/2 - size/2, y + ySize/2 - size/2, //x and y
				size, size, //size
				color,
				13,
				20
				));
	}
	
	public void generateDeathSplatter(double angle) {
		Game.entityhandler.addEntity(new EnemyBloodSplatter(
				x + xSize/2 - 9, y + ySize/2 - 9,
				angle
				));
	}

	public void deathAnimation() {
		int particles = (int)(6 + Math.random() * 2);
		double angle = Math.random() * 6.28;
		double angleStep = 6.28 / particles;
		
		generateDeathExplosion();
		
		for (int i = 0; i < particles; i++) {
			double particleVelocity = 16.5;
			generateDeathParticle(particleVelocity * Math.cos(angle) / 2, particleVelocity * Math.sin(angle) - 6);
			angle += angleStep;
		}
		
		boolean splatter = true;
		
		if (splatter) {
			particles = (int)(25 + Math.random() * 10);
			angle = Math.random() * 6.28;
			angleStep = 6.28 / particles;
			
			for (int i = 0; i < particles; i++) {
				generateDeathSplatter(angle);
				angle += angleStep;
			}
		}
	}
	
	public void kill() {
		deathAnimation();
		Game.entityhandler.removeEntity(this);
	}
	
	public int getEnemyValue() {
		return 1;
	}
	

}
