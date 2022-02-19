package algo4.bst.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import algo4.bst.BST;

public class PerfectlyBalance {
	public static void main(String [] args) {
		List<Integer> inputList = inputList();
		BST<Integer, Integer> bst = new BST<>();
		add(inputList, bst);
	}
	
	private static void add(List<Integer> inputList, BST<Integer, Integer> bst) {
		add(inputList, 0, inputList.size() - 1, bst);
	}
	
	private static void add(List<Integer> inputList, int low, int high, BST<Integer, Integer> bst) {
		if (low <= high) {
			int mid = (low + high) / 2;
			int key = inputList.get(mid);
			bst.put(key, key * key);
			add(inputList, low, mid - 1, bst);
			add(inputList, mid + 1, low, bst);
		}
	}
	
	private static List<Integer> inputList() {
		Random rand = new Random();
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i <= 100; i++) {
			list.add(rand.nextInt());
		}
		return list;
	}
}
