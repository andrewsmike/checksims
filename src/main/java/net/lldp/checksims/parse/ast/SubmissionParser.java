package net.lldp.checksims.parse.ast;

import java.util.stream.Stream;

import net.lldp.checksims.parse.SubmissionPercentableCalculator;
import net.lldp.checksims.submission.InvalidSubmissionException;
import net.lldp.checksims.submission.Submission;

public class SubmissionParser implements SubmissionPercentableCalculator<PercentableAST>
{
    private final LanguageDependantSyntaxParser ldsp;
    
    public SubmissionParser(LanguageDependantSyntaxParser ldsp)
    {
        this.ldsp = ldsp;
    }
    
    @Override
    public PercentableAST generateFromSubmission(Submission s) throws InvalidSubmissionException
    {
        Stream<AST> asts = ldsp.sourceToDefaultcontext(s, s.getContentAsString()).stream().map(A -> A.accept(ldsp.getTreeWalker()));
        AST past = new AST.UnorderedAST(asts);
        return new PercentableAST(past);
    }

    @Override
    public Class<PercentableAST> getTypeClass()
    {
        return PercentableAST.class;
    }
    
}
