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
