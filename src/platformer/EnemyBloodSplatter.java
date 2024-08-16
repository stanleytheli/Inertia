package platformer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class EnemyBloodSplatter extends Entity {

	public double xTrue, yTrue, angle;
	public Color color;
	
	public double xVelocity, yVelocity;
	public int xSize, ySize;
	public int xSizeOriginal, ySizeOriginal;
	public int xOriginal, yOriginal;
	public boolean falling = true;
	
	public int ticksAlive, lifespan = 35;
	public int minSize = 8;
	
	public MapHandler maphandler = Game.maphandler;
	public EntityHandler entityhandler = Game.entityhandler;
	
	public int alpha = 150;
		
	public EnemyBloodSplatter(int x, int y, int xVelocity, int yVelocity) {
		super(x, y);
		xOriginal = x;
		yOriginal = y;
		
		xTrue = x;
		yTrue = y;
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		
		int size = randomint(16, 20);
		this.xSizeOriginal = size;
		this.ySizeOriginal = size;
		
		xSize = xSizeOriginal;
		ySize = ySizeOriginal;
	}
	
	public EnemyBloodSplatter(int x, int y, double angle) {
		super(x, y);
		xOriginal = x;
		yOriginal = y;

		double speed = randomint(5, 6);
		xVelocity = speed * Math.sin(angle);
		yVelocity = speed * Math.cos(angle);

		xTrue = x;
		yTrue = y;
		
		int size = randomint(16, 20);
		this.xSizeOriginal = size;
		this.ySizeOriginal = size;

		xSize = xSizeOriginal;
		ySize = ySizeOriginal;
	}
	
	public EnemyBloodSplatter(int x, int y, int xSize, int ySize, double angle, double speed) {
		super(x, y);
		xOriginal = x;
		yOriginal = y;

		xVelocity = speed * Math.sin(angle);
		yVelocity = speed * Math.cos(angle);

		xTrue = x;
		yTrue = y;
		this.xSizeOriginal = xSize;
		this.ySizeOriginal = ySize;
		
		xSize = xSizeOriginal;
		ySize = ySizeOriginal;
		
	}

	@Override
	public void tick() {
		for (int i = 0; i < 2; i++) {
			if (falling) {				
				xTrue += xVelocity;
				yTrue += yVelocity;
				
				x = (int)xTrue;
				y = (int)yTrue;
				
				yVelocity += 0.4;
	
				//air resistance?
				xVelocity *= 0.97;
				//yVelocity *= 0.95;
			
				int sizeDecrement = max((int)(distance(x, y, xOriginal, yOriginal) - 20)/7, 0);
				xSize = xSizeOriginal - sizeDecrement;
				ySize = ySizeOriginal - sizeDecrement;
				
				//hitbox check routine --- if all 4 pixels surrounding the rectangle are touching, plus the rectangle itself, then this is probably embedded right into the ground
				if (embedded()) {
					falling = false;
					
					/*int sizeDecrement = max((int)(distance(x, y, xOriginal, yOriginal) - 20)/7, 0);
					xSize -= sizeDecrement;
					ySize -= sizeDecrement;*/
					
					double speed = Math.sqrt(xVelocity * xVelocity + yVelocity * yVelocity);
					
					for (int j = 0; j < 30; j++) {
						if (embedded()) {
							
							xTrue -= xVelocity / speed;
							yTrue -= yVelocity / speed;
							x = (int)xTrue;
							y = (int)yTrue;
						} else {
							break;
						}
					}
										
					if (xSize < 3) {
						Game.entityhandler.removeEntity(this);
					}
					
					xSize = max(xSize, minSize);
					ySize = max(ySize, minSize);

					break;
				}
				
				ticksAlive++;
				if (ticksAlive > lifespan) {
					Game.entityhandler.removeEntity(this);
				}
			}
		}
	}

	@Override
	public void render(Graphics g, Graphics2D g2d) {
		if (!falling) {
			g.setColor(new Color(255, 55, 55, alpha));
			g.fillRect(x, y, xSize, ySize);
		}
	}
	
	public boolean embedded() {		
		return maphandler.touchingGroundObject(new Rectangle(x-1, y-1, 1, 1)) &&
				maphandler.touchingGroundObject(new Rectangle(x+xSize, y-1, 1, 1)) &&
				maphandler.touchingGroundObject(new Rectangle(x-1, y+ySize, 1, 1)) &&
				maphandler.touchingGroundObject(new Rectangle(x+xSize, y+ySize, 1, 1)) &&
				maphandler.touchingGroundObject(new Rectangle(x+xSize/2, y+ySize/2, 1, 1));
		/*return maphandler.touchingGroundObject(new Rectangle(x + xSize/2 -1,y-1, 1, 1)) &&
				maphandler.touchingGroundObject(new Rectangle(x + xSize, y + ySize/2 - 1, 1, 1)) &&
				maphandler.touchingGroundObject(new Rectangle(x + xSize/2 -1, y + ySize, 1, 1)) &&
				maphandler.touchingGroundObject(new Rectangle(x-1, y + ySize/2 - 1, 1 ,1)) &&
				maphandler.touchingGroundObject(new Rectangle(x, y, xSize, ySize));*/
	}
	

}
