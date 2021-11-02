import javax.swing.*;
import java.awt.event.*;
import javax.sound.midi.*;
import java.util.*;

/**
 * Handles mouse press, release, and drag events on the Piano.
 */
public class PianoMouseListener extends MouseAdapter {
	// You are free to add more instance variables if you wish.
	private ArrayList<Key> _keys;
	private Key _onKey; // the key that is currently pressed

	/**
	 * @param keys the list of keys in the piano.
	 */
	public PianoMouseListener(ArrayList<Key> keys) {
		_keys = keys;
	}

	@Override
	/**
	 * This method is called by Swing whenever the user drags the mouse.
	 * 
	 * @param e the MouseEvent containing the (x,y) location, relative to the
	 *          upper-left-hand corner of the entire piano, of where the mouse is
	 *          currently located.
	 */
	public void mouseDragged(MouseEvent e) {
		for (Key k : _keys) {
			if (k.getPolygon().contains(e.getX(), e.getY()) && k != _onKey) {
				if (_onKey != null) {
					_onKey.play(false);
				}
				_onKey = k;
				_onKey.play(true);
			}
		}
	}

	@Override
	/**
	 * This method is called by Swing whenever the user presses the mouse.
	 * 
	 * @param e the MouseEvent containing the (x,y) location, relative to the
	 *          upper-left-hand corner of the entire piano, of where the mouse is
	 *          currently located.
	 */
	public void mousePressed(MouseEvent e) {
		for (Key key : _keys) {
			if (key.getPolygon().contains(e.getX(), e.getY()) && key != _onKey) {
				key.play(true); // Note that the key should eventually be turned off!
				_onKey = key;
				System.out.println("This key was pressed: " + key);
			}
		}
	}

	@Override
	/**
	 * This method is called by Swing whenever the user releases the mouse.
	 * 
	 * @param e the MouseEvent containing the (x,y) location, relative to the
	 *          upper-left-hand corner of the entire piano, of where the mouse is
	 *          currently located.
	 */
	public void mouseReleased(MouseEvent e) {
		if (_onKey != null) {
			_onKey.play(false);
			_onKey = null;
		}
	}
}
