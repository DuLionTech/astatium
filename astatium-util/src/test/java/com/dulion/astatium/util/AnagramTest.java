package com.dulion.astatium.util;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class AnagramTest {

	@Test
	public void anagramTest() {
		String[] result = Anagram.produce("ABCDEFG");
		System.out.println(result.length);

		Set<String> items = new HashSet<>();
		Arrays.stream(result).forEach(item -> {
			if (!items.add(item)) {
				fail(item);
			}
		});
	}
}
