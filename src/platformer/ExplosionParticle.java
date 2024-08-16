package platformer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ExplosionParticle extends Particle {
	
	public int growth = 5;
	
	public ExplosionParticle(int x, int y, int xSize, int ySize) {
		super(x, y, xSize, ySize, new Color(255, 255, 255));
	}
	public ExplosionParticle(int x, int y, int xSize, int ySize, int growth) {
		super(x, y, xSize, ySize, new Color(255, 255 ,255));
		this.growth = growth;
	}
	public ExplosionParticle(int x, int y, int xSize, int ySize, int growth, int lifespan) {
		super(x, y, xSize, ySize, new Color(255, 255, 255), 0, lifespan);
		this.growth = growth;
	}

	public ExplosionParticle(int x, int y, int xSize, int ySize, Color color) {
		super(x, y, xSize, ySize, color);
	}
	public ExplosionParticle(int x, int y, int xSize, int ySize, Color color, int growth) {
		super(x, y, xSize, ySize, color);
		this.growth = growth;
	}
	public ExplosionParticle(int x, int y, int xSize, int ySize, Color color, int growth, int lifespan) {
		super(x, y, xSize, ySize, color, 0, lifespan);
		this.growth = growth;
	}


	@Override
	public void animation() {
		x -= growth/2;
		y -= growth/2;
		xSize += growth;
		ySize += growth;
		
	}
	
	@Override
	public void render(Graphics g, Graphics2D g2d) {
		g.setColor(color);
		g2d.setComposite(makeTransparent((float)1 - ((float)lifeTimer/(float)lifespan)));
		
		g.fillOval(x, y, xSize, ySize);
		
		g2d.setComposite(makeTransparent(1));
	}

}
