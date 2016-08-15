/**
 * Copyright 2016 Phillip DuLion
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.dulion.astatium.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Quick<T> {

  private final Comparator<? super T> comparator;

  private final T[] values;

  private final int lower;

  private final int upper;

  static final List<Integer> allSorts = new ArrayList<>();

  static final List<Integer> allSwaps = new ArrayList<>();

  private int sorts = 0;

  private int swaps = 0;

  private Quick(T[] values, Comparator<? super T> comparator) {
    this.comparator = comparator;
    this.values = Arrays.copyOf(values, values.length);
    this.upper = values.length / 2;
    this.lower = (0 == values.length % 2) ? upper - 1 : upper;
    this.sort(0, values.length - 1);
    allSorts.add(sorts);
    allSwaps.add(swaps);
  }

  public static <T> T[] sort(T[] values, Comparator<? super T> comparator) {
    return new Quick<>(values, comparator).values;
  }

  private void sort(int first, int last) {
    if (first >= last) {
      return;
    }

    if (first > upper) {
      return;
    }

    if (last < lower) {
      return;
    }

    sorts++;

    int left = first;
    int next = first + 1;
    int right = last;
    T key = values[first];
    while (next <= right) {
      int compare = comparator.compare(key, values[next]);
      if (compare > 0) {
        swap(left++, next++);
      } else if (compare < 0) {
        swap(right--, next);
      } else {
        next++;
      }
    }

    sort(first, left - 1);
    sort(right + 1, last);
  }

  private void swap(int one, int two) {
    swaps++;
    T value = values[one];
    values[one] = values[two];
    values[two] = value;
  }
}
