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

package net.lldp.checksims.algorithm.linesimilarity;

import net.lldp.checksims.ChecksimsException;
import net.lldp.checksims.algorithm.AlgorithmResults;
import net.lldp.checksims.algorithm.InternalAlgorithmError;
import net.lldp.checksims.algorithm.SimilarityDetector;
import net.lldp.checksims.parse.token.PercentableTokenListDecorator;
import net.lldp.checksims.parse.token.TokenList;
import net.lldp.checksims.parse.token.TokenTypeMismatchException;
import net.lldp.checksims.submission.ConcreteSubmission;
import net.lldp.checksims.submission.Submission;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import static net.lldp.checksims.testutil.AlgorithmUtils.checkResults;
import static net.lldp.checksims.testutil.AlgorithmUtils.checkResultsIdenticalSubmissions;
import static net.lldp.checksims.testutil.AlgorithmUtils.checkResultsNoMatch;
import static net.lldp.checksims.testutil.SubmissionUtils.submissionFromString;

/**
 * Tests for the Line Comparison algorithm
 */
public class LineSimilarityCheckerTest {
    private Submission empty;
    private Submission abc;
    private Submission aabc;
    private Submission abcde;
    private Submission def;
    private SimilarityDetector<PercentableTokenListDecorator> lineCompare;

    @Before
    public void setUp() throws Exception {
        empty = submissionFromString("Empty", "");
        abc = submissionFromString("ABC", "A\nB\nC\n");
        aabc = submissionFromString("AABC", "A\nA\nB\nC\n");
        abcde = submissionFromString("ABCDE", "A\nB\nC\nD\nE\n");
        def = submissionFromString("DEF", "D\nE\nF\n");

        lineCompare = LineSimilarityChecker.getInstance(); // TODO: change
    }
    
    public AlgorithmResults cmp(Submission a, Submission b) throws TokenTypeMismatchException, InternalAlgorithmError
    {
        return lineCompare.detectSimilarity(Pair.of(a, b),
                lineCompare.getPercentableCalculator().fromSubmission(a),
                lineCompare.getPercentableCalculator().fromSubmission(b));
    }
    
    @Test
    public void TestEmptySubmissionIsZeroPercentSimilar() throws ChecksimsException {
        AlgorithmResults results = cmp(empty, empty);

        checkResultsIdenticalSubmissions(results);
    }

    @Test
    public void TestEmptySubmissionAndNonemptySubmission() throws ChecksimsException {
        AlgorithmResults results = cmp(empty, abc);

        checkResultsNoMatch(results, empty, abc);
    }

    @Test
    public void TestIdenticalSubmissions() throws Exception {
        AlgorithmResults results = cmp(abc, abc);

        checkResultsIdenticalSubmissions(results);
    }

    @Test
    public void TestSubmissionStrictSubset() throws ChecksimsException {
        AlgorithmResults results = cmp(abc, abcde);

        TokenList expectedAbc = TokenList.cloneTokenList(lineCompare.getPercentableCalculator().fromSubmission(abc).getDataCopy());
        expectedAbc.stream().forEach((token) -> token.setValid(false));
        TokenList expectedAbcde = TokenList.cloneTokenList(lineCompare.getPercentableCalculator().fromSubmission(abcde).getDataCopy());
        for(int i = 0; i <= 2; i++) {
            expectedAbcde.get(i).setValid(false);
        }

        checkResults(results, abc, abcde,
                new PercentableTokenListDecorator(expectedAbc),
                new PercentableTokenListDecorator(expectedAbcde));
    }

    @Test
    public void TestSubmissionsNoOverlap() throws ChecksimsException {
        AlgorithmResults results = cmp(abc, def);

        checkResultsNoMatch(results, abc, def);
    }

    @Test
    public void TestSubmissionsSomeOverlap() throws ChecksimsException {
        AlgorithmResults results = cmp(abcde, def);

        TokenList expectedAbcde = TokenList.cloneTokenList(lineCompare.getPercentableCalculator().fromSubmission(abcde).getDataCopy());
        expectedAbcde.get(3).setValid(false);
        expectedAbcde.get(4).setValid(false);

        TokenList expectedDef = TokenList.cloneTokenList(lineCompare.getPercentableCalculator().fromSubmission(def).getDataCopy());
        expectedDef.get(0).setValid(false);
        expectedDef.get(1).setValid(false);

        checkResults(results, abcde, def,
                new PercentableTokenListDecorator(expectedAbcde),
                new PercentableTokenListDecorator(expectedDef));
    }

    @Test
    public void TestSubmissionsDuplicatedToken() throws ChecksimsException {
        AlgorithmResults results = cmp(aabc, abc);

        TokenList expectedAbc = TokenList.cloneTokenList(lineCompare.getPercentableCalculator().fromSubmission(abc).getDataCopy());
        expectedAbc.stream().forEach((token) -> token.setValid(false));

        TokenList expectedAabc = TokenList.cloneTokenList(lineCompare.getPercentableCalculator().fromSubmission(aabc).getDataCopy());
        expectedAabc.stream().forEach((token) -> token.setValid(false));

        checkResults(results, aabc, abc,
                new PercentableTokenListDecorator(expectedAabc),
                new PercentableTokenListDecorator(expectedAbc));
    }

    @Test
    public void TestSubmissionDuplicatedTokenNotInOtherSubmission() throws ChecksimsException {
        AlgorithmResults results = cmp(aabc, def);

        checkResultsNoMatch(results, aabc, def);
    }
}
