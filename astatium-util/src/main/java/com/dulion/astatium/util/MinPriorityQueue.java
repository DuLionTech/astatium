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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class MinPriorityQueue<T> implements Iterable<T> {

  private static final int DEFAULT_SIZE = 8;

  private T[] queue;

  private final Comparator<T> comparator;

  private int size;

  public MinPriorityQueue(Comparator<T> comparator) {
    this(comparator, DEFAULT_SIZE);
  }

  @SuppressWarnings("unchecked")
  public MinPriorityQueue(Comparator<T> comparator, int initialCapacity) {
    this.queue = (T[]) new Object[initialCapacity];
    this.comparator = comparator;
    this.size = 0;
  }

  public MinPriorityQueue<T> add(T item) {
    ensureCapacity(size);

    int lowerIndex = size++;
    while (lowerIndex > 0) {
      int upperIndex = (lowerIndex - 1) >> 1;
      T upperItem = queue[upperIndex];
      if (comparator.compare(item, upperItem) < 0) {
        break;
      }
      queue[lowerIndex] = upperItem;
      lowerIndex = upperIndex;
    }
    queue[lowerIndex] = item;

    return this;
  }

  public MinPriorityQueue<T> addAll(Collection<T> collection) {
    collection.stream().forEach(this::add);
    return this;
  }

  public Iterator<T> iterator() {
    return null;
  }

  @SuppressWarnings("unchecked")
  private void ensureCapacity(int min) {
    if (min >= queue.length) {
      T[] next = (T[]) new Object[queue.length * 2];
      System.arraycopy(queue, 0, next, 0, queue.length);
      queue = next;
    }
  }

}
