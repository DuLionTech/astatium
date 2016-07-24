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
import java.util.HashMap;
import java.util.Map;

import com.dulion.astatium.mesh.Location;

public final class RationalLocation implements Location
{
	private static final Map<BigInteger, BigInteger> FLYWEIGHT = new HashMap<>();

	private final BigInteger m_numerator;

	private final BigInteger m_denominator;

	private transient int m_hashCode = 0;

	public RationalLocation(BigInteger numerator, BigInteger denomintor)
	{
		m_numerator = flyweight(numerator);
		m_denominator = flyweight(denomintor);
	}

	public BigInteger getNumerator()
	{
		return m_numerator;
	}

	public BigInteger getDenominator()
	{
		return m_denominator;
	}

	public Location derive(Location parent, int index)
	{
		BigInteger numerator = product(m_numerator, ((RationalLocation) parent).getNumerator(), index);
		BigInteger denominator = product(m_denominator, ((RationalLocation) parent).getDenominator(), index);
		return new RationalLocation(numerator, denominator);
	}

	public Location add(Location parent)
	{
		BigInteger numerator = m_numerator.add(((RationalLocation) parent).getNumerator());
		BigInteger denominator = m_denominator.add(((RationalLocation) parent).getDenominator());
		return new RationalLocation(numerator, denominator);
	}

	@Override
	public int compareTo(Location other)
	{
		// Fails LSP
		assert other instanceof RationalLocation;
		if (this == other) return 0;
		
		final BigInteger first = m_numerator.multiply(((RationalLocation) other).getDenominator());
		final BigInteger second = ((RationalLocation) other).getNumerator().multiply(m_denominator);
		return first.compareTo(second);
	}

	@Override
	public int hashCode()
	{
		if (m_hashCode == 0)
		{
			m_hashCode = 31 * m_numerator.hashCode() + m_denominator.hashCode();
		}

		return m_hashCode;
	}

	@Override
	public boolean equals(Object that)
	{
		if (this == that) return true;
		if (null == that) return false;
		if (getClass() != that.getClass()) return false;

		final RationalLocation other = (RationalLocation) that;
		if (m_numerator != other.getNumerator()) return false;
		if (m_denominator != other.getDenominator()) return false;

		return true;
	}

	@Override
	public String toString()
	{
		return m_numerator + "/" + m_denominator;
	}

	private BigInteger product(BigInteger first, BigInteger second, int index)
	{
		BigInteger b = BigInteger.valueOf(index + 2);
		return b.multiply(first).subtract(second);
	}

	private static BigInteger flyweight(BigInteger numerator)
	{
		synchronized(FLYWEIGHT)
		{
			BigInteger flyweight = FLYWEIGHT.get(numerator);
			if (null != flyweight) return flyweight;
			
			FLYWEIGHT.put(numerator, numerator);
			return numerator;
		}
	}
}
