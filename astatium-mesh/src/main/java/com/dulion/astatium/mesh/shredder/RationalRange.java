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

import java.math.BigInteger;

import com.dulion.astatium.mesh.Location;
import com.dulion.astatium.mesh.Range;

public final class RationalRange implements Range
{
	private final Location m_lower;

	private final Location m_upper;

	public RationalRange(Location lower, Location upper)
	{
		m_lower = lower;
		m_upper = upper;
	}

	public RationalRange(BigInteger numerator, BigInteger denominator)
	{
		m_lower = m_upper = new RationalLocation(numerator, denominator);
	}

	@Override
	public Location getUpper()
	{
		return m_upper;
	}

	@Override
	public Location getLower()
	{
		return m_lower;
	}

	@Override
	public boolean isDescendant(Range other)
	{
		int lower = m_lower.compareTo(other.getLower());
		int upper = m_upper.compareTo(other.getUpper());
		return (0 > lower && 0 < upper);
	}

	@Override
	public int compareTo(Range other)
	{
		if (this == other) return 0;
		return m_lower.compareTo(other.getLower());
	}

	@Override
	public int hashCode()
	{
		return m_lower.hashCode();
	}

	@Override
	public boolean equals(Object that)
	{
		if (this == that) return true;
		if (null == that) return false;
		if (getClass() != that.getClass()) return false;

		final Range other = (Range) that;
		if (m_lower != other.getLower()) return false;

		return true;
	}

	@Override
	public String toString()
	{
		return m_lower + ":" + m_upper;
	}

	/**
	 * Derive a child rational locator based on a parent rational and a position of the child.
	 * 
	 * @param parent The parent (a12 and a22) of the this rational (a11 and a21).
	 * @param index The 0-base index of the child.
	 * @return The derived rational number identifying the child at position.
	 */
	Range child(Range parent, int index)
	{
		Location lower = ((RationalLocation) m_upper).derive(parent.getUpper(), index);
		Location upper = ((RationalLocation) lower).add(m_upper);
		return new RationalRange(lower, upper);
	}
}
