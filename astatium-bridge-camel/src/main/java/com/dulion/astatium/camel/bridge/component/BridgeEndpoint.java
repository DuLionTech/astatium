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

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.util.ObjectHelper;

import com.dulion.astatium.camel.bridge.spring.BridgeFactory;
import com.dulion.astatium.camel.bridge.spring.BridgeProducerHandler;

@UriEndpoint(scheme = "proxy", title = "AtProxy", syntax = "proxy://name", consumerClass = BridgeConsumer.class, label = "Astatium Proxy")
public class BridgeEndpoint extends DefaultEndpoint {

	private final BridgeProducerHandler m_handler;

	private long m_timeout;

	public BridgeEndpoint(String uri, Component component) {
		super(uri, component);

		BridgeFactory factory = getCamelContext().getRegistry().lookupByNameAndType(BridgeFactory.ID, BridgeFactory.class);
		if (null == factory) {
			throw new IllegalStateException("BridgeFactory was not found in registry.");
		}

		m_handler = factory.lookupHandler(getEndpointKey());
		if (null == m_handler) {
			throw new IllegalArgumentException("No ProxyMethodHandler found matching: " + getEndpointKey());
		}
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		Consumer consumer = new BridgeConsumer(this, processor);
		configureConsumer(consumer);
		return consumer;
	}

	@Override
	public Producer createProducer() throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public BridgeConsumer getConsumer() {
		return m_handler.getConsumer();
	}

	public void setConsumer(BridgeConsumer consumer) {
		m_handler.setConsumer(consumer);
	}

	public long getTimeout() {
		return m_timeout;
	}

	public void setTimeout(long timeout) {
		m_timeout = timeout;
	}

	protected String getKey() {
		String uri = getEndpointUri();
		if (uri.indexOf('?') != -1) {
			return ObjectHelper.before(uri, "?");
		} else {
			return uri;
		}
	}
}
