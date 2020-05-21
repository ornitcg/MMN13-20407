package heap;

import java.util.ArrayList;

/**
 * MaxMinHeap is a data structure of an almost full binary tree in which, all
 * values in an even levels are larger than all of their descendants, and values
 * in odd levels are smaller than all of their descendants. This class builds
 * such a heap from an input file, that contains integer values, and maintains
 * it.
 * 
 * @author (Ornit Cohen Gindi)
 * @version (20407-2020b)
 */
public class MaxMinHeap {
	protected ArrayList<Integer> _heap; // an array that represents the MAX-MIN heap
	protected int _heapSize; // the size of the heap

	final static int ROOT = 0;
	final static int LEFT = 1;
	final static int RIGHT = 2;

/////////////////////////////////////////////CONSTRUCTORS////////////////////////////////
	/**
	 * Constructor of MaxMinHeap from array of integers given as parameter.
	 * Complexity O(nlgn)
	 * 
	 * @param intArray An array of integers
	 */
	public MaxMinHeap(ArrayList<Integer> intArray) {
		_heap = new ArrayList<>(intArray);
		_heapSize = _heap.size();
		buildHeap();
	}// end constructor

	/**
	 * Copy constructor
	 * 
	 * @param other The heap to copy
	 */
	public MaxMinHeap(MaxMinHeap other) {
		_heap = new ArrayList<>(other._heap);
		_heapSize = other._heapSize;
	}// end constructor

/////////////////////////////////////////PRIME METHODS//////////////////////////////////////////////////////////	

	/**
	 * This Builds a MAX-MIN heap from an array of integers initially given to the
	 * MaxMinHeap constructor.
	 */
	private void buildHeap() {
		// loop on all MIN (odd) levels from top down, and heapifying all 'nodes' of the
		// tree
		for (int node = 0; node < _heapSize; node++)
			if (indexDepth(node) % 2 == 1) {// all the MIN first
				// System.out.println("BuildHeap on node#" + node + "\n");// debug
				heapify(node, "build");
			}

		// loop on all MAx (even) levels from bottom to top
		for (int node = _heapSize - 1; node >= 0; node--)
			if (indexDepth(node) % 2 == 0) { // all the MAX second
				// System.out.println("BuildHeap on node#" + node + "\n");// debug
				heapify(node, "build");
			}

	}// end buildHeap

	/**
	 * Corrects the heap for a specific index. Complexity O(lgn)
	 * 
	 * @param current index of array representing the binary tree heap
	 * @param build   is assigned true when calling heapify from buildHeap, and
	 *                false otherwise
	 **/
	private void heapify(int current, String mode) {
		// System.out.println("heapify : node# " + ind + "\n" + this); // debug
		int depth = indexDepth(current);
		int maxChild = childInd(current, 1); // 1 for finding max. saving index of the maximum of an index's children
		int minChild = childInd(current, 0); // 0 for finding min. saving index of the maximum of an index's children
		int maxGrandchild = grandchildInd(current, 1); // 1 for finding max. saving index of maximum of an index's
														// grandchildren
		int minGrandchild = grandchildInd(current, 0); // 0 for finding min. saving index of maximum of an index's
														// grandchildren
		int grandparent = parent(parent(current));
		Integer currentVal = get(current);
		Integer parent = get(parent(current));

		if (depth % 2 == 0) { // if depth level is even(max)

			if (hasGrandchild(current) && maxGrandchild != current) {
//				 System.out.println("current " + current + " < maxGrandchild " + get(maxGrandchild)); // debug
				swap(_heap, current, maxGrandchild);
				heapify(maxGrandchild, mode);
			} else if (hasChild(current) && currentVal < get(maxChild)) {
//				 System.out.println("current " + current + " < maxChild " + get(maxChild)); //debug
				swap(_heap, current, maxChild);
				heapify(maxChild, mode);
			} else if (!hasChild(current) && parent != null && currentVal < parent) {
				// comment : case ind is at the bottom of the tree
//				 System.out.println("current " + current + " < parent " + parent); // debug
				swap(_heap, current, parent(current));
				heapify(parent(current), mode);
			} else if (!mode.contentEquals("build") && grandparent != -1 && currentVal > get(grandparent)) {
				// comment:correction towards MAX node
//				 System.out.println("current " + current + " > grandparent " +
//				 get(grandparent)); // debug
				swap(_heap, current, grandparent);
				heapify(grandparent, mode);
			}

		} // end if MAX
		else // if depth level is odd(min)
		{
			if (grandparent > -1 && currentVal < get(grandparent)) { // case of no grandparent
//				 System.out.println("current " + currentVal + " < grandparent " + get(grandparent)); // debug
				swap(_heap, current, grandparent);
				heapify(grandparent, mode);
			} else if (!mode.equals("build")) {
				if (hasGrandchild(current) && currentVal > get(minGrandchild)) {
//					System.out.println("current " + currentVal + " > minGrandchild " + get(minGrandchild)); // debug
					swap(_heap, current, minGrandchild);
					heapify(minGrandchild, mode);
				} else if (hasChild(current) && currentVal > get(minChild)) {
//					 System.out.println("current " + currentVal + " > minChild " + get(minChild));// debug
					swap(_heap, current, minChild);
					heapify(minChild, mode);
				} else if (!hasGrandchild(current) && !hasChild(current) && parent != null && currentVal > parent) {
//					 System.out.println("current " + currentVal + " > parent " + parent); // debug
					swap(_heap, current, parent(current));
//					 System.out.println("swap " +current+ " with "+ parent); //debug
					heapify(parent(current), mode);
				}
			} // end
		} // end if MIN
	}

