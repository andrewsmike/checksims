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
package net.lldp.checksims.parse;

import net.lldp.checksims.ChecksimsException;
import net.lldp.checksims.parse.token.SubmissionTokenizer;
import net.lldp.checksims.parse.token.TokenType;
import net.lldp.checksims.parse.token.tokenizer.Tokenizer;
import net.lldp.checksims.submission.Submission;

/**
 * 
 * @author ted
 *
 * @param <T> a subtype of the Percentable interface
 */
public interface SubmissionPercentableCalculator<T extends Percentable>
{
    /**
     * gets a Percentable of the corresponding type from the submission
     * and creates a new one if it does not already exist
     * 
     * @param s the submission to be created from
     * @return a percentable type of the submission
     * @throws InvalidSubmissionException if the submission is in any way invalid (generally used for parsing)
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
     * @throws InvalidSubmissionException 
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
