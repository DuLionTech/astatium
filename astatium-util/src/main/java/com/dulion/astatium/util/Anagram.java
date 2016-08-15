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
      if (length < 2) {
        return;
      }

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
