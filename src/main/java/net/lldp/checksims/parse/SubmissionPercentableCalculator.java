package net.lldp.checksims.parse;

import net.lldp.checksims.ChecksimsException;
import net.lldp.checksims.parse.token.SubmissionTokenizer;
import net.lldp.checksims.parse.token.TokenType;
import net.lldp.checksims.parse.token.tokenizer.Tokenizer;
import net.lldp.checksims.submission.Submission;

public interface SubmissionPercentableCalculator<T extends Percentable>
{
    T fromSubmission(Submission s);

    public static SubmissionPercentableCalculator<?> fromString(String optionValue) throws ChecksimsException
    {
        return new SubmissionTokenizer(Tokenizer.getTokenizer(TokenType.fromString(optionValue))); // default for now?
    }
}
