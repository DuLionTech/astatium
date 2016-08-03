package com.dulion.astatium.mesh;

import java.io.InputStream;
import java.io.Reader;

public interface Shredder
{
	DataGraph shred(InputStream in) throws Exception; 

	DataGraph shred(Reader in) throws Exception; 
}
