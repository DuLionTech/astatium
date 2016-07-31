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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.core.annotation.AnnotationConfigurationException;
import org.springframework.stereotype.Component;

import com.dulion.astatium.camel.bridge.Bridge;
import com.dulion.astatium.camel.bridge.BridgeEndpoint;

@Component(value=BridgeFactory.ID)
public class BridgeFactory implements BeanClassLoaderAware {

	public static final String ID = "db16eadd-8044-4418-8332-cf2b303fbc20";

	private static final Method OBJECT_EQUALS = getObjectMethod("equals", Object.class);

	private static final Method OBJECT_HASHCODE = getObjectMethod("hashCode");

	private static final Method OBJECT_TOSTRING = getObjectMethod("toString");

	private static final String DUPLICATE_MESSAGE = "Duplicate endpoint name or overloaded method name: ";

	private final Map<String, BridgeProducerHandler> reverse = new HashMap<>();

	private final Map<Method, BridgeProducerHandler> forward = new HashMap<>();

	private ClassLoader classLoader;
	
	public BridgeFactory() {
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public BridgeProducerHandler lookupHandler(String endpointKey) {
		BridgeProducerHandler handler = reverse.get(endpointKey);
		if (null == handler) {
			handler = new BridgeProducerHandler();
			reverse.put(endpointKey, handler);
		}
		
		return handler;
	}

	public <T> T create(Class<T> type) throws Exception {
		String controllerName = type.getAnnotation(Bridge.class).value();
		for (Method method : type.getMethods()) {
			BridgeEndpoint endpoint = method.getAnnotation(BridgeEndpoint.class);
			String endpointName = (null != endpoint) ? endpoint.value() : method.getName();
			
			BridgeProducerHandler handler = lookupHandler("bridge://" + controllerName + '/' + endpointName);
			handler.setMethod(method);
			
			if (null != forward.put(method, handler)) {
				throw new AnnotationConfigurationException(DUPLICATE_MESSAGE + endpointName);
			}
		}

		return type.cast(Proxy.newProxyInstance(classLoader, new Class<?>[] { type }, new Handler()));
	}

	private static Method getObjectMethod(String name, Class<?>... types) {
		try {
			return Object.class.getMethod(name, types);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private class Handler implements InvocationHandler {
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			BridgeProducerHandler handler = forward.get(method);
			if (null != handler) {
				return handler.invoke(args);
			}

			if (OBJECT_EQUALS.equals(method)) {
				return this.equals(args[0]);
			}

			if (OBJECT_HASHCODE.equals(method)) {
				return this.hashCode();
			}

			if (OBJECT_TOSTRING.equals(method)) {
				return this.toString();
			}

			throw new UnsupportedOperationException(method.toString());
		}
	}
}
