package algo4.searching;

import java.util.HashMap;
import java.util.Map;

public class MapST<Key, Value> implements ST<Key, Value> {
	
	private Map<Key, Value> map;
	
	public MapST() {
		map = new HashMap<>();
	}
	
	@Override
	public void put(Key k, Value v) {
		map.put(k, v);
	}

	@Override
	public Value get(Key k) {
		return map.get(k);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Iterable<Key> keys() {
		return map.keySet();
	}
}
