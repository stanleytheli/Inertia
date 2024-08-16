package platformer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class DroneEnemy extends Enemy {

	public Player player;
	public double xTrue, yTrue, rotorAngle = 0, droneAngle = 0, rotorAngleSpeed = 0.65;
	public boolean seenPlayer = false;
	public double sightRange = 1100;
		
	public int lightFlickerTime = 40;
	public int flickerOffset = (int)(Math.random() * (2 * lightFlickerTime));
	
	public double speed = 0, maxSpeed = 10, maxCloseSpeed = 15, minSpeed = 4, moveAngle = 0, turnSpeed = 0.035;
	
	public double wallAvoidance = 2;
	public double angleToPlayer;
	
	public double sawAngle = 0, sawSpeed = 0.05;
	
	/*
	entities>>droneEnemy.-200.-200
	entities>>droneEnemy.0.-300
	entities>>droneEnemy.200.-200
	*/
	
	public DroneEnemy(int x, int y) {
		super(x, y);
		this.gravity = 0;
		this.player = Game.player;
		
		xTrue = x;
		yTrue = y;
	}

	@Override
	public void behavior() {
		rotorAngle += rotorAngleSpeed;
		droneAngle = xVelocity * 0.025;
		
		sawAngle += sawSpeed;
		
		angleToPlayer = angleToPlayer();
		
		double angularSeparation = Math.acos(Math.cos(angleToPlayer - moveAngle));

		if (canSeePlayer() && distanceToPlayer() < sightRange) {
			if (!seenPlayer) {
				moveAngle = angleToPlayer;
				seenPlayer = true;
			}
			
			if (angularSeparation < 0.8) {
				speed += 0.5;
				
				double maxSpeed = this.maxSpeed;
				if (distanceToPlayer() < 300) {
					maxSpeed = maxCloseSpeed;
				}
				
				if (speed > maxSpeed) {
					speed = maxSpeed;
				}
				
			} else {
				speed -= 0.25;
				if (speed < minSpeed) {
					speed = minSpeed;
				}
			}
		} else {
			speed -= 0.25;
			if (speed < 0) {
				speed = 0;
			}
		}
		
		moveAngle += turnSpeed * (Math.sin(angleToPlayer - moveAngle) > 0 ? 1 : -1);
		
		xVelocity = speed * Math.cos(moveAngle);
		yVelocity = speed * Math.sin(moveAngle);
		
		avoidance();
		
	}

	@Override
	public void postPhysicsBehavior() {
		
	}
	
	@Override
	public Rectangle getHitbox() {
		return new Rectangle(x - 35, y - 35, 70, 70);
	}
	
	@Override
	public void render(Graphics g, Graphics2D g2d) {
		g2d.rotate(droneAngle, x, y);
				
		renderSaw(g, g2d);
		
		g.setColor(new Color(0, 0, 0));
		g.fillRect(x - 35, y + 5, 70, 15);
		
		g2d.rotate(Math.PI/4, x, y);
		g.fillRect(x - 34, y + 15, 27, 10);
		g2d.rotate(-Math.PI/4, x, y);
		
		g2d.rotate(-Math.PI/4, x, y);
		g.fillRect(x+7, y + 15, 29, 10);
		g2d.rotate(Math.PI/4, x, y);
		
		if ((Game.gameTicks + flickerOffset)%(2 * lightFlickerTime) < lightFlickerTime) {
			g.setColor(new Color(255, 0, 0));
			g.fillOval(x - 29, y + 8, 8, 8);
		}
		
		g.setColor(new Color(0, 0, 0, 75));
		g.fillOval(x - 80, y - 30, 50, 25);
		g.fillOval(x + 30, y - 30, 50, 25);
		
		g.setColor(new Color(0, 0, 0));
		g2d.setStroke(new BasicStroke(7));		
		g.drawOval(x - 80, y - 30, 50, 25);
		g.drawOval(x + 30, y - 30, 50, 25);
				
		int centerXL = x - 55;
		int centerXR = x + 55;
		int centerY = y - 18;
		int deltaX = (int)(25 * Math.cos(rotorAngle));
		int deltaY = (int)(12.5 * Math.sin(rotorAngle));
		
		g2d.setStroke(new BasicStroke(5));
		g.drawLine(centerXL - deltaX, centerY - deltaY, centerXL + deltaX, centerY + deltaY);
		g.drawLine(centerXR - deltaX, centerY - deltaY, centerXR + deltaX, centerY + deltaY);

		g2d.rotate(-droneAngle, x, y);
	}
	
	@Override
	public void generateDeathParticle(double xVelocity, double yVelocity) {
		if (Math.random() > 0.5) {
			Game.entityhandler.addEntity(new GravityParticle(
					x, y, //x and y position
					25, 25, //size
					new Color(0, 0, 0), //color
					30, //lifespan
					(int)(xVelocity), //x velocity
					(int)(yVelocity) //y velocity
					));		
		} else {
			Game.entityhandler.addEntity(new GravityParticle(
					x, y, //x and y position
					60, 10, //size
					new Color(0, 0, 0), //color
					30, //lifespan
					(int)(xVelocity), //x velocity
					(int)(yVelocity) //y velocity
					));		
		}
	}

	@Override
	public void deathAnimation() {
		int particles = 6;
		double currentAngle = Math.random() * 6.28;
		double angleStep = 6.28 / particles;
				
		for (int i = 0; i < particles; i++) {
			double particleVelocity = 12 + Math.random() * 8;
			double angle = currentAngle + 0.5 * (2*(Math.random()-0.5));
			
			generateDeathParticle(particleVelocity * Math.cos(angle) / 1.5, particleVelocity * Math.sin(angle) - 5);
			
			currentAngle += angleStep;
		}
	}
	
	@Override
	public void tick() { //the tick() method basically involves physics
		if (Game.lockEnemies) {
			return;
		}
		
		behavior(); //How the enemy moves
			
		//physics calculations
		if (!touchingLevel()) {
			yVelocity += gravity;
			midairTime++;	
		}
		 
		yTrue += yVelocity;
		y = (int)yTrue;
		
		if (touchingLevel()) {
			if (yVelocity < 0) {
				while (touchingLevel()) {
					yTrue += 1;
					y = (int)yTrue;
				}
			} else {
				while (touchingLevel()) {
					yTrue -= 1;
					y = (int)yTrue;
				}
				midairTime = 0;
			}
			yVelocity = 0;
			speed = xVelocity;
		}
		
		xTrue += xVelocity;
		x = (int)xTrue;

		if (touchingLevel()) {
			if (xVelocity > 0) {
				while (touchingLevel()) {
					xTrue -= 1;
					x = (int)xTrue;
				}
			} else {
				while (touchingLevel()) {
					xTrue += 1;
					x = (int)xTrue;
				}
			}
			xVelocity = 0;
			speed = yVelocity;
		}

		if (maphandler.touchingKillObject(getHitbox())) {
			kill();
		}
		
		postPhysicsBehavior();
	}

	@Override
	public double angleToPlayer() {
		return (Game.player.x + 35 < x ? Math.PI : 0) + Math.atan(((double)(Game.player.y + 35) - (double)y)/((double)(Game.player.x + 35) - (double)x));
	}
	@Override
	public double distanceToPlayer() {
		return distance(x, y, Game.player.x + 35, Game.player.y + 35);
	}
	@Override
	public boolean canSeePlayer() {
		return hasLineOfSight(x, y, Game.player.x + 35, Game.player.y + 35);
	}

	public void avoidance() {
		if (touchingLevel(new Rectangle(x + 35, y + 35, 70, 70))) {
			xVelocity -= wallAvoidance;
			yVelocity -= wallAvoidance;
		}
		if (touchingLevel(new Rectangle(x + 35, y - 105, 70, 70))) {
			xVelocity -= wallAvoidance;
			yVelocity += wallAvoidance;
		}
		if (touchingLevel(new Rectangle(x - 105, y - 105, 70, 70))) {
			xVelocity += wallAvoidance;
			yVelocity += wallAvoidance;
		}
		if (touchingLevel(new Rectangle(x - 105, y + 35, 70, 70))) {
			xVelocity += wallAvoidance;
			yVelocity -= wallAvoidance;
		}
		
		
		if (maphandler.touchingKillObject(new Rectangle(x + 35, y + 35, 70, 70))) {
			xVelocity -= 2 * wallAvoidance;
			yVelocity -= 2 * wallAvoidance;
		}
		if (maphandler.touchingKillObject(new Rectangle(x + 35, y - 105, 70, 70))) {
			xVelocity -= 2 * wallAvoidance;
			yVelocity += 2 * wallAvoidance;
		}
		if (maphandler.touchingKillObject(new Rectangle(x - 105, y - 105, 70, 70))) {
			xVelocity += 2 * wallAvoidance;
			yVelocity += 2 * wallAvoidance;
		}
		if (maphandler.touchingKillObject(new Rectangle(x - 105, y + 35, 70, 70))) {
			xVelocity += 2 * wallAvoidance;
			yVelocity -= 2 * wallAvoidance;
		}

	}
	
	public void renderSaw(Graphics g, Graphics2D g2d) {
		//center = x, y + 12
		//g.fillOval(x - 25, y - 13, 50, 50);
		
		int spikes = 10;
		double outerRadius = 31;
		double innerRadius = 25;
		
		int[] pointsX = new int[2*spikes];
		int[] pointsY = new int[2*spikes];
		
		double angleStep = 0.56 * (Math.PI / spikes);
		double angle = -0.03 + sawAngle % (2 * angleStep) - angleStep;
		
		for (int i = 0; i < 2 * spikes; i++) {
			double radius = outerRadius;
			if (i % 2 == 0) {
				radius = innerRadius;
			}
			
			pointsX[i] = x + (int)(Math.round(radius * Math.cos(angle)));
			pointsY[i] = y + 12 + (int)(Math.round(radius * Math.sin(angle)));
			
			angle += angleStep;
		}

		g.setColor(new Color(100, 100, 100));
		g.fillPolygon(pointsX, pointsY, 2*spikes);
	}
}
