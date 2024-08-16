package platformer;

import java.awt.Color;

public class DecorationObject extends MapObject {
	
	public DecorationObject(int x, int y, int xSize, int ySize) {
		super(x, y, xSize, ySize, new Color(210, 210, 210));
		customColor = false;
	}
	
	public DecorationObject(int x, int y, int xSize, int ySize, Color color) {
		super(x, y, xSize, ySize, color);
		customColor = true;
	}
	
	@Override
	public String getName() {
		return "decoration";
	}

}
