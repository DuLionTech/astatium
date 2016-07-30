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
package com.dulion.astatium.mesh.camel;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.UriEndpointComponent;

import com.dulion.astatium.mesh.Context;
import com.dulion.astatium.mesh.meta.ContextBuilder;
import com.dulion.astatium.mesh.meta.ContextLoader;
import com.dulion.astatium.mesh.meta.memory.MemoryContextBuilder;
import com.dulion.astatium.mesh.meta.memory.MemoryContextLoader;
import com.dulion.astatium.mesh.shredder.ContextManager;

public class MeshComponent extends UriEndpointComponent {

	private final ContextManager contextManager;

	public MeshComponent() {
		super(MeshEndpoint.class);
		contextManager = contextManager(contextLoader(), contextBuilder());
	}
	
	public MeshComponent(CamelContext context) {
		super(context, MeshEndpoint.class);
		contextManager = contextManager(contextLoader(), contextBuilder());
	}
	
	public ContextManager getContextManager() {
		return contextManager;
	}

	@Override
	protected MeshEndpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
		MeshEndpoint endpoint = new MeshEndpoint(uri, this);
		setProperties(endpoint, parameters);
		return endpoint;
	}

	private ContextManager contextManager(ContextLoader loader, ContextBuilder<Context> builder) {
		return new ContextManager(loader, builder);
	}
	
	private ContextLoader contextLoader() {
		// TODO: getCamelContext().getRegistry().lookupByNameAndType(...)
		return new MemoryContextLoader();
	}

	private ContextBuilder<Context> contextBuilder() {
		// TODO: getCamelContext().getRegistry().lookupByNameAndType(...)
		return new MemoryContextBuilder<>();
	}
}
