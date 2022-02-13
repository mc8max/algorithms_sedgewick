package algo4.searching;

import java.util.LinkedList;
import java.util.List;

public class SequentialST<Key, Value> implements ST<Key, Value> {

	class Node {
		Key key;
		Value value;
		Node next;
		
		public Node(Key k, Value v, Node next) {
			this.key = k;
			this.value = v;
			this.next = next;
		}
	}
	
	protected Node first;
	
	public SequentialST() {
		first = null;
	}
	
	@Override
	public void put(Key k, Value v) {
		if (v == null) {
			delete(k);
			return;
		}
		
		for (Node node = first; node != null; node = node.next) {
			if (node.key.equals(k)) {
				node.value = v;
				return;
			}
		}
		first = new Node(k, v, first);
	}
	
	public void delete(Key k) {
		if (isEmpty()) {
			return;
		}
		Node prev = null;
		Node current = first;
		while (current != null) {
			if (current.key.equals(k)) {
				if (prev == null) {
					first = first.next;
				} else {
					prev.next = current.next;
				}
				return;
			}
			prev = current;
			current = current.next;
		}
	}

	@Override
	public Value get(Key k) {
		for (Node node = first; node != null; node = node.next) {
			if (node.key.equals(k)) {
				return node.value;
			}
		}
		return null;
	}

	@Override
	public int size() {
		int count = 0;
		for (Node node = first; node != null; node = node.next) {
			count++;
		}
		return count;
	}

	@Override
	public Iterable<Key> keys() {
		List<Key> list = new LinkedList<>();
		for (Node node = first; node != null; node = node.next) {
			list.add(node.key);
		}
		return list;
	}
}
