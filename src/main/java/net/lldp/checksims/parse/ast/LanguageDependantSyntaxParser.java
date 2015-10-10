package net.lldp.checksims.parse.ast;

import java.util.Set;

import net.lldp.checksims.submission.Submission;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public interface LanguageDependantSyntaxParser
{

    ParseTreeVisitor<AST> getTreeWalker();

    Set<ParserRuleContext> sourceToDefaultcontext(Submission sub, String contentAsString);

}
