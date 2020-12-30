package engine;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A class containing useful IO methods
 * @author nathan
 *
 */
public abstract class GameAPI {
	
	/**
	 * Makes a deep copy of the given LinkedList of characters.
	 * @param keys The list to copy
	 * @return A copy of the original list
	 */
	private static LinkedList<Character> cloneKeyList (LinkedList<Character> keys) {
		LinkedList<Character> copy = new LinkedList<Character> ();
		Iterator<Character> iter = keys.iterator ();
		while (iter.hasNext ()) {
			copy.add (new Character (iter.next ()));
		}
		return copy;
	}

	/**
	 * Returns true if the key with the given character code was pressed down since the last call to resetKeyBuffers().
	 * @param charCode The character code to check for
	 * @return Whether the given key was pressed
	 */
	public boolean keyPressed (int charCode) {
		return GameLoop.getInputImage ().keyPressed (charCode);
	}
	
	/**
	 * Returns true if the key with the given character code was released since the last call to resetKeyBuffers().
	 * @param charCode The character code to check for
	 * @return Whether the given key was released
	 */
	public boolean keyReleased (int charCode) {
		return GameLoop.getInputImage ().keyReleased (charCode);
	}
	
	/**
	 * Returns true if the key with the given character code is currently being pressed down.
	 * @param charCode The character code to check for
	 * @return Whether the given key is pressed down
	 */
	public boolean keyDown (int charCode) {
		return GameLoop.getInputImage ().keyDown (charCode);
	}
	
	/**
	 * Gets a list of the keys currently pressed down, sorted from lowest to highest character code.
	 * @return A char[] representing all the keys currently pressed down
	 */
	public char[] getKeysDown () {
		return GameLoop.getInputImage ().getKeysDown ();
	}
	
	/**
	 * Gets a list of all the keys events fired since the last call to resetKeyBuffers() in the order they were pressed.
	 * @return A LinkedList representing all of the key events
	 */
	public LinkedList<KeyEvent> getKeyEvents () {
		return GameLoop.getInputImage ().getKeyEvents ();
	}
	
	/**
	 * Gets a list of the text typed since the last call to resetKeyBuffers(); responds to key combinations.
	 * @return The text typed since calling resetKeyBuffers
	 */
	public String getTyped () {
		return GameLoop.getInputImage ().getTyped ();
	}
	
	/**
	 * Returns true if the given mouse button was clicked.
	 * @param button The mouse button to check
	 * @return Whether the button is clicked
	 */
	public boolean mouseButtonClicked (int button) {
		return GameLoop.getInputImage ().mouseButtonClicked (button);
	}
	
	/**
	 * Returns true if the given mouse button was released.
	 * @param button The mouse button to check
	 * @return Whether the button was released
	 */
	public boolean mouseButtonReleased (int button) {
		return GameLoop.getInputImage ().mouseButtonReleased (button);
	}
	
	/**
	 * Returns true if the given mouse button is held down.
	 * @param button The mouse button to check
	 * @return Whether the button is held down
	 */
	public boolean mouseButtonDown (int button) {
		return GameLoop.getInputImage ().mouseButtonDown (button);
	}
	
	/**
	 * Gets the current x-coordinate of the mouse cursor.
	 * @return The cursor x-coordinate
	 */
	public int getCursorX () {
		return 0;
	}
	
	/**
	 * Gets the current y-coordinate of the mouse cursor.
	 * @return The cursor y-coordinate
	 */
	public int getCursorY () {
		return 0;
	}
	
	/**
	 * Gets a list of all the mosue events fired since the last call to resetKeyBuffers() in the order they were pressed.
	 * @return A LinkedList representing all of the recent mouse events
	 */
	public LinkedList<MouseEvent> getMouseEvents () {
		return GameLoop.getInputImage ().getMouseEvents ();
	}
}
