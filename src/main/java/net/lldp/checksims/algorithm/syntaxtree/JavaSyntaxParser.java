package net.lldp.checksims.algorithm.syntaxtree;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

import net.lldp.checksims.parse.Percentable;
import net.lldp.checksims.parse.Real;
import net.lldp.checksims.parse.ast.AST;
import net.lldp.checksims.parse.ast.ASTFactory;
import net.lldp.checksims.parse.ast.LanguageDependantSyntaxParser;
import net.lldp.checksims.parse.ast.java.Java8Lexer;
import net.lldp.checksims.parse.ast.java.Java8Parser;
import net.lldp.checksims.submission.InvalidSubmissionException;
import net.lldp.checksims.submission.Submission;


public class JavaSyntaxParser implements LanguageDependantSyntaxParser
{

    @Override
    public ParseTreeVisitor<AST> getTreeWalker()
    {
        return new FullyImplementedTreeWalker();
    }

    @Override
    public Set<ParserRuleContext> sourceToDefaultcontext(Submission s, String contentAsString) throws InvalidSubmissionException
    {
        String in = Arrays.asList(contentAsString.split("\n"))
            .stream()
            .filter(A -> !(A.contains("import")||A.contains("package")))
            .collect(Collectors.joining("\n"));
        
        Java8Parser j8p = ASTFactory.makeParser(s.getName(), new ANTLRInputStream(in), Java8Parser.class, Java8Lexer.class);
        
        Set<ParserRuleContext> result = new HashSet<>();
        
        try
        {
            while(true)
            {
                result.add(j8p.typeDeclaration());
            }
        }
        catch(ASTFactory.EOFParsingException epe)
        {
            
        }
        catch(ASTFactory.SyntaxErrorException see)
        {
            s.addType(InvalidSubmission.class, new InvalidSubmission());
            throw new InvalidSubmissionException(see);
        }
        
        return result;
    }

}

class InvalidSubmission implements Percentable
{
    @Override
    public Real getPercentageMatched()
    {
        return new Real(-1);
    }
}
