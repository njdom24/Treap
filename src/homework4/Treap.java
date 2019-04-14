package homework4;

import java.io.Serializable;
import java.util.Random;

/**
 * A class to represent a treap, that is, a BST with node placement
 * randomized by probabilistic heap-like priorities
 * @author CS284 team
 */
public class Treap<E extends Comparable<E>> extends BinarySearchTree<E> {
    public static class Node<E> implements Serializable
	{
	public E data; // key for the search
	public int priority; // random heap priority
	public Node<E> left;
	public Node<E> right;
	public Node<E> parent;

	/** Creates a new node with the given data and priority. The
	 *  pointers to child nodes are null. Throw exceptions if data
	 *  is null. 
	 */
	public Node(E data, int priority, Node<E> parent) {
	    if(data == null)
	    	throw new IllegalArgumentException("Data is null");

	    this.data = data;
	    this.priority = priority;
	    left = right = null;
	    this.parent = parent;
	}

		public Node<E> rotateRight() {
			if(right != null)
				right.parent = parent;
			parent.left = right;
			//parent.left.parent = parent;
			right = parent;
			Node<E> grandParent = parent.parent;
			parent.parent = this;
			parent = grandParent;
			if(grandParent != null)
				if(grandParent.left == right)
					grandParent.left = this;
				else
					grandParent.right = this;
			return this;
		}

	public Node<E> rotateLeft() {
		if(left != null)
			left.parent = parent;
		parent.right = left;
		//parent.right.parent = parent;
		left = parent;
		Node<E> grandParent = parent.parent;
		parent.parent = this;
		parent = grandParent;
		if(grandParent != null)
			if(grandParent.left == left)
				grandParent.left = this;
			else
				grandParent.right = this;
		return this;
	}

	@Override
	public String toString() {
		//return "Node";
			return "(key=" + data.toString() + ", priority=" + priority + ')';
		}
    }

    private Random priorityGenerator;
    public Node<E> root;
    //private Node<E> parent;

    /** Create an empty treap. Initialize {@code priorityGenerator}
     * using {@code new Random()}. See {@url
     * http://docs.oracle.com/javase/8/docs/api/java/util/Random.html}
     * for more information regarding Java's pseudo-random number
     * generator. 
     */
    public Treap() {
		priorityGenerator = new Random();
    }


    /** Create an empty treap and initializes {@code
     * priorityGenerator} using {@code new Random(seed)}
     */
    public Treap(long seed) {
    	priorityGenerator = new Random(seed);
    }

    public boolean add(E key) {
    	int priority = priorityGenerator.nextInt(100);
    	if(priorityAvailable(root, priority)) {
			add(root, key, priority);
		}
    	else
    		return false;

		return addReturn;
    }

	public boolean add(E key, int forced) {
		int priority = forced;
		if(priorityAvailable(root, priority)) {
			add(root, key, priority);
		}
		else
			return false;

		return addReturn;
	}

	/*
	private Node<E> add2(Node<E> localRoot, E item, int priority, Node<E> prevRoot) {
		if (localRoot == null) {
			// item is not in the tree; insert it.
			addReturn = true;
			return new Node<E>(item, priority, prevRoot);
		} else if (item.compareTo(localRoot.data) == 0) {
			// item is equal to localRoot.data
			addReturn = false;
			return localRoot;
		} else if (item.compareTo(localRoot.data) < 0) {
			// item is less than localRoot.data
			localRoot.left = add(localRoot.left, item, priority, localRoot);
			return localRoot;
		} else {
			// item is greater than localRoot.data
			localRoot.right = add(localRoot.right, item, priority, localRoot);
			return localRoot;
		}
	}
	 */

