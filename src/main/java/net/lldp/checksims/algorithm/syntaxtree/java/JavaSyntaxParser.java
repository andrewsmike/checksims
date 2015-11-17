/*
s * CDDL HEADER START
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
package net.lldp.checksims.algorithm.syntaxtree.java;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

import net.lldp.checksims.parse.Percentable;
import net.lldp.checksims.parse.ast.AST;
import net.lldp.checksims.parse.ast.ASTFactory;
import net.lldp.checksims.parse.ast.LanguageDependantSyntaxParser;
import net.lldp.checksims.parse.ast.java.Java8Lexer;
import net.lldp.checksims.parse.ast.java.Java8Parser;
import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.util.data.Real;

/**
 * 
 * @author ted
 * A language dependant syntax parser for generating ASTs from java source
 */
public class JavaSyntaxParser implements LanguageDependantSyntaxParser
{

    @Override
    public ParseTreeVisitor<AST> getTreeWalker()
    {
        return new SuperQuickTreeWalker();
    }

    @Override
    public Set<ParserRuleContext> sourceToDefaultcontext(Submission s, String contentAsString)
    {
        String in = Arrays.asList(contentAsString.split("\n"))
            .stream()
            .filter(A -> !(A.contains("import")||A.contains("package")))
            .collect(Collectors.joining("\n"));
        
        Java8Parser j8p = ASTFactory.makeParser(s.getName(), new ANTLRInputStream(in), Java8Parser.class, Java8Lexer.class);
        
        Set<ParserRuleContext> result = new HashSet<>();
        boolean endOfFile = true;
        while(endOfFile)
        {
            try
            {
                result.add(j8p.typeDeclaration()); // cant use compilationUnit here, since code is mashed together
            }
            catch(ASTFactory.EOFParsingException epe)
            {
                endOfFile = false;
            }
            catch(ASTFactory.SyntaxErrorException see)
            {
                s.setFlag("invalid");
                System.out.println("Syntax Error for assignment: " + s.getName());
                return new HashSet<>();
            }
        }
        
        return result;
    }

}

/**
 * 
 * @author ted
 * sometimes students submit bad code, what else can I say?
 */
class InvalidSubmission implements Percentable
{
    @Override
    public Real getPercentageMatched()
    {
        return new Real(-1);
    }
}
