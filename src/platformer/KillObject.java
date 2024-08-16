package platformer;

import java.awt.Color;

public abstract class KillObject extends MapObject {

	public Color defaultColor = new Color(210, 210, 210);
	
	public KillObject(int x, int y, int xSize, int ySize) {
		super(x, y, xSize, ySize, new Color(210, 210, 210));
		customColor = false;
	}
	
	public KillObject(int x, int y, int xSize, int ySize, Color color) {
		super(x, y, xSize, ySize, color);
		customColor = true;
	}

}
