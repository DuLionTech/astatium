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
package com.dulion.astatium.service.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dulion.astatium.camel.bridge.Bridge;
import com.dulion.astatium.camel.bridge.BridgeEndpoint;
import com.dulion.astatium.service.model.context.ContextRegister;

@Bridge("contexts")
@RestController
@RequestMapping("/contexts")
public interface ContextEndpoint {

	@BridgeEndpoint("all")
	@RequestMapping(method = RequestMethod.GET)
	ContextRegister index();

}
