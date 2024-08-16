package platformer;

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

public class GroundObject extends MapObject {
	
	public GroundObject(int x, int y, int xSize, int ySize) {
		super(x, y, xSize, ySize, new Color(210, 210, 210));
		customColor = false;
	}
	
	public GroundObject(int x, int y, int xSize, int ySize, Color color) {
		super(x, y, xSize, ySize, color);
		customColor = true;
	}

	@Override
	public String getName() {
		return "ground";
	}
}
