package platformer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseTracker implements MouseMotionListener {

	public int mouseX = 1, mouseY = 1;
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		/*
		mouseX = e.getX() - 500;
		mouseY = e.getY() - 500;
		if (mouseX == 0) {
			mouseX = 1;
		}
		if (mouseY == 0) {
			mouseY = 1;
		}*/
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
		mouseX = e.getX() - Window.windowX/2;
		mouseY = e.getY() - Window.windowY/2;
		if (mouseX == 0) {
			mouseX = 1;
		}
		if (mouseY == 0) {
			mouseY = 1;
		}
		
	}

}
