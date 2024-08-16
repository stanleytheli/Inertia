package platformer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ConfettiParticle extends Particle { //similar to GravityParticle but rotation affects position, making it look like paper
	public double angleVelocity = (Math.random() - 0.5) * 0.2;
	public double xVelocity;
	public double yVelocity;
	
	public ConfettiParticle(int x, int y, int xSize, int ySize, double xVelocity, double yVelocity) {
		super(x, y, xSize, ySize, new Color(255, 255, 255), Math.random() * (Math.PI * 2));
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		
	}
	
	public ConfettiParticle(int x, int y, int xSize, int ySize, Color color, int lifespan, double xVelocity, double yVelocity) {
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
	
	public void render(Graphics g, Graphics2D g2d) {
		if (angle != 0) {
			g2d.rotate(angle, x - (xSize/2), y - (ySize/2)); //oh, the wonders of a sign error!
		}
		
		g.setColor(color);
		g2d.setComposite(makeTransparent((float)1 - ((float)lifeTimer/(float)lifespan)));
		
		g.fillRect(x, y, xSize, ySize);
		
		g2d.setComposite(makeTransparent(1));
		if (angle != 0) {
			g2d.rotate(-angle, x - (xSize/2), y - (ySize/2));
		}
	}


}
