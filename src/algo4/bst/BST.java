package algo4.bst;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class BST<Key extends Comparable<Key>, Value> {
	public static final int RANK_NOT_FOUND = -1;
	public static final boolean RED = true;
	public static final boolean BLACK = false;

	private Node root;
	private Node cache;

	public class Node {
		private Key key;
		private Value value;
		private Node left;
		private Node right;
		private int size;
		private boolean color;

		private Node prev;
		private Node next;

		public Node(Key key, Value value, int size, boolean color) {
			this.key = key;
			this.value = value;
			this.size = size;
			this.color = color;
			this.prev = null;
			this.next = null;
		}

		static boolean isBinaryTree(Node n) {
			if (n == null) {
				return true;
			}

			int size = 1;
			if (n.left != null) {
				size += n.left.size;
			}

			if (n.right != null) {
				size += n.right.size;
			}

			if (n.size != size) {
				return false;
			}

			return isBinaryTree(n.left) && isBinaryTree(n.right);
		}
	}

	public int size() {
		return size(root);
	}

	protected int size(Node n) {
		return n == null ? 0 : n.size;
	}

	public boolean contains(Key key) {
		Optional<Node> cache = checkCache(key);
		return cache.isEmpty() ? (get(key) != null) : true;
	}

	public Value get(Key key) {
		Optional<Node> cache = checkCache(key);
		return cache.isEmpty() ? get(key, root).value : cache.get().value;
	}

	protected Node get(Key key, Node node) {
		if (node == null) {
			return null;
		}

		int cmp = key.compareTo(node.key);
		if (cmp == 0) {
			setCache(node);
			return node;
		} else if (cmp < 0) {
			return get(key, node.left);
		} else {
			return get(key, node.right);
		}
	}

	public void put(Key key, Value value) {
		Optional<Node> cache = checkCache(key);
		if (cache.isEmpty()) {
			root = put(key, value, root);
			root.color = BLACK;
		} else {
			cache.get().value = value;
		}
	}

	protected Node put(Key key, Value value, Node node) {
		if (node == null) {
			return new Node(key, value, 1, RED);
		}

		int cmp = key.compareTo(node.key);
		if (cmp == 0) {
			setCache(node);
			node.value = value;
		} else if (cmp < 0) {
			node.left = put(key, value, node.left);
			updatePrevious(node);
		} else {
			node.right = put(key, value, node.right);
			updateNext(node);
		}

		if (isRed(node.right) && !isRed(node.left)) {
			node = rotateLeft(node);
		}
		if (isRed(node.left) && isRed(node.left.left)) {
			node = rotateRight(node);
		}
		if (isRed(node.left) && isRed(node.right)) {
			flipColor(node);
		}
		
		node.size = size(node.left) + size(node.right) + 1;
		return node;
	}

	public Key min() {
		Node minNode = min(root);
		return minNode == null ? null : minNode.key;
	}

	protected Node min(Node node) {
		if (node == null) {
			return null;
		}
		Node curr = node;
		while (curr.left != null) {
			curr = curr.left;
		}
		return curr;
	}

	public Key max() {
		Node maxNode = max(root);
		return maxNode == null ? null : maxNode.key;
	}

	protected Node max(Node node) {
		if (node == null) {
			return null;
		}
		Node curr = node;
		while (curr.right != null) {
			curr = curr.right;
		}
		return curr;
	}

	public Key floor(Key key) {
		return floor(key, root).map(n -> n.key).orElse(null);
	}

	protected Optional<Node> floor(Key key, Node node) {
		if (node == null) {
			return Optional.empty();
		}

		int cmp = key.compareTo(node.key);
		if (cmp == 0) {
			setCache(node);
			return Optional.of(node);
		} else if (cmp < 0) {
			return floor(key, node.left);
		} else {
			return Optional.of(floor(key, node.right).orElse(node));
		}
	}

	public Key ceiling(Key key) {
		return ceiling(key, root).map(n -> n.key).orElse(null);
	}

	protected Optional<Node> ceiling(Key key, Node node) {
		if (node == null) {
			return Optional.empty();
		}

		int cmp = key.compareTo(node.key);
		if (cmp == 0) {
			setCache(node);
			return Optional.of(node);
		} else if (cmp > 0) {
			return ceiling(key, node.right);
		} else {
			return Optional.of(ceiling(key, node.left).orElse(node));
		}
	}

	public Key select(int rank) {
		return select(root, rank).map(n -> n.key).orElse(null);
	}

	protected Optional<Node> select(Node node, int rank) {
		if (node == null || node.size < rank) {
			return Optional.empty();
		}
		int leftRank = size(node.left);
		if (leftRank == rank) {
			return Optional.of(node);
		} else if (leftRank > rank) {
			return select(node.left, rank);
		} else {
			return select(node.right, rank - leftRank - 1);
		}
	}

	public int rank(Key k) {
		return rank(k, root);
	}

	protected int rank(Key k, Node node) {
		if (node == null) {
			return RANK_NOT_FOUND;
		}

		int cmp = k.compareTo(node.key);
		if (cmp == 0) {
			setCache(node);
			return size(node.left);
		} else if (cmp < 0) {
			return rank(k, node.left);
		}
		int rank = rank(k, node.right);
		if (rank != RANK_NOT_FOUND) {
			return rank + 1 + size(node.left);
		}
		return RANK_NOT_FOUND;
	}

	public void deleteMin() {
		root = deleteMin(root);
	}

	protected Node deleteMin(Node node) {
		if (node == null) {
			return null;
		}
		if (node.left == null) {
			return node.right;
		}

		node.left = deleteMin(node.left);
		node.size = 1 + size(node.left) + size(node.right);
		updatePrevious(node);
		return node;
	}

	public void deleteMax() {
		root = deleteMax(root);
	}

	protected Node deleteMax(Node node) {
		if (node == null) {
			return null;
		}
		if (node.right == null) {
			return node.left;
		}

		node.right = deleteMin(node.right);
		node.size = 1 + size(node.left) + size(node.right);
		updateNext(node);
		return node;
	}

	public void delete(Key key) {
		root = delete(key, root);
	}

	protected Node delete(Key key, Node node) {
		if (node == null) {
			return null;
		}

		int cmp = key.compareTo(node.key);
		if (cmp < 0) {
			node.left = delete(key, node.left);
			updatePrevious(node);
		} else if (cmp > 0) {
			node.right = delete(key, node.right);
			updateNext(node);
		} else {
			if (node.left == null) {
				return node.right;
			}
			if (node.right == null) {
				return node.left;
			}
			Node temp = node;
			node = min(temp.right);
			node.right = deleteMin(temp.right);
			node.left = temp.left;
		}

		node.size = size(node.left) + size(node.right) + 1;
		return node;
	}

	private void print(Node node) {
		if (node == null) {
			return;
		}

		print(node.left);
		System.out.print(node.key + " ");
		print(node.right);
	}

	public Iterable<Key> keys() {
		return keys(min(), max());
	}

	public Iterable<Key> keys(Key low, Key high) {
		Queue<Key> queue = new LinkedList<>();
		keys(root, queue, low, high);
		return queue;
	}

	public Iterable<Key> keysNonRecursive() {
		if (root == null) {
			return Collections.emptyList();
		}

		Stack<Node> stack = new Stack<>();

		Node node = root;
		while (node != null) {
			stack.add(node);
			node = node.left;
		}

		Queue<Key> queue = new LinkedList<>();
		while (!stack.isEmpty()) {
			node = stack.pop();
			queue.add(node.key);
			node = node.right;
			while (node != null) {
				stack.add(node);
				node = node.left;
			}
		}

		return queue;
	}

	private void keys(Node node, Queue<Key> queue, Key low, Key high) {
		if (node == null) {
			return;
		}

		int cmpLow = node.key.compareTo(low);
		int cmpHigh = node.key.compareTo(high);

		if (cmpLow > 0) {
			keys(node.left, queue, low, high);
		}

		if (cmpLow >= 0 && cmpHigh <= 0) {
			queue.add(node.key);
		}

		if (cmpHigh < 0) {
			keys(node.right, queue, low, high);
		}
	}

	public int height() {
		return height(root);
	}

	private int height(Node node) {
		if (node == null) {
			return 0;
		}

		return 1 + Math.max(height(node.left), height(node.right));
	}

	public double avgCompares() {
		int count = avgCompares(root, 0);
		if (count > 0) {
			return ((double) count) / size(root);
		}
		return count;
	}

	private int avgCompares(Node node, int compares) {
		if (node == null) {
			return 0;
		}

		compares++;
		return compares + avgCompares(node.left, compares) + avgCompares(node.right, compares);
	}

	protected boolean hasCache() {
		return cache != null;
	}

	protected Optional<Node> checkCache(Key key) {
		if (hasCache()) {
			if (cache.key.equals(key)) {
				return Optional.of(cache);
			}
		}
		clearCache();
		return Optional.empty();
	}

	protected void setCache(Node n) {
		cache = n;
	}

	protected void clearCache() {
		cache = null;
	}

	public boolean orderCheck() {
		if (root != null) {
			return orderCheck(root, min(), max());
		}
		return true;
	}

	private boolean orderCheck(Node node, Key min, Key max) {
		if (node == null) {
			return true;
		}

		int cmpLeft = node.key.compareTo(min);
		if (cmpLeft < 0) {
			return false;
		}

		int cmpRight = node.key.compareTo(max);
		if (cmpRight > 0) {
			return false;
		}

		return orderCheck(node.left, min, max) && orderCheck(node.right, min, max);
	}

	public boolean hasNoDuplicates() {
		return hasNoDuplicates(root, new HashSet<Key>());
	}

	private boolean hasNoDuplicates(Node node, Set<Key> existingKeys) {
		if (node == null) {
			return true;
		}

		if (existingKeys.contains(node.key)) {
			return false;
		}
		existingKeys.add(node.key);
		return hasNoDuplicates(root.left, existingKeys) && hasNoDuplicates(root.right, existingKeys);
	}

	public boolean isBST() {
		if (!hasNoDuplicates()) {
			return false;
		}
		if (!Node.isBinaryTree(root)) {
			return false;
		}
		if (!orderCheck()) {
			return false;
		}
		return true;
	}

	public boolean checks() {
		for (int rank = 0; rank < size(); rank++) {
			if (rank != rank(select(rank))) {
				return false;
			}
		}
		return true;
	}

	public Key next(Key key) {
		Node node = get(key, root);
		if (node == null) {
			return null;
		}
		node = node.next;
		return node == null ? null : node.key;
	}

	public Key prev(Key key) {
		Node node = get(key, root);
		if (node == null) {
			return null;
		}
		node = node.prev;
		return node == null ? null : node.key;
	}

	private void updatePrevious(Node node) {
		Node maxLeft = max(node.left);
		if (maxLeft != null) {
			maxLeft.next = node;
		}
		node.prev = maxLeft;
	}

	private void updateNext(Node node) {
		Node minNext = min(node.right);
		if (minNext != null) {
			minNext.prev = node;
		}
		node.next = minNext;
	}

	public void printLevel() {
		printLevel(root);
	}

	private void printLevel(Node node) {
		Queue<Node> queue = new LinkedList<>();
		queue.add(node);

		while (!queue.isEmpty()) {
			Node n = queue.remove();
			System.out.print(n.key + " ");
			if (n.left != null) {
				queue.add(n.left);
			}
			if (n.right != null) {
				queue.add(n.right);
			}
		}
	}

	private Node rotateLeft(Node h) {
		Node x = h.right;
		h.right = x.left;
		x.left = h;
		x.color = h.color;
		h.color = RED;
		x.size = h.size;
		h.size = 1 + size(h.left) + size(h.right);
		return x;
	}
	
	private Node rotateRight(Node h) {
		Node x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = h.color;
		h.color = RED;
		x.size = h.size;
		h.size = 1 + size(h.left) + size(h.right); 
		return x;
	}
	
	private void flipColor(Node h) {
		h.color = RED;
		if (h.left != null) {
			h.left.color = BLACK;
		}
		if (h.right != null) {
			h.right.color = BLACK;
		}
	}

	private boolean isRed(Node node) {
		if (node == null) {
			return false;
		}
		return node.color == RED;
	}
}
