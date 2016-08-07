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

package com.dulion.astatium.mesh.shredder;

import com.dulion.astatium.mesh.Location;
import com.dulion.astatium.mesh.Range;

import java.math.BigInteger;

public final class RationalRange implements Range {
  private final Location lower;

  private final Location upper;

  public RationalRange(Location lower, Location upper) {
    this.lower = lower;
    this.upper = upper;
  }

  public RationalRange(BigInteger numerator, BigInteger denominator) {
    lower = upper = new RationalLocation(numerator, denominator);
  }

  @Override
  public Location getUpper() {
    return upper;
  }

  @Override
  public Location getLower() {
    return lower;
  }

  @Override
  public boolean isDescendant(Range other) {
    int lo = lower.compareTo(other.getLower());
    int up = upper.compareTo(other.getUpper());
    return (lo < 0 && up > 0);
  }

  @Override
  public int compareTo(Range other) {
    if (this == other) {
      return 0;
    }
    
    return lower.compareTo(other.getLower());
  }

  @Override
  public int hashCode() {
    return lower.hashCode();
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    
    if (null == that) {
      return false;
    }
    
    if (getClass() != that.getClass()) {
      return false;
    }

    final Range other = (Range) that;
    if (lower != other.getLower()) {
      return false;
    }

    return true;
  }

  @Override
  public String toString() {
    return lower + ":" + upper;
  }

  /**
   * Derive a child rational locator based on a parent rational and a position of the child.
   * 
   * @param parent The parent (a12 and a22) of the this rational (a11 and a21).
   * @param index The 0-base index of the child.
   * @return The derived rational number identifying the child at position.
   */
  Range child(Range parent, int index) {
    Location lo = ((RationalLocation) upper).derive(parent.getUpper(), index);
    Location up = ((RationalLocation) lo).add(upper);
    return new RationalRange(lo, up);
  }
}