	/**
	 * Removes the node of the maximum value from heap
	 * 
	 * @return The maximum value that use to be in heap before its removal
	 */
	public Integer extractMax() {
		if (_heapSize == 0)
			return null;
		Integer max = get(0);
		// System.out.println(max);// debug
		if (_heapSize > 1)
			swap(_heap, 0, _heapSize - 1);
		_heap.remove(_heapSize - 1);
		_heapSize -= 1; // update heap size
		if (_heapSize > 1)
			heapify(0, "extract");
		return max;
	}

	/**
	 * Removes the node of the minimum value from heap
	 * 
	 * @return The minimum value that use to be in heap before its removal
	 */
	public Integer extractMin() {
		if (_heapSize == 0) // empty heap
			return null;
		Integer heapMinInd = childInd(0, 0);// index 0, and mode 0 - for minimum
		Integer heapMinVal = get(heapMinInd);// save value
		if (_heapSize > 2)
			// no need to swap in case of 1 or 2 elements in heap. since then the min will
			// be the last one anyway
			swap(_heap, heapMinInd, _heapSize - 1); // switch between last and the minimum
		_heap.remove(_heapSize - 1); // remove the min (it's now in the last place of the array)
		_heapSize -= 1; // update size of heap
		if (_heapSize > 3)
			heapify(heapMinInd, "extract"); // min is the index of a node with a different value than before swap. needs
											// to
		// be corrected
		return heapMinVal;
	}

	/**
	 * Removes the node of the requested index from heap
	 * 
	 * @param index Index of value to delete from heap
	 * @return true if deletion was successful and false otherwise
	 */
	public Integer heapDelete(int index) {
		if (index < 0 || index >= _heapSize) // required index is out of bounds
			return null;
		Integer indexVal = get(index); // keep for returned value
		Integer last = get(_heapSize - 1); // for readability
		_heap.set(index, last); // mark the wanted to be removed index
		_heap.remove(_heapSize - 1);
		_heapSize -= 1;
		heapify(index, "delete"); // make the new maximum go to the top and then extract it
		return indexVal;
	}

	/**
	 * Adds a new value of Integer type to heap
	 * 
	 * @param key A value to add to Max-Min heap
	 */
	public void heapInsert(Integer key) {
		if (_heapSize == 0) { // case of empty heap
			_heapSize = 1;
			_heap.add(0, key);
			return;
		}
		_heap.add(_heapSize, key); // place the max value kept aside, at the end of the array
		_heapSize += 1; // update size of heap
		heapify(_heapSize - 1, "insert"); // O(lgn)
	}

