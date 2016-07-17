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
package com.dulion.astatium.service;

import java.util.Arrays;
import java.util.List;

import org.apache.camel.spring.boot.CamelSpringBootApplicationController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.dulion.astatium.camel.bridge.BridgePackage;
import com.dulion.astatium.service.route.ServiceRouteBuilder;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

@EnableWebMvc
@Configuration
@EnableAutoConfiguration
@BridgePackage("com.dulion.astatium.service.web")
//@ImportResource("classpath:META-INF/spring/astatium-camel-beans.xml")
public class AstatiumService {

	public static void main(String[] args) {
		SpringApplication.run(AstatiumService.class, args)
				.getBean(CamelSpringBootApplicationController.class).run();
	}
	
	@Bean
	public ServiceRouteBuilder serviceRouteBuilder() {
		return new ServiceRouteBuilder();
	}

	@Bean
	public List<AbstractHttpMessageConverter<?>> marshaller() {
		return Arrays.asList(
                new StringHttpMessageConverter(),
                new Jaxb2RootElementHttpMessageConverter(),
                new MappingJackson2HttpMessageConverter(
                        Jackson2ObjectMapperBuilder.json()
                                .annotationIntrospector(
                                        AnnotationIntrospectorPair.create(
                                                new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()),
                                                new JacksonAnnotationIntrospector()))
                                .build()));
	}
}
