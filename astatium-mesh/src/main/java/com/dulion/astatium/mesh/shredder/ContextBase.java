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

import com.dulion.astatium.mesh.Context;
import com.dulion.astatium.mesh.Range;
import com.dulion.astatium.mesh.meta.ContextData;

import java.math.BigInteger;

import javax.xml.namespace.QName;

public class ContextBase implements Context {
  
  private static final Range PARENT_LOCATOR = new RationalRange(BigInteger.ONE, BigInteger.ZERO);

  private final Range range;

  private final ContextData data;

  private final QName name;

  public ContextBase(ContextData data, Range range) {
    this.range = range;
    this.data = data;
    this.name = new QName(data.getNamespace(), data.getName());
  }

  @Override
  public int getContextId() {
    return data.getContextId();
  }

  @Override
  public Context getParent() {
    return null;
  }

  public Range getParentLocator() {
    return PARENT_LOCATOR;
  }

  @Override
  public final Range getRange() {
    return range;
  }

  @Override
  public final QName getName() {
    return name;
  }

  @Override
  public int getDepth() {
    return 0;
  }
}
