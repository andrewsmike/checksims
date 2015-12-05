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

import java.util.stream.Stream;

import net.lldp.checksims.parse.SubmissionPercentableCalculator;
import net.lldp.checksims.parse.ast.ASTFactory.EOFParsingException;
import net.lldp.checksims.submission.Submission;

/**
 * 
 * @author ted
 *
 *
 * The submission parser uses a language dependant syntax parser to generate
 * and combine many ASTs present in submissions, and combine them into a single
 * AST for comparison.
 */
public class SubmissionParser implements SubmissionPercentableCalculator<AST>
{
    private final LanguageDependantSyntaxParser ldsp;
    
    /**
     * Default Submission parser, requires a languageDependantSyntaxParser
     * @param ldsp the language dependant syntax parser to use
     */
    public SubmissionParser(LanguageDependantSyntaxParser ldsp)
    {
        this.ldsp = ldsp;
    }
    
    @Override
    public AST generateFromSubmission(Submission s)
    {
        try
        {
            Stream<AST> asts = ldsp.sourceToDefaultcontext(s, s.getContentAsString()).stream().map(A -> A.accept(ldsp.getTreeWalker()));
            return new AST("#PROGRAM", asts).cacheFingerprinting();
        }
        catch(EOFParsingException eof)
        {
            return new AST("#INVALID AST").cacheFingerprinting();
        }
        
    }

    @Override
    public Class<AST> getTypeClass()
    {
        return AST.class;
    }
    
}
