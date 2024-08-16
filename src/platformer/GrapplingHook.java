package platformer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class GrapplingHook extends Entity {

	public Player player;
	public double xVelocity, yVelocity, xTrue, yTrue, gravity = 0;
	public boolean locked = false; //locked onto ground?
	public boolean active = false; //ability active?
	public boolean rendering = false; //ability rendering?
	public boolean latched = false; //locked onto enemy?
	public Enemy latchedEnemy;
	
	public double restLength = 200; //100
	public double springConstant = 0.004; //0.004
	public double breakLength = 800; //700
	public double maxForceLength = 450; //450
	public double yForceMultiplier = 1.6; //1.6
	public double tugMultiplier = 16; //12
	
	public int thickness = 6;
	public double defaultThickness = 6;
	public double length;
	
	public double angle = 0;
	public double angularVelocity = 0;
	
	public double fishingRodLength = 125;
	public int fishingRodX, fishingRodY;
	public double fishingRodAngle;
	public double fishingRodAngleOffset;
	public double reelAngle = 0;
	
	public GrapplingHook(int x, int y, int xVelocity, int yVelocity) {
		super(x, y);
		
		xTrue = x;
		yTrue = y;
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;		
		player = Game.player;
	}
	
	@Override
	public void tick() {
		double lastLength = length;

		length = distance(player.x + 35, player.y + 35, x, y);
		if (Math.abs(length) < 1) {
			length = 1;
		}

		reelAngle += min(0.015 * (length - lastLength), 0.3);
		// active TRUE locked FALSE
		// launching
		// active TRUE locked TRUE
		// locked
		// active FALSE locked FALSE
		// reeling in
		

		if (!active) {
			
			//reeling in
			fishingRodAngleOffset -= 0.25;
			if (fishingRodAngleOffset < - 3 * Math.PI / 4) {
				fishingRodAngleOffset = - 3 * Math.PI / 4;
			}
			
			xTrue += (player.x + 35 - xTrue)/10;
			yTrue += (player.y + 35 - yTrue)/10;
			
			double returnSpeed = 20;
			xTrue += returnSpeed * (player.x + 35 - xTrue)/length;
			yTrue += returnSpeed * (player.y + 35 - yTrue)/length;
						
			x = (int)xTrue;
			y = (int)yTrue;
			
			if (length < 100) {
				rendering = false;
			}
						
		} else {
			
			thickness = (int)((1-(length-restLength)/(breakLength-restLength)) * defaultThickness) + 1;
			thickness = max(1, thickness);
			
			if (locked) {
				//locked
				
				fishingRodAngleOffset = 0;
				
				double force = (length - restLength > 0) ? springConstant * (length - restLength) : 0;
				if (force > springConstant * maxForceLength) {
					force = springConstant * maxForceLength;
				}
				
				Game.xVelocity += force * (x - (player.x + 35))/length;
				Game.yVelocity += force * (y - (player.y + 35))/length * yForceMultiplier;
				
			/*} else if (latched) {
				
				x = latchedEnemy.x + latchedEnemy.xSize/2;
				y = latchedEnemy.y + latchedEnemy.ySize/2;*/
				
			} else {
				//launching 
				
				fishingRodAngleOffset *= 0.9;
				if (Math.abs(fishingRodAngleOffset) > 0.1) {
					fishingRodAngleOffset -= 0.1 * Math.signum(fishingRodAngleOffset);
				} else {
					fishingRodAngleOffset = 0;
				}
				
				xTrue += xVelocity;
				yTrue += yVelocity;
	
				x = (int)xTrue;
				y = (int)yTrue;
				
				if (active && Game.maphandler.touchingGroundObject(getHitbox())) {
					locked = true;
					
					while (Game.maphandler.touchingGroundObject(getHitbox())) {
						xTrue -= xVelocity/5;
						yTrue -= yVelocity/5;
						x = (int)xTrue;
						y = (int)yTrue;
					}
				}
				
				/*if (active && Game.entityhandler.touchingCountedEnemy(getHitbox())) {
					latched = true;
					latchedEnemy = Game.entityhandler.getCountedEnemyTouching(getHitbox());	
				}*/
	
				yVelocity += gravity;
	
				if (Game.maphandler.touchingKillObject(getHitbox())) {
					
					if (LavaObject.class.isAssignableFrom(Game.maphandler.getKillObjectTouching(getHitbox()).getClass())) {
						gravity = 0.5;
						yVelocity *= 0.5;
						xVelocity *= 0.7;
						yVelocity -= 6*gravity;
						
						angularVelocity += 0.01 * Math.sin(Math.PI/2 - angle);
					}
									
				}
	
				angle += angularVelocity;
				angularVelocity *= 0.99;

			}
			
			if (!hasLineOfSight(x, y, player.x + 35, player.y + 35)) {
				endAbility();
			}
			
			if (length > breakLength) {
				endAbility();
			}

		}
		
	}
	
	@Override
	public void render(Graphics g, Graphics2D g2d) {
		
		if (!rendering) {
			return;
		}
		
		if (Math.abs(player.x + 35 - xTrue) > 1) {
			fishingRodAngle = Math.atan((player.y + 35 - yTrue) / (player.x + 35 - xTrue));
		} else {
			fishingRodAngle = Math.atan((player.y + 35 - yTrue) * Math.signum(player.x + 35 - xTrue));
		}
		if (player.x + 35 > xTrue) {
			fishingRodAngle += Math.PI - fishingRodAngleOffset;
		} else {
			fishingRodAngle += fishingRodAngleOffset;
		}
		fishingRodX = player.x + 35 + (int)(fishingRodLength * Math.cos(fishingRodAngle));
		fishingRodY = player.y + 35 + (int)(fishingRodLength * Math.sin(fishingRodAngle));
 
		g.setColor(new Color(80, 80, 80));
		g2d.setStroke(new BasicStroke(thickness));
		/*if (locked) {
			g.drawLine(x, y, fishingRodX, fishingRodY);	
		} else {
			g.drawLine(x, y, player.x + 35, player.y + 35);	
		}*/
		g.drawLine(x, y, fishingRodX, fishingRodY);	

		g2d.rotate(angle - Math.PI/2, x ,y);
		g.setColor(new Color(0, 0, 0));
		g.fillRect(x-3, y, 6, 45);
		g.fillRect(x-3, y+39, 20, 6);
		g.fillRect(x+14, y + 30, 6, 15);
		
		g.setColor(new Color(255, 255, 255));
		g.fillRect(x-15, y-15, 30, 30);
		g.setColor(new Color(255, 0, 0));
		g.fillRect(x-15, y-5, 30, 10);
		g2d.rotate(-(angle - Math.PI/2), x, y);
		
		//if (locked) {
			g2d.setStroke(new BasicStroke(12));
			g.setColor(new Color(38, 209, 55));
			g.drawLine(player.x + 35, player.y + 35, fishingRodX, fishingRodY);
			
			g2d.rotate(fishingRodAngle, player.x + 35, player.y + 35);
			
			if (player.x + 35 > x) {
				g2d.rotate(Math.PI, player.x + 35, player.y + 35);
				g2d.rotate(Math.PI/4 - reelAngle, player.x - 37 + 9, player.y + 37 + 9);
				
				g2d.setStroke(new BasicStroke(12));
				g.setColor(new Color(80, 80, 80));
				g.drawRect(player.x - 37, player.y + 38, 16, 16);
				g.setColor(new Color(115, 115, 115));
				g.fillRect(player.x -38 , player.y + 37, 18, 18);
				
				g2d.rotate(-Math.PI/4 + reelAngle, player.x - 37 + 9, player.y + 37 + 9);
				g2d.rotate(-Math.PI, player.x + 35, player.y + 35);

			} else {
				//g.fillRect(player.x + 35, player.y + 29, (int)fishingRodLength, 12);

				g2d.rotate(Math.PI/4 + reelAngle, player.x + 90 + 9, player.y + 37 + 9);
							
				g2d.setStroke(new BasicStroke(12));
				g.setColor(new Color(80, 80, 80));
				g.drawRect(player.x + 91, player.y + 38, 16, 16);
				g.setColor(new Color(115, 115, 115));
				g.fillRect(player.x + 90, player.y + 37, 18, 18);
				
				g2d.rotate(-Math.PI/4 - reelAngle, player.x + 90 + 9, player.y + 37 + 9);
			}
			
			g2d.rotate(-fishingRodAngle, player.x + 35, player.y + 35);
		//}
		
	}
	
	public void acquirePlayerPosition(double xVelocity, double yVelocity) {
		xTrue = player.x + 35;
		yTrue = player.y + 35;
		x = (int)xTrue;
		y = (int)yTrue;

		//this.xVelocity = Game.xVelocity + xVelocity;
		//this.yVelocity = Game.yVelocity + yVelocity;
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
	}
	
	public void startAbility(double xVelocity, double yVelocity) {
		acquirePlayerPosition(xVelocity, yVelocity);
		active = true;
		locked = false;
		rendering = true;
		latched = false;
		
		xTrue += this.xVelocity;
		yTrue += this.yVelocity;

		x = (int)xTrue;
		y = (int)yTrue;
		
		gravity = 0;
		
		fishingRodAngleOffset = - Math.PI / 2;
		
	}
	
	public void endAbility() {
		active = false;
		locked = false;
		latched = false;
	}
	
	public void tug() {
		
		if (locked) {
			double force = (length - restLength > 0) ? springConstant * (length - restLength) : 0;
			if (force > springConstant * maxForceLength) {
				force = springConstant * maxForceLength;
			}
			
			Game.xVelocity += tugMultiplier * force * (x - (player.x + 35))/length;
			Game.yVelocity += tugMultiplier * force * (y - (player.y + 35))/length;
		} /*else if (latched) {
			
			
			
		}*/

	}
	
	public Rectangle getHitbox() {
		return new Rectangle(x-12, y-12, 24, 24);
	}
	
	public void stopRendering() {
		rendering = false;
	}

}
