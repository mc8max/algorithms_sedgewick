package algo4.searching.test;

import org.junit.jupiter.api.Test;

import algo4.searching.BinarySearchST;

public class TestBinarySearchSymbolTable {
	private BinarySearchST<Integer, Integer> create() {
		return new BinarySearchST<>(64);
	}
	
	@Test
	public void test1_100() {
		BinarySearchST<Integer, Integer> st = create();
		final int SIZE = 100;
		for (int i = 0; i < SIZE; i++) {
			st.put(i, i * i);
		}
		
		assert(st.size() == SIZE);
		assert(st.min() == 0);
		assert(st.max() == SIZE - 1);
		assert(st.ceil(-1) == 0);
		assert(st.ceil(100) == null);
		assert(st.floor(100) == 99);
		assert(st.floor(-1) == null);
		
		for (int i = 0; i < SIZE; i++) {
			assert(st.contains(i));
			assert(st.rank(i) == i);
		}
		
		testSortedOrder(st);
	}
	
	@Test
	public void testEven() {
		BinarySearchST<Integer, Integer> st = create();
		final int SIZE = 100;
		for (int i = 0; i < SIZE; i+=2) {
			st.put(i, i * i);
		}
		
		assert(st.size() == SIZE / 2);
		assert(st.min() == 0);
		assert(st.max() == 98);
		assert(st.ceil(3) == 4);
		assert(st.floor(3) == 2);
		
		for (int i = 0; i < SIZE; i++) {
			if (i % 2 == 0) {
				assert(st.contains(i));
				assert(st.rank(i) == i / 2);
			}
			else {
				assert(!st.contains(i));
			}
		}
		
		testSortedOrder(st);
	}
	
	@Test
	public void testDelete() {
		BinarySearchST<Integer, Integer> st = create();
		final int SIZE = 100;
		for (int i = 0; i < SIZE; i++) {
			st.put(i, i * i);
		}
		
		for (int i = 0; i < SIZE / 2; i++) {
			st.delete(i);
		}
		
		assert(st.size() == SIZE / 2);
		assert(st.min() == 50);
		assert(st.max() == SIZE - 1);
		
		testSortedOrder(st);
	}
	
	private void testSortedOrder(BinarySearchST<Integer, Integer> st) {
		int prev = 0;
		boolean first = true;
		for (Integer key : st.keys()) {
			if (first) {
				first = false;
			} else {
				assert (prev < key);
			}
			prev = key;
		}
	}
}
