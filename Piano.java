import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.sound.midi.*;

/**
 * Implements a simulated piano with 36 keys.
 */
public class Piano extends JPanel {
	// DO NOT MODIFY THESE CONSTANTS
	public static int START_PITCH = 48;
	public static int WHITE_KEY_WIDTH = 40;
	public static int BLACK_KEY_WIDTH = WHITE_KEY_WIDTH / 2;
	public static int WHITE_KEY_HEIGHT = 200;
	public static int BLACK_KEY_HEIGHT = WHITE_KEY_HEIGHT / 2;
	public static int NUM_WHITE_KEYS_PER_OCTAVE = 7;
	public static int NUM_OCTAVES = 3;
	public static int NUM_WHITE_KEYS = NUM_WHITE_KEYS_PER_OCTAVE * NUM_OCTAVES;
	public static int WIDTH = NUM_WHITE_KEYS * WHITE_KEY_WIDTH;
	public static int HEIGHT = WHITE_KEY_HEIGHT;

	private ArrayList<Key> _black_keys = new ArrayList<>();
	private ArrayList<Key> _white_keys = new ArrayList<>();
	private Receiver _receiver;
	private PianoMouseListener _mouseListener;

	/**
	 * Returns the list of black keys in the piano.
	 * 
	 * @return the list of black keys.
	 */
	public java.util.List<Key> getBlackKeys() {
		return _black_keys;
	}

	/**
	 * Returns the list of white keys in the piano.
	 * 
	 * @return the list of white keys.
	 */
	public java.util.List<Key> getWhiteKeys() {
		return _white_keys;
	}

	/**
	 * Sets the MIDI receiver of the piano to the specified value.
	 * 
	 * @param receiver the MIDI receiver
	 */
	public void setReceiver(Receiver receiver) {
		_receiver = receiver;
	}

	/**
	 * Returns the current MIDI receiver of the piano.
	 * 
	 * @return the current MIDI receiver
	 */
	public Receiver getReceiver() {
		return _receiver;
	}

	// DO NOT MODIFY THIS METHOD.
	/**
	 * @param receiver the MIDI receiver to use in the piano.
	 */
	public Piano(Receiver receiver) {
		// Some Swing setup stuff; don't worry too much about it.
		setFocusable(true);
		setLayout(null);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		setReceiver(receiver);
		_mouseListener = new PianoMouseListener(_keys);
		addMouseListener(_mouseListener);
		addMouseMotionListener(_mouseListener);
		makeKeys();
	}

	/**
	 * Returns the PianoMouseListener associated with the piano.
	 * 
	 * @return the PianoMouseListener associated with the piano.
	 */
	public PianoMouseListener getMouseListener() {
		return _mouseListener;
	}

	/**
	 * Creates a black key for the piano with top-left corner at (topLeftX, 0) and
	 * width and height specified by the constants BLACK_KEY_WIDTH and
	 * BLACK_KEY_HEIGHT.
	 *
	 * @param topLeft
	 * @return
	 */
	private Key makeBlackKey(int topLeftX) {
		final int[] xPoints = { topLeftX, topLeftX + BLACK_KEY_WIDTH, topLeftX + BLACK_KEY_WIDTH, topLeftX };
		final int[] yPoints = { 0, 0, BLACK_KEY_HEIGHT, BLACK_KEY_HEIGHT };
		final Polygon polygon = new Polygon(xPoints, yPoints, xPoints.length);
		Key key = new Key(polygon, Color.BLACK, START_PITCH, this);
		_keys.add(key);
		return key;
	}

	// TODO: implement this method. You should create and use several helper methods
	// to do so.
	/**
	 * Instantiate all the Key objects with their correct polygons and pitches, and
	 * add them to the _keys array.
	 */
	private void makeKeys() {
		// Just as an example, this draws the left-most black key at its proper
		// position.
		final int[] xCoords = new int[] { WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2, WHITE_KEY_WIDTH + BLACK_KEY_WIDTH / 2,
				WHITE_KEY_WIDTH + BLACK_KEY_WIDTH / 2, WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2 };
		final int[] yCoords = new int[] { 0, 0, BLACK_KEY_HEIGHT, BLACK_KEY_HEIGHT };
		final Polygon polygon = new Polygon(xCoords, yCoords, xCoords.length);
		final Key key = new Key(polygon, Color.BLACK, START_PITCH, this);

		// Add this key to the list of keys so that it gets painted.
		_keys.add(key);
	}

	// DO NOT MODIFY THIS METHOD.
	@Override
	/**
	 * Paints the piano and all its constituent keys.
	 * 
	 * @param g the Graphics object to use for painting.
	 */
	public void paint(Graphics g) {
		// Delegates to all the individual keys to draw themselves.
		for (Key key : _keys) {
			key.paint(g);
		}
	}
}
