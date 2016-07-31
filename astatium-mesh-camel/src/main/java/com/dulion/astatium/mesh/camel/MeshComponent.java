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
import java.util.Set;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.UriEndpointComponent;

import com.dulion.astatium.mesh.meta.ContextBuilder;
import com.dulion.astatium.mesh.meta.ContextLoader;
import com.dulion.astatium.mesh.meta.memory.MemoryContextBuilder;
import com.dulion.astatium.mesh.meta.memory.MemoryContextLoader;
import com.dulion.astatium.mesh.shredder.ContextManager;

public class MeshComponent extends UriEndpointComponent {

	private final ContextManager manager;

	public MeshComponent() {
		super(MeshEndpoint.class);
		manager = contextManager();
	}

	public MeshComponent(CamelContext context) {
		super(context, MeshEndpoint.class);
		manager = contextManager();
	}

	public ContextManager getContextManager() {
		return manager;
	}

	@Override
	protected MeshEndpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters)
			throws Exception {
		MeshEndpoint endpoint = new MeshEndpoint(uri, this, manager);
		setProperties(endpoint, parameters);
		return endpoint;
	}

	private ContextManager contextManager() {
		Set<ContextManager> managers = getCamelContext().getRegistry().findByType(ContextManager.class);
		if (managers.size() == 1) {
			return managers.iterator().next();
		}
		if (managers.size() > 1) {
			throw new IllegalStateException("Only a single instance of ContextManager allowed.");
		}

		return new ContextManager(contextLoader(), contextBuilder());
	}

	private ContextLoader contextLoader() {
		Set<ContextLoader> loaders = getCamelContext().getRegistry().findByType(ContextLoader.class);
		if (loaders.size() == 1) {
			return loaders.iterator().next();
		}
		if (loaders.size() > 1) {
			throw new IllegalStateException("Only one instance of ContextLoader allowed.");
		}

		return new MemoryContextLoader();
	}

	private ContextBuilder contextBuilder() {
		Set<ContextBuilder> builders = getCamelContext().getRegistry().findByType(ContextBuilder.class);
		if (builders.size() == 1) {
			return builders.iterator().next();
		}
		if (builders.size() > 1) {
			throw new IllegalStateException("Only one instance of ContextBuilder allowed.");
		}

		return new MemoryContextBuilder();
	}
}
