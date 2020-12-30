package unused;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

import engine.GameObject;
import engine.HashIndexedTree;

/**
 * Stores and organizes references to all GameObjects, includes methods for searching and object interaction
 * @author nathan
 *
 */
public class ObjectHandler {
	
	/**
	 * Stores all the classes currently in use, and their respective objects
	 */
	private static HashIndexedTree<String, QuadTreeNode> classTrees = new HashIndexedTree <String, QuadTreeNode> ("GameObject", null);;
	
	/**
	 * The bounding box representing the collision field--i.e. where collision detection can occur
	 * Should be a square with a power of 2 for the side lengths
	 */
	private static Rectangle bounds = null;
	
	/**
	 * ObjectHandler cannot be constructed.
	 */
	private ObjectHandler () {
		
	}
	
	/**
	 * Initializes this class's static variables; must be called before the creation of any GameObjects.
	 * @param bounds The bounding rectangle to be used as the collision field (the area where collision is "active"); must be a square with a power of 2 side length
	 */
	public static void init (Rectangle bounds) {
		ObjectHandler.bounds = bounds;
	}
	
	/**
	 * A node of a quad tree used for collision detection.
	 * @author nathan
	 *
	 */
	private static class QuadTreeNode {
		
		/**
		 * The 4 children of this node
		 */
		public QuadTreeNode[] nodes;
		
		/**
		 * The bounding rectangle of this quadrant
		 */
		public Rectangle bound;
		
		/**
		 * The dummmy head of the list of GameObjects referenced by this node
		 */
		public GameObject listHead;
		
		/**
		 * The dummy tail of the list of GameObjects referenced by this node
		 */
		public GameObject listTail;
		
		/**
		 * Constructs a new QuadTreeNode with the given bounding reigon.
		 * @param bound The bounding reigon of this node
		 */
		public QuadTreeNode (Rectangle bound) {
			nodes = new QuadTreeNode[4];
			this.bound = bound;
			listHead = new DummyObject ();
			listTail = new DummyObject ();
			//listHead.next = listTail;
			//listTail.previous = listHead;
		}
		
		/**
		 * Creates an inner node for more percise categorization; num corresponds to its position within this node.
		 * @param num The position of the quadrant represented by the new node:
		 * 0 is top-left
		 * 1 is top-right
		 * 2 is bottom-left
		 * 3 is bottom-right
		 * Using other values will result in undefined behavior.
		 */
		public void createInnerNode (int num) {
			int xCoord = (num & 0x1) * (bound.width / 2) + bound.x;
			int yCoord = ((num & 0x2) >> 1) * (bound.height / 2) + bound.y;
			nodes [num] = new QuadTreeNode (new Rectangle (xCoord, yCoord, bound.width / 2, bound.height / 2));
		}
		
	}
	
	/**
	 * Returns the node with the given properties; constructs it and all nodes that would contain it if it/they does/do not yet exist.
	 * @param gridWidth The width of the cells of the grid to return; should be the same as gridHeight
	 * @param gridHeight The height of the cells of the grid to return; should be the same as gridWidth
	 * @param cellHoriz The horizontal offset of the grid cell to return
	 * @param cellVert The vertical offset of the grid cell to return
	 * @param root The root of the QuadTree to search
	 * @return The node at the given grid position
	 */
	private static QuadTreeNode getNode (int gridWidth, int gridHeight, int cellHoriz, int cellVert, QuadTreeNode root) {
		QuadTreeNode currentNode = root;
		while (currentNode.bound.width != gridWidth) {
			int cellX = (cellHoriz * gridWidth - currentNode.bound.x) / (currentNode.bound.width / 2);
			int cellY = (cellVert * gridHeight - currentNode.bound.y) / (currentNode.bound.width / 2);
			int cellId = cellY * 2 + cellX;
			if (currentNode.nodes [cellId] == null) {
				currentNode.createInnerNode (cellId);
			}
			currentNode = currentNode.nodes [cellId];
		}
		return currentNode;
	}
	
