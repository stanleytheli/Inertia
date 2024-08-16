package platformer;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

public class Player extends Entity {
	
	public Color playerColor = new Color(255, 255, 255);
	
	private double angle = 0;

	public Player(int x, int y) {
		super(x, y);
	}

	@Override
	public void tick() {
		angle = 0;
		if (Game.midairTime > 2) {
			angle = Game.xVelocity * 0.0067;
		}

	}

	@Override
	public void render(Graphics g, Graphics2D g2d) {
		if(!Game.renderingPlayer) {
			return;
		}
		
		if (Game.spawnInvincibilityTimer > 0 && (Game.gameTicks/5) % 2 == 1) {
			return;
		}
		
		g2d.rotate(angle, x+35, y+35);
		
		g.setColor(playerColor);
		g.fillRect(x, y, 70, 70);
		
		if (Game.totalVelocity > Game.velocityRequired) {
			g.setColor(new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), 100)); //player color but transparent
			
			double difference = 3 * (Game.totalVelocity - 16);
			
			difference = Math.max(Math.min(difference, 35), 15);
			
			g.fillRect((int)(x - (difference/2)), (int)(y - (difference/2)), (int)(difference + 70), (int)(difference + 70));
			
		}
		
		g2d.rotate(-angle, x+35, y+35);


	}
	
	public void moveX(int xMove) {
		x += xMove;
	}
	public void moveY(int yMove) {
		y += yMove;
	}
	
	public void teleport(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public Rectangle getHitbox() {
		return new Rectangle(x, y, 70, 70);
	}
	
	public boolean touchingLevel() {
		if (Game.noClip) {
			return false;
		}
		
		return Game.maphandler.touchingGroundObject(getHitbox());
	}

	
	public boolean touchingKillObjects() {
		return Game.maphandler.touchingKillObject(getHitbox());
	}
	
	public boolean touchingEnemy() {
		return Game.entityhandler.touchingEnemy(getHitbox());
	}
	
	public AlphaComposite makeTransparent(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type,alpha));
	}

}
