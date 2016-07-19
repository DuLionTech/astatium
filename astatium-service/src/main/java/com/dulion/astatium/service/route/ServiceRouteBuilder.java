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

import java.util.List;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;

import com.dulion.astatium.service.model.context.ContextRegister;
import com.dulion.astatium.service.model.file.FileEntry;
import com.dulion.astatium.service.model.file.FileRegister;

public class ServiceRouteBuilder extends RouteBuilder {
	
	@Value("${atatium.service.files.path}")
	private String path;
	
	@Override
	public void configure() throws Exception {
		configureContext();
		configureFile();
	}

	private void configureContext() {
		ContextRegister list = new ContextRegister();
		from("bridge://contexts/all").setBody().constant(list);
	}

	private void configureFile() {
		FileRegister fileList = new FileRegister();
		List<FileEntry> list = fileList.getFileEntries();
		FileEntry file;
		
		file = new FileEntry();
		file.setPath("eo2425.txt");
		list.add(file);
		
		file = new FileEntry();
		file.setPath("eo2425ex991.txt");
		list.add(file);
		
		file = new FileEntry();
		file.setPath("edgr-2004_10k.xmlt");
		list.add(file);
		
		file = new FileEntry();
		file.setPath("edgr-20050228.xsd");
		list.add(file);
		
		file = new FileEntry();
		file.setPath("edgr-20050228_cal.xml");
		list.add(file);
		
		file = new FileEntry();
		file.setPath("edgr-20050228_lab.xml");
		list.add(file);
		
		file = new FileEntry();
		file.setPath("edgr-20050228_pre.xml");
		list.add(file);
		
		from("bridge://files/all").setBody().constant(fileList);
	}
}
