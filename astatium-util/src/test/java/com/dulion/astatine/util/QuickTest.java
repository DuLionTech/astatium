/**
 * Copyright 2016 Phillip DuLion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dulion.astatine.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.Test;

import com.dulion.astatium.util.Quick;

public class QuickTest {

	@Test
	public void testMany() {
		IntStream.range(0, 1000).forEach(i -> randomTest());
		System.out.println(Quick.allSorts.stream().mapToInt(Integer::intValue).average());
		System.out.println(Quick.allSwaps.stream().mapToInt(Integer::intValue).average());
		System.out.println(2 * Math.log(50));
	}
	
	public void randomTest() {
		Random random = new Random();
		Integer[] values = random.ints(500, 0, 100).mapToObj(Integer::new).toArray(Integer[]::new);
		Integer[] partial = Quick.sort(values, Integer::compare);
		Arrays.sort(values);

		int upper = values.length / 2;
		int lower = (0 == values.length % 2) ? upper - 1 : upper;

		assertEquals(values[lower], partial[lower]);
		assertEquals(values[upper], partial[upper]);
	}

	public void breakingTest() {
		Integer[] values = new Integer[] { 4, 47, 46, 87, 27, 32, 28, 66, 38, 47 };
		Integer[] partial = Quick.sort(values, Integer::compare);

		Arrays.sort(values);
		Arrays.stream(values).forEach(i -> System.out.printf("%d ", i));

		int upper = values.length / 2;
		int lower = (0 == values.length % 2) ? upper - 1 : upper;

		System.out.printf("%n%d %d", values[lower], values[upper]);
		System.out.printf("%n%d %d", partial[lower], partial[upper]);
		
		assertArrayEquals(values, partial);
	}
}
