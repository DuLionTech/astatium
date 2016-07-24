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

import java.util.function.BiConsumer;

import com.dulion.astatium.mesh.meta.ContextData;
import com.dulion.astatium.mesh.meta.ContextLoader;
import com.dulion.astatium.mesh.meta.EdgeData;

public class MemoryContextLoader implements ContextLoader
{
	@Override
	public Loader loader()
	{
		return new Instance();
	}
	
	private class Instance implements Loader
	{
		@Override
		public Loader withCallback(BiConsumer<ContextData, EdgeData> callback)
		{
			return this;
		}

		/** 
		 * @see com.expedia.fcts.booking.sync.shred.redis.Loader#load()
		 */
		@Override
		public void load()
		{
			// Do nothing
		}
	}
}
