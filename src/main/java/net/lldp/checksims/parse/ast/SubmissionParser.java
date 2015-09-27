package net.lldp.checksims.parse.ast;

import org.antlr.v4.runtime.ParserRuleContext;

import net.lldp.checksims.parse.SubmissionPercentableCalculator;
import net.lldp.checksims.submission.Submission;

public class SubmissionParser implements SubmissionPercentableCalculator<PercentableAST>
{
    private final LanguageDependantSyntaxParser ldsp;
    
    public SubmissionParser(LanguageDependantSyntaxParser ldsp)
    {
        this.ldsp = ldsp;
    }
    
    @Override
    public PercentableAST generateFromSubmission(Submission s)
    {
        ParserRuleContext prc = ldsp.sourceToDefaultcontext(s, s.getContentAsString());
        AST past = prc.accept(ldsp.getTreeWalker());
        return new PercentableAST(past);
    }

    @Override
    public Class<PercentableAST> getTypeClass()
    {
        return PercentableAST.class;
    }
    
}
