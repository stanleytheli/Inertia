package platformer;

import java.awt.AlphaComposite;
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

public abstract class Particle extends Entity {

	public Color color;
	public int xSize;
	public int ySize;
	
	public double angle = 0;
	
	public int lifespan = 30;
	public int lifeTimer = 0;
		
	public Particle(int x, int y, int xSize, int ySize, Color color) {
		super(x, y);
		this.xSize = xSize;
		this.ySize = ySize;
		this.color = color;
	}
	public Particle(int x, int y, int xSize, int ySize, Color color, double angle) {
		super(x, y);
		this.xSize = xSize;
		this.ySize = ySize;
		this.color = color;
		this.angle = angle;
	}

	public Particle(int x, int y, int xSize, int ySize, Color color, double angle, int lifespan) {
		super(x, y);
		this.xSize = xSize;
		this.ySize = ySize;
		this.color = color;
		this.lifespan = lifespan;
		this.angle = angle;
	}
	
	public void tick() {
		animation();
		lifeTimer++;
		if (lifeTimer > lifespan) {
			Game.entityhandler.removeEntity(this);
		}
	}
	
	public abstract void animation();

	public void render(Graphics g, Graphics2D g2d) {
		if (angle != 0) {
			g2d.rotate(angle, x + (xSize/2), y + (ySize/2));
		}
		
		g.setColor(color);
		g2d.setComposite(makeTransparent((float)1 - ((float)lifeTimer/(float)lifespan)));
		
		g.fillRect(x, y, xSize, ySize);
		
		g2d.setComposite(makeTransparent(1));
		if (angle != 0) {
			g2d.rotate(-angle, x + (xSize/2), y + (ySize/2));
		}
	}
	
	public AlphaComposite makeTransparent(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type,alpha));
	}
	

}
