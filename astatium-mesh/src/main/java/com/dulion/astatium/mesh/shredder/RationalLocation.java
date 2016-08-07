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

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public final class RationalLocation implements Location {
  private static final Map<BigInteger, BigInteger> FLYWEIGHT = new HashMap<>();

  private final BigInteger numerator;

  private final BigInteger denominator;

  private transient int hashCode = 0;

  public RationalLocation(BigInteger numerator, BigInteger denomintor) {
    this.numerator = flyweight(numerator);
    this.denominator = flyweight(denomintor);
  }

  public BigInteger getNumerator() {
    return numerator;
  }

  public BigInteger getDenominator() {
    return denominator;
  }

  public Location derive(Location parent, int index) {
    BigInteger num = product(numerator, ((RationalLocation) parent).getNumerator(), index);
    BigInteger den = product(denominator, ((RationalLocation) parent).getDenominator(), index);
    return new RationalLocation(num, den);
  }

  public Location add(Location parent) {
    BigInteger num = numerator.add(((RationalLocation) parent).getNumerator());
    BigInteger den = denominator.add(((RationalLocation) parent).getDenominator());
    return new RationalLocation(num, den);
  }

  @Override
  public int compareTo(Location other) {
    // Fails LSP
    assert other instanceof RationalLocation;
    if (this == other) {
      return 0;
    }

    final BigInteger first = numerator.multiply(((RationalLocation) other).getDenominator());
    final BigInteger second = ((RationalLocation) other).getNumerator().multiply(denominator);
    return first.compareTo(second);
  }

  @Override
  public int hashCode() {
    if (hashCode == 0) {
      hashCode = 31 * numerator.hashCode() + denominator.hashCode();
    }

    return hashCode;
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

    final RationalLocation other = (RationalLocation) that;
    if (numerator != other.getNumerator()) {
      return false;
    }
    
    if (denominator != other.getDenominator()) {
      return false;
    }

    return true;
  }

  @Override
  public String toString() {
    return numerator + "/" + denominator;
  }

  private BigInteger product(BigInteger first, BigInteger second, int index) {
    return BigInteger.valueOf(index + 2).multiply(first).subtract(second);
  }

  private static BigInteger flyweight(BigInteger numerator) {
    synchronized (FLYWEIGHT) {
      BigInteger flyweight = FLYWEIGHT.get(numerator);
      if (null != flyweight) {
        return flyweight;
      }

      FLYWEIGHT.put(numerator, numerator);
      return numerator;
    }
  }
}
