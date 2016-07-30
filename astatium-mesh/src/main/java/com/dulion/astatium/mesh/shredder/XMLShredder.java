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

import java.io.StringReader;
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

public class XMLShredder implements Shredder
{
	private final ContextManager m_manager;
	
	private final XMLInputFactory m_inputFactory = XMLInputFactory.newInstance();

	public XMLShredder(ContextManager manager)
	{
		m_manager = manager;
		m_inputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
		m_inputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
		m_inputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
	}

	@Override
	public DataGraph shred(String text) throws XMLStreamException
	{
		try (StringReader reader = new StringReader(text))
		{
			XMLStreamReader stream = m_inputFactory.createXMLStreamReader(reader);
			DataNode[] items = new Instance(stream).shred();
			return new DefaultDataGraph(m_manager, items, 0, items.length, text);
		}
	}

	/**
	 * Temporary method used for debugging and runtime interaction.
	 */
	@Override
	public void rendezvous(Exchange exchange)
	{
		m_manager.rendezvous();
	}

	private class Instance
	{
		private final List<DataNode> m_shreds = new ArrayList<>();

		private final XMLStreamReader m_stream;
		
		private int m_itemId = 0;

		public Instance(XMLStreamReader stream)
		{
			m_stream = stream;
		}

		public DataNode[] shred() throws XMLStreamException
		{
			startDocument();
			
			DataNode[] items = new DataNode[m_shreds.size()];
			return m_shreds.toArray(items);
		}

		private void startDocument() throws XMLStreamException
		{
			while (m_stream.hasNext())
			{
				int eventType = m_stream.next();
				switch (eventType)
				{
				case XMLEvent.START_ELEMENT:
					startElement(m_manager.getRootContext(), -1);
					break;

				case XMLEvent.END_DOCUMENT:
					return;
				}
			}
		}

		private int startElement(Context parent, int parentId) throws XMLStreamException
		{
			Context context = m_manager.getElementContext(parent, m_stream.getName());
			DefaultDataNode tag = new DefaultDataNode(context, parentId, m_itemId++, "");
			m_shreds.add(tag);
			
			int size = shredAttributes(tag); 
			size += shredElements(tag);
			tag.setSize(size);

			return size + 1;
		}

		private int shredAttributes(DataNode parent)
		{
			int count = m_stream.getAttributeCount();
			for (int i = 0; i < count; i++)
			{
				QName name = m_stream.getAttributeName(i);
				String text = m_stream.getAttributeValue(i);
				
				Context leaf = m_manager.getAttributeContext(parent.getContext(), name);
				m_shreds.add(new DefaultDataNode(leaf, parent.getNodeId(), m_itemId++, text));
			}
			
			return count;
		}

		private int shredElements(DefaultDataNode parent) throws XMLStreamException
		{
			int count = 0;
			while (m_stream.hasNext())
			{
				int eventType = m_stream.next();
				switch (eventType)
				{
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

		private void shredCharacters(DefaultDataNode parent) throws XMLStreamException
		{
			String text = m_stream.getText();
			if (StringUtils.isNotBlank(text))
			{
				parent.setText(text);
			}
		}
	}
}
