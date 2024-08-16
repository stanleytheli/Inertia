package platformer;
import java.awt.Color;

public class EntityState {
	public int x, y, xSize, ySize;
	public double angle;
	public Color color;
	
	public EntityState(int x, int y, int xSize, int ySize, double angle) {
		this.x = x;
		this.y = y;
		this.xSize = xSize;
		this.ySize = ySize;
		this.angle = angle;
		
	}
	
	public EntityState(int x, int y, int xSize, int ySize, double angle, Color color) {
		this.x = x;
		this.y = y;
		this.xSize = xSize;
		this.ySize = ySize;
		this.angle = angle;
		this.color = color;
	}
}
