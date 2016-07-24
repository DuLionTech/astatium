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
import com.dulion.astatium.mesh.Range;
import com.dulion.astatium.mesh.meta.ContextData;

public class ContextChild extends ContextBase
{
	private final Context m_parent;

	public ContextChild(Context parent, ContextData data, Range range)
	{
		super(data, range);
		m_parent = parent;
	}

	@Override
	public Context getParent()
	{
		return m_parent;
	}

	@Override
	public Range getParentLocator()
	{
		return m_parent.getRange();
	}

	@Override
	public int getDepth()
	{
		return m_parent.getDepth() + 1;
	}
}
