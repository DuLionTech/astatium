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

import java.io.IOException;
import java.util.Set;

import org.apache.camel.spring.GenericBeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import com.dulion.astatium.camel.bridge.Bridge;

public class BridgePackageScanner {

	private static final String CONSUMER_FACTORY = BridgeFactory.class.getName();

	private static final String CONSUMER_METHOD = "create";
	
	private final XingCamelComponentProvider provider = new XingCamelComponentProvider();
	
	private final BeanNameGenerator generator = new AnnotationBeanNameGenerator();
	
	private final BeanDefinitionRegistry registry;

	private final ClassLoader classLoader;

	private final String factoryName;
	
	public BridgePackageScanner(BeanDefinitionRegistry registry, ClassLoader classLoader) {
		this.registry = registry;
		this.classLoader = classLoader;

		try {
			MetadataReader reader = provider.getMetadataReaderFactory().getMetadataReader(CONSUMER_FACTORY);
			ScannedGenericBeanDefinition definition = new ScannedGenericBeanDefinition(reader);
			factoryName = generator.generateBeanName(definition, registry);
			registry.registerBeanDefinition(factoryName, definition);
		} catch (IOException ex) {
			throw new GenericBeansException(String.format("Unable to register factory class: %s", CONSUMER_FACTORY), ex);
		}
	}
	
	public void scan(Set<String> basePackages) throws LinkageError {
		for (String basePackage : basePackages) {
			registerProxyBeans(basePackage);
		}
	}

	private void registerProxyBeans(String basePackage) throws LinkageError {
		for (AnnotatedBeanDefinition definition : provider.findComponents(basePackage)) {
			String beanType = definition.getBeanClassName();
			String beanName = generator.generateBeanName(definition, registry);
			try {
				definition.setBeanClassName(null);
				definition.setFactoryBeanName(factoryName);
				definition.setFactoryMethodName(CONSUMER_METHOD);
				definition.getConstructorArgumentValues().addGenericArgumentValue(ClassUtils.forName(beanType, classLoader));
				registry.registerBeanDefinition(beanName, definition);
			} catch (ClassNotFoundException ex) {
				throw new GenericBeansException(String.format("Unable to register bean: %s", beanType), ex);
			}
		}
	}

	private static class XingCamelComponentProvider extends ClassPathScanningCandidateComponentProvider {
		public XingCamelComponentProvider() {
			super(false);
			addIncludeFilter(new AnnotationTypeFilter(Bridge.class));
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Iterable<AnnotatedBeanDefinition> findComponents(String searchPath) {
			return (Iterable) super.findCandidateComponents(searchPath);
		}

		@Override
		protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
			return beanDefinition.getMetadata().isInterface();
		}
	}
}
