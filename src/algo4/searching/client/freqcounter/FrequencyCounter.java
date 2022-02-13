package algo4.searching.client.freqcounter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import algo4.searching.BinarySearchST;
import algo4.searching.MapST;
import algo4.searching.ST;
import algo4.searching.SequentialST;

public class FrequencyCounter {
	public static void main(String [] args) {
		File inputFile = new File(args[0]);
		int length = Integer.parseInt(args[1]);
		run(new MapST<>(), inputFile, length);
//		run(new SequentialST<>(), inputFile, length);
		run(new BinarySearchST<>(64), inputFile, length);
	}
	
	static void run(ST<String, Integer> st, File inputFile, int length) {
		long start = System.nanoTime();
		try (Scanner sc = new Scanner(inputFile)) {
			
			while (sc.hasNext()) {
				String word = sc.next();
				if (word.length() < length) {
					continue;
				}
				if (st.contains(word)) {
					st.put(word, st.get(word) + 1);
				}
				else {
					st.put(word, 1);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		List<String> maxWords = new LinkedList<>();
		int maxCount = 0;
		for (String key : st.keys()) {
			int count = st.get(key);
			if (maxCount < count) {
				maxCount = count;
				maxWords.clear();
				maxWords.add(key);
			} else if (maxCount == count) {
				maxWords.add(key);
			}
		}
		
		long time = System.nanoTime() - start;
		time /= 1000000;
		
		System.out.println(st.getClass().getSimpleName() + ": " + maxCount + " - " + time + "msec");
		for (String word : maxWords) {
			System.out.print(word + " ");
		}
		System.out.println();
	}
}
