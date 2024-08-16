package platformer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class DroneGunEnemy extends DroneEnemy {
			
	public double favoredRange = 350;
	public double range = 750;
	public double playerAvoidance = 4;
	
	public double gunAngle;
	public double gunAngleOffset = 0;
	public double gunRecoilOffset = 0;

	//entities>>droneGunEnemy.-200.0
	
	public int shotTime = 150;
	public int shotTimer = (int)(0.5 * (double)shotTime);
	
	public DroneGunEnemy(int x, int y) {
		super(x, y);
		this.gravity = 0;
		this.player = Game.player;
		
		xTrue = x;
		yTrue = y;
		maxSpeed = 8;
	}

	@Override
	public void behavior() {
		rotorAngle += rotorAngleSpeed;
		droneAngle = xVelocity * 0.025;
				
		angleToPlayer = angleToPlayer();

		double angularSeparation = Math.acos(Math.cos(angleToPlayer - moveAngle));

		//movement behavior
		
		if (canSeePlayer() && distanceToPlayer() < sightRange) {
			if (!seenPlayer) {
				seenPlayer = true;
				moveAngle = angleToPlayer;
			}
			
			if (distanceToPlayer() > favoredRange) {
				moveAngle += turnSpeed * (Math.sin(angleToPlayer - moveAngle) > 0 ? 1 : -1);

				if (angularSeparation < 0.8) {
					speed += 0.5;				
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
				moveAngle += 3 * turnSpeed * (Math.sin(angleToPlayer - moveAngle) > 0 ? -1 : 1);

				if (angularSeparation < 0.8) {

					moveAngle += turnSpeed * (Math.sin(angleToPlayer - moveAngle) > 0 ? -1 : 1);

				} else {
					speed += 1.25;
					if (speed > playerAvoidance) {
						speed = playerAvoidance;
					}
				}
			}
			
		} else {
			speed -= 0.25;
			if (speed < 0) {
				speed = 0;
			}

		}
		
		xVelocity = speed * Math.cos(moveAngle);
		yVelocity = speed * Math.sin(moveAngle);

		avoidance();
		
		gunRecoilOffset *= 0.95;
		if (gunRecoilOffset < 1) { 
			gunRecoilOffset = 0;
		}
		
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
	
	public void shoot() {
		int xCoordinate = (int)(x + (90 * Math.cos(angleToPlayer)));
		int yCoordinate = (int)(y + (90 * Math.sin(angleToPlayer)));
		gunRecoilOffset = 30;

		Game.entityhandler.addEntity(new EnemyBullet(xCoordinate, yCoordinate, angleToPlayer));
	}

	@Override
	public void render(Graphics g, Graphics2D g2d) {
		g2d.rotate(droneAngle, x, y);
		
		g.setColor(new Color(0, 0, 0));
		g.fillRect(x - 35, y + 5, 70, 15);
		g.fillRect(x - 4, y + 15, 8, 25);
		
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
		
		//gun center:
		// x, y + 12
		// -->
		// x - 12 * Math.sin(droneAngle), y + 12 * Math.cos(droneAngle)
		
		if (canSeePlayer() && distanceToPlayer() < range) {
			gunAngle = angleToPlayer + gunAngleOffset;
		} else {
			gunAngle = 0;
		}
		
		int gunCenterX = x - (int)(37 * Math.sin(droneAngle));
		int gunCenterY = y + (int)(37 * Math.cos(droneAngle));
		
		g2d.rotate(gunAngle, gunCenterX, gunCenterY);

		g.setColor(new Color(255, 0, 0, 30));
		for (int i = 0; i < 15; i++) {
			g.fillRect(gunCenterX + 15 - (int)gunRecoilOffset, gunCenterY + 3, 70 - 3*i, 5);
		}
		
		g.setColor(new Color(0, 0, 0));
		g.fillRect(gunCenterX - 20 - (int)gunRecoilOffset, gunCenterY - 8, 40, 16);
		g.fillRect(gunCenterX + 10 - (int)gunRecoilOffset, gunCenterY - 5, 40, 10);
		
		g2d.rotate(-gunAngle, gunCenterX, gunCenterY);
		
		//entities>>droneGunEnemy.0.-200
		
	}
		
}
