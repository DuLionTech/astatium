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

import static java.lang.System.arraycopy;
import static java.util.Arrays.sort;

import java.util.ArrayList;
import java.util.List;

import com.dulion.astatium.mesh.Context;
import com.dulion.astatium.mesh.DataGraph;
import com.dulion.astatium.mesh.DataNode;
import com.dulion.astatium.mesh.Range;

public class DefaultDataGraph implements DataGraph
{
	private final ContextManager m_manager;
	
	private final int m_memoSize;
	
	private final DataNode[] m_shredsById;

	private final DataNode[] m_shredsByLocation;

	DefaultDataGraph(ContextManager manager, DataNode[] shreds, int start, int size)
	{
		m_manager = manager;
		m_memoSize = size;
		m_shredsById = shreds;
		
		m_shredsByLocation = new DataNode[size];
		arraycopy(shreds, start, m_shredsByLocation, 0, size);
		sort(m_shredsByLocation, DataNode.byContext());
	}

	@Override
	public boolean contains(String name)
	{
		for (Context context : m_manager.findContexts(name))
		{
			if (0 <= findTag(context.getRange())) return true;
		}

		return false;
	}

	@Override
	public List<DataNode> find(String name)
	{
		List<DataNode> list = new ArrayList<DataNode>();
		for (Context match : m_manager.findContexts(name))
		{
			collect(list, match.getRange());
		}

		return list;
	}

	@Override
	public List<DataNode> find(Context context, String name)
	{
		List<DataNode> list = new ArrayList<DataNode>();
		for (Context match : m_manager.findContexts(context, name))
		{
			collect(list, match.getRange());
		}

		return list;
	}

	@Override
	public DataGraph descendantsOf(DataNode shred)
	{
		int start = shred.getNodeId() + 1;
		return new DefaultDataGraph(m_manager, m_shredsById, start, shred.getSize());
	}

	@Override
	public int size()
	{
		return m_memoSize;
	}

	private void collect(List<DataNode> list, Range range)
	{
		int index = findTag(range);
		if (0 <= index)
		{
			// Tag may not be unique, so findTag may not be pointing to the first occurrence.
			while (0 < index && 0 == m_shredsByLocation[index - 1].getRange().compareTo(range))
			{
				index--;
			}

			final int size = m_shredsByLocation.length;
			while (size > index && 0 == m_shredsByLocation[index].getRange().compareTo(range))
			{
				list.add(m_shredsByLocation[index++]);
			}
		}
	}

	private int findTag(Range range)
	{
		int low = 0;
		int high = m_shredsByLocation.length - 1;
		while (low <= high)
		{
			int mid = (low + high) >>> 1;
			int compare = m_shredsByLocation[mid].getRange().compareTo(range);
			if (compare < 0)
			{
				low = mid + 1;
			}
			else if (compare > 0)
			{
				high = mid - 1;
			}
			else
			{
				return mid;
			}
		}

		return -1;
	}
}
