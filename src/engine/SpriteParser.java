package engine;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * A class for parsing an image into multiple subimages.
 * 
 * TODO DESCRIBE PARAMETER FORMAT
 * 
 * @author nathan
 *
 */
public class SpriteParser {
	
	/**
	 * The list of parameters for this SpriteParser
	 */
	private ArrayList<String> parameters;
	
	/**
	 * Constructs a new SpriteParser object from the given filepath.
	 * @param filepath The filepath to the list of parameters
	 */
	public SpriteParser (String filepath) {
		parameters = new ArrayList<String> ();
		File workingFile = new File (filepath);
		Scanner fileScanner;
		try {
			fileScanner = new Scanner (workingFile);
		} catch (FileNotFoundException e) {
			return;
		}
		while (fileScanner.hasNextLine ()) {
			parameters.add (fileScanner.nextLine ());
		}
		fileScanner.close ();
	}
	
	/**
	 * Constructs a new SpriteParser object with the given list of parameters.
	 * @param parameters The list of parameters for this parser
	 */
	public SpriteParser (ArrayList<String> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Parses the given source image according to this SpriteParser's parameters.
	 * @param source The source image to parse
	 * @return A list of BufferedImages as the result of parsing the image
	 */
	public BufferedImage[] parse (BufferedImage source) {
		if (parameters == null || parameters.size () == 0) {
			return new BufferedImage[] {source};
		}
		LinkedList<BufferedImage> frames = new LinkedList<BufferedImage> ();
		for (int i = 0; i < parameters.size (); i ++) {
			String param = parameters.get (i);
			Scanner paramScanner = new Scanner (param);
			if (paramScanner.hasNext ()) {
				BufferedImage[] images;
				switch (paramScanner.next ()) {
					case "rectangle":
						frames.add (source.getSubimage (paramScanner.nextInt (), paramScanner.nextInt (), paramScanner.nextInt (), paramScanner.nextInt ()));
						break;
					case "grid":
						images = splitGrid (source, paramScanner.nextInt (), paramScanner.nextInt ());
						for (int j = 0; j < images.length; j ++) {
							frames.add (images [j]);
						}
						break;
					case "indexedGrid":
						images = splitGrid (source, paramScanner.nextInt (), paramScanner.nextInt ());
						while (paramScanner.hasNextInt ()) {
							frames.add (images [paramScanner.nextInt ()]);
						}
						break;
					default:
						break;
				}
			}
		}
		return frames.toArray (new BufferedImage[0]);
	}
	
	/**
	 * Splits the BufferedImage src, relative to a grid, into an array of BufferedImage(s).
	 * @param src The source image
	 * @param cellWidth The width of the cells in the grid
	 * @param cellHeight The height of the cells in the grid
	 * @return The list of BufferedImages taken from the grid
	 */
	private static BufferedImage[] splitGrid (BufferedImage src, int cellWidth, int cellHeight) {
		int imgWidth = src.getWidth ();
		int imgHeight = src.getHeight ();
		int cellsHoriz = imgWidth / cellWidth;
		int cellsVert = imgHeight / cellHeight;
		BufferedImage[] images = new BufferedImage [cellsVert * cellsHoriz];
		for (int i = 0; i < cellsVert; i ++) {
			for (int j = 0; j < cellsHoriz; j ++) {
				images [i * cellsHoriz + j] = src.getSubimage (cellWidth * j, cellHeight * i, cellWidth, cellHeight);
			}
		}
		return images;
	}
}