	/**
	 * Returns the node with the given properties; constructs it and all nodes that would contain it if it/they does/do not yet exist.
	 * @param gridWidth The width of the cells of the grid to return; should be the same as gridHeight
	 * @param gridHeight The height of the cells of the grid to return; should be the same as gridWidth
	 * @param cellHoriz The horizontal offset of the grid cell to return
	 * @param cellVert The vertical offset of the grid cell to return
	 * @param root The root of the QuadTree to search
	 * @return The node at the given grid position
	 */
	private static LinkedList<GameObject> getObjects (int gridWidth, int gridHeight, int cellHoriz, int cellVert, QuadTreeNode root) {
		LinkedList<GameObject> collisions = new LinkedList<GameObject> ();
		QuadTreeNode currentNode = root;
		while (currentNode.bound.width != gridWidth) {
			int cellX = (cellHoriz * gridWidth - currentNode.bound.x) / (currentNode.bound.width / 2);
			int cellY = (cellVert * gridHeight - currentNode.bound.y) / (currentNode.bound.width / 2);
			int cellId = cellY * 2 + cellX;
			if (currentNode.nodes [cellId] == null) {
				break;
			}
			//GameObject workingObject = currentNode.listHead.next;
			//while (workingObject != currentNode.listTail) {
			//	collisions.add (workingObject);
			//}
			currentNode = currentNode.nodes [cellId];
		}
		return collisions;
	}
	
	/**
	 * Inserts an object into the ObjectHandler.
	 * @param obj The object to insert
	 */
	public static void insert (GameObject obj) {
		QuadTreeNode usedNode = classTrees.get (obj.getClass ().getSimpleName ());
		if (usedNode == null) {
			usedNode = new QuadTreeNode (bounds);
			addClass (obj);
		}
		insert (obj, usedNode);
	}
	
	/**
	 * Inserts an object into the given QuadTree.
	 * @param object The object to insert
	 * @param root The root of the tree to add the object to
	 */
	private static void insert (GameObject object, QuadTreeNode root) {
		int encaseWidth = log2ceil (Math.max (object.getHitbox ().width, object.getHitbox ().height));
		while (encaseWidth != 0 && encaseWidth < root.bound.width) {
			int objX = object.getHitbox ().x % encaseWidth;
			int objY = object.getHitbox ().y % encaseWidth;
			if (new Rectangle (0, 0, encaseWidth, encaseWidth).contains (new Rectangle (objX, objY, object.getHitbox ().width, object.getHitbox ().height))) {
				break;
			}
			encaseWidth *= 2;
		}
		int cellX = object.getHitbox ().x / encaseWidth;
		int cellY = object.getHitbox ().y / encaseWidth;
		QuadTreeNode temp = getNode (encaseWidth, encaseWidth, cellX, cellY, root);
		temp.listTail.insert (object);
	}
	
	/**
	 * Returns the smallest integer power of 2 greater than the given value.
	 * @param val The value of which to find a greater power of 2 than it
	 * @return The smallest integer power of 2 greater than val
	 */
	private static int log2ceil (int val) {
		val -= 1;
		val |= (val >> 1);
		val |= (val >> 2);
		val |= (val >> 4);
		val |= (val >> 8);
		val |= (val >> 16);
		return val + 1;
	}
	
	public static LinkedList<GameObject> search (QuadTreeNode head, Rectangle bounds) {
		if (head == null) {
			return new LinkedList<GameObject> ();
		}
		int encaseWidth = log2ceil (Math.max (bounds.width, bounds.height));
		while (encaseWidth != 0 && encaseWidth < head.bound.width) {
			int objX = bounds.x % encaseWidth;
			int objY = bounds.y % encaseWidth;
			if (new Rectangle (0, 0, encaseWidth, encaseWidth).contains (new Rectangle (objX, objY, bounds.width, bounds.height))) {
				break;
			}
			encaseWidth *= 2;
		}
		int cellX = bounds.x / encaseWidth;
		int cellY = bounds.y / encaseWidth;
		return getObjects (encaseWidth, encaseWidth, cellX, cellY, head);
	}
	
	public static LinkedList<GameObject> search (String objName, Rectangle bounds) {
		return search (classTrees.get (objName), bounds);
	}
	
	public static LinkedList<GameObject> search (String objName) {
		return search (objName, bounds);
	}
	
	public static LinkedList<GameObject> searchChildren (String objName, Rectangle bounds) {
		LinkedList<GameObject> result = new LinkedList<GameObject> ();
		LinkedList<QuadTreeNode> names = classTrees.getAllChildren (objName);
		Iterator<QuadTreeNode> iter = names.iterator ();
		while (iter.hasNext ()) {
			result.addAll (search (iter.next (), bounds));
		}
		return result;
	}
	
	public static LinkedList<GameObject> searchChildren (String objName) {
		return searchChildren (objName, bounds);
	}
	
