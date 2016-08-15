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

import java.util.Comparator;
import java.util.Iterator;

public class MinPriorityQueue<T> implements Iterable<T> {

  private static final int DEFAULT_SIZE = 8;

  // private T[] queue;

  // private final Comparator<T> comparator;

  // private int size;

  public MinPriorityQueue() {
    this(DEFAULT_SIZE, null);
  }

  public MinPriorityQueue(Comparator<T> comparator) {
    this(DEFAULT_SIZE, comparator);
  }

  public MinPriorityQueue(int initialCapacity) {
    this(initialCapacity, null);
  }

  /**
   * Initialize priority queue.
   * 
   * @param initialCapacity - initial size of priority queue.
   * @param comparator - Function to use for comparing values.
   */
  public MinPriorityQueue(int initialCapacity, Comparator<T> comparator) {
    // this.queue = (T[]) new Object[initialCapacity];
    // this.comparator = comparator;
    // this.size = 0;
  }

  public MinPriorityQueue<T> addAll(Iterable<T> iterable) {
    return this;
  }

  public Iterator<T> iterator() {
    return null;
  }

  public void ensureCapacity(int min) {

  }

}
