package engine;

public class AnimationHandler {
	
	/**
	 * The image for this AnimationHandler to draw
	 */
	private Sprite image;
	
	/**
	 * The frame which corresponds to the starting time
	 */
	private int startFrame;
	
	/**
	 * The time the animation started
	 */
	private long startTime;
	
	/**
	 * The time of each frame, in milliseconds
	 */
	private double frameTime;
	
	/**
	 * Whether or not this animation repeats
	 */
	private boolean repeat;
	
	/**
	 * Constructs a new AnimationHandler with the given image, defaulting to a static image.
	 * @param image The image to use
	 */
	public AnimationHandler (Sprite image) {
		this (image, 0);
		repeat = true;
	}
	
	/**
	 * Constructs a new AnimationHandler with the given image, with each frame lasting for frameTime milliseconds.
	 * @param image The image to use
	 * @param frameTime The numer of milliseconds to show each frame for; displays a static image if set to 0
	 */
	public AnimationHandler (Sprite image, double frameTime) {
		this.image = image;
		startTime = RenderLoop.frameStartTime ();
		startFrame = 0;
		this.frameTime = frameTime;
		repeat = true;
	}
	
	/**
	 * Draws the sprite's current animation frame at the given x and y coordinates.
	 * @param x The x coordinate to draw at
	 * @param y The y coordinate to draw at
	 */
	public void draw (double x, double y) {
		if (image != null) {
			if (frameTime == 0) {
				startTime = RenderLoop.frameStartTime ();
				image.draw ((int)x, (int)y, startFrame);
			} else {
				long elapsedTime = RenderLoop.frameStartTime () - startTime;
				int elapsedFrames = ((int)(((double)elapsedTime) / ((double)frameTime)) + startFrame);
				if (!repeat && elapsedFrames >= image.getFrameCount ()) {
					image.draw ((int)x, (int)y, image.getFrameCount () - 1);
				} else {
					int frame = elapsedFrames % image.getFrameCount ();
					image.draw ((int)x, (int)y, frame);
				}
			}
		}
	}
	
	/**
	 * Sets the image used by this AnimationHandler to the given sprite.
	 * @param image The image to use
	 */
	public void setImage (Sprite image) {
		this.image = image;
	}
	
	/**
	 * Sets the image used by this AnimationHandler to the given sprite, and restarts the animation from the beginning.
	 * @param image The image to use
	 */
	public void resetImage (Sprite image) {
		setImage (image);
		startTime = RenderLoop.frameStartTime ();
		startFrame = 0;
	}
	
	/**
	 * Sets the current frame of the animation to the given frame.
	 * @param frame The frame to use
	 */
	public void setAnimationFrame (int frame) {
		startFrame = frame;
		startTime = RenderLoop.frameStartTime ();
	}
	
	/**
	 * Sets the time between frames to the given time.
	 * @param frameTime The time to use, in milliseconds
	 */
	public void setFrameTime (double frameTime) {
		this.frameTime = frameTime;
	}
	
	/**
	 * Sets whether this animation repeats or not after it is complete.
	 * @param repeats Whether the animation repeats
	 */
	public void setRepeat (boolean repeats) {
		repeat = repeats;
	}
	
	/**
	 * Gets the image used by this AnimationHandler.
	 * @return The image field for this AnimationHandler
	 */
	public Sprite getImage () {
		return image;
	}
	
	/**
	 * Gets the frame which would be drawn at the time this method is called. Not guarenteed to be the frame that will be drawn next.
	 * @return The current frame for this AnimationHandler
	 */
	public int getFrame () {
		if (image != null) {
			long elapsedTime = RenderLoop.frameStartTime () - startTime;
			return ((int)(((double)elapsedTime) / ((double)frameTime)) + startFrame) % image.getFrameCount ();
		} else {
			return -1;
		}
	}
	
	/**
	 * Gets the time each frame should be displayed.
	 * @return The length of time, in milliseconds, to display each frame
	 */
	public double getFrameTime () {
		return frameTime;
	}
	
	/**
	 * Returns true if this animation is set to repeat; false otherwise.
	 * @return Whether this animation repeats or not
	 */
	public boolean repeats () {
		return repeat;
	}
}
