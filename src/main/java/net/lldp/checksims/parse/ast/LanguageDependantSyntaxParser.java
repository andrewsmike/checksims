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
package net.lldp.checksims.parse.ast;

import java.util.Set;

import net.lldp.checksims.submission.Submission;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * An interface for abstracting over syntax parsers for any implemented language.
 * @author ted
 *
 */
public interface LanguageDependantSyntaxParser
{

    /**
     * Get the ANTLR treewalker for this language
     * @return a treewalker for the language specified. This should be generated
     * by antlr
     */
    ParseTreeVisitor<AST> getTreeWalker();

    /**
     * get all top-level parser rule contexts that are relevant to AST creation and comparison
     * @param sub the submission to parse
     * @param contentAsString the content as a string
     * @return a set of ParserRulecontexts so that they may be turned into ASTs
     */
    Set<ParserRuleContext> sourceToDefaultcontext(Submission sub, String contentAsString);

}
