package com.dulion.astatium.mesh;

import org.apache.camel.Exchange;

public interface Shredder
{
	DataGraph shred(String text) throws Exception; 

	void rendezvous(Exchange exchange);
}
