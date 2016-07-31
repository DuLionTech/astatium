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
package com.dulion.astatium.mesh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.dulion.astatium.mesh.meta.ContextBuilder;
import com.dulion.astatium.mesh.meta.ContextLoader;
import com.dulion.astatium.mesh.meta.memory.MemoryContextBuilder;
import com.dulion.astatium.mesh.meta.memory.MemoryContextLoader;
import com.dulion.astatium.mesh.shredder.ContextManager;
import com.dulion.astatium.mesh.shredder.XMLShredder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class BundleTest {
	
	@Configuration
	public static class ContextConfiguration {
		
		@Bean
		public Shredder shredder(ContextManager factory) {
			return new XMLShredder(factory);
		}
		
		@Bean
		public ContextManager factory(ContextLoader loader, ContextBuilder builder) {
			return new ContextManager(loader, builder);
		}
		
		@Bean
		public ContextLoader contextLoader() {
			return new MemoryContextLoader();
		}

		@Bean
		public ContextBuilder contextBuilder() {
			return new MemoryContextBuilder();
		}
	}

	@Resource
	private Shredder shredder;

	@Test
	public void findByName() throws Exception {
		Reader reader = new StringReader("<a><b><c>123</c></b></a>");
		DataGraph memo = shredder.shred(reader);
		assertTrue(memo.contains("b"));
		assertFalse(memo.contains("e"));
	}

	@Test
	public void oneLevelDescendants() throws Exception {
		Reader reader = new StringReader("<a><b><c>123</c></b><b><c>456</c></b></a>");
		DataGraph memo = shredder.shred(reader);
		List<DataNode> bees = memo.find("b");
		assertEquals(2, bees.size());

		{
			DataGraph descendants = memo.descendantsOf(bees.get(0));
			List<DataNode> cees = descendants.find("c");
			assertEquals(1, cees.size());
			assertEquals("123", cees.get(0).getText());
		}

		{
			DataGraph descendants = memo.descendantsOf(bees.get(1));
			List<DataNode> cees = descendants.find("c");
			assertEquals(1, cees.size());
			assertEquals("456", cees.get(0).getText());
		}
	}

	@Test
	public void twoLevelDescendants() throws Exception {
		// @formatter:off
		Reader reader = new StringReader(
				  "<a>"
				+ "  <b>"
				+ "    <c>"
				+ "      <d>123</d>"
				+ "      <d>abc</d>"
				+ "    </c>"
				+ "    <c>"
				+ "      <d>456</d>"
				+ "    </c>"
				+ "  </b>"
				+ "  <b>"
				+ "    <c>"
				+ "      <d>789</d>"
				+ "    </c>"
				+ "  </b>"
				+ "  <b>"
				+ "    <c>xyz</c>"
				+ "  </b>"
				+ "</a>");
		// @formatter:on

		DataGraph memo = shredder.shred(reader);
		assertEquals(12, memo.size());

		List<DataNode> bees = memo.find("b");
		assertEquals(3, bees.size());

		{
			DataGraph first = memo.descendantsOf(bees.get(0));
			assertEquals(5, first.size());

			List<DataNode> cees = first.find("c");
			assertEquals(2, cees.size());
			{
				DataGraph second = first.descendantsOf(cees.get(0));
				assertEquals(2, second.size());

				List<DataNode> dees = second.find("d");
				assertEquals(2, dees.size());
				assertEquals("123", dees.get(0).getText());
				assertEquals("abc", dees.get(1).getText());
			}

			{
				DataGraph second = first.descendantsOf(cees.get(1));
				List<DataNode> dees = second.find("d");
				assertEquals(1, dees.size());
				assertEquals("456", dees.get(0).getText());
			}
		}

		{
			DataGraph first = memo.descendantsOf(bees.get(2));
			assertEquals(1, first.size());

			List<DataNode> cees = first.find("c");
			assertEquals(1, cees.size());

			DataGraph second = first.descendantsOf(cees.get(0));
			assertEquals(0, second.size());
			assertEquals("xyz", cees.get(0).getText());
		}
	}
}
