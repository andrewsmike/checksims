package net.lldp.checksims.parse.ast;

import java.util.stream.Stream;

import net.lldp.checksims.parse.SubmissionPercentableCalculator;
import net.lldp.checksims.submission.Submission;

public class SubmissionParser implements SubmissionPercentableCalculator<AST>
{
    private final LanguageDependantSyntaxParser ldsp;
    
    public SubmissionParser(LanguageDependantSyntaxParser ldsp)
    {
        this.ldsp = ldsp;
    }
    
    @Override
    public AST generateFromSubmission(Submission s)
    {
        Stream<AST> asts = ldsp.sourceToDefaultcontext(s, s.getContentAsString()).stream().map(A -> A.accept(ldsp.getTreeWalker()));
        return new AST("#PROGRAM", asts);
    }

    @Override
    public Class<AST> getTypeClass()
    {
        return AST.class;
    }
    
}
