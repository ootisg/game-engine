package engine;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * A loop for the game logic; mostly copy-pasted from RenderLoop
 * @author nathan
 *
 */
public class GameLoop implements Runnable {

	/**
	 * The maximum framerate the game can run at
	 */
	public static final double stepsPerSecond = 60;
	/**
	 * The time of the last update to the GameWindow, in nanoseconds.
	 */
	static private long lastUpdate;
	/**
	 * The image of the input from the past GameLogic frame
	 */
	static private InputManager inputImage;
	
	@Override
	public void run () {
		while (true) {
			//Get the target time in nanoseconds for this iteration; should be constant if stepsPerSecond doesn't change
			long targetNanoseconds = (long)(1000000000 / stepsPerSecond);
			//Get the time before running the game logic
			long startTime = System.nanoTime ();
			//doGameLogic
			inputImage = RenderLoop.wind.getInputImage ();
			ObjectHandler.callAll ();
			RenderLoop.wind.resetInputBuffers ();
			//Calculate elapsed time and time to sleep for
			lastUpdate = System.nanoTime ();
			long elapsedTime = lastUpdate - startTime;
			int sleepTime = (int)((targetNanoseconds - elapsedTime) / 1000000) - 1;
			if (sleepTime < 0) {
				sleepTime = 0;
			}
			//Sleep until ~1ms before it's time to calculate the next step
			try {
				Thread.currentThread ().sleep (sleepTime);
			} catch (InterruptedException e) {
				//Do nothing; the while loop immediately after handles this case well
			}
			//Wait until the next step should be executed
			while (System.nanoTime () - startTime < targetNanoseconds) {
				
			}
		}
	}
	
	/**
	 * Gets the input image from the start of this game logic iteration.
	 * @return The input image from the start of this iteration
	 */
	public static InputManager getInputImage () {
		return inputImage;
	}
}