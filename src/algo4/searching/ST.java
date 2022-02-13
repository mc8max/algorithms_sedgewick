package algo4.searching;

public interface ST<Key, Value> {
	
	public void put(Key k, Value v);
	
	public Value get(Key k);
	
	public default void delete(Key k) {
		put(k, null);
	}
	
	public default boolean contains(Key k) {
		return get(k) != null;
	}
	
	public int size();
	
	public default boolean isEmpty() {
		return size() == 0;
	}
	
	public Iterable<Key> keys();
}