	/**
	 * Sorts the values of the Max-Min heap. Does not change the original heap
	 * 
	 * @return An ArrayList containing sorted values of heap
	 */
	public ArrayList<Integer> heapSort() {
		MaxMinHeap tempHeap = new MaxMinHeap(this); // copy heap
		ArrayList<Integer> sorted = new ArrayList<Integer>();
		for (int i = 0; i < _heapSize; i++) {
			Integer tempMax = tempHeap.extractMax();
			sorted.add(0, tempMax);
		}
		return sorted;
	}// end of heapSort

//////////////////////////////////////////////////////HELPER METHODS/////////////////////////////////

	/**
	 * Gets the value at a specific index
	 * 
	 * @param index The place of the wanted value
	 * @return The value at the given index
	 */
	public Integer get(int index) {
		if (index < 0 || index >= _heapSize)
			return null;
		return _heap.get(index);
	}

	/**
	 * Checks if current heap is empty
	 * 
	 * @return True if heap is empty and false otherwise
	 */
	public boolean isEmpty() {
		return _heapSize == 0;
	}

	/**
	 * Builds a String that displays the MaxMinHeap as a binary tree
	 * 
	 * @return String That displays the MaxMinHeap as a binary tree
	 */
	public String toString() {
		int pad1 = IOUtils.PAD1; // 5
		int pad2 = IOUtils.PAD2; // 3

		if (_heapSize == 0)
			return "\nHEAP IS EMPTY! NOTHING TO DISPLAY\n";

		System.out.println("\n********** HEAP DISPLAY AS TREE: ************\n");
		String heapStr = "";
		int finalDepth = indexDepth(_heapSize);
		int height;
		int index = 0;
		for (int depth = 0; depth <= finalDepth; depth++) // loop for each row of the tree
		{
			height = finalDepth - depth; // starts with 0;
			int unitNumber = (int) Math.pow(2, height); // spaces that come before the numbers in each row
			int subTreeWidth = unitNumber * pad1 + unitNumber - 1;
			int spacesBefore = subTreeWidth / 2 - pad1 / 2; // spaces to print before each row of numbers
			int spacesBetween = 2 * spacesBefore + 1; // spaces to print between the tree numbers
			String title = "(Level" + IOUtils.spacePad(depth, pad2) + ")";
			if (depth % 2 == 0)
				title += " MAX ";
			else
				title += " MIN ";
			heapStr += title + IOUtils.printSpaces(spacesBefore);
			int maxNodesInLevel = (int) Math.pow(2, depth);
			for (int node = 1; node <= maxNodesInLevel; node++) // loop on the number of nodes in a row
			{
				if (index < _heapSize)
					heapStr += IOUtils.spacePad(get(index), pad1);
				if (node < maxNodesInLevel)
					heapStr += IOUtils.printSpaces(spacesBetween);
				index++;
			}
			heapStr += "\n\n"; // a row of space between levels
		}
		return new String(heapStr);
	}

	/**
	 * Displays the MaxMinHeap as an array Complexity O(n) (n is the length of the
	 * array representing the heap)
	 */
	public void showHeapAsArray() {
		if (_heapSize == 0) {
			System.out.println("\nHEAP IS EMPTY! NOTHING TO DISPLAY\n");
			return;
		}
		System.out.println("\nDisplay Max-Min heap as an array:\n");

		String heapStr = "";
		for (int i = 0; i < _heapSize; i++)
			if (i == _heapSize - 1)
				heapStr += get(i);
			else
				heapStr += get(i) + ", ";
		System.out.println(heapStr);
	}

	/**
	 * Replaces values in array, between index ind1 and index ind2 Complexity O(1)
	 * 
	 * @param array array if integers
	 * @param ind1  index number 1 in array
	 * @param ind2  index number 2 in array
	 */
	private static void swap(ArrayList<Integer> array, int ind1, int ind2) {
		if (ind1 == ind2) // no swap happens if indices are identical
			return;
		int temp = array.get(ind1);
		array.set(ind1, array.get(ind2));
		array.set(ind2, temp);
	}// end swap

