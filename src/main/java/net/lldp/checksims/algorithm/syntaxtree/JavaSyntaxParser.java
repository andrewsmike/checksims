package net.lldp.checksims.algorithm.syntaxtree;

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
        Java8Parser j8p = ASTFactory.makeParser(new ANTLRInputStream(contentAsString), Java8Parser.class, Java8Lexer.class);
        return j8p.classDeclaration();
    }

}
