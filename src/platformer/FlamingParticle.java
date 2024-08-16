package platformer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

public class FlamingParticle extends Particle {
	
	public int trailLength = 7;
	public int omission = 2;
	public Color trailFinalColor = new Color(255, 0, 0);
	public Color trailStartColor = new Color(255, 200, 0);
	public int xSizeOriginal, ySizeOriginal;
	
	public int trailLifespan = 15;
	public int rChange = (trailFinalColor.getRed() - trailStartColor.getRed())/lifespan;
	public int gChange = (trailFinalColor.getGreen() - trailStartColor.getGreen())/lifespan;
	public int bChange = (trailFinalColor.getBlue() - trailStartColor.getBlue())/lifespan;
	
	public LinkedList<EntityState> states = new LinkedList<EntityState>();

	public double angleVelocity = (Math.random() - 0.5) * 0.2;
	public double xVelocity;
	public double yVelocity;
	
	public FlamingParticle(int x, int y, int xSize, int ySize, double xVelocity, double yVelocity) {
		super(x, y, xSize, ySize, new Color(255, 255, 255), Math.random() * (Math.PI * 2));
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		
		xSizeOriginal = xSize;
		ySizeOriginal = ySize;
	}
	
	public FlamingParticle(int x, int y, int xSize, int ySize, Color color, double xVelocity, double yVelocity) {
		super(x, y, xSize, ySize, color, Math.random() * (Math.PI * 2));
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		
		xSizeOriginal = xSize;
		ySizeOriginal = ySize;
	}

	
	public FlamingParticle(int x, int y, int xSize, int ySize, Color color, int lifespan, double xVelocity, double yVelocity) {
		super(x, y, xSize, ySize, color, Math.random() * (Math.PI * 2), lifespan);
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		
		xSizeOriginal = xSize;
		ySizeOriginal = ySize;
	}

	@Override
	public void animation() {
		x += xVelocity;
		y += yVelocity;
		
		angle += angleVelocity;
		
		yVelocity += 0.4;
		
		yVelocity *= 0.9;
		xVelocity *= 0.95;
		
		xSize = (int)((double)xSizeOriginal * (1.0 - (double)lifeTimer/(double)lifespan));
		ySize = (int)((double)ySizeOriginal * (1.0 - (double)lifeTimer/(double)lifespan));
	}
	
	public void tick() {
		animation();
		lifeTimer++;
		if (lifeTimer > lifespan) {
			Game.entityhandler.removeEntity(this);
		}
		
		states.add(0, getState());
		if (states.size() > trailLength) {
			states.removeLast();
		}
	}

	public void render(Graphics g, Graphics2D g2d) {
		//g2d.setComposite(makeTransparent((float)(0.5 + 0.5*((float)1 - ((float)lifeTimer/(float)lifespan)))));
		//g2d.setComposite(makeTransparent((float)1 - ((float)lifeTimer/(float)lifespan)));
		
		//render trail
		for (int i = omission; i < trailLength; i++) {
			if (i > states.size() - 1) {
				break;
			}
			
			//set color correctly
			//set transparency correctly
			int R = trailStartColor.getRed() + i * rChange;
			int G = trailStartColor.getGreen() + i * gChange;
			int B = trailStartColor.getBlue() + i * bChange;
			int A = (int)(255 * (1 - (double)i/(double)trailLength));
			if (A > 255 || A < 0) {
				break;
			}

			g.setColor(new Color(R, G, B, A));
			renderState(g, g2d, states.get(i), i);
		}
		
		//render particle
		if (angle != 0) {
			g2d.rotate(angle, x + (xSize/2), y + (ySize/2));
		}
		
		g.setColor(color);
		
		g.fillRect(x, y, xSize, ySize);
		
		if (angle != 0) {
			g2d.rotate(-angle, x + (xSize/2), y + (ySize/2));
		}
		
		//g2d.setComposite(makeTransparent(1));
	}
	
	public EntityState getState() {
		return new EntityState(x, y, xSize, ySize, angle);
	}

	public void renderState(Graphics g, Graphics2D g2d, EntityState state, int parameter) {
		if (state.angle != 0) {
			g2d.rotate(state.angle, state.x + (state.xSize/2), state.y + (state.ySize/2));
		}
		g.fillRect(state.x - parameter/2, state.y - parameter/2, state.xSize + parameter, state.ySize + parameter);
		if (state.angle != 0) {
			g2d.rotate(-state.angle, state.x + (state.xSize/2), state.y + (state.ySize/2));
		}
	}
}