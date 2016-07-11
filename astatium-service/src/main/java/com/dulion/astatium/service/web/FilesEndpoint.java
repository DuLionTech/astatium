package com.dulion.astatium.service.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dulion.astatium.camel.bridge.Bridge;
import com.dulion.astatium.camel.bridge.BridgeEndpoint;
import com.dulion.astatium.service.model.Hello;

@Bridge("files")
@RestController("/files")
public interface FilesEndpoint {

	@BridgeEndpoint("all")
	@RequestMapping(method = RequestMethod.GET)
	Hello index();

}
