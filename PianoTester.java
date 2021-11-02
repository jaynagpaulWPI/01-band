import static org.junit.jupiter.api.Assertions.*;
import javax.swing.*;
import javax.sound.midi.*;
import java.awt.event.*;
import org.junit.jupiter.api.*;

/**
 * Contains a set of unit tests for the Piano class.
 */
class PianoTester {
	private TestReceiver _receiver;
	private Piano _piano;
	private PianoMouseListener _mouseListener;

	private MouseEvent makeMouseEvent(int x, int y) {
		return new MouseEvent(_piano, 0, 0, 0, x, y, 0, false);
	}

	@BeforeEach
	void setup() {
		// A new TestReceiver will be created before running *each*
		// test. Hence, the "turn on" and "turn off" counts will be
		// reset to 0 before *each* test.
		_receiver = new TestReceiver();
		_piano = new Piano(_receiver);
		_mouseListener = _piano.getMouseListener();
	}

	@Test
	void testClickUpperLeftMostPixel() {
		// Pressing the mouse should cause the key to turn on.
		_mouseListener.mousePressed(makeMouseEvent(0, 1));
		assertTrue(_receiver.isKeyOn(Piano.START_PITCH));
	}

	@Test
	void testDragWithinKey() {
		// Test that pressing and dragging the mouse *within* the same key
		// should cause the key to be turned on only once, not multiple times.
		// Use makeMouseEvent and TestReceiver.getKeyOnCount.
		// TODO complete me

		_mouseListener.mouseDragged(makeMouseEvent(0, 1));
		assertEquals(_receiver.getKeyOnCount(Piano.START_PITCH), 1);
	}

	// TODO write at least 3 more tests!

	@Test
	void testKeyReleaseOutOfBounds() {
		// original test #1
		// Test that pressing a key and dragging out of bounds before
		// releasing will stop the key from playing

		_mouseListener.mouseDragged(makeMouseEvent(0, 1));
		_mouseListener.mouseDragged(makeMouseEvent(0, Piano.WHITE_KEY_HEIGHT * 3));
		_mouseListener.mouseReleased(makeMouseEvent(0, Piano.WHITE_KEY_HEIGHT * 3));

		assertEquals(_receiver.getKeyOffCount(Piano.START_PITCH), 1);
		assertFalse(_receiver.isKeyOn(Piano.START_PITCH));
	}

	@Test
	void testKeyTurnedOff() {
		// original test #2
		// Test that releasing a key turns it off

		_mouseListener.mousePressed(makeMouseEvent(0, 1));
		_mouseListener.mouseReleased(makeMouseEvent(0, 1));

		assertEquals(_receiver.getKeyOffCount(Piano.START_PITCH), 1);
	}

	@Test
	void testDragToDifferentKey() {
		// original test #36
		// Test that pressing and dragging the mouse to a new key
		// should cause the previous key to be turned off and the new
		// key to be turned on

		_mouseListener.mouseDragged(makeMouseEvent(0, 1));
		_mouseListener.mouseDragged(makeMouseEvent(Piano.WHITE_KEY_WIDTH + 1, 1));

		assertEquals(_receiver.getKeyOffCount(Piano.START_PITCH), 1);
		assertTrue(_receiver.isKeyOn(Piano.START_PITCH + 1));

	}
}
