package com.dulion.astatium.service.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dulion.astatium.camel.bridge.Bridge;
import com.dulion.astatium.camel.bridge.BridgeEndpoint;
import com.dulion.astatium.service.model.sec.FilingList;

@Bridge("sec")
@RestController
@RequestMapping("/sec")
public interface SecEndpoint {

	@BridgeEndpoint("filings")
	@RequestMapping(path="/filings", method = RequestMethod.GET)
	FilingList filings();

}
