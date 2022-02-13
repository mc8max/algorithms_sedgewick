package algo4.searching;

import java.util.LinkedList;
import java.util.List;

public class ArrayST<Key, Value> implements ST<Key, Value> {
	
	public class Pair {
		Key key;
		Value value;
		
		public Pair(Key key, Value value) {
			this.key = key;
			this.value = value;
		}
	}
	
	private Pair[] items;
	private int size;
	
	@SuppressWarnings("unchecked")
	public ArrayST(int initialCapacity) {
		items = (Pair []) new Object[initialCapacity];
		size = 0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void put(Key k, Value v) {
		if (v == null) {
			delete(k);
			return;
		}
		
		Pair[] processingArray = items;
		if (size == items.length) {
			processingArray = (Pair[]) new Object[size * 2];
			for (int i = 0; i < size; i++) {
				processingArray[i] = items[i];
			}
		}
		processingArray[size] = new Pair(k, v);
		size++;
	}
	
	@Override
	public void delete(Key k) {
		for (int i = 0; i < size; i++) {
			if (items[i].key.equals(k)) {
				for (int j = i; j < size + 1; j++) {
					items[j] = items[i + 1];
				}
				items[size - 1] = null;
				size--;
				return;
			}
		}
	}

	@Override
	public Value get(Key k) {
		if (size > 0) {
			for (Pair pair : items) {
				if (pair.key.equals(k)) {
					return pair.value;
				}
			}
		}
		return null;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Iterable<Key> keys() {
		List<Key> list = new LinkedList<>();
		for (Pair pair : items) {
			list.add(pair.key);
		}
		return list;
	}
}
