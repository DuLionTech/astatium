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
package com.dulion.astatium.camel.bridge.component;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.UriEndpointComponent;
import org.apache.camel.spi.Metadata;

public class BridgeComponent extends UriEndpointComponent {

	@Metadata(defaultValue = "30000")
	private long m_timeout = 30000L;

	public BridgeComponent() {
		super(BridgeEndpoint.class);
	}

	public BridgeComponent(CamelContext context) {
		super(context, BridgeEndpoint.class);
	}

	@Override
	protected BridgeEndpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
		BridgeEndpoint endpoint = new BridgeEndpoint(uri, this);
		endpoint.setTimeout(m_timeout);
		setProperties(endpoint, parameters);
		return endpoint;
	}
	
	public long getTimeout() {
		return m_timeout;
	}
	
	public void setTimeout(long timeout) {
		m_timeout = timeout;
	}
}
