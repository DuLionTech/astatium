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

import static java.util.Arrays.sort;
import static java.util.Collections.unmodifiableList;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;

import org.apache.commons.collections4.ListValuedMap;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dulion.astatium.mesh.Context;
import com.dulion.astatium.mesh.Location;
import com.dulion.astatium.mesh.Range;
import com.dulion.astatium.mesh.meta.ContextBuilder;
import com.dulion.astatium.mesh.meta.ContextData;
import com.dulion.astatium.mesh.meta.ContextLoader;
import com.dulion.astatium.mesh.meta.ContextType;
import com.dulion.astatium.mesh.meta.EdgeData;
import com.google.common.base.Strings;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class ContextManager
{
	private final static Logger LOG = LoggerFactory.getLogger(ContextManager.class);

	private final static boolean LOG_TREE = false;

	/**
	 * The root of all contexts.
	 */
	private final ContextBase m_root;

	/**
	 * Prefix trie map containing full and partial names mapped to lists of fully qualified names.
	 */
	private final Trie<String, List<QName>> m_nameTrie = new PatriciaTrie<>();

	/**
	 * Map of fully qualified names to lists of contexts.
	 */
	private final ListValuedMap<QName, Context> m_contextMap = MultiMapUtils.newListValuedHashMap();

	/**
	 * All attribute contexts. This enables us to quickly find attribute children of a parent context by name.
	 * 
	 * Elements and Attributes can have the same name with different structures. We don't want to mix these.
	 */
	private final Table<Integer, QName, Context> m_attributeTable = HashBasedTable.create();

	/**
	 * All element contexts. This enables us to quickly find element children of a parent context by name.
	 * 
	 * Elements and Attributes can have the same name with different structures. We don't want to mix these.
	 */
	private final Table<Integer, QName, Context> m_elementTable = HashBasedTable.create();

	private final ContextLoader m_contextLoader;

	private final ContextBuilder<Context> m_contextBuilder;

	public ContextManager(ContextLoader contextLoader, ContextBuilder<Context> contextBuilder)
	{
		m_contextLoader = contextLoader;
		m_contextBuilder = contextBuilder;
		Range range = new RationalRange(BigInteger.valueOf(2), BigInteger.ONE);

		// @formatter:off
		ContextData context = ContextData.builder()
				.contextId(0)
				.namespace("urn:com:expedia:fcts:booking")
				.name("Base")
				.type(ContextType.BASE)
				.build();
		// @formatter:on

		m_root = new ContextBase(context, range);
	}

	@PostConstruct
	public void initialize()
	{
		Map<Integer, Context> metaMap = new HashMap<>();
		metaMap.put(m_root.getContextId(), m_root);
		m_contextLoader.loader().withCallback((context, edge) -> loadChild(metaMap, context, edge)).load();

		LOG.info(String.format("Number of names: %,d", m_contextMap.keySet().size()));
		LOG.info(String.format("Number of tries: %,d", m_nameTrie.size()));
		LOG.info(String.format("Number of paths: %,d", m_contextMap.size()));
	}

	List<Context> findContexts(Context context, String name)
	{
		Range parent = context.getRange();
		return findContexts(name).stream()
			.filter(child -> parent.isDescendant(child.getRange()))
			.collect(Collectors.toList());
	}

	List<Context> findContexts(String name)
	{
		return unmodifiableList(m_contextMap.get(QName.valueOf(name)));
	}

	Context getRootContext()
	{
		return m_root;
	}

	Context getElementContext(Context parent, QName name)
	{
		return getChild(m_elementTable, ContextType.ELEMENT, parent, name);
	}

	Context getAttributeContext(Context parent, QName name)
	{
		return getChild(m_attributeTable, ContextType.ATTRIBUTE, parent, name);
	}

	private Context getChild(Table<Integer, QName, Context> table, ContextType type, Context parent, QName name)
	{
		synchronized (table)
		{
			Context child = table.get(parent.getContextId(), name);
			if (null == child)
			{
				child = m_contextBuilder.builder()
					.withParent(parent)
					.withType(type)
					.withName(name)
					.withCallback((context, edge) -> createChild(parent, context, edge))
					.build();
				table.put(parent.getContextId(), name, child);
			}

			return child;
		}
	}

	private void loadChild(Map<Integer, Context> metaMap, ContextData context, EdgeData edge)
	{
		Context parent = metaMap.get(edge.getParentId());
		if (null == parent)
		{
			throw new IllegalStateException("Missing parent: " + edge.getParentId());
		}

		Context child = createChild(parent, context, edge);
		if (null != metaMap.put(child.getContextId(), child))
		{
			throw new IllegalStateException("Duplicate child: " + context.getContextId());
		}

		switch (context.getType())
		{
		case ATTRIBUTE:
			m_attributeTable.put(parent.getContextId(), child.getName(), child);
			break;
		case ELEMENT:
			m_elementTable.put(parent.getContextId(), child.getName(), child);
			break;
		default:
			LOG.warn("Unexpected metadata type: {}", context.getType());
		}
	}

	private Context createChild(Context parent, ContextData context, EdgeData edge)
	{
		Range parentLocator = ((ContextBase) parent).getParentLocator();
		Range range = ((RationalRange) parent.getRange()).child(parentLocator, edge.getIndex());
		ContextChild child = new ContextChild(parent, context, range);

		updateNameTable(child);

		return child;
	}

	private void updateNameTable(ContextChild child)
	{
		List<Context> list = m_contextMap.get(child.getName());
		if (list.isEmpty())
		{
			// First time we've encountered this name, so add name to trie.
			updateNameTrie(child);
		}

		list.add(child);
	}

	private void updateNameTrie(ContextChild child)
	{
		String name = child.getName().getLocalPart();

		putNameTrie(name, child.getName());

		int last = name.length() - 2;
		for (int i = 1; i <= last; i++)
		{
			if (Character.isUpperCase(name.charAt(i)))
			{
				putNameTrie(name.substring(i), child.getName());
			}
		}
	}

	private void putNameTrie(String name, QName value)
	{
		String key = name.toLowerCase();
		List<QName> list = m_nameTrie.get(key);
		if (null == list)
		{
			list = new ArrayList<>();
			m_nameTrie.put(key, list);
			list.add(value);
		}
		else
		{
			int index = Collections.binarySearch(list, value, (first, second) -> {
				int result = first.getNamespaceURI().compareTo(second.getNamespaceURI());
				if (0 == result)
				{
					result = first.getLocalPart().compareTo(second.getLocalPart());
				}
				return result;
			});

			assert index < 0;

			list.add(-index - 1, value);
		}
	}

	/**
	 * Temporary method used for debugging and runtime interaction.
	 */
	public void rendezvous()
	{
		if (LOG_TREE) logTree();

		logNamesMatching("Additional");
	}

	private void logNamesMatching(String name)
	{
		List<QName> keyList = m_nameTrie.get(name.toLowerCase());
		if (null == keyList)
		{
			LOG.info("Name not found: {}", name);
		}
		else
		{
			keyList.stream().forEach(key -> {
				LOG.info("Name: {}", key);
				for (Context context : m_contextMap.get(key))
				{
					LOG.info("    Path: {}", buildPath(context));
				}
			});
		}
	}

	@SuppressWarnings("unused")
	private void logNamesLike(String name)
	{
		SortedMap<String, List<QName>> prefixMap = m_nameTrie.prefixMap(name.toLowerCase());
		prefixMap.values().stream().flatMap(nameList -> nameList.stream()).sorted((first, second) -> {
			int result = first.getLocalPart().compareTo(second.getLocalPart());
			return 0 != result ? result : first.getNamespaceURI().compareTo(second.getNamespaceURI());
		}).forEach(key -> LOG.info("Name: {}", key));
	}

	private String buildPath(Context context)
	{
		return (0 == context.getContextId()) ? ""
				: buildPath(context.getParent()) + "/" + context.getName().getLocalPart();
	}

	private void logTree()
	{
		String space = Strings.repeat("  ", 40);

		Context[] array = new Context[m_contextMap.size()];
		m_contextMap.values().toArray(array);
		sort(array, Context.byContext());

		int maxSize = 0;
		for (Context context : array)
		{
			Location location = context.getRange().getLower();
			BigInteger numerator = ((RationalLocation) location).getNumerator();

			String num = Base64.getEncoder().encodeToString(numerator.toByteArray());
			int numSize = numerator.bitLength() / 8 + 1;
			if (numSize > maxSize)
			{
				maxSize = numSize;
			}

			BigInteger denominator = ((RationalLocation) location).getDenominator();
			String den = Base64.getEncoder().encodeToString(denominator.toByteArray());
			int denSize = denominator.bitLength() / 8 + 1;
			if (denSize > maxSize)
			{
				maxSize = denSize;
			}

			int depth = context.getDepth() - 1;
			String indent = space.substring(0, depth << 1);
			LOG.info(String.format("[%15s|%15s]%s", num, den, indent + context.getName()));
		}

		LOG.info("Max byte size: {}", maxSize);
	}
}