	private void add(Node<E> localRoot, E item, int priority)
	{
		if(root == null)
			root = new Node<E>(item, priority, null);
		else if (item.compareTo(localRoot.data) == 0) {
			addReturn = false;
		}
		else if (item.compareTo(localRoot.data) < 0) {
			if(localRoot.left == null) {
				localRoot.left = new Node<E>(item, priority, localRoot);
				heapify(localRoot.left);
				addReturn = true;
			}
			else
				add(localRoot.left, item, priority);
		}
		else {
			if(localRoot.right == null) {
				localRoot.right = new Node<E>(item, priority, localRoot);

				heapify(localRoot.right);
				addReturn = true;
			}
			else
				add(localRoot.right, item, priority);
		}
	}

    //Returns new local root of the tree
    private Node<E> heapify(Node<E> n) {
		while(n.parent != null && n.priority < n.parent.priority) {
			if(n.parent.left == n) {
				n.rotateRight();
			}
			else {
				n.rotateLeft();
			}
		}
		while(n.parent != null)
			n = n.parent;

		//System.out.println("RETURNING: " + n);
		root = n;
		return n;
	}

	private void forceRoot(){
		while(root.parent != null)
			root = root.parent;
	}

    public E delete(E key) {
		//only has left child: rotate right (key is the parent)
		//only has right child: rotate left (key is the parent)
		//highest child is on the right: rotate left - because the child moves up
		//once key has no children, its parent should replace its pointer with null

		return delete(key, root);
    }

    public E delete(E key, Node<E> localRoot) {

		if(localRoot == null)
			return null;

		if(key.compareTo(localRoot.data) == 0) {//if it has no children

			if(localRoot.left == null && localRoot.right == null) {
				if(localRoot.parent == null)
					root = null;
				else {
					if(localRoot.parent.left.data == key)
						localRoot.parent.left = null;
					else
						localRoot.parent.right = null;
				}
				return key;
			}
			else if(localRoot.left == null) {//only has a right child
				if(localRoot == root)
					root = localRoot.right.rotateLeft();
				else
					localRoot.right.rotateLeft();

				return delete(key, localRoot);
				//return delete(key, localRoot);
			}
			else if (localRoot.right == null){//only has a left child
				if(localRoot == root)
					root = localRoot.left.rotateRight();
				else
					localRoot.left.rotateRight();

				return delete(key, localRoot);
				//return delete(key, localRoot);
			}
			else {//has two children
				if(localRoot.left.priority < localRoot.right.priority)
					if(localRoot == root)
						root = localRoot.left.rotateRight();
					else
						localRoot.left.rotateRight();
				else
					if(localRoot == root)
						root = localRoot.right.rotateLeft();
					else
						localRoot.right.rotateLeft();

				return delete(key, localRoot);
			}
		}
		else if(key.compareTo(localRoot.data) < 0) {
			return delete(key, localRoot.left);
		}
		else {
			return delete(key, localRoot.right);
		}
	}

    private boolean find(Node<E> root, E key) {
		if(root == null)
			return false;

		if(key.compareTo(root.data) == 0) {
			return true;
		}
		else if(key.compareTo(root.data) < 0) {
			return find(root.left, key);
		}
		else {
			return find(root.right, key);
		}
    }

    public E find(E key) {
	// YOUR    CODE    HERE
	return null;
    }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		preOrderTraverse(root, 1, sb);
		return sb.toString();
	}

	/**
	 * Perform a preorder traversal.
	 * @param node The local root
	 * @param depth The depth
	 * @param sb The string buffer to save the output
	 */
	private void preOrderTraverse(Node<E> node, int depth,
								  StringBuilder sb) {
		for (int i = 1; i < depth; i++) {
			sb.append("  ");
		}
		if (node == null) {
			sb.append("null\n");
		} else {
			sb.append(node.toString());
			sb.append("\n");
			preOrderTraverse(node.left, depth + 1, sb);
			preOrderTraverse(node.right, depth + 1, sb);
		}
	}

	private boolean priorityAvailable(Node<E> node, int priority) {
		if(node != null){
			if(node.priority == priority)
				return false;

			return	priorityAvailable(node.left, priority) &&
					priorityAvailable(node.right, priority);
		}
		return true;
	}
}