	/**
	 * Calculates the level number counting from top to bottom of the binary tree
	 * heap. Count starts from 0 . Complexity O(1)
	 * 
	 * @param ind index of array representing the tree
	 * @return level at which the index is
	 */
	private static int indexDepth(int ind) {
		return (int) (Math.log(ind + 1) / Math.log(2));
	}

	/**
	 * Calculates the index of the parent given index ind Complexity O(1)
	 * 
	 * @param index The index to calculate for
	 * @return The index of ind's parent
	 */
	private static int parent(int index) {
		int parentInd = (index + 1) / 2 - 1;
		if (parentInd < 0)
			return -1;
		return parentInd; // returns the floor of the result which is the right answer
	}

	/**
	 * Calculates the index of the left son of the given index ind. Complexity O(1)
	 * 
	 * @param current The index to calculate for
	 * @return The index of ind's left son
	 */
	private static int leftSon(int current) {
		return (current + 1) * 2 - 1;
	}

	/**
	 * Calculates the index of the right son of the given index ind. Complexity O(1)
	 * 
	 * @param current The index to calculate for
	 * @return The index of ind's right son
	 */
	private static int rightSon(int current) {
		return (current + 1) * 2;
	}

	/**
	 * Calculates if a value in the heap binary tree, has any children. (Complexity
	 * O(1))
	 * 
	 * @param current an index at the array that represents the binary tree
	 * @return true If the value has at lease one child
	 */
	private boolean hasChild(int current) {
		return leftSon(current) <= _heapSize;
	}

	/**
	 * Calculates the max value between a given index and its two sons (or son).
	 * (Complexity O(1))
	 * 
	 * @param current index to calculate for
	 * @return index of the maximum value between ind and its sons
	 */
	private int childInd(int current, int mode) {
		int left = leftSon(current); // left already considers the 0
		int right = rightSon(current); // right already considers the 0
		int max = current; // initialization
		int min = current; // initialization

		if (left < _heapSize) // check if left child exists
		{
			if (get(left) > get(max))
				max = left;
			if (get(left) < get(min))
				min = left;
		}
		if (right < _heapSize) // check if right child exists
		{
			if (get(right) > get(max))
				max = right;
			if (get(right) < get(min))
				min = right;
		}
		if (mode == 1)
			return max;
		return min;
	}

	/**
	 * Calculates if a value in the heap binary tree, has any grandchildren.
	 * (Complexity O(1))
	 * 
	 * @param current an index at the array that represents the binary tree
	 * @return true If the value has at least one grand child
	 */
	private boolean hasGrandchild(int current) {
		if (!hasChild(current)) // if no child then no grand child
			return false;
		return hasChild(leftSon(current)); // first grand child comes from left son.
	}

	/**
	 * Calculates the max or min value between a given index and its four possible
	 * grandchildren (or less). Complexity O(1)
	 * 
	 * @param current index to calculate for
	 * @param mode    - 1 to calculate max grandchild and, 0 to calculate min
	 *                grandchild
	 * @return index of the maximum or minimum value between ind and its
	 *         grandchildren
	 */
	private int grandchildInd(int current, int mode) {
		current = current + 1; // to avoid the 0 case
		int[] grandchildren = { current * 4, current * 4 + 1, current * 4 + 2, current * 4 + 3 }; // indices of
																									// grandchildren of
																									// ind
		int max = current - 1;
		int min = current - 1;
		int grandchild;
		for (int i = 0; i < grandchildren.length; i++) {
			grandchild = grandchildren[i] - 1;
			if (grandchild < _heapSize && rightSon(current - 1) < _heapSize) {
				if (get(grandchild) > get(max))
					max = grandchild;
				if (get(grandchild) < get(min))
					min = grandchild;
			}
		}
		// in case of less than 3 grandchildren. comparison to right son is required
		if (grandchildren[2] - 1 >= _heapSize && rightSon(current - 1) < _heapSize)
			if (get(rightSon(current - 1)) > get(max))
				max = rightSon(current - 1);
			else if (get(rightSon(current - 1)) < get(min))
				min = rightSon(current - 1);
		if (mode == 0)
			return min;
		return max; // else (mode ==1)

	}// end grandchildInd

}
