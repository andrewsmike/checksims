package net.lldp.checksims.parse.ast;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public interface LanguageDependantSyntaxParser
{

    ParseTreeVisitor<AST> getTreeWalker();

    ParserRuleContext sourceToDefaultcontext(String contentAsString);

}
