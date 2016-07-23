package com.dulion.astatium.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SubsetSumTest {

	@Test
	public void subsetTest06() {
		int[][] result = SubsetSum.search(6, 3, 2, 7, 1);
		assertEquals(7, result.length);
		int i = 0;
		assertArrayEquals(new int[] { 3, 2, 1 }, result[i++]);
		assertArrayEquals(new int[] { 3, 2 }, result[i++]);
		assertArrayEquals(new int[] { 3, 1 }, result[i++]);
		assertArrayEquals(new int[] { 3 }, result[i++]);
		assertArrayEquals(new int[] { 2, 1 }, result[i++]);
		assertArrayEquals(new int[] { 2 }, result[i++]);
		assertArrayEquals(new int[] { 1 }, result[i++]);
	}

	@Test
	public void subsetTest07() {
		int[][] result = SubsetSum.search(7, 3, 2, 7, 1);
		assertEquals(8, result.length);
		int i = 0;
		assertArrayEquals(new int[] { 3, 2, 1 }, result[i++]);
		assertArrayEquals(new int[] { 3, 2 }, result[i++]);
		assertArrayEquals(new int[] { 3, 1 }, result[i++]);
		assertArrayEquals(new int[] { 3 }, result[i++]);
		assertArrayEquals(new int[] { 2, 1 }, result[i++]);
		assertArrayEquals(new int[] { 2 }, result[i++]);
		assertArrayEquals(new int[] { 7 }, result[i++]);
		assertArrayEquals(new int[] { 1 }, result[i++]);
	}

	@Test
	public void subsetTest13() {
		int[][] result = SubsetSum.search(13, 3, 2, 7, 1);
		assertEquals(15, result.length);
		int i = 0;
		assertArrayEquals(new int[] { 3, 2, 7, 1 }, result[i++]);
		assertArrayEquals(new int[] { 3, 2, 7 }, result[i++]);
		assertArrayEquals(new int[] { 3, 2, 1 }, result[i++]);
		assertArrayEquals(new int[] { 3, 2 }, result[i++]);
		assertArrayEquals(new int[] { 3, 7, 1 }, result[i++]);
		assertArrayEquals(new int[] { 3, 7 }, result[i++]);
		assertArrayEquals(new int[] { 3, 1 }, result[i++]);
		assertArrayEquals(new int[] { 3 }, result[i++]);
		assertArrayEquals(new int[] { 2, 7, 1 }, result[i++]);
		assertArrayEquals(new int[] { 2, 7 }, result[i++]);
		assertArrayEquals(new int[] { 2, 1 }, result[i++]);
		assertArrayEquals(new int[] { 2 }, result[i++]);
		assertArrayEquals(new int[] { 7, 1 }, result[i++]);
		assertArrayEquals(new int[] { 7 }, result[i++]);
		assertArrayEquals(new int[] { 1 }, result[i++]);
	}

}
