package net.lldp.checksims.algorithm.syntaxtree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

import net.lldp.checksims.parse.ast.AST;
import net.lldp.checksims.parse.ast.ASTFactory;
import net.lldp.checksims.parse.ast.LanguageDependantSyntaxParser;
import net.lldp.checksims.parse.ast.java.Java8Lexer;
import net.lldp.checksims.parse.ast.java.Java8Parser;


public class JavaSyntaxParser implements LanguageDependantSyntaxParser
{

    @Override
    public ParseTreeVisitor<AST> getTreeWalker()
    {
        return new FullyImplementedTreeWalker();
    }

    @Override
    public ParserRuleContext sourceToDefaultcontext(String contentAsString)
    {
        String in = Arrays.asList(contentAsString.split("\n"))
            .stream()
            .filter(A -> !(A.contains("import")||A.contains("package")))
            .collect(Collectors.joining("\n"));
        
        Java8Parser j8p = ASTFactory.makeParser(new ANTLRInputStream(in), Java8Parser.class, Java8Lexer.class);
        return j8p.typeDeclaration();
    }

}
