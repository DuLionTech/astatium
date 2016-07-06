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
package com.dulion.astatium.service.route;

import org.apache.camel.builder.RouteBuilder;

import com.dulion.astatium.service.model.Hello;

public class ServiceRouteBuilder extends RouteBuilder {
	
	@Override
	public void configure() throws Exception {
		Hello hello = new Hello();
		hello.setName("Hello World!");
		from("bridge://contexts/all").setBody().constant(hello);
	}

}
