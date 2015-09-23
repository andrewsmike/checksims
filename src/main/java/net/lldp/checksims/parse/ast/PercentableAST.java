package net.lldp.checksims.parse.ast;

import net.lldp.checksims.parse.Percentable;
import net.lldp.checksims.parse.Real;

public class PercentableAST implements Percentable
{
    private final AST original;
    
    public PercentableAST(AST past)
    {
        original = past;
    }

    @Override
    public Real getPercentageMatched()
    {
        throw new RuntimeException("can't evaluate getPercentageMatched()");
    }

    public Real getPercent(PercentableAST comt)
    {
        return original.compareToAST(comt.original);
    }
    
}
