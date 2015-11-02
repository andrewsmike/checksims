package net.lldp.checksims.algorithm.syntaxtree.cpp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

import net.lldp.checksims.parse.ast.AST;
import net.lldp.checksims.parse.ast.ASTFactory;
import net.lldp.checksims.parse.ast.LanguageDependantSyntaxParser;
import net.lldp.checksims.parse.ast.cpp.CPP14Lexer;
import net.lldp.checksims.parse.ast.cpp.CPP14Parser;
import net.lldp.checksims.submission.Submission;


public class CPPSyntaxParser implements LanguageDependantSyntaxParser
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
            .map(A -> A.trim())
            .filter(A -> A.length() > 0)
            .filter(A -> A.trim().codePointAt(0) != '#')
            .collect(Collectors.joining("\n"));

       	CPP14Parser cp = ASTFactory.makeParser(s.getName(), new ANTLRInputStream(in), CPP14Parser.class, CPP14Lexer.class);

        Set<ParserRuleContext> result = new HashSet<>();
        boolean endOfFile = true;
        while(endOfFile)
        {
            try
            {
                result.add(cp.declarationseq());
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
