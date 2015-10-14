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

import java.lang.reflect.InvocationTargetException;

import org.antlr.v4.runtime.*;

/**
 * 
 * @author ted
 *
 * An AST factory for interacting with the often messy ANTLR auto-generated code
 */
public class ASTFactory
{
    /**
     * Create a lexer for the given input
     * @param sn the name of the submission
     * @param inputText the ASTLR stream of text
     * @param clazz the type of lexer to create
     * @return A lexer of the type provided, with added error listeners
     */
    static public Lexer makeLexer(String sn, ANTLRInputStream inputText, Class<? extends Lexer> clazz) {
        Lexer lexer;
        try
        {
            lexer = clazz.getConstructor(CharStream.class).newInstance(inputText);
            lexer.removeErrorListeners();
            lexer.addErrorListener(
                    new BaseErrorListener() {
                        @Override
                        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                int line, int charPositionInLine, String msg,
                                RecognitionException e)
                        {
                            throw new SyntaxErrorException(sn, msg);
                        }
                    }
            );
            return lexer;
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e1)
        {
            throw new RuntimeException(e1);
        }
        
    }
    
    /**
     * Creates a parser of a given type
     * @param sn the name of the submission
     * @param inputText the ANTLR stream generated from the submission
     * @param pclazz the Class type of the parser to be generated
     * @param lclazz the Lexer type of the lexer that the parser will use
     * @return A parser of the provided type, lexing with the provided lexer
     */
    static public <T extends Parser> T makeParser(String sn, ANTLRInputStream inputText, Class<T> pclazz, Class<? extends Lexer> lclazz) {
        
        try
        {
            final Lexer lexer = makeLexer(sn, inputText, lclazz);
            final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            T parser = pclazz.getConstructor(TokenStream.class).newInstance(tokenStream);
            parser.removeErrorListeners();
            parser.addErrorListener(
                    new BaseErrorListener() {
                        @Override
                        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                int line, int charPositionInLine, String msg,
                                RecognitionException e)
                        {
                            CommonToken token = (CommonToken)offendingSymbol;
                            if ("<EOF>".equals(token.getText()))
                            {
                                throw new EOFParsingException();
                            }
                            throw new SyntaxErrorException(sn, msg);
                        }
                    }
            );
            return parser;
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e1)
        {
            throw new RuntimeException(e1);
        }
        
        
    }
    
    /**
     * 
     * @author ted
     * A simple exception to be thrown when Parsing is complete
     */
    public static class EOFParsingException extends RuntimeException
    {
        
    }
    
    /**
     * 
     * @author ted
     * A special exception (rather than general RuntimeException) to be thrown if a syntax error occurs
     */
    public static class SyntaxErrorException extends RuntimeException
    {
        public SyntaxErrorException(String studentName, String message)
        {
            super(studentName + " :: " + message);
        }
        
        @Override
        public void printStackTrace()
        {
            super.printStackTrace();
        }
    }
}