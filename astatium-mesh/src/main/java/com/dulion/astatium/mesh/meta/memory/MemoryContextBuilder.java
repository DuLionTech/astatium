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

package com.dulion.astatium.mesh.meta.memory;

import com.dulion.astatium.mesh.Context;
import com.dulion.astatium.mesh.meta.ChildData;
import com.dulion.astatium.mesh.meta.ContextBuilder;
import com.dulion.astatium.mesh.meta.ContextData;
import com.dulion.astatium.mesh.meta.ContextType;
import com.dulion.astatium.mesh.meta.EdgeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.xml.namespace.QName;


public class MemoryContextBuilder implements ContextBuilder {
  private static final String CHILD_ELEMENT_PREFIX = "element:";
  private static final String CHILD_ATTRIBUTE_PREFIX = "attribute:";

  private final List<Map<String, ChildData>> children = new ArrayList<>();

  public MemoryContextBuilder() {
    children.add(new HashMap<>());
  }

  @Override
  public Builder builder() {
    return new Instance();
  }

  private class Instance implements Builder {
    private Context parent;

    private ContextType type;

    private QName name;

    private BiFunction<ContextData, EdgeData, Context> callback;

    @Override
    public Builder withParent(Context parent) {
      this.parent = parent;
      return this;
    }

    @Override
    public Builder withType(ContextType type) {
      this.type = type;
      return this;
    }

    @Override
    public Builder withName(QName name) {
      this.name = name;
      return this;
    }

    @Override
    public Builder withCallback(BiFunction<ContextData, EdgeData, Context> callback) {
      this.callback = callback;
      return this;
    }

    @Override
    public Context build() {
      ChildData child = childData();

      ContextData contextData = ContextData.builder()
          .contextId(child.getContextId())
          .namespace(name.getNamespaceURI())
          .name(name.getLocalPart())
          .type(type)
          .build();

      EdgeData edgeData = EdgeData.builder()
          .parentId(parent.getContextId())
          .childId(child.getContextId())
          .index(child.getIndex())
          .build();

      return callback.apply(contextData, edgeData);
    }

    private ChildData childData() {
      Map<String, ChildData> childMap = children.get(parent.getContextId());
      String childKey =
          ((type == ContextType.ATTRIBUTE) ? CHILD_ATTRIBUTE_PREFIX : CHILD_ELEMENT_PREFIX) + name;

      ChildData child = childMap.get(childKey);
      if (null == child) {
        child = ChildData.builder().index(childMap.size()).contextId(children.size()).build();
        childMap.put(childKey, child);
        children.add(new HashMap<>());
      }

      return child;
    }
  }
}
