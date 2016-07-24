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
package com.dulion.astatium.mesh.meta;

import java.util.function.BiFunction;

import javax.xml.namespace.QName;

import com.dulion.astatium.mesh.Context;

public interface ContextBuilder<T>
{
	Builder<T> builder();

	public interface Builder<T>
	{
		Builder<T> withParent(Context parent);

		Builder<T> withType(ContextType type);

		Builder<T> withName(QName name);

		Builder<T> withCallback(BiFunction<ContextData, EdgeData, T> callback);

		T build();
	}
}