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
	public static int NUM_BLACK_KEYS_PER_OCTAVE = 5;
	public static int NUM_KEYS_PER_OCTAVE = NUM_WHITE_KEYS_PER_OCTAVE + NUM_BLACK_KEYS_PER_OCTAVE;
	public static int NUM_OCTAVES = 3;
	public static int NUM_WHITE_KEYS = NUM_WHITE_KEYS_PER_OCTAVE * NUM_OCTAVES;
	public static int WIDTH = NUM_WHITE_KEYS * WHITE_KEY_WIDTH;
	public static int HEIGHT = WHITE_KEY_HEIGHT;
	public static int TOP = 0;

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
	 * Creates a key for the piano
	 * 
	 * @param upperLeftX the x-coordinate of the top-left corner of the key
	 * @param upperWidth the width of the key at the top edge
	 * @param lowerLeftX the x-coordinate of the top-left corner of the key
	 * @param lowerWidth the width of the key at the bottom edge
	 * @param height     the height of the key
	 * @param color      the color of the key
	 * @param pitch      the pitch of the key
	 * 
	 * @return the key
	 */
	private Key makeKey(int lowerLeftX, int lowerWidth, int upperLeftX, int upperWidth, int height, Color color,
			int pitch) {
		/*-
		 * Example Key: left white key
		 * upperLeftX -> ____					|
		 *				 |  |					|
		 *				 |  |___ <- upperRightX |
		 *				 |	   |				|	
		 *				 |	   |				|
		 * lowerLeftX -> |_____| <- lowerRightX | <- height
		 */

		// Drawing from upper left to right and then down, left, up again.
		final int[] xPoints = { upperLeftX, upperLeftX + upperWidth, upperLeftX + upperWidth, lowerLeftX + lowerWidth,
				lowerLeftX + lowerWidth, lowerLeftX, lowerLeftX, upperLeftX, upperLeftX };

		// If it's a black key, the height is the same as the black key height, so we
		// can use this as a common abstraction
		final int[] yPoints = { TOP, TOP, BLACK_KEY_HEIGHT, BLACK_KEY_HEIGHT, height, height, BLACK_KEY_HEIGHT,
				BLACK_KEY_HEIGHT, TOP };

		final Polygon polygon = new Polygon(xPoints, yPoints, xPoints.length);
		return new Key(polygon, color, pitch, this);
	}

	/**
	 * Creates a black key for the piano with bottom-left corner at (lowerLeftX,
	 * BLACK_KEY_HEIGHT) and width and height specified by the constants
	 * BLACK_KEY_WIDTH and BLACK_KEY_HEIGHT.
	 *
	 * @param lowerLeftX the x-coordinate of the bottom-left corner of the key
	 * @param pitch      the pitch of the key
	 * @return the black key
	 */
	private Key makeBlackKey(int lowerLeftX, int pitch) {
		final int upperLeftX = lowerLeftX;
		final int upperWidth = BLACK_KEY_WIDTH;
		final int lowerWidth = BLACK_KEY_WIDTH;
		final int height = BLACK_KEY_HEIGHT;

		return makeKey(lowerLeftX, lowerWidth, upperLeftX, upperWidth, height, Color.BLACK, pitch);
	}

	/**
	 * Creates a white key for the piano with bottom-left corner at (lowerLeftX,
	 * WHITE_KEY_HEIGHT) and width and height specified by the constants
	 * WHITE_KEY_WIDTH and WHITE_KEY_HEIGHT.
	 *
	 * @param lowerLeftX the x-coordinate of the bottom-left corner of the key
	 * @param upperLeftX the x-coordinate of the top-left corner of the key
	 * @param upperWidth the width of the key at the top edge
	 * @param pitch      the pitch of the key
	 * @return the white key
	 */
	private Key makeWhiteKey(int lowerLeftX, int upperLeftX, int upperWidth, int pitch) {
		final int lowerWidth = WHITE_KEY_WIDTH;
		final int height = WHITE_KEY_HEIGHT;

		return makeKey(lowerLeftX, lowerWidth, upperLeftX, upperWidth, height, Color.WHITE, pitch);
	}

	/**
	 * Creates a left white key for the piano with bottom-left corner at
	 * (lowerLeftX, WHITE_KEY_HEIGHT)
	 *
	 * @param lowerLeftX the x-coordinate of the bottom-left corner of the key
	 * @param pitch      the pitch of the key
	 * @return
	 */
	private Key makeLeftWhiteKey(int lowerLeftX, int pitch) {
		final int upperLeftX = lowerLeftX;
		final int upperWidth = WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2;

		return makeWhiteKey(lowerLeftX, upperLeftX, upperWidth, pitch);
	}

	/**
	 * Creates a right white key for the piano with bottom-left corner at
	 * (lowerLeftX, WHITE_KEY_HEIGHT)
	 *
	 * @param lowerLeftX the x-coordinate of the bottom-left corner of the key
	 * @param pitch      the pitch of the key
	 * @return
	 */
	private Key makeRightWhiteKey(int lowerLeftX, int pitch) {
		int upperLeftX = lowerLeftX + BLACK_KEY_WIDTH / 2;
		int upperWidth = WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2;

		return makeWhiteKey(lowerLeftX, upperLeftX, upperWidth, pitch);
	}

	/**
	 * Creates a middle white key for the piano with bottom-left corner at
	 * (lowerLeftX, WHITE_KEY_HEIGHT)
	 *
	 * @param lowerLeftX the x-coordinate of the bottom-left corner of the key
	 * @param pitch      the pitch of the key
	 * @return
	 */
	private Key makeMiddleWhiteKey(int lowerLeftX, int pitch) {
		final int upperLeftX = lowerLeftX + BLACK_KEY_WIDTH / 2;
		final int upperWidth = WHITE_KEY_WIDTH - BLACK_KEY_WIDTH;

		return makeWhiteKey(lowerLeftX, upperLeftX, upperWidth, pitch);
	}

	/**
	 * Creates an octave for the piano
	 *
	 * @param leftX  the x-coordinate of the leftmost key of the octave
	 * @param octave the octave number (starting at 0)
	 * @return
	 */
	private void makeOctave(int leftX, int octave) {
		final int firstPitch = octave * NUM_KEYS_PER_OCTAVE + START_PITCH;
		final Key[] keys = { // Declarative list of all the keys in an octave
				makeLeftWhiteKey(leftX, firstPitch),
				makeBlackKey(leftX + WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2, firstPitch + 1),
				makeMiddleWhiteKey(leftX + WHITE_KEY_WIDTH, firstPitch + 2),
				makeBlackKey(leftX + 2 * WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2, firstPitch + 3),
				makeRightWhiteKey(leftX + WHITE_KEY_WIDTH * 2, firstPitch + 4),

				makeLeftWhiteKey(leftX + WHITE_KEY_WIDTH * 3, firstPitch + 5),
				makeBlackKey(leftX + 4 * WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2, firstPitch + 6),
				makeMiddleWhiteKey(leftX + WHITE_KEY_WIDTH * 4, firstPitch + 7),
				makeBlackKey(leftX + WHITE_KEY_WIDTH * 5 - BLACK_KEY_WIDTH / 2, firstPitch + 8),
				makeMiddleWhiteKey(leftX + WHITE_KEY_WIDTH * 5, firstPitch + 9),
				makeBlackKey(leftX + WHITE_KEY_WIDTH * 6 - BLACK_KEY_WIDTH / 2, firstPitch + 10),
				makeRightWhiteKey(leftX + WHITE_KEY_WIDTH * 6, firstPitch + 11), };
		_keys.addAll(Arrays.asList(keys));
	}

	/**
	 * Instantiate all the Key objects with their correct polygons and pitches, and
	 * add them to the _keys array.
	 */
	private void makeKeys() {
		for (int i = 0; i < NUM_OCTAVES; i++) {
			makeOctave(WHITE_KEY_WIDTH * NUM_WHITE_KEYS_PER_OCTAVE * i, i);
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