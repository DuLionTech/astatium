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
package com.dulion.astatium.mesh.shredder;

import com.dulion.astatium.mesh.Context;
import com.dulion.astatium.mesh.DataNode;
import com.dulion.astatium.mesh.Range;

public class DefaultDataNode implements DataNode
{
	private final Context m_context;
	
	private final int m_parentId;
	
	private final int m_itemId;
	
	private int m_size = 0;
	
	private String m_text;

	public DefaultDataNode(Context context, int parentId, int itemId, String text)
	{
		m_parentId = parentId;
		m_itemId = itemId;
		m_context = context;
		m_text = text;
	}
	
	@Override
	public Context getContext()
	{
		return m_context;
	}
	
	@Override
	public int getNodeId()
	{
		return m_itemId;
	}
	
	@Override
	public int getParentId()
	{
		return m_parentId;
	}
	
	@Override
	public int getSize()
	{
		return m_size;
	}

	@Override
	public String getText()
	{
		return m_text;
	}

	@Override
	public Range getRange()
	{
		return m_context.getRange();
	}
	
	@Override
	public Range getParentRange()
	{
		return m_context.getParent().getRange();
	}

	@Override
	public boolean isSibling(DataNode other)
	{
		return other.getParentId() == m_parentId;
	}
	
	public void setSize(int size)
	{
		m_size = size;
	}

	public void setText(String text)
	{
		m_text = text;
	}

	@Override
	public String toString()
	{
		if (m_text.length() > 0)
		{
			return m_context.getName() + ": \"" + m_text + '"';
		}
		
		return m_context.getName().toString();
	}
}
