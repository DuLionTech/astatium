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
	private final Context context;
	
	private final int parentId;
	
	private final int itemId;
	
	private int size = 0;
	
	private String text;

	public DefaultDataNode(Context context, int parentId, int itemId, String text)
	{
		this.parentId = parentId;
		this.itemId = itemId;
		this.context = context;
		this.text = text;
	}
	
	@Override
	public Context getContext()
	{
		return context;
	}
	
	@Override
	public int getNodeId()
	{
		return itemId;
	}
	
	@Override
	public int getParentId()
	{
		return parentId;
	}
	
	@Override
	public int getSize()
	{
		return size;
	}

	@Override
	public String getText()
	{
		return text;
	}

	@Override
	public Range getRange()
	{
		return context.getRange();
	}
	
	@Override
	public Range getParentRange()
	{
		return context.getParent().getRange();
	}

	@Override
	public boolean isSibling(DataNode other)
	{
		return other.getParentId() == parentId;
	}
	
	public void setSize(int size)
	{
		this.size = size;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	@Override
	public String toString()
	{
		if (text.length() > 0)
		{
			return context.getName() + ": \"" + text + '"';
		}
		
		return context.getName().toString();
	}
}
