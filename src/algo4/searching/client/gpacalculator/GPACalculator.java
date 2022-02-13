package algo4.searching.client.gpacalculator;

import algo4.searching.BinarySearchST;

public class GPACalculator {
	public static void main(String[] args) {
		double gpa = 0;
		BinarySearchST<String, Double> st = createTable();
		for (String a : args) {
			if (st.contains(a)) {
				gpa += st.get(a);
			} else {
				throw new RuntimeException("Unrecognized grade: " + a);
			}
		}
		if (args.length > 0) {
			System.out.println("GPA = " + gpa / args.length);
		}
		else {
			System.out.println("GPA = " + 0);
		}
	}

	static BinarySearchST<String, Double> createTable() {
		BinarySearchST<String, Double> st = new BinarySearchST<>(11);
		st.put("A+", 4.33);
		st.put("A", 4.00);
		st.put("A-", 3.67);
		st.put("B+", 3.33);
		st.put("B", 3.00);
		st.put("B-", 2.67);
		st.put("C+", 2.33);
		st.put("C", 2.00);
		st.put("C-", 1.67);
		st.put("D", 1.00);
		st.put("F", 0.00);
		return st;
	}
}
