/**
 * 
 * Class: Tree2345
 *
 * Created by Ross Miller on 4/20/10
 * 
 * This class creates and manages a (2,5)-tree.
 * 
 * VARIABLES
 * ---------
 * private Node root
 * 		The root Node of the tree
 * 
 * METHODS
 * -------
 * public Tree2345();
 * 		Constructs an empty (2,5)-tree
 * 
 * public String toString();
 * 		Generates a string representation of this tree as a concordence
 * 
 * public boolean find(String value);
 * 		Starts off a recursive search for a node.
 * 
 * void add(String value, int paragraph, int line)
 * 		Adds an entry to the tree with the given value, paragraph, and line
 * 
 * private void promoteIfNecessary(Node currentNode)
 * 		Checks to see if a given node has too many entries. If it does, it rearranges the tree.
 * 
 */
public class Tree2345 {
	private Node root;

    /**
     * Constructs an empty (2,5)-tree
     * Postconditions: A new (2,5)-tree is created containing no entries
     * @return object of type Tree2345
     */
	public Tree2345() {
		root = null;
	}

    /**
     * Generates a string representation of this tree as a concordence
     * Postconditions: Outputs a header and then traverses the tree in order to output every entry in reverse order along with their occurences
     * @return A String representation of this tree
     */
	public String toString() {
		String str = "Word                    Occurrences [form: (Paragraph#, Line#)]\n----                    -----------\n";
		if (root != null)
			str += root.toString();
		return str;
	}

    /**
     * Starts off a recursive search for a node
     * Postconditions: Outputs how many numbers there are, the number of rows and columns, the time it takes to sort the numbers, and all of the numbers in sorted order
     * @param value The word being searched for
     * @return True if this value is in the tree, false if it is not
     */
	public boolean find(String value) {
		return root.findNode(value) != null;
	}

    /**
     * Adds an entry to the tree with the given value, paragraph, and line
     * Postconditions: A new entry is added to the tree, and the tree may have had to rebalance itself
     * @param value A string representing the entry to be added
     * @param paragraph The paragraph that this string was found on
     * @param line The line that this string was found on
     */
	void add(String value, int paragraph, int line) {
		// If this is the first entry to be added
		if (root == null) {
			root = new Node(); // First entry added
			root.data[0] = new Entry(value, paragraph, line);
		} else {
			// Otherwise search for the right node to put it in
			Node foundNode = root.findNode(value);
			if (foundNode != null) {
				foundNode.data[foundNode.indexOfElement(value)].addOccurence(
						paragraph, line);
			} else {
				Node nodeToBePlacedIn = root.nodeToBePlacedIn(value);
				nodeToBePlacedIn.add(value, paragraph, line);
			}
		}
	}

    /**
     * Checks to see if a given node has too many entries. If it does, it rearranges the tree
     * Preconditions: The given argument is not null
     * Postconditions: The tree may be rebalanced
     * @param currentNode The node which will be checked to see if it needs to be fixed
     */
	private void promoteIfNecessary(Node currentNode) {
		if (currentNode.dataSize() == currentNode.data.length) {
			Entry middle = currentNode.data[2];

			// Divide up the left and right side
			Entry[] leftData = { null, null, null, null, null };
			Node[] leftPtrs = { null, null, null, null, null, null };
			leftData[0] = currentNode.data[0];
			leftData[1] = currentNode.data[1];
			leftPtrs[0] = currentNode.ptrs[0];
			leftPtrs[1] = currentNode.ptrs[1];
			leftPtrs[2] = currentNode.ptrs[2];

			Entry[] rightData = { null, null, null, null, null };
			Node[] rightPtrs = { null, null, null, null, null, null };
			rightData[0] = currentNode.data[3];
			rightData[1] = currentNode.data[4];
			rightPtrs[0] = currentNode.ptrs[3];
			rightPtrs[1] = currentNode.ptrs[4];
			rightPtrs[2] = currentNode.ptrs[5];

			Node leftNode = new Node(leftData,
					leftPtrs);
			Node rightNode = new Node(rightData,
					rightPtrs);

			if (currentNode.getParent() == null) {
				// Create a new parent node to hold all of the data
				Entry[] data = { null, null, null, null, null };
				Node[] ptrs = { null, null, null, null, null, null };
				data[0] = middle;
				ptrs[0] = leftNode;
				ptrs[1] = rightNode;
				root = new Node(data, ptrs);
			} else {
				int index = currentNode.getParent().insertionPoint(middle.getName());

				// Shift array entries over 1 to make room for new entry
				for (int i = currentNode.data.length - 2; i >= index; i--) {
					currentNode.getParent().data[i + 1] = currentNode
							.getParent().data[i];
				}
				for (int i = currentNode.ptrs.length - 2; i >= index + 1; i--) {
					currentNode.getParent().ptrs[i + 1] = currentNode
							.getParent().ptrs[i];
				}

				currentNode.getParent().data[index] = middle;
				currentNode.getParent().ptrs[index] = leftNode;
				currentNode.getParent().ptrs[index + 1] = rightNode;

				// See if we need to make another promotion
				promoteIfNecessary(currentNode.getParent());
			}
		}
	}


