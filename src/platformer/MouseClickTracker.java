package platformer;

import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseClickTracker implements MouseListener {

	public static boolean mouseDown = false;
	public static boolean rightMouseDown = false;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getButton() == MouseEvent.BUTTON1) {
			mouseDown = true;
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			rightMouseDown = true;
		}
		
		//mouseEvent e.getButton():
		//1 - left click
		//2 - scroll wheel click+turn?? idfk
		//3 - right click
		//4 and 5 - back/forward buttons
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getButton() == MouseEvent.BUTTON1) {
			mouseDown = false;
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			rightMouseDown = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
