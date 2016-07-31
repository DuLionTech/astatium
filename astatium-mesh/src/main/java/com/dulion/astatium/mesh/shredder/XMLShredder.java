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
package com.dulion.astatium.mesh.shredder;

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;

import com.dulion.astatium.mesh.Context;
import com.dulion.astatium.mesh.DataGraph;
import com.dulion.astatium.mesh.DataNode;
import com.dulion.astatium.mesh.Shredder;

public class XMLShredder implements Shredder {
	private final ContextManager manager;

	private final XMLInputFactory inputFactory = XMLInputFactory.newInstance();

	public XMLShredder(ContextManager manager) {
		this.manager = manager;
		inputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
		inputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
		inputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
	}

	@Override
	public DataGraph shred(InputStream in) throws XMLStreamException {
		return shred(inputFactory.createXMLStreamReader(in));
	}

	@Override
	public DataGraph shred(Reader in) throws XMLStreamException {
		return shred(inputFactory.createXMLStreamReader(in));
	}
	
	private DataGraph shred(XMLStreamReader stream) throws XMLStreamException {
		DataNode[] items = new Instance(stream).shred();
		return new DefaultDataGraph(manager, items, 0, items.length);
	}

	/**
	 * Temporary method used for debugging and runtime interaction.
	 */
	@Override
	public void rendezvous(Exchange exchange) {
		manager.rendezvous();
	}

	private class Instance {
		private final List<DataNode> shreds = new ArrayList<>();

		private final XMLStreamReader stream;

		private int itemId = 0;

		public Instance(XMLStreamReader stream) {
			this.stream = stream;
		}

		public DataNode[] shred() throws XMLStreamException {
			startDocument();

			DataNode[] items = new DataNode[shreds.size()];
			return shreds.toArray(items);
		}

		private void startDocument() throws XMLStreamException {
			while (stream.hasNext()) {
				int eventType = stream.next();
				switch (eventType) {
				case XMLEvent.START_ELEMENT:
					startElement(manager.getRootContext(), -1);
					break;

				case XMLEvent.END_DOCUMENT:
					return;
				}
			}
		}

		private int startElement(Context parent, int parentId) throws XMLStreamException {
			Context context = manager.getElementContext(parent, stream.getName());
			DefaultDataNode tag = new DefaultDataNode(context, parentId, itemId++, "");
			shreds.add(tag);

			int size = shredAttributes(tag);
			size += shredElements(tag);
			tag.setSize(size);

			return size + 1;
		}

		private int shredAttributes(DataNode parent) {
			int count = stream.getAttributeCount();
			for (int i = 0; i < count; i++) {
				QName name = stream.getAttributeName(i);
				String text = stream.getAttributeValue(i);

				Context leaf = manager.getAttributeContext(parent.getContext(), name);
				shreds.add(new DefaultDataNode(leaf, parent.getNodeId(), itemId++, text));
			}

			return count;
		}

		private int shredElements(DefaultDataNode parent) throws XMLStreamException {
			int count = 0;
			while (stream.hasNext()) {
				int eventType = stream.next();
				switch (eventType) {
				case XMLEvent.START_ELEMENT:
					count += startElement(parent.getContext(), parent.getNodeId());
					break;

				case XMLEvent.CHARACTERS:
					shredCharacters(parent);
					break;

				case XMLEvent.END_ELEMENT:
					return count;
				}
			}
			return count;
		}

		private void shredCharacters(DefaultDataNode parent) throws XMLStreamException {
			String text = stream.getText();
			if (StringUtils.isNotBlank(text)) {
				parent.setText(text);
			}
		}
	}
}
