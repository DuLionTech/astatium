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
	
	private Method method;
	
	private volatile BridgeConsumer consumer;

	public BridgeProducerHandler() {
	}
	
	public Method getMethod() {
		return method;
	}
	
	public void setMethod(Method method) {
		this.method = method;
	}

	public BridgeConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(BridgeConsumer consumer) {
		if (null != consumer && null != this.consumer) {
			String message = String.format(ONLY_ONE, this);
			throw new IllegalArgumentException(message);
		}
		
		this.consumer = consumer;
	}

	public Object invoke(Object[] args) throws Exception {
		Exchange exchange = new DefaultExchange(consumer.getEndpoint(), ExchangePattern.InOut);
		exchange.getIn().setBody(new BeanInvocation(method, args));
		consumer.createUoW(exchange);
        consumer.getProcessor().process(exchange);
        
        Class<?> type = method.getReturnType();
        if (Void.TYPE == type) {
        	return null;
        }

        return exchange.getIn().getBody(type);
	}
}
