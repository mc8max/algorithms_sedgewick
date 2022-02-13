package algo4.searching;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BinarySearchST<Key extends Comparable<Key>, Value> implements OrderedST<Key, Value> {

	private class Item implements Comparable<Item>{
		private Key key;
		private Value value;
		
		public Item(Key key, Value value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public int compareTo(BinarySearchST<Key, Value>.Item o) {
			return this.key.compareTo(o.key);
		}
	}
	
	private class Cache {
		Key key;
		int rank;
		
		public Cache(Key key, int rank) {
			this.key = key;
			this.rank = rank;
		}
	}

	private Item[] items;
	private int size;
	protected Cache cache;

	@SuppressWarnings("unchecked")
	public BinarySearchST(int initialCapacity) {
		items = new BinarySearchST.Item[initialCapacity];
		size = 0;
		cache = null;
	}

	@Override
	public void put(Key k, Value v) {
		if (v != null) {
			if (size > 0 && k.compareTo(max()) > 0) {
				insert(k, v, size);
				return;
			}
			
			if (size == 0 || k.compareTo(min()) < 0) {
				insert(k, v, 0);
				return;
			}
			
			int floor = floorRank(k, 0, size -1);
			if (floor != RANK_NOT_FOUND && items[floor].key.equals(k)) {
				items[floor].value = v;
				return;
			}
			
			insert(k, v, floor == RANK_NOT_FOUND ? 0 : floor + 1);
		}
		else {
			delete(k);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void insert(Key k, Value v, int pos) {
		Item[] processingItems = items;
		
		if (size == items.length) {
			processingItems = (Item[]) new BinarySearchST.Item[size * 2];
			for (int i = 0; i < pos; i++) {
				processingItems[i] = items[i];
			}
		}
		
		for (int i = size; i > pos; i--) {
			processingItems[i] = items[i - 1];
		}
		
		processingItems[pos] = new Item(k, v);
		items = processingItems;
		size++;
	}
	
	@Override
	public void delete(Key k) {
		int rank = rank(k);
		if (rank == RANK_NOT_FOUND) {
			return;
		}
		
		for (int i = rank; i < size - 1; i++) {
			items[i] = items[i + 1];
		}
		items[size - 1] = null;
		size--;
	}

	@Override
	public Value get(Key k) {
		int rank = rank(k, 0, size - 1);
		return rank == RANK_NOT_FOUND ? null : items[rank].value;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Key min() {
		if (size > 0) {
			return items[0].key;
		}
		return null;
	}

	@Override
	public Key max() {
		if (size > 0) {
			return items[size - 1].key;
		}
		return null;
	}

	@Override
	public Key floor(Key k) {
		int rank = floorRank(k, 0, size - 1);
		return rank == RANK_NOT_FOUND ? null : items[rank].key;
	}

	private int floorRank(Key k, int low, int high) {
		Optional<Cache> cache = checkCache(k);
		if (cache.isPresent()) {
			return cache.get().rank;
		}
		
		if (low > high) {
			return RANK_NOT_FOUND;
		}
		
		int floor = RANK_NOT_FOUND;
		if (k.compareTo(items[high].key) >= 0) {
			return high;
		}
		if (k.compareTo(items[low].key) < 0) {
			return RANK_NOT_FOUND;
		}
		
		while (low <= high) {
			int mid = (low + high) / 2;
			int cmp = k.compareTo(items[mid].key);
			if (cmp == 0) {
				addCache(items[mid].key, mid);
				return mid;
			} else if (cmp > 0) {
				floor = mid;
				low = mid + 1;
			} else if (cmp < 0) {
				high = mid - 1;
			}
		}
		return floor;
	}

	@Override
	public Key ceil(Key k) {
		int rank = ceilRank(k, 0, size - 1);
		return rank == -1 ? null : items[rank].key;
	}
	
	private int ceilRank(Key k, int low, int high) {
		Optional<Cache> cache = checkCache(k);
		if (cache.isPresent()) {
			return cache.get().rank;
		}
		
		if (low > high) {
			return RANK_NOT_FOUND;
		}
		
		int ceil = RANK_NOT_FOUND;
		if (k.compareTo(items[high].key) > 0) {
			return RANK_NOT_FOUND;
		}
		if (k.compareTo(items[low].key) <= 0) {
			return low;
		}
		
		while (low <= high) {
			int mid = (low + high) / 2;
			int cmp = k.compareTo(items[mid].key);
			if (cmp == 0) {
				addCache(items[mid].key, mid);
				return mid;
			} else if (cmp > 0) {
				low = mid + 1;
			} else if (cmp < 0) {
				ceil = mid;
				high = mid - 1;
			}
		}
		return ceil;
	}

	@Override
	public int rank(Key k) {
		return rank(k, 0, size - 1);
	}

	private int rank(Key k, int low, int high) {
		Optional<Cache> cache = checkCache(k);
		if (cache.isPresent()) {
			return cache.get().rank;
		}
		while (low <= high) {
			int mid = (low + high) / 2;
			int compare = items[mid].key.compareTo(k);
			if (compare == 0) {
				addCache(items[mid].key, mid);
				return mid;
			} else if (compare < 0) {
				low = mid + 1;
			} else {
				high = mid - 1;
			}
		}
		return RANK_NOT_FOUND;
	}

	@Override
	public Key select(int rank) {
		return items[rank].key;
	}

	@Override
	public Iterable<Key> keys(Key low, Key high) {
		low = ceil(low);
		high = floor(high);

		if (high.compareTo(low) < 0) {
			return Collections.emptySet();
		}

		List<Key> list = new LinkedList<>();
		for (int r = rank(low); r <= rank(high); r++) {
			list.add(items[r].key);
		}
		return list;
	}
	
	protected boolean hasCache() {
		return cache != null;
	}
	
	protected Optional<Cache> checkCache(Key key) {
		if (hasCache()) {
			if (cache.key.equals(key)) {
				return Optional.of(cache);
			}
		}
		clearCache();
		return Optional.empty();
	}
	
	protected void addCache(Key key, int rank) {
		cache = new Cache(key, rank);
	}
	
	protected void clearCache() {
		cache  = null;
	}
}
