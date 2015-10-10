package net.lldp.checksims.algorithm.syntaxtree;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import net.lldp.checksims.parse.ast.AST;
import net.lldp.checksims.parse.ast.java.Java8BaseVisitor;

public class SuperQuickTreeWalker extends Java8BaseVisitor<AST> 
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
                t = new AST(pt.getText());
            }
            asts.add(t);
        }
        
        return new AST(name, asts.stream());
    }
}
