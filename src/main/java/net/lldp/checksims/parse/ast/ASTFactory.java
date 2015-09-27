package net.lldp.checksims.parse.ast;

import java.lang.reflect.Constructor;
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
    static public Lexer makeLexer(ANTLRInputStream inputText, Class<? extends Lexer> clazz) {
        Lexer lexer;
        try
        {
            lexer = clazz.getConstructor(CharStream.class).newInstance(inputText);
            lexer.addErrorListener(
                    new BaseErrorListener() {
                        @Override
                        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                int line, int charPositionInLine, String msg,
                                RecognitionException e)
                        {
                            throw new RuntimeException(msg, e);
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
    static public <T extends Parser> T makeParser(ANTLRInputStream inputText, Class<T> pclazz, Class<? extends Lexer> lclazz) {
        
        try
        {
            final Lexer lexer = makeLexer(inputText, lclazz);
            final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            T parser = pclazz.getConstructor(TokenStream.class).newInstance(tokenStream);
            parser.addErrorListener(
                    new BaseErrorListener() {
                        @Override
                        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                int line, int charPositionInLine, String msg,
                                RecognitionException e)
                        {
                            throw new RuntimeException(msg, e);
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
}