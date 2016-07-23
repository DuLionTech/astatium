package com.dulion.astatium.util;

import java.util.ArrayList;
import java.util.List;

public class Anagram {

	public static String[] produce(String sequence) {
		return new Instance(sequence).produce();
	}
	
	private static class Instance {
		
		private char[] sequence;
		
		private List<String> anagrams = new ArrayList<>();
		
		public Instance(String sequence) {
			this.sequence = sequence.toCharArray();
		}

		private String[] produce() {
			anagram(sequence.length);
			return anagrams.toArray(new String[anagrams.size()]);
		}

		private void anagram(int length) {
			if (length < 2) return;
			
			for (int i = 0; i < length; i++) {
				anagram(length - 1);
				if (length == 2) {
					anagrams.add(new String(sequence));
				}
				rotate(length);
			}
		}
		
		private void rotate(int length) {
			char temp = sequence[0];
			System.arraycopy(sequence, 1, sequence, 0, length - 1);
			sequence[length - 1] = temp;
		}
	}
}
