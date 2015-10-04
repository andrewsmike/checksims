package net.lldp.checksims.parse.ast;

import java.lang.reflect.InvocationTargetException;

import org.antlr.v4.runtime.*;

/**
 * The DijkstraFactory is responsible for constructing all, or parts of a Dijkstra
 * compiler. It is a standard Factory class.
 * 
 * @version Jan 26, 2015
 */
public class ASTFactory
{
    /**
     * Create a Dijkstra lexer using the specified input stream containing the text
     * @param inputText the ANTLRInputStream that contains the program text
     * @return the Dijkstra lexer
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
     * @param inputText
     * @return
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
    
    public static class EOFParsingException extends RuntimeException
    {
        
    }
    
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