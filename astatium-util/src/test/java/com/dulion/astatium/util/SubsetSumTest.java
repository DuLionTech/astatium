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

package com.dulion.astatium.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SubsetSumTest {

  @Test
  public void subsetTest06() {
    int[][] result = SubsetSum.search(6, 3, 2, 7, 1);
    assertEquals(7, result.length);
    int index = 0;
    assertArrayEquals(new int[] {3, 2, 1}, result[index++]);
    assertArrayEquals(new int[] {3, 2}, result[index++]);
    assertArrayEquals(new int[] {3, 1}, result[index++]);
    assertArrayEquals(new int[] {3}, result[index++]);
    assertArrayEquals(new int[] {2, 1}, result[index++]);
    assertArrayEquals(new int[] {2}, result[index++]);
    assertArrayEquals(new int[] {1}, result[index++]);
  }

  @Test
  public void subsetTest07() {
    int[][] result = SubsetSum.search(7, 3, 2, 7, 1);
    assertEquals(8, result.length);
    int index = 0;
    assertArrayEquals(new int[] {3, 2, 1}, result[index++]);
    assertArrayEquals(new int[] {3, 2}, result[index++]);
    assertArrayEquals(new int[] {3, 1}, result[index++]);
    assertArrayEquals(new int[] {3}, result[index++]);
    assertArrayEquals(new int[] {2, 1}, result[index++]);
    assertArrayEquals(new int[] {2}, result[index++]);
    assertArrayEquals(new int[] {7}, result[index++]);
    assertArrayEquals(new int[] {1}, result[index++]);
  }

  @Test
  public void subsetTest13() {
    int[][] result = SubsetSum.search(13, 3, 2, 7, 1);
    assertEquals(15, result.length);
    int index = 0;
    assertArrayEquals(new int[] {3, 2, 7, 1}, result[index++]);
    assertArrayEquals(new int[] {3, 2, 7}, result[index++]);
    assertArrayEquals(new int[] {3, 2, 1}, result[index++]);
    assertArrayEquals(new int[] {3, 2}, result[index++]);
    assertArrayEquals(new int[] {3, 7, 1}, result[index++]);
    assertArrayEquals(new int[] {3, 7}, result[index++]);
    assertArrayEquals(new int[] {3, 1}, result[index++]);
    assertArrayEquals(new int[] {3}, result[index++]);
    assertArrayEquals(new int[] {2, 7, 1}, result[index++]);
    assertArrayEquals(new int[] {2, 7}, result[index++]);
    assertArrayEquals(new int[] {2, 1}, result[index++]);
    assertArrayEquals(new int[] {2}, result[index++]);
    assertArrayEquals(new int[] {7, 1}, result[index++]);
    assertArrayEquals(new int[] {7}, result[index++]);
    assertArrayEquals(new int[] {1}, result[index++]);
  }

}
