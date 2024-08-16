package platformer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyTracker implements KeyListener {
	
	public boolean jumpKeyPressed, leftKeyPressed, rightKeyPressed, downKeyPressed, slowModeKeyPressed;
	public int jumpChar = KeyEvent.VK_W, jumpChar2 = KeyEvent.VK_SPACE, leftChar = KeyEvent.VK_A, rightChar = KeyEvent.VK_D, downChar = KeyEvent.VK_S, slowModeChar = KeyEvent.VK_SHIFT, pauseChar = KeyEvent.VK_ESCAPE;
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == jumpChar || e.getKeyCode() == jumpChar2) {
			jumpKeyPressed = true;
		}
		if (e.getKeyCode() == leftChar) {
			leftKeyPressed = true;
		}
		if (e.getKeyCode() == rightChar) {
			rightKeyPressed = true;
		}
		if (e.getKeyCode() == downChar) {
			downKeyPressed = true;
		}
		if (e.getKeyCode() == slowModeChar) {
			slowModeKeyPressed = true;
		}
		if (e.getKeyCode() == pauseChar) {
			Game.togglePaused();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		
		if (e.getKeyCode() == jumpChar || e.getKeyCode() == jumpChar2) {
			jumpKeyPressed = false;
		}
		if (e.getKeyCode() == leftChar) {
			leftKeyPressed = false;
		}
		if (e.getKeyCode() == rightChar) {
			rightKeyPressed = false;
		}
		if (e.getKeyCode() == downChar) {
			downKeyPressed = false;
		}
		if (e.getKeyCode() == slowModeChar) {
			slowModeKeyPressed = false;
		}

	}

}
