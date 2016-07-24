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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.namespace.QName;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.dulion.astatium.mesh.Context;
import com.dulion.astatium.mesh.meta.ContextBuilder;
import com.dulion.astatium.mesh.meta.ContextLoader;
import com.dulion.astatium.mesh.meta.memory.MemoryContextBuilder;
import com.dulion.astatium.mesh.meta.memory.MemoryContextLoader;
import com.dulion.astatium.mesh.shredder.ContextManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ContextManagerTest
{
	@Configuration
	static class SpringConfiguration
	{
		@Bean
		public ContextLoader contextLoader()
		{
			return new MemoryContextLoader();
		}

		@Bean
		public ContextBuilder<Context> contextBuilder()
		{
			return new MemoryContextBuilder<>();
		}

		@Bean
		public ContextManager factory()
		{
			return new ContextManager();
		}
	}

	@Resource
	private ContextManager m_manager;

	private final List<Context> m_nodes = new ArrayList<>();

	@Test
	public void childOrdering() throws Exception
	{
		// Create
		m_nodes.add(m_manager.getRootContext());
		populate("/", 4, 3);
		m_nodes.remove(0); // Remove base element.

		System.out.println("Natural Order");
		m_nodes.stream().forEach(node -> System.out.printf("%-19s %s\n", node.getRange(), node.getName()));

		// Sort
		List<Context> copy = new ArrayList<>(m_nodes);
		copy.sort(Context.byContext());

		System.out.println("Sorted by Locator");
		copy.stream().forEach(node -> System.out.printf("%-19s %s\n", node.getRange(), node.getName()));

		assertEquals(m_nodes, copy);
	}

	private void populate(CharSequence path, int width, int depth)
	{
		if (depth < 0) return;

		Context parent = m_nodes.get(m_nodes.size() - 1);
		for (int j = 0; j < width; j++)
		{
			StringBuilder text = new StringBuilder(path);
			text.append(j);
			m_nodes.add(m_manager.getElementContext(parent, new QName(text.toString())));
			text.append('/');
			populate(text, width, depth - 1);
		}
	}
}
