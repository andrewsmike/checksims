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
 * Copyright (c) 2014-2015 Nicholas DeMarinis, Matthew Heon, and Dolan Murvihill
 */

package net.lldp.checksims.algorithm.preprocessor;

import net.lldp.checksims.algorithm.AlgorithmResults;
import net.lldp.checksims.algorithm.InternalAlgorithmError;
import net.lldp.checksims.algorithm.SimilarityDetector;
import net.lldp.checksims.algorithm.linesimilarity.LineSimilarityChecker;
import net.lldp.checksims.parse.Percentable;
import net.lldp.checksims.parse.Real;
import net.lldp.checksims.parse.token.TokenType;
import net.lldp.checksims.parse.token.TokenTypeMismatchException;
import net.lldp.checksims.parse.token.tokenizer.Tokenizer;
import net.lldp.checksims.submission.ConcreteSubmission;
import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.submission.ValidityIgnoringSubmission;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Common Code Removal via Line Comparison.
 */
public class CommonCodeLineRemovalPreprocessor implements SubmissionPreprocessor {
    private final Submission common;
    private static final SimilarityDetector<?> algorithm = LineSimilarityChecker.getInstance();
    private static final Logger logs = LoggerFactory.getLogger(CommonCodeLineRemovalPreprocessor.class);

    /**
     * @return Dummy instance of CommonCodeLineRemovalPreprocessor with empty common code
     */
    public static CommonCodeLineRemovalPreprocessor getInstance() {
        return new CommonCodeLineRemovalPreprocessor(new ConcreteSubmission("Empty", ""));
    }

    /**
     * Create a Common Code Removal preprocessor using Line Compare.
     *
     * @param common Common code to remove
     */
    public CommonCodeLineRemovalPreprocessor(Submission common) {
        checkNotNull(common);

        this.common = common;
    }

    private static <T extends Percentable> AlgorithmResults getResults(Submission rf, Submission com, SimilarityDetector<T> a)
            throws TokenTypeMismatchException, InternalAlgorithmError
    {
        T rft = a.getPercentableCalculator().fromSubmission(rf);
        T comt = a.getPercentableCalculator().fromSubmission(com);
        
        return a.detectSimilarity(Pair.of(rf, com), rft, comt);
    }
    
    /**
     * Perform common code removal using Line Comparison.
     *
     * @param removeFrom Submission to remove common code from
     * @return Input submission with common code removed
     * @throws InternalAlgorithmError Thrown on error removing common code
     */
    @Override
    public Submission process(Submission removeFrom) throws InternalAlgorithmError {
        logs.debug("Performing common code removal on submission " + removeFrom.getName());

        // Use the new submissions to compute this
        AlgorithmResults results;
        Tokenizer tokenizer = Tokenizer.getTokenizer(TokenType.LINE);

        // This exception should never happen, but if it does, just rethrow as InternalAlgorithmException
        try {
            results = getResults(removeFrom, common, algorithm).inverse();
        } catch(TokenTypeMismatchException e) {
            throw new InternalAlgorithmError(e.getMessage());
        }

        // The results contains two TokenLists, representing the final state of the submissions after detection
        // All common code should be marked invalid for the input submission's final list
        Percentable listWithCommonInvalid;
        Real percentMatched;
        if(new ValidityIgnoringSubmission(results.a, tokenizer).equals(removeFrom)) {
            listWithCommonInvalid = results.getPercentableA();
            percentMatched = results.percentMatchedA();
        } else if(new ValidityIgnoringSubmission(results.b, tokenizer).equals(removeFrom)) {
            listWithCommonInvalid = results.getPercentableB();
            percentMatched = results.percentMatchedB();
        } else {
            throw new RuntimeException("Unreachable code!");
        }

        // Recreate the string body of the submission from this new list
        String newBody = listWithCommonInvalid.toString();

        DecimalFormat d = new DecimalFormat("###.00");
        logs.trace("Submission " + removeFrom.getName() + " contained " + d.format(percentMatched.multiply(new Real(100)).asDouble())
                + "% common code");
        logs.trace("Removed " + listWithCommonInvalid + " percent of submission");

        return new ConcreteSubmission(removeFrom.getName(), newBody);
    }

    /**
     * @return Name of the implementation as it will be seen in the registry
     */
    @Override
    public String getName() {
        return "commoncodeline";
    }

    @Override
    public String toString() {
        return "Common Code Line Removal preprocessor, removing common code submission " + common.getName();
    }

    @Override
    public int hashCode() {
        return getName().hashCode() ^ common.getName().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof CommonCodeLineRemovalPreprocessor)) {
            return false;
        }

        CommonCodeLineRemovalPreprocessor otherPreprocessor = (CommonCodeLineRemovalPreprocessor)other;

        return otherPreprocessor.common.equals(common);
    }
}
