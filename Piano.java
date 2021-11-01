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

	private ArrayList<Key> _keys = new ArrayList<>();
	private Receiver _receiver;
	private PianoMouseListener _mouseListener;

	/**
	 * Returns the list of keys in the piano.
	 * 
	 * @return the list of keys.
	 */
	public java.util.List<Key> getKeys() {
		return _keys;
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
	 * Creates a black key for the piano with top-left corner at (leftX, 0) and
	 * width and height specified by the constants BLACK_KEY_WIDTH and
	 * BLACK_KEY_HEIGHT.
	 *
	 * @param topLeft
	 * @return
	 */
	private Key makeBlackKey(int leftX, int pitch) {
		final int[] xPoints = { leftX, leftX + BLACK_KEY_WIDTH, leftX + BLACK_KEY_WIDTH, leftX };
		final int[] yPoints = { 0, 0, BLACK_KEY_HEIGHT, BLACK_KEY_HEIGHT };
		final Polygon polygon = new Polygon(xPoints, yPoints, xPoints.length);
		Key key = new Key(polygon, Color.BLACK, pitch, this);
		return key;
	}

	/**
	 * Creates a left white key for the piano with top-left corner at (leftX, 0)
	 *
	 * @param leftX
	 * @return
	 */
	private Key makeLeftWhiteKey(int leftX, int pitch) {
		// Not drawing a rectangle for the white key because it is easier to
		// draw a polygon around the black key.
		int upperRightX = leftX + WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2;
		int lowerRightX = leftX + WHITE_KEY_WIDTH;

		final int[] xPoints = { leftX, upperRightX, upperRightX, lowerRightX, lowerRightX, leftX };
		final int[] yPoints = { 0, 0, BLACK_KEY_HEIGHT, BLACK_KEY_HEIGHT, WHITE_KEY_HEIGHT, WHITE_KEY_HEIGHT, 0 };
		final Polygon polygon = new Polygon(xPoints, yPoints, xPoints.length);
		Key key = new Key(polygon, Color.WHITE, pitch, this);
		return key;
	}

	/**
	 * Creates a right white key for the piano with top-left corner at (leftX, 0)
	 *
	 * @param leftX
	 * @return
	 */
	private Key makeRightWhiteKey(int leftX, int pitch) {
		// Not drawing a rectangle for the white key because it is easier to
		// draw a polygon around the black key.
		int upperLeftX = leftX + BLACK_KEY_WIDTH / 2;
		int lowerLeftX = leftX;
		int rightX = leftX + WHITE_KEY_WIDTH;

		final int[] xPoints = { upperLeftX, rightX, rightX, lowerLeftX, lowerLeftX, upperLeftX, upperLeftX };
		final int[] yPoints = { 0, 0, WHITE_KEY_HEIGHT, WHITE_KEY_HEIGHT, BLACK_KEY_HEIGHT, BLACK_KEY_HEIGHT, 0 };
		final Polygon polygon = new Polygon(xPoints, yPoints, xPoints.length);
		Key key = new Key(polygon, Color.WHITE, pitch, this);
		return key;
	}

	/**
	 * Creates a middle white key for the piano with top-left corner at (leftX, 0)
	 *
	 * @param leftX
	 * @return
	 */
	private Key makeMiddleWhiteKey(int leftX, int pitch) {
		// Not drawing a rectangle for the white key because it is easier to
		// draw a polygon around the black key.
		// TODO: better var names and refactor
		int upperLeftX = leftX + BLACK_KEY_WIDTH / 2;
		int lowerLeftX = leftX;
		int upperRightX = leftX + WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2;
		int lowerRightX = leftX + WHITE_KEY_WIDTH;

		final int[] xPoints = { upperLeftX, upperRightX, upperRightX, lowerRightX, lowerRightX, lowerLeftX, lowerLeftX,
				upperLeftX, upperLeftX };
		final int[] yPoints = { 0, 0, BLACK_KEY_HEIGHT, BLACK_KEY_HEIGHT, WHITE_KEY_HEIGHT, WHITE_KEY_HEIGHT,
				BLACK_KEY_HEIGHT, BLACK_KEY_HEIGHT, 0 };

		final Polygon polygon = new Polygon(xPoints, yPoints, xPoints.length);
		Key key = new Key(polygon, Color.WHITE, pitch, this);
		return key;
	}

	/**
	 * TODO: Implement this method. Creates the octave of both white and black keys.
	 *
	 * @param leftX
	 * @return
	 */
	private void makeOctave(int leftX, int octave) {
		_keys.add(makeBlackKey(leftX + WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2, octave*12+START_PITCH+1));
		_keys.add(makeLeftWhiteKey(leftX,  octave*12+START_PITCH));
		_keys.add(makeMiddleWhiteKey(leftX + WHITE_KEY_WIDTH,  octave*12+START_PITCH+2));
		_keys.add(makeBlackKey(leftX + 2 * WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2,  octave*12+START_PITCH+3));
		_keys.add(makeRightWhiteKey(leftX + WHITE_KEY_WIDTH * 2, octave*12+START_PITCH+4));
		_keys.add(makeLeftWhiteKey(leftX + WHITE_KEY_WIDTH * 3,  octave*12+START_PITCH+5));
		_keys.add(makeBlackKey(leftX + 4 * WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2,  octave*12+START_PITCH+6));
		_keys.add(makeMiddleWhiteKey(leftX + WHITE_KEY_WIDTH * 4, octave*12+START_PITCH+7));
		_keys.add(makeBlackKey(leftX + WHITE_KEY_WIDTH * 5 - BLACK_KEY_WIDTH / 2,  octave*12+START_PITCH+8));
		_keys.add(makeMiddleWhiteKey(leftX + WHITE_KEY_WIDTH * 5, octave*12+START_PITCH+9));
		_keys.add(makeBlackKey(leftX + WHITE_KEY_WIDTH * 6 - BLACK_KEY_WIDTH / 2,  octave*12+START_PITCH+10));
		_keys.add(makeRightWhiteKey(leftX + WHITE_KEY_WIDTH * 6, octave*12+START_PITCH+11));
	}

	// TODO: implement this method. You should create and use several helper methods
	// to do so.
	/**
	 * Instantiate all the Key objects with their correct polygons and pitches, and
	 * add them to the _keys array.
	 */
	private void makeKeys() {
		for( int i=0; i<NUM_OCTAVES; i++) {
		makeOctave(WHITE_KEY_WIDTH * 7*i, i);
		}

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