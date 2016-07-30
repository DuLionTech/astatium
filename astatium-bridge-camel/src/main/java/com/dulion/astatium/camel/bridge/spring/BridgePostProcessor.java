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

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import com.dulion.astatium.camel.bridge.BridgePackage;

@Component
public class BridgePostProcessor implements BeanDefinitionRegistryPostProcessor, BeanClassLoaderAware {

	private static final String PACKAGE = BridgePackage.class.getName();
	
	private static final Logger LOG = LoggerFactory.getLogger(BridgePostProcessor.class);

	private ClassLoader m_classLoader;
	
	public BridgePostProcessor() {
		LOG.info("Camel Proxy post-processor created");
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		m_classLoader = classLoader;
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		Set<String> basePackages = findBasePackages(registry);
		if (basePackages.size() > 0) {
			LOG.info("Registering XingCamel annotated interfaces in {}", basePackages);
			new BridgePackageScanner(registry, m_classLoader).scan(basePackages);
		}
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// pass
	}

	private Set<String> findBasePackages(BeanDefinitionRegistry registry) {
		Set<String> basePackages = new LinkedHashSet<>();
		for (String beanName : registry.getBeanDefinitionNames()) {
			BeanDefinition definition = registry.getBeanDefinition(beanName);
			if (definition instanceof AnnotatedBeanDefinition) {
				Map<String, Object> attributes = ((AnnotatedBeanDefinition) definition).getMetadata().getAnnotationAttributes(PACKAGE);
				if (null != attributes) {
					for (String value : (String[]) attributes.get("value")) {
						basePackages.add(value);
					}
				}
			}
		}
		
		return basePackages;
	}
}
