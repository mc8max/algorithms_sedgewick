package algo4.searching;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OrderedSequentialST<Key extends Comparable<Key>, Value> extends SequentialST<Key, Value>
		implements OrderedST<Key, Value> {

	@Override
	public void put(Key k, Value v) {
		if (v == null) {
			delete(k);
			return;
		}
		
		Node prev = null;
		Node curr = first;
		while (curr != null) {
			int cmp = curr.key.compareTo(k);
			if (cmp == 0) {
				curr.value = v;
				return;
			} else if (cmp > 0) {
				Node node = new Node(k, v, curr);
				if (prev != null) {
					prev.next = node;
				} else {
					first = node;
				}
				return;
			}
			
			prev = curr;
			curr = curr.next;
		}
		
		Node node = new Node(k, v, null);
		if (prev != null) {
			prev.next = node;
		} else {
			first = node;
		}
	}
	
	@Override
	public Key min() {
		return first != null ? first.key : null;
	}

	@Override
	public Key max() {
		if (first != null) {
			Node node = first;
			while (node.next != null) {
				node = node.next;
			}
			return node.key;
		}
		return null;
	}

	@Override
	public Key floor(Key k) {
		Optional<Node> opt = getFloor(k);
		return opt.isEmpty() ? null : opt.get().key;
	}
	
	private Optional<Node> getFloor(Key k) {
		Node prev = null;
		Node curr = first;
		while (curr != null) {
			int cmp = curr.key.compareTo(k);
			if (cmp == 0) {
				return Optional.of(curr);
			} else if (cmp > 0) {
				break;
			}
		}
		return Optional.ofNullable(prev);
	}

	@Override
	public Key ceil(Key k) {
		Optional<Node> opt = getCeil(k);
		return opt.isEmpty() ? null : opt.get().key;
	}
	
	private Optional<Node> getCeil(Key k) {
		Node curr = first;
		while (curr != null) {
			int cmp = curr.key.compareTo(k);
			if (cmp == 0) {
				return Optional.of(curr);
			} else if (cmp < 0) {
				return Optional.ofNullable(curr.next);
			}
		}
		return Optional.empty();
	}

	@Override
	public int rank(Key k) {
		int rank = 0;
		Node curr = first;
		while (curr != null) {
			int cmp = curr.key.compareTo(k);
			if (cmp == 0) {
				return rank;
			} else if (cmp > 0) {
				return RANK_NOT_FOUND;
			}
			rank++;
		}
		return RANK_NOT_FOUND;
	}

	@Override
	public Key select(int rank) {
		if (rank >= 0) {
			Node curr = first;
			while (curr != null && rank > 0) {
				curr = curr.next;
				rank--;
			}
			return curr != null ? curr.key : null;
		}
		return null;
	}

	@Override
	public Iterable<Key> keys(Key low, Key high) {
		Optional<Node> opt = getCeil(low);
		if (opt.isEmpty()) {
			return Collections.emptySet();
		}
		
		Node lowNode = opt.get();
		
		opt = getFloor(high);
		if (opt.isEmpty()) {
			return Collections.emptySet();
		}
		
		Node highNode = opt.get();
		List<Key> list = new LinkedList<>();
		while (lowNode != highNode) {
			list.add(lowNode.key);
			lowNode = lowNode.next;
		}
		list.add(lowNode.key);
		return list;
	}

}
