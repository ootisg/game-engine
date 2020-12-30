package engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Represents a drawable image
 * @author nathan
 *
 */
public class Sprite {
	/**
	 * The global image cache
	 */
	private static HashMap<String, CacheNode> cache = new HashMap<String, CacheNode> ();
	/**
	 * The image data this sprite contains
	 */
	private BufferedImage[] images;
	/**
	 * The filepath to this sprite, if applicable
	 */
	private String imagePath;
	/**
	 * The filepath to the parsing parameters, if applicable
	 */
	private String parsePath;
	/**
	 * Whether the sprite is animated or not
	 */
	private boolean isAnimated;
	
	/**
	 * Constructs a sprite with the given image filepath and parsing parameter filepath.
	 * @param imagepath The filepath to the image to use
	 * @param parsepath The filepath to the parsing parameters to use
	 */
	public Sprite (String imagepath, String parsepath) {
		this.imagePath = imagepath;
		this.parsePath = parsepath;
		String key = imagepath + ":" + parsepath;
		CacheNode data = cache.get (key);
		if (data == null) {
			SpriteParser parser = new SpriteParser (parsepath);
			File imageFile = new File (imagepath);
			BufferedImage img = null;
			try {
				img = ImageIO.read (imageFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			images = parser.parse (img);
			cache.put (key, new CacheNode (key, images));
		} else {
			images = data.getData ();
		}
		if (images.length > 1) {
			isAnimated = true;
		} else {
			isAnimated = false;
		}
	}
	
	/**
	 * Constructs a sprite with the given image and parsing parameter filepath. Does not support caching.
	 * @param image The image to use
	 * @param parsepath The filepath to the parsing parameters to use
	 */
	public Sprite (BufferedImage image, String parsepath) {
		this.parsePath = parsepath;
		SpriteParser parser = new SpriteParser (parsepath);
		images = parser.parse (image);
		if (images.length > 1) {
			isAnimated = true;
		} else {
			isAnimated = false;
		}
	}
	
	/**
	 * Constructs a sprite with the given image and parser. Does not support caching.
	 * @param image The image to use
	 * @param parser The parser to use
	 */
	public Sprite (BufferedImage image, SpriteParser parser) {
		images = parser.parse (image);
		if (images.length > 1) {
			isAnimated = true;
		} else {
			isAnimated = false;
		}
	}
	
	/**
	 * Constructs a sprite with the given image path and parser. Does not support caching.
	 * @param imagepath The image to use
	 * @param parser The parser to use
	 */
	public Sprite (String imagepath, SpriteParser parser) {
		this.imagePath = imagepath;
		File imageFile = new File (imagepath);
		BufferedImage img = null;
		try {
			img = ImageIO.read (imageFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		images = parser.parse (img);
		if (images.length > 1) {
			isAnimated = true;
		} else {
			isAnimated = false;
		}
	}
	
	/**
	 * Constructs a sprite with the given image filepath.
	 * @param imagepath The filepath to use for the image
	 */
	public Sprite (String imagepath) {
		this.imagePath = imagepath;
		CacheNode data = cache.get (imagepath);
		if (data == null) {
			File imageFile = new File (imagepath);
			BufferedImage img = null;
			try {
				img = ImageIO.read (imageFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			images = new BufferedImage[] {img};
			cache.put (imagepath, new CacheNode (imagepath, images));
		} else {
			images = data.getData ();
		}
		isAnimated = false;
	}
	
	/**
	 * Copy constructor for ease of inheretence
	 * @param sprite The Sprite object to copy
	 */
	public Sprite (Sprite sprite) {
		this.images = sprite.images;
		this.isAnimated = sprite.isAnimated;
		this.imagePath = sprite.imagePath;
		this.parsePath = sprite.parsePath;
	}
	
	/**
	 * Gets the frame count of this sprite.
	 * @return The number of frames in this sprite
	 */
	public int getFrameCount () {
		return images.length;
	}
	
	/**
	 * Constructs a sprite with the given image. Does not support caching.
	 * @param image The image to use
	 */
	public Sprite (BufferedImage image) {
		images = new BufferedImage[] {image};
	}
	
	/**
	 * Draws the first frame of this sprite at the given x and y coordinates.
	 * @param usedX The x coordinate to draw this sprite at
	 * @param usedY The y coordinate to draw this sprite at
	 */
	public void draw (int usedX, int usedY) {
		draw (usedX, usedY, 0);
	}
	
	/**
	 * Draws the given frame of this sprite at the given x and y coordinates.
	 * @param usedX The x coordinate to draw this sprite at
	 * @param usedY The y coordinate to draw this sprite at
	 * @param frame The frame of this sprite to draw
	 */
	public void draw (int usedX, int usedY, int frame) {
		if (frame < images.length) {
			RenderLoop.wind.getBufferGraphics ().drawImage (images [frame], usedX, usedY, null);
		}
	}
	
	/**
	 * Gets the BufferedImage representing the given frame of the sprite.
	 * @param frame The frame to get
	 * @return the given frame
	 */
	public BufferedImage getFrame (int frame) {
		return images [frame];
	}
	
	/**
	 * Gets the filepath of the image used to create this sprite.
	 * @return the filepath of this sprite's image; returns null if not applicable
	 */
	public String getImagePath () {
		return imagePath;
	}
	
	/**
	 * Gets the filepath of the file used to parse this sprite.
	 * @return the filepath used to parse this sprite; returns null if not applicable
	 */
	public String getParsePath () {
		return parsePath;
	}
	
	private class CacheNode {
		
		/**
		 * The number of times the data in this node has been accessed
		 */
		private int accessCount;
		/**
		 * The images stored in this node
		 */
		private BufferedImage[] data;
		/**
		 * The key associated with this node
		 */
		private String key;
		
		/**
		 * Construct a new CacheNode indexed by the given key, holding the given data.
		 * @param key The key to index this node
		 * @param data The data to store in this node
		 */
		public CacheNode (String key, BufferedImage[] data) {
			accessCount = 0;
			this.key = key;
			this.data = data;
			cache.put (key, this);
		}
		
		/**
		 * Gets the key value associated with this node.
		 * @return This node's key
		 */
		public String getKey () {
			return key;
		}
		
		/**
		 * Gets the data stored by this node.
		 * @return This node's data
		 */
		public BufferedImage[] getData () {
			accessCount ++;
			return data;
		}
		
		/**
		 * Gets the number of times getData has been called on this node.
		 * @return The number of calls to getData
		 */
		public int getAccessCount () {
			return accessCount;
		}
		
		/**
		 * Removes this element from the cache.
		 * @return True if the element was removed; false oftherwise
		 */
		public boolean remove () {
			return cache.remove (key, this);
		}
	}
}
