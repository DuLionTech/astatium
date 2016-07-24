/**
 * Copyright 2016 Phillip DuLion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dulion.astatium.mesh.meta.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import javax.xml.namespace.QName;

import com.dulion.astatium.mesh.Context;
import com.dulion.astatium.mesh.meta.ChildData;
import com.dulion.astatium.mesh.meta.ContextBuilder;
import com.dulion.astatium.mesh.meta.ContextData;
import com.dulion.astatium.mesh.meta.ContextType;
import com.dulion.astatium.mesh.meta.EdgeData;


public class MemoryContextBuilder<T> implements ContextBuilder<T>
{
	private static final String CHILD_ELEMENT_PREFIX = "element:";
	private static final String CHILD_ATTRIBUTE_PREFIX = "attribute:";

	private List<Map<String, ChildData>> m_children = new ArrayList<>();
	
	public MemoryContextBuilder()
	{
		m_children.add(new HashMap<>());
	}

	@Override
	public Builder<T> builder()
	{
		return new Instance();
	}

	private class Instance implements Builder<T>
	{
		private Context m_parent;

		private ContextType m_type;

		private QName m_name;

		private BiFunction<ContextData, EdgeData, T> m_callback;

		@Override
		public Builder<T> withParent(Context parent)
		{
			m_parent = parent;
			return this;
		}

		@Override
		public Builder<T> withType(ContextType type)
		{
			m_type = type;
			return this;
		}

		@Override
		public Builder<T> withName(QName name)
		{
			m_name = name;
			return this;
		}

		@Override
		public Builder<T> withCallback(BiFunction<ContextData, EdgeData, T> callback)
		{
			m_callback = callback;
			return this;
		}

		@Override
		public T build()
		{
			ChildData child = childData();

			// @formatter:off
			ContextData contextData = ContextData.builder()
					.contextId(child.getContextId())
					.namespace(m_name.getNamespaceURI())
					.name(m_name.getLocalPart())
					.type(m_type)
					.build();

			EdgeData edgeData = EdgeData.builder()
					.parentId(m_parent.getContextId())
					.childId(child.getContextId())
					.index(child.getIndex())
					.build();
			// @formatter:on

			return m_callback.apply(contextData, edgeData);
		}

		private ChildData childData()
		{
			Map<String, ChildData> childMap = m_children.get(m_parent.getContextId());
			String childKey = ((m_type == ContextType.ATTRIBUTE) ? CHILD_ATTRIBUTE_PREFIX : CHILD_ELEMENT_PREFIX) + m_name;

			ChildData child = childMap.get(childKey);
			if (null == child)
			{
				child = ChildData.builder().index(childMap.size()).contextId(m_children.size()).build();
				childMap.put(childKey, child);
				m_children.add(new HashMap<>());
			}

			return child;
		}
	}
}
