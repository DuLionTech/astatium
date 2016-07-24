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
package com.dulion.astatium.mesh;

import java.util.Comparator;

public interface DataNode
{
	int getParentId();

	int getNodeId();
	
	int getSize();
	
	String getText();

	Context getContext();
	
	Range getRange();

	Range getParentRange();

	boolean isSibling(DataNode other);

	public static Comparator<? super DataNode> byContext()
	{
		return (left, right) -> left.getRange().compareTo(right.getRange());
	}

	public static Comparator<? super DataNode> byNodeId()
	{
		return (left, right) -> Integer.compare(left.getNodeId(), right.getNodeId());
	}
}