	/**
	 * 
	 * Class: Node
	 * 
	 * An inner class of Tree2345 which manages groups of up to 5 entries, where are called nodes
	 * 
	 * 
	 * METHODS
	 * -------
	 * private Node(Entry[] newData, Node[] newPtrs);
	 * 		Constructs a new Node object with a list of ptrs and data
	 * 
	 * private Node getParent();
	 * 		Determines the parent of this Node. Returns null if this parent is the root.
	 * 
	 * private Node nodeToBePlacedIn(String value);
	 * 		Determines which Node this entry would be placed in if it were to be added to the tree.
	 * 
	 * private int indexOfElement(String value);
	 * 		Returns the index of the given value in the Node. Returns -1 if it is not in the node at all
	 * 
	 * private int insertionPoint(String value);
	 * 		Determines in the case that the given value is not in this node but is in
	 * 		one of its descendants, the index in this Node's pointer
	 * 
	 * private Node findNode(String value);
	 * 		Recursive function used to determine whether or not a value is contained inside the given
	 * 		node or any of its descendants
	 * 
	 * private int dataSize();
	 * 		Determines the number of entries in this node
	 * 
	 * private void add(String value, int paragraph, int line);	
	 * 		Adds this value to the current Node. Increases the occurance if it already exists
	 * 
	 * public String toString();
	 * Prints out each of this node's and its descendents in reverse infix order.
	 * 
	 * 
	 */
	private class Node {
		private Entry[] data = { null, null, null, null, null };

		private Node[] ptrs = { null, null, null, null, null, null };

		private Node() {};

	    /**
	     * Constructs a new Node object with a list of ptrs and data
	     * Preconditions: newData.length should be less that 5, and newPtrs.length should be less than 6
	     * Postconditions: A new Node object is created in memory
	     * @param newData An array of Entry objects that will be in this array
	     * @param newPtrs An array of Node objects that are this node's children
	     * @return An instance of Node with the entries and pointers given by the arguments
	     */
		private Node(Entry[] newData, Node[] newPtrs) {
			for (int i = 0; i < data.length; i++) {
				data[i] = newData[i];
			}

			for (int i = 0; i < ptrs.length; i++) {
				ptrs[i] = newPtrs[i];
			}
		}

	    /**
	     * Determines the parent of this Node. Returns null if this parent is the root
	     * Preconditions: This node has been added to the tree already
	     * @return The Node object which is the parent of this node. Returns null if this Node has no parent
	     */
		private Node getParent() {
			String value = data[0].getName();
			Node currentNode = root;
			Node parentNode = null;
			while (currentNode != null) {
				for (int i = 0; i < currentNode.dataSize(); i++) {
					if (currentNode.data[i].getName().equals(value))
						return parentNode;
				}

				parentNode = currentNode;
				currentNode = currentNode.ptrs[currentNode
						.insertionPoint(value)];
			}
			return null;
		}

		
	    /**
	     * Determines which Node this entry would be placed in if it were to be added to the tree. This should only be called on externally from the root of the tree
	     * @param value The string who's potential Node gets returned
	     * @return A Node object which is the exact node that this value would be placed in if it were the next one to be added to the tree
	     */
		private Node nodeToBePlacedIn(String value) {
			if (this.data[this.insertionPoint(value)] != null
					&& this.data[this.insertionPoint(value)].getName().equals(value)) {
				return this;
			} else if (this.ptrs[0] == null) {
				return this;
			} else {
				return ptrs[insertionPoint(value)].nodeToBePlacedIn(value);
			}

		}

	    /**
	     * Returns the index of the given value in the Node. Returns -1 if it is not in the node at all
	     * @param value The value who's position in the Node is to be tested
 	     * @return The index of the given value in the Node, or -1 if it is not in the node at all
	     */
		private int indexOfElement(String value) {
			for (int i = 0; i < dataSize(); i++) {
				if (data[i].getName().equals(value))
					return i;
			}
			return -1;
		}

		/**
	     * Determines in the case that the given value is not in this node but is in
	     * 		one of its descendants, the index in this Node's pointer
	     * @param value The value that is eventually trying to be found
	     * @return The index of the pointer you'd have to follow if you wanted to find this value
	     * 		in one of this Node's descendants
	     */
		private int insertionPoint(String value) {
			int i = 0;
			while (i < data.length && data[i] != null
					&& data[i].getName().compareTo(value) < 0) {
				i++;
			}
			return i;
		}

		/**
	     * Recursive function used to determine whether or not a value is contained inside the given node or any of its descendants
	     * @param value The value to be tested for existence in this node or any of its descendants
	     * @return The Node if it is this Node or any of its descendants. Returns null otherwise
	     */
		private Node findNode(String value) {
			if (this.indexOfElement(value) != -1)
				return this;
			else if (this.ptrs[this.insertionPoint(value)] == null)
				return null;
			else
				return this.ptrs[this.insertionPoint(value)].findNode(value);
		}

		/**
	     * Determines the number of entries in this node
	     * @return The number of entries currently in this node
	     */
		private int dataSize() {
			int i = 0;
			while (i < data.length && data[i] != null)
				i++;
			return i;
		}

		/**
	     * Adds this value to the current Node. Increases the occurance if it already exists
	     * Postconditions: This entry is either added to this node, or the occurence is noted if the entry has already been added
	     * @param value The value to be added to the current node
	     * @param paragraph The paragraph of this entry
	     * @param line The line number of this entry
	     */
		private void add(String value, int paragraph, int line) {
			if (indexOfElement(value) != -1)
				data[indexOfElement(value)].addOccurence(paragraph, line);

			int index = insertionPoint(value);
			for (int i = 3; i >= index; i--) {
				data[i + 1] = data[i];
			}

			data[index] = new Entry(value, paragraph, line);
			promoteIfNecessary(this);
		}

		/**
	     * Prints out each of this node's and its descendents in reverse infix order
	     * @return The reverse infix form of node
	     */
		public String toString() {
			String str = "";

			for (int i = 0; i <= data.length-1; i++) {
				if (ptrs[i] != null)
					str += ptrs[i].toString();
				if (data[i] != null)
					str += data[i].toString();
			}

			Node lastPtr = ptrs[ptrs.length-1];
			if (lastPtr != null)
				str += lastPtr.toString();

			return str;
		}
	}
}