	/**
	 * Represents a dummy node in a list of GameObjects
	 * @author nathan
	 *
	 */
	private static class DummyObject extends GameObject {
		
		/**
		 * Standard no-arg constructor; note that the object is not inserted by GameObject's no-arg constructor.
		 */
		public DummyObject () {
			super ();
		}
		
		/**
		 * Inserts an object before this node; intended for use with the tail node.
		 */
		public void insert (GameObject obj) {
			//previous.next = obj;
			//obj.previous = previous;
			//obj.next = this;
			previous = obj;
		}
	}
	
	/**
	 * Adds the class of obj to the class hierarchy stored in ObjectHandler.
	 * @param obj The GameObject whose class to add
	 */
	private static void addClass (GameObject obj) {
		Class<?> workingClass = obj.getClass ();
		Stack<Class<?>> toAdd = new Stack<Class<?>> ();
		while (!workingClass.getName ().equals ("engine.GameObject") && classTrees.get (workingClass.getSimpleName ()) == null) {
			toAdd.push (workingClass);
			workingClass = workingClass.getSuperclass ();
		}
		while (!toAdd.isEmpty ()) {
			Class<?> topClass = toAdd.pop ();
			QuadTreeNode usedObject;
			if (toAdd.isEmpty ()) {
				usedObject = new QuadTreeNode (bounds);
			} else {
				usedObject = null;
			}
			classTrees.addChild (topClass.getSuperclass ().getSimpleName (), topClass.getSimpleName (), usedObject);
		}
	}
	/*
	public void searchTest () {
		root = new QuadTreeNode (bounds);
		cRoot = new ClassTreeNode ("GameObject");
		
		Random random = new Random ();
		
		int iters1 = 10;
		int iters2 = 1000000;
		
		for (int i = 0; i < iters1; i ++) {
			byte[] data = new byte[16];
			random.nextBytes (data);
			//cRoot.addClass (new String (data));
		}
		
		for (int j = 1; j < 200; j ++) {
			cRoot = new ClassTreeNode ("GameObject");
			

			
			for (int i = 0; i < j; i ++) {
				byte[] data = new byte[16];
				random.nextBytes (data);
				cRoot.addClass (new String (data));
			}
			System.out.println (j);
			long start = System.nanoTime ();
			for (int i = 0; i < iters2; i ++) {
				String searchString = cRoot.children.get (random.nextInt (j)).className;
				cRoot.search1 (searchString);
			}
			System.out.println (System.nanoTime () - start);
			start = System.nanoTime ();
			for (int i = 0; i < iters2; i ++) {
				String searchString = cRoot.children.get (random.nextInt (j)).className;
				cRoot.search2 (searchString);
			}
			System.out.println (System.nanoTime () - start);
		}
	}
	public ClassTreeNode search1 (String className) {
		int startIndex = 0;
		int midIndex = children.size () / 2;
		int endIndex = children.size () - 1;
		ClassTreeNode startElement = children.get (startIndex);
		ClassTreeNode midElement = children.get (midIndex);
		ClassTreeNode endElement = children.get (endIndex);
		while (midElement.className != className) {
			if (startIndex == midIndex || midIndex == endIndex) {
				return null;
			}
			if (midElement.className.compareTo (className) > 0) {
				endIndex = midIndex;
				midIndex = (midIndex - startIndex) / 2 + startIndex;
				startElement = children.get (startIndex);
				midElement = children.get (midIndex);
				endElement = children.get (endIndex);
			} else {
				int temp = startIndex;
				startIndex = midIndex;
				midIndex = (endIndex - midIndex) / 2 + startIndex;
				startElement = children.get (startIndex);
				midElement = children.get (midIndex);
				endElement = children.get (endIndex);
			}
		}
		return midElement;
	}
	
	public ClassTreeNode search2 (String className) {
		for (int i = 0; i < children.size (); i ++) {
			if (children.get (i).className.equals (className)) {
				return children.get (i);
			}
		}
		return null;
	}*/
	/*	private class ClassTreeNode implements Comparable {
	
	public ArrayList<ClassTreeNode> children;
	public String className;
	
	public ClassTreeNode (String className) {
		this.className = className;
		children = new ArrayList<ClassTreeNode> ();
	}
	
	public void addClass (String className) {
		children.add (new ClassTreeNode (className));
		Collections.sort (children);
	}

	@Override
	public int compareTo (Object arg0) {
		return className.compareTo (((ClassTreeNode)arg0).className);
	}
}*/
}
