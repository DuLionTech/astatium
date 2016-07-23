package com.dulion.astatium.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubsetSum {

	public static int[][] search(int total, int... values) {
		return new Instance(total, values).search();
	}

	private static class Instance {

		private final int total;

		private final int[] values;

		private final int[] solution;

		private final List<int[]> solutions = new ArrayList<>();

		public Instance(int total, int[] values) {
			this.total = total;
			this.values = values;
			this.solution = new int[values.length];
		}

		public int[][] search() {
			search(0, 0, 0);
			return solutions.toArray(new int[solutions.size()][]);
		}

		public void search(int index, int size, int sum) {
			if (index < values.length) {
				search(index + 1, size + 1, sum + (solution[size] = values[index]));
				search(index + 1, size, sum);
			} else if (size > 0 && sum <= total) {
				solutions.add(Arrays.copyOf(solution, size));
			}
		}

	}
}
