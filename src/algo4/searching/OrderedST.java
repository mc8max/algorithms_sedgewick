package algo4.searching;

public interface OrderedST<Key extends Comparable<Key>, Value> extends ST<Key, Value> {
	
	public static final int RANK_NOT_FOUND = -1;
	
	public Key min();
	
	public Key max();
	
	public Key floor(Key k);
	
	public Key ceil(Key k);
	
	public int rank(Key k);
	
	public Key select(int rank);
	
	public default void deleteMin() {
		delete(min());
	}
	
	public default void deleteMax() {
		delete(max());
	}
	
	public default int size(Key low, Key high) {
		if (high.compareTo(low) < 0) {
			return 0;
		}
		
		if (contains(high)) {
			return rank(high) - rank(low) + 1;
		}
		return rank(high) - rank(low);
	}
	
	public Iterable<Key> keys(Key low, Key high);
	
	@Override
	public default Iterable<Key> keys() {
		return keys(min(), max());
	}
}
