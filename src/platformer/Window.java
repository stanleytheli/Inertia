package platformer;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Window extends Canvas {

	/**
	 * 
	 * 1:34:85
	 */
	public static int windowX = 1000;
	public static int windowY = 1000;
	
	public static Cursor defaultCursor;
	public static Game game;
	
	private static final long serialVersionUID = 591771478813554689L;
	
	public static void main(String[] args) {
	     new Window(windowX + 16, windowY + 39, "INERTIA", new Game());
	}

	public Window(int width, int height, String title, Game game) {
		Window.game = game;

		JFrame frame = new JFrame(title);
		
		defaultCursor = frame.getCursor();

		frame.setCursor(frame.getToolkit().createCustomCursor(
	    		new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
	            "null"));
						
		
		frame.setSize(new Dimension(width,height));
		frame.setPreferredSize(new Dimension(width,height));
		//frame.setMaximumSize(new Dimension(width,height));
		//frame.setMinimumSize(new Dimension(width,height));
	
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		frame.add(game);
		game.frame = frame;
		game.requestFocus();
		game.start();
		
	}
	
	public static void showCursor() {
		game.frame.setCursor(defaultCursor);
	}
	public static void hideCursor() {
		game.frame.setCursor(game.frame.getToolkit().createCustomCursor(
	    		new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
	            "null"));
	}
}