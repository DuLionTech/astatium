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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.dulion.astatium.mesh.Range;

import org.junit.Test;

import java.math.BigInteger;

public class LocatorTest {
  @Test
  public void constructorToString() {
    Range rationalRange = new RationalRange(BigInteger.ONE, BigInteger.ZERO);
    assertEquals("1/0:1/0", rationalRange.toString());
  }

  @Test
  public void topLevelChildren() {
    Range parent = new RationalRange(BigInteger.ONE, BigInteger.ZERO);
    RationalRange rationalRange = new RationalRange(BigInteger.valueOf(2), BigInteger.ONE);
    assertEquals("3/2:5/3", rationalRange.child(parent, 0).toString());
    assertEquals("5/3:7/4", rationalRange.child(parent, 1).toString());
    assertEquals("7/4:9/5", rationalRange.child(parent, 2).toString());
    assertEquals("9/5:11/6", rationalRange.child(parent, 3).toString());
    assertEquals("11/6:13/7", rationalRange.child(parent, 4).toString());
  }


  @Test
  public void containsChild() {
    RationalRange level01 = new RationalRange(BigInteger.ONE, BigInteger.ZERO);
    RationalRange level02 = new RationalRange(BigInteger.valueOf(2), BigInteger.ONE);
    Range level03 = level02.child(level01, 2);
    Range level04 = ((RationalRange) level03).child(level02, 4);

    assertTrue(level03.isDescendant(level04));
    assertFalse(level04.isDescendant(level03));
  }

  @Test
  public void containsSibling() {
    RationalRange level01 = new RationalRange(BigInteger.ONE, BigInteger.ZERO);
    RationalRange level02 = new RationalRange(BigInteger.valueOf(2), BigInteger.ONE);
    Range level03a = level02.child(level01, 1);
    Range level03b = level02.child(level01, 3);
    Range level03c = level02.child(level01, 5);
    Range level04a = ((RationalRange) level03a).child(level02, 1);
    Range level04c = ((RationalRange) level03c).child(level02, 1);

    assertFalse(level03b.isDescendant(level03c));
    assertFalse(level03b.isDescendant(level04a));
    assertFalse(level03b.isDescendant(level04c));
  }
}
