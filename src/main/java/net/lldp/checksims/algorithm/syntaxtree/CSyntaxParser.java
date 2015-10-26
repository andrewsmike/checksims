package net.lldp.checksims.algorithm.syntaxtree;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

import net.lldp.checksims.parse.ast.AST;
import net.lldp.checksims.parse.ast.ASTFactory;
import net.lldp.checksims.parse.ast.LanguageDependantSyntaxParser;
import net.lldp.checksims.parse.ast.c.CLexer;
import net.lldp.checksims.parse.ast.c.CParser;
import net.lldp.checksims.submission.Submission;


public class CSyntaxParser implements LanguageDependantSyntaxParser
{

    @Override
    public ParseTreeVisitor<AST> getTreeWalker()
    {
        return new BasicCTreeWalker();
    }

    @Override
    public Set<ParserRuleContext> sourceToDefaultcontext(Submission s, String contentAsString)
    {
        String in = Arrays.asList(contentAsString.split("\n"))
            .stream()
            .map(A -> A.trim())
            .filter(A -> A.length() > 0)
            .filter(A -> A.trim().codePointAt(0) != '#')
            .collect(Collectors.joining("\n"));
        
       	CParser cp = ASTFactory.makeParser(s.getName(), new ANTLRInputStream(in), CParser.class, CLexer.class);
        
        Set<ParserRuleContext> result = new HashSet<>();
        boolean endOfFile = true;
        while(endOfFile)
        {
            try
            {
                result.add(cp.translationUnit());
            }
            catch(ASTFactory.EOFParsingException epe)
            {
                endOfFile = false;
            }
            catch(ASTFactory.SyntaxErrorException see)
            {
                System.out.println("Syntax Error for assignment: " + s.getName());
                return new HashSet<>();
            }
        }
        
        return result;
    }

}