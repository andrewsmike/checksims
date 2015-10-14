/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License (the "License").
 * You may not use this file except in compliance with the License.
 *
 * See LICENSE.txt included in this distribution for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at LICENSE.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 * Copyright (c) 2015 Ted Meyer and Michael Andrews
 */
package net.lldp.checksims.parse;

/**
 * 
 * @author ted
 * 
 * A class for simplifying and working with real numbers. 
 *
 */
public class Real implements Percentable
{
    public static final Real ZERO = new Real(0);

    public static final Real ONE = new Real(1);
    
    private final int numerator, denominator;

    /**
     * Constructor for creating a non-integral Real object
     * 
     * @param num a numerator
     * @param denom a denominator
     */
    public Real(int num, int denom)
    {
        numerator = num;
        denominator = denom;
    }

    /**
     * Constructor for creating an integral Real object
     * 
     * @param unit whole, integral number
     */
    public Real(int unit)
    {
        numerator = unit;
        denominator = 1;
    }

    /**
     * Constructor for creating a non-integral Real,
     * loses precision for the sake of not casting
     * outside the class.
     * 
     * @param n numerator
     * @param d denominator
     */
    public Real(long n, long d)
    {
        this((int)n, (int)d);
    }

    /**
     * Adds two reals in an immutable fashion
     * @param r A real to add with this
     * @return the sum of this real and the provided real
     */
    public Real add(Real r)
    {
        int newNum = numerator * r.denominator;
        newNum += r.numerator * denominator;
        return new Real(newNum, denominator * r.denominator);
    }

    /**
     * Subtracts a real from this one in an immutable fashion
     * @param r the real to subtract
     * @return this value minus the provided real
     */
    public Real subtract(Real r)
    {
        int newNum = numerator * r.denominator;
        newNum -= r.numerator * denominator;
        return new Real(newNum, denominator * r.denominator);
    }

    /**
     * The quotient of this real and the provided real, in an immutable fashioin
     * @param r a real to divide this by
     * @return the result of this real divided by the provided real
     */
    public Real divide(Real r)
    {
        return new Real(numerator * r.denominator, denominator * r.numerator);
    }

    /**
     * The product of this real and another, in an immutable fashion
     * @param r another real
     * @return this real times the other real
     */
    public Real multiply(Real r)
    {
        return new Real(numerator * r.numerator, denominator * r.denominator);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Real other = (Real) obj;
        if (numerator == 0 && other.numerator == 0)
        {
            return true;
        }
        if (denominator != other.denominator)
        {
            return false;
        }
        if (numerator != other.numerator)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        if (denominator == 1 || numerator == 0)
        {
            return numerator + "";
        }
        return numerator + "/" + denominator;
    }

    /**
     * parses a real in the form of a "X/Y"
     * @param value a string value of a real
     * @return parses a real from a string
     */
    public static Real parseReal(String value)
    {
        if (value.contains("/"))
        {
            String[] frac = value.split("/");

            return new Real(Integer.parseInt(frac[0]), Integer.parseInt(frac[1]));
        }

        return new Real(Integer.parseInt(value));
    }

    /**
     * Greater than
     * @param valu another real
     * @return whether this real is greater than the provided real
     */
    public boolean greaterThan(Real valu)
    {
        return (numerator * valu.denominator) > (valu.numerator * denominator);
    }
    
    /**
    * Greater than or equal to
    * @param valu another real
    * @return whether this real is greater than or equal tothe provided real
    */
    public boolean greaterThanEqualTo(Real valu)
    {
        return (numerator * valu.denominator) >= (valu.numerator * denominator);
    }
    
    /**
     * equal to
     * @param valu another real
     * @return whether this real is equal to the provided real
     */
    public boolean gtEQ(double a)
    {
        return asDouble() >= a;
    }

    /**
     * Greater than
     * @param valu a double
     * @return whether this real is greater than the provided double precision floating point
     */
    public boolean gt(double a)
    {
        return asDouble() > a;
    }

    @Override
    public Real getPercentageMatched()
    {
        return this;
    }

    /**
     * Get this real as a floating point double.
     * @return the numerator / denominator as a double.
     */
    public double asDouble()
    {
        return ((double)numerator) / denominator;
    }

    /**
     * no
     * @param r
     * @return
     */
    public Real scoreSummation(Real r)
    {
        return new Real(numerator+r.numerator, denominator+r.denominator);
    }
}
