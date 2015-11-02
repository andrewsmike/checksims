package net.lldp.checksims.algorithm.syntaxtree.c;

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
        return new SuperQuickTreeWalker();
    }
    
    public static boolean macroLine(String s) {
        s = s.trim();
        return s.length() > 0 && s.codePointAt(0) == '#';
    }

    @Override
    public Set<ParserRuleContext> sourceToDefaultcontext(Submission s, String contentAsString)
    {
        String in = Arrays.asList(contentAsString.split("\n"))
            .stream()
            .filter(A -> !macroLine(A))
            .collect(Collectors.joining("\n"));
        in = in.replace('\r', ' ');
        
       	CParser cp = ASTFactory.makeParser(s.getName(), new ANTLRInputStream(in), CParser.class, CLexer.class);
        
        Set<ParserRuleContext> result = new HashSet<>();
        try
        {
            result.add(cp.translationUnit());
        }
        catch(ASTFactory.SyntaxErrorException see)
        {
            System.out.println("Syntax Error for assignment: " + s.getName());
            s.setFlag("invalid");
        }
        
        return result;
    }
}