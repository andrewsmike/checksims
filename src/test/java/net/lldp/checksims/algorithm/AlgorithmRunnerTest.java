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

package net.lldp.checksims.algorithm;

import net.lldp.checksims.ChecksimsException;
import net.lldp.checksims.parse.SubmissionPercentableCalculator;
import net.lldp.checksims.parse.token.PercentableTokenListDecorator;
import net.lldp.checksims.parse.token.SubmissionTokenizer;
import net.lldp.checksims.parse.token.TokenType;
import net.lldp.checksims.parse.token.TokenTypeMismatchException;
import net.lldp.checksims.parse.token.tokenizer.Tokenizer;
import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.testutil.AlgorithmUtils;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static net.lldp.checksims.testutil.SubmissionUtils.submissionFromString;
import static net.lldp.checksims.testutil.SubmissionUtils.setFromElements;
import static java.util.Collections.singleton;

/**
 * Tests for the AlgorithmRunner class
 */
public class AlgorithmRunnerTest {
    private Submission a;
    private Submission b;
    private Submission c;
    private Submission d;

    private SimilarityDetector<PercentableTokenListDecorator> detectNothing;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() {
        detectNothing = new SimilarityDetector<PercentableTokenListDecorator>() {

            @Override
            public String getName() {
                return "nothing";
            }

            @Override
            public SubmissionPercentableCalculator<PercentableTokenListDecorator> getPercentableCalculator()
            {
                return new SubmissionTokenizer(Tokenizer.getTokenizer(TokenType.CHARACTER));
            }

            @Override
            public AlgorithmResults detectSimilarity(Pair<Submission, Submission> ab,
                    PercentableTokenListDecorator rft, PercentableTokenListDecorator comt)
                    throws TokenTypeMismatchException, InternalAlgorithmError
            {
                return new AlgorithmResults(ab, rft, comt);
            }
        };

        a = submissionFromString("A", "A");
        b = submissionFromString("B", "B");
        c = submissionFromString("C", "C");
        d = submissionFromString("D", "D");
    }

    @Test
    public void TestRunAlgorithmNull() throws ChecksimsException {
        expectedEx.expect(NullPointerException.class);

        AlgorithmRunner.runAlgorithm(null, detectNothing);
    }

    @Test
    public void TestRunAlgorithmNullAlgorithm() throws ChecksimsException {
        expectedEx.expect(NullPointerException.class);

        AlgorithmRunner.runAlgorithm(singleton(Pair.of(a, b)), null);
    }

    @Test
    public void TestRunAlgorithmEmptySet() throws ChecksimsException {
        expectedEx.expect(IllegalArgumentException.class);

        AlgorithmRunner.runAlgorithm(new HashSet<>(), null);
    }

    @Test
    public void TestRunAlgorithmSinglePair() throws ChecksimsException {
        Set<Pair<Submission, Submission>> submissions = singleton(Pair.of(a, b));
        Collection<AlgorithmResults> results = AlgorithmRunner.runAlgorithm(submissions, detectNothing);

        AlgorithmUtils.checkResultsContainsPairs(results, submissions);
    }

    @Test
    public void TestRunAlgorithmTwoPairs() throws ChecksimsException {
        Set<Pair<Submission, Submission>> submissions = setFromElements(Pair.of(a, b), Pair.of(a, c));
        Collection<AlgorithmResults> results = AlgorithmRunner.runAlgorithm(submissions, detectNothing);

        AlgorithmUtils.checkResultsContainsPairs(results, submissions);
    }

    @Test
    public void TestRunAlgorithmThreePairs() throws ChecksimsException {
        Set<Pair<Submission, Submission>> submissions = setFromElements(Pair.of(a, b), Pair.of(a, c), Pair.of(b, c));
        Collection<AlgorithmResults> results = AlgorithmRunner.runAlgorithm(submissions, detectNothing);

        AlgorithmUtils.checkResultsContainsPairs(results, submissions);
    }

    @Test
    public void TestRunAlgorithmAllPossiblePairs() throws ChecksimsException {
        Set<Pair<Submission, Submission>> submissions = setFromElements(Pair.of(a, b), Pair.of(a, c), Pair.of(a, d), Pair.of(b, c), Pair.of(b, d), Pair.of(c, d));
        Collection<AlgorithmResults> results = AlgorithmRunner.runAlgorithm(submissions, detectNothing);

        AlgorithmUtils.checkResultsContainsPairs(results, submissions);
    }
}
