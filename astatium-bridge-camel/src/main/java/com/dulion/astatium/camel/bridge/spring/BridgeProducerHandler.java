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
package com.dulion.astatium.camel.bridge.spring;

import java.lang.reflect.Method;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.bean.BeanInvocation;
import org.apache.camel.impl.DefaultExchange;

import com.dulion.astatium.camel.bridge.component.BridgeConsumer;

public class BridgeProducerHandler {
	
	private static final String ONLY_ONE = "Cannot add a 2nd consumer to the same endpoint. Endpoint %s only allows one consumer.";
	
	private Method m_method;
	
	private volatile BridgeConsumer m_consumer;

	public BridgeProducerHandler() {
	}
	
	public Method getMethod() {
		return m_method;
	}
	
	public void setMethod(Method method) {
		m_method = method;
	}

	public BridgeConsumer getConsumer() {
		return m_consumer;
	}

	public void setConsumer(BridgeConsumer consumer) {
		if (null != consumer && null != m_consumer) {
			String message = String.format(ONLY_ONE, this);
			throw new IllegalArgumentException(message);
		}
		
		m_consumer = consumer;
	}

	public Object invoke(Object[] args) throws Exception {
		Exchange exchange = new DefaultExchange(m_consumer.getEndpoint(), ExchangePattern.InOut);
		exchange.getIn().setBody(new BeanInvocation(m_method, args));
		m_consumer.createUoW(exchange);
        m_consumer.getProcessor().process(exchange);
        
        Class<?> type = m_method.getReturnType();
        if (Void.TYPE == type) {
        	return null;
        }

        return exchange.getIn().getBody(type);
	}
}
