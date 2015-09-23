package net.lldp.checksims.parse;

import net.lldp.checksims.ChecksimsException;
import net.lldp.checksims.parse.token.SubmissionTokenizer;
import net.lldp.checksims.parse.token.TokenType;
import net.lldp.checksims.parse.token.tokenizer.Tokenizer;
import net.lldp.checksims.submission.Submission;

public interface SubmissionPercentableCalculator<T extends Percentable>
{
    /**
     * gets a Percentable of the corresponding type from the submission
     * and creates a new one if it does not already exist
     * 
     * @param s the submission to be created from
     * @return a percentable type of the submission
     */
    default T fromSubmission(Submission s) {
        if (s.contains(getTypeClass())) {
            return s.get(getTypeClass());
        }
        T t = generateFromSubmission(s);
        s.addType(getTypeClass(), t);
        return t;
    }
    
    /**
     * Get the type of this SubmissoinPercentableCalculator, since generics are compile time
     * @return a class of type T
     */
    Class<T> getTypeClass();
    
    /**
     * generate a new T from the submission
     * 
     * @param s the submission
     * @return a T generated from the submission
     */
    T generateFromSubmission(Submission s);

    /**
     * TODO: return types other than a tokenizer
     * 
     * @param optionValue the type of tokenizer
     * @return a SPC which tokenizes based on the given input type
     * @throws ChecksimsException if there is no tokenizer
     */
    public static SubmissionPercentableCalculator<?> fromString(String optionValue) throws ChecksimsException
    {
        return new SubmissionTokenizer(Tokenizer.getTokenizer(TokenType.fromString(optionValue))); // default for now?
    }
}
