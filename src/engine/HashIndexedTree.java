package engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A hierarchical data structure with fast access through a HashTable.
 * @author nathan
 *
 * @param <T> The object type for the HashTable keys
 * @param <Q> The object type to store in the tree
 */
public class HashIndexedTree<T,Q> {
	
	/**
	 * Provides quick access to elements
	 */
	private HashMap<T,Node<Q>> elements;
	
	/**
	 * Creates a new HashIndexedTree with the root node containing headObj, indexed by headKey
	 * @param headKey The index for the root node
	 * @param headObj The data contained in the root node; may be null
	 */
	public HashIndexedTree (T headKey, Q headObj) {
		elements = new HashMap<T,Node<Q>> ();
		Node head = new Node (null);
		head.data = headObj;
		elements.put (headKey, head);
	}
	
	/**
	 * Represents a node in the tree portion of the data structure
	 * @author nathan
	 *
	 * @param <R> The type of the node; will always be Q.
	 */
	private class Node<R> {
		
		/**
		 * The parent of this node
		 */
		public Node<R> parent;
		
		/**
		 * The data contained by this node
		 */
		public R data;
		
		/**
		 * The children this node has
		 */
		public LinkedList<Node<R>> children;
		
		/**
		 * Constructs a new node with the given parent node.
		 * @param parent The parent node to use
		 */
		public Node (Node parent) {
			this.parent = parent;
			this.children = new LinkedList<Node<R>> ();
		}
		
		/**
		 * Adds a given element to the children of this node.
		 * @param element The data to be stored in the new node
		 * @return A reference to the newly created node
		 */
		public Node add (R element) {
			Node<R> working = new Node<R> (this);
			working.data = element;
			children.add (working);
			return working;
		}
		
		/**
		 * Removes a given element from this node's children.
		 * @param element The data element to remove
		 * @return True if a node was removed; false otherwise
		 */
		public boolean remove (R element) {
			Iterator<Node<R>> iter = children.iterator ();
			while (iter.hasNext ()) {
				if (element.equals (iter.next ().data)) {
					iter.remove ();
					return true;
				}
			}
			return false;
		}
		
		/**
		 * Returns a list of all of this node's children, starting at the bottom of the tree.
		 * @return A linked list containing all of this node's children
		 */
		public LinkedList<R> getAllChildren () {
			LinkedList<R> result = new LinkedList<R> ();
			getAllChildren (result);
			return result;
		}
		
		/**
		 * A recursive method which traverses the entire tree under this node.
		 * @param fillList The list to store the found elements
		 */
		private void getAllChildren (LinkedList<R> fillList) {
			Iterator<Node<R>> iter = children.iterator ();
			while (iter.hasNext ()) {
				Node<R> workingNode = iter.next ();
				if (workingNode.children != null) {
					workingNode.getAllChildren (fillList);
				}
				if (workingNode.data != null) {
					fillList.add (workingNode.data);
				}
			}
		}
	}
	
	/**
	 * Adds a new node containing element and indexed by key to the given parent node.
	 * @param parent The parent node to add to
	 * @param key The key used to index this element through a HashTable
	 * @param element The data stored in the new node
	 */
	public void addChild (T parent, T key, Q element) {
		Node head = elements.get (parent);
		elements.put (key, head.add (element));
	}
	
	/**
	 * Removes the child indexed by the given key.
	 * @param key The key of the node to remove
	 */
	public void removeChild (T key) {
		Node<Q> toRemove = elements.get (key);
		toRemove.parent.remove (toRemove.data);
		elements.remove (key);
	}
	
	/**
	 * Returns all of the children of the node indexed by parentKey.
	 * @param parentKey The index of the parent node
	 * @return A list containing all the children stored under the parent key
	 */
	public LinkedList<Q> getAllChildren (T parentKey) {
		LinkedList<Q> result = new LinkedList<Q> ();
		Node<Q> head = elements.get (parentKey);
		return head.getAllChildren ();
	}
	
	/**
	 * Returns the node indexed by the given key.
	 * @param key The index of the element to retrieve
	 * @return The node at the given key
	 */
	public Q get (T key) {
		Node<Q> element = elements.get (key);
		if (element != null) {
			return element.data;
		}
		return null;
	}
	
	/**
	 * Represents a query to use when searching for objects in the ObjectHandler
	 * @author nathan
	 *
	 */
	public class SearchQuery {
		
	}
	
	
}
