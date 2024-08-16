package platformer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class CrateObject extends GroundObject {

	public CrateObject(int x, int y, int xSize, int ySize) {
		super(x, y, xSize, ySize);
	}

	@Override
	public void render(Graphics g, Graphics2D g2d) {
		g.setColor(new Color(148, 95, 25));
		g.fillRect((int)(x), (int)(y), xSize, ySize);
		
		double thickness = Math.min((((double)xSize)*0.15),(((double)ySize)*0.15));
		
		g.setColor(new Color(199, 127, 32)); //decorations
		g.fillRect((int)(x), (int)(y), xSize, (int) thickness);
		g.fillRect((int)(x+xSize-thickness + 1), (int)(y), (int) thickness, ySize);
		g.fillRect((int)(x), (int)(y+ySize-thickness + 1), xSize, (int) thickness);
		g.fillRect((int)(x), (int)(y), (int) thickness, ySize);
		
		g2d.setStroke(new BasicStroke((float) thickness));
		g.drawLine((int)(x+thickness), (int)(y+thickness), (int)(x+xSize-thickness), (int)(y+ySize-thickness));
		//g.drawLine((int)(x+xSize-thickness), (int)(y+thickness), (int)(x+thickness), (int)(y+ySize-thickness));

	}
	
	@Override
	public String getName() {
		return "crate";
	}
	
}
