package net.lldp.checksims.parse.token;

import net.lldp.checksims.parse.Percentable;
import net.lldp.checksims.util.data.Real;

public class PercentableTokenListDecorator implements Percentable
{
    private final TokenList data;
    
    public PercentableTokenListDecorator(TokenList data)
    {
        this.data = data;
    }
    
    @Override
    public Real getPercentageMatched()
    {
        long validTokenCount = data.stream().filter((token) -> !token.isValid()).count();
        if (data.size() == 0)
        {
            return Real.ZERO;
        }
        return new Real(validTokenCount, data.size());
    }

    public int size()
    {
        return data.size();
    }
    
    public TokenList getDataCopy()
    {
        return data;
    }

    @Override
    public String toString()
    {
        return data.join(true);
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other == null)
        {
            return false;
        }
        if (other == this)
        {
            return true;
        }
        if (!(other instanceof PercentableTokenListDecorator))
        {
            return false;
        }
        return ((PercentableTokenListDecorator)other).data.equals(this.data);
    }

    public TokenList getImmutableDataCopy()
    {
        return TokenList.immutableCopy(data);
    }
}
