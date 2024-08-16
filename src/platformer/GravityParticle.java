package platformer;

import java.awt.Color;

public class GravityParticle extends Particle {

	public double angleVelocity = (Math.random() - 0.5) * 0.2;
	public double xVelocity;
	public double yVelocity;
	
	public GravityParticle(int x, int y, int xSize, int ySize, double xVelocity, double yVelocity) {
		super(x, y, xSize, ySize, new Color(255, 255, 255), Math.random() * (Math.PI * 2));
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
	}
	
	public GravityParticle(int x, int y, int xSize, int ySize, Color color, double xVelocity, double yVelocity) {
		super(x, y, xSize, ySize, color, Math.random() * (Math.PI * 2));
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
	}

	
	public GravityParticle(int x, int y, int xSize, int ySize, Color color, int lifespan, double xVelocity, double yVelocity) {
		super(x, y, xSize, ySize, color, Math.random() * (Math.PI * 2), lifespan);
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
	}
	
	
	

	@Override
	public void animation() {
		x += xVelocity;
		y += yVelocity;
		
		angle += angleVelocity;
		
		yVelocity += 0.4;
		
		yVelocity *= 0.9;
		xVelocity *= 0.95;
	}

}
