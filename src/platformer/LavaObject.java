package platformer;

import java.awt.BasicStroke;
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

public class LavaObject extends KillObject {
	
	public static double lavaWaveSpeed = 1;
	
	private Graphics g;
	private Graphics2D g2d;
	
	public LavaObject(int x, int y, int xSize, int ySize) {
		super(x, y, xSize, ySize, new Color(255, 106, 0));
		customColor = false;
	}

	public LavaObject(int x, int y, int xSize, int ySize, Color color) {
		super(x, y, xSize, ySize, color);
		customColor = true;
	}

	@Override
	public void tick() {
		
		lavaParticleRoll();
		
		if (xSize > 400) {
			lavaParticleRoll();
		}
		
		if (xSize > 1000) {
			lavaParticleRoll();
		}
		
		if (xSize > 2000) {
			lavaParticleRoll();
		}
		
	}
	
	public void generateLavaParticle() {
		Game.entityhandler.addEntity(new GravityParticle(
				(int)(x + (Math.random() * xSize)), y + 8, //x and y position
				15, 15, //size
				color, //color
				45, //lifespan
				(int)((2 * (Math.random() - 0.5))*6), //x velocity
				(int)(-15 - (Math.random() * 7)) //y velocity
				));

	}
	
	
	public void gradientLine(Graphics g, Graphics2D g2d, int height, int thickness) {
		g.fillRect(x, y + 8 - height, xSize, thickness);
	}
	
	public void gradient(Graphics g, Graphics2D g2d, int height) {
		int resolution = 5;
		int startAlpha = 150;
		
		for (int i = 0; i < height; i += resolution) {
			g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), startAlpha - (int)(startAlpha *((double)i/(double)height))));
			gradientLine(g, g2d, i, resolution);
		}
	}
	
	@Override
	public void render(Graphics g, Graphics2D g2d) {
		this.g = g;
		this.g2d = g2d;
		
		gradient(g, g2d, 35);
		
		g.setColor(color);
		g2d.setStroke(new BasicStroke(1));		
		
		g.fillRect(x, y + 8, xSize, ySize - 8); 
		
		for (int i = x; i < x + xSize; i++) { //lava waves
			g.setColor(color);
			
			g.drawLine(i, y -  waveHeight(i), i, y + 8);
			
		}
				
		
	}
	
	public int waveHeight(int x) { //formula for wave height
		//classic lava rendering:
		//double output = 8 * Math.sin((2 * Math.PI)/75 * ((double)x + lavaWaveSpeed * (double)Game.gameTicks));
		//new lava rendering:
		double output = 3 * Math.sin((2 * Math.PI)/75 * ((double)x + lavaWaveSpeed * (double)Game.gameTicks)) 
				+  5 * Math.cos((2 * Math.PI)/200 * ((double)(x) - lavaWaveSpeed * (Game.gameTicks)))
				+ 1 * Math.sin((2 * Math.PI)/50 * ((double)x + 2 * lavaWaveSpeed * (double)Game.gameTicks));
		return (int)output;
	}
	
	public void lavaParticleRoll() {
		if ((int)(Math.random() * 120) == 1) {
			generateLavaParticle();
		}

	}
	
	@Override
	public String getName() {
		return "lava";
	}	
	
}
