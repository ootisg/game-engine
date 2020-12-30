package unused;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Stores objects of type T paired with a String key in a character-by-character search tree.
 * @author nathan
 *
 * @param <T>
 */
public class StringSearchTree<T> {
	
	private BranchNode head;
	
	public StringSearchTree () {
		head = new BranchNode (null);
	}
	
	private class TreeNode {
		
		public TreeNode parent;
		
		public TreeNode (TreeNode parent) {
			this.parent = parent;
		}
	}
	
	private class BranchNode extends TreeNode {
		
		public Object[] children;
		public LinkedList<StringSearchTree<T>.LeafNode> outOfSetList;
		public TreeNode lastAccessed;
		
		public BranchNode (TreeNode parent) {
			super (parent);
			children = new Object[127];
		}
		
		public TreeNode get (char index) throws IllegalArgumentException {
			int charVal = (int)index;
			if (charVal > 127) {
				throw new IllegalArgumentException ();
			}
			lastAccessed = (StringSearchTree<T>.TreeNode)children [charVal];
			return lastAccessed;
		}
		
		public TreeNode getOutOfSet (String key) {
			if (outOfSetList == null) {
				return null;
			}
			Iterator<LeafNode> iter = outOfSetList.iterator ();
			while (iter.hasNext ()) {
				LeafNode workingElement = iter.next ();
				if (workingElement.key.equals (key)) {
					lastAccessed = workingElement;
					return lastAccessed;
				}
			}
			return null;
		}
		
		public BranchNode makeBranch (LeafNode leaf) {
			BranchNode newBranch = new BranchNode (this);
			children [leaf.parentIndex] = newBranch;
			//Could potentially be optimized
			add (leaf.key, leaf.data);
			return newBranch;
		}
		
		public void addLeaf (LeafNode leaf) {
			children [leaf.parentIndex] = leaf;
		}
	}
	
	private class LeafNode extends TreeNode {
		
		public String key;
		public T data;
		public int parentIndex;
		
		public LeafNode (TreeNode parent, int parentIndex, String key, T data) {
			super (parent);
			this.key = key;
			this.data = data;
			this.parentIndex = parentIndex;
		}
	}
	
	public T get (String key) {
		TreeNode workingNode = head;
		if (key.equals ("")) {
			LeafNode blankNode = (StringSearchTree<T>.LeafNode)head.getOutOfSet (key);
			if (blankNode != null) {
				return blankNode.data;
			} else {
				return null;
			}
		}
		int i = 0;
		while (true) {
			if (workingNode instanceof StringSearchTree.BranchNode) {
				try {
					workingNode = ((BranchNode)workingNode).get (key.charAt (i));
				} catch (IllegalArgumentException e) {
					return (T)((BranchNode)workingNode).getOutOfSet (key);
				}
			} else if (workingNode instanceof StringSearchTree.LeafNode) {
				if (((LeafNode)workingNode).key.equals (key)) {
					return ((LeafNode)workingNode).data;
				} else {
					break;
				}
			} else if (workingNode == null) {
				break;
			}
			i ++;
		}
		return null;
	}
	
	public void add (String key, T data) {
		TreeNode workingNode = head;
		TreeNode previous = null;
		int index = 0;
		while (true) {
			if (workingNode instanceof StringSearchTree.BranchNode) {
				try {
					previous = workingNode;
					workingNode = ((BranchNode)workingNode).get (key.charAt (index));
				} catch (IllegalArgumentException e) {
					if (((BranchNode)workingNode).outOfSetList == null) {
						((BranchNode)workingNode).outOfSetList.add (new LeafNode (workingNode, key.charAt (index), key, data));
						return;
					}
				}
				if (workingNode == null) {
					((BranchNode)previous).addLeaf (new LeafNode (previous, key.charAt (index), key, data));
					return;
				}
				index ++;
			} else if (workingNode instanceof StringSearchTree.LeafNode) {
				workingNode = ((BranchNode)workingNode.parent).makeBranch ((LeafNode)workingNode);
			}
		}
	}
}