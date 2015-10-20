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
package net.lldp.checksims.algorithm.syntaxtree.python;

import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import net.lldp.checksims.parse.ast.AST;
import net.lldp.checksims.parse.ast.python.Python3BaseVisitor;

/**
 * 
 * @author ted
 * 
 * A treewalker for java that doesn't mess around with every node type
 *
 */
public class SuperQuickTreeWalker extends Python3BaseVisitor<AST> 
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
        
        return new AST(name, asts.stream());
    }
}
