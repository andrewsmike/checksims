package net.lldp.checksims.parse.token;

import net.lldp.checksims.parse.SubmissionPercentableCalculator;
import net.lldp.checksims.parse.token.tokenizer.Tokenizer;
import net.lldp.checksims.submission.Submission;

public class SubmissionTokenizer implements SubmissionPercentableCalculator<PercentableTokenListDecorator>
{
    private final Tokenizer tokenizer;
    
    public SubmissionTokenizer(Tokenizer tokenizer)
    {
        this.tokenizer = tokenizer;
    }
    
    @Override
    public PercentableTokenListDecorator generateFromSubmission(Submission s)
    {
        return new PercentableTokenListDecorator(tokenizer.splitString(s.getContentAsString()));
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other == null)
        {
            return false;
        }
        if (other == this)
        {
            return true;
        }
        if (!(other instanceof SubmissionTokenizer))
        {
            return false;
        }
        return ((SubmissionTokenizer)other).tokenizer.equals(this.tokenizer);
    }

    @Override
    public Class<PercentableTokenListDecorator> getTypeClass()
    {
        return PercentableTokenListDecorator.class;
    }

}
