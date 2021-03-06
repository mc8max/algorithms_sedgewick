package algo4.bst.test;

import java.util.Queue;

import org.junit.Test;

import algo4.bst.BST;

public class TestBST {
	private BST<Integer, Integer> create() {
		return new BST<Integer, Integer>();
	}
	
	private BST<Integer, Integer> create1_100() {
		BST<Integer, Integer> bst = create();
		final int SIZE = 100;
		for (int i = 0; i < SIZE; i++) {
			bst.put(i, i * i);
		}
		return bst;
	}
	
	@Test
	public void test1_100() {
		final int SIZE = 100;
		BST<Integer, Integer> bst = create1_100();
		
		assert(bst.orderCheck());
		assert(bst.size() == SIZE);
		
		int prev = -1;
		for (int key : bst.keys()) {
			assert(prev < key);
			assert(bst.rank(key) == key);
			prev = key;
		}
		
		System.out.println(bst.avgCompares());
		
		assert(bst.min() == 0);
		assert(bst.max() == 99);
		assert(bst.checks());
		
		assert(bst.ceiling(-99) == 0);
		assert(bst.floor(199) == 99);
		
		for (int i = 0; i < 50; i++) {
			bst.delete(i);
		}
		
		assert(bst.size() == 50);
		assert(bst.min() == 50);
		assert(bst.max() == 99);
	}
	
	@Test
	public void testKeys() {
		BST<Integer, Integer> bst = create1_100();
		
		Queue<Integer> queue = (Queue<Integer>) bst.keys();
		for (Integer key : bst.keysNonRecursive()) {
			assert(!queue.isEmpty());
			int k = queue.remove();
			assert(key == k);
		}
	}
}
