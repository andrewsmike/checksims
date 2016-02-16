package net.lldp.checksims.algorithm.syntaxtree.c;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import net.lldp.checksims.parse.ast.AST;
import net.lldp.checksims.parse.ast.c.CBaseVisitor;
import net.lldp.checksims.util.data.Range;

public class SuperQuickTreeWalker extends CBaseVisitor<AST> 
{
    @Override
    public AST visitChildren(RuleNode rn)
    {
        String name = rn.getClass().getSimpleName();
        int children = rn.getChildCount();
        
        List<AST> asts = new LinkedList<AST>();
        for(int i = 0; i < children; i++)
        {
            ParseTree pt = rn.getChild(i);
            
            AST t = pt.accept(this);
            if (t == null) {
                t = new AST(name);
            }
            asts.add(t);
        }
        
        Interval range = rn.getSourceInterval();
        
        return new AST(name, new Range(range.a, range.b+1), asts.stream());
    }
}
