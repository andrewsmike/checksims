package net.lldp.checksims.parse;

/**
 * Created by ted on 12/19/14.
 */
public class Real implements Percentable
{
    public static final Real ZERO = new Real(0);

    public static final Real ONE = new Real(1);
    
    private final int numerator, denominator;

    public Real(int num, int denom)
    {
        numerator = num;
        denominator = denom;
    }

    public Real(int unit)
    {
        numerator = unit;
        denominator = 1;
    }

    public Real(long n, long d)
    {
        this((int)n, (int)d);
    }

    public Real add(Real r)
    {
        int newNum = numerator * r.denominator;
        newNum += r.numerator * denominator;
        return new Real(newNum, denominator * r.denominator);
    }

    public Real subtract(Real r)
    {
        int newNum = numerator * r.denominator;
        newNum -= r.numerator * denominator;
        return new Real(newNum, denominator * r.denominator);
    }

    public Real divide(Real r)
    {
        return new Real(numerator * r.denominator, denominator * r.numerator);
    }

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

    public String toString()
    {
        if (denominator == 1 || numerator == 0)
        {
            return numerator + "";
        }
        return numerator + "/" + denominator;
    }

    public static Real parseReal(String value)
    {
        if (value.contains("/"))
        {
            String[] frac = value.split("/");

            return new Real(Integer.parseInt(frac[0]), Integer.parseInt(frac[1]));
        }

        return new Real(Integer.parseInt(value));
    }

    public boolean greaterThan(Real valu)
    {
        return (numerator * valu.denominator) > (valu.numerator * denominator);
    }
    
    public boolean greaterThanEqualTo(Real valu)
    {
        return (numerator * valu.denominator) >= (valu.numerator * denominator);
    }

    public boolean gtEQ(double a)
    {
        return asDouble() >= a;
    }

    public boolean gt(double a)
    {
        return asDouble() > a;
    }

    @Override
    public Real getPercentageMatched()
    {
        return this;
    }

    public double asDouble()
    {
        return ((double)numerator) / denominator;
    }

    
    
    

    public Real simpleAverage(Real r)
    {
        return new Real(numerator+r.numerator, denominator+r.denominator);
    }

    public Real squareNumerator()
    {
        return new Real(numerator*numerator, denominator);
    }

    public Real divideDenominator(int i)
    {
        if (i == 0)
        {
            return this;
        }
        return new Real(numerator, denominator/i);
    }

    public Real sqrtNumerator()
    {
        return new Real((int) Math.sqrt(numerator), denominator);
    }

    public Real divideNumerator(int size)
    {
        return new Real(numerator/size, denominator);
    }
}