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

import net.lldp.checksims.parse.SubmissionPercentableCalculator;
import net.lldp.checksims.parse.token.PercentableTokenListDecorator;
import net.lldp.checksims.parse.token.SubmissionTokenizer;
import net.lldp.checksims.parse.token.TokenList;
import net.lldp.checksims.parse.token.TokenType;
import net.lldp.checksims.parse.token.tokenizer.Tokenizer;
import net.lldp.checksims.submission.InvalidSubmissionException;
import net.lldp.checksims.submission.Submission;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static net.lldp.checksims.testutil.SubmissionUtils.submissionFromString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for Algorithm Results
 */
public class AlgorithmResultsTest {
    private Submission a;
    private Submission b;
    private Submission abcd;
    private Submission empty;
    
    private final SubmissionPercentableCalculator<PercentableTokenListDecorator> SPC =
            new SubmissionTokenizer(Tokenizer.getTokenizer(TokenType.CHARACTER));

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() {
        a = submissionFromString("A", "A");
        b = submissionFromString("B", "B");
        abcd = submissionFromString("ABCD", "ABCD");
        empty = submissionFromString("Empty", "");
    }

    @Test
    public void TestCreateAlgorithmResultsNullA() throws InvalidSubmissionException {
        expectedEx.expect(NullPointerException.class);
        
        new AlgorithmResults(null, b, SPC.fromSubmission(a), SPC.fromSubmission(b));
    }

    @Test
    public void TestCreateAlgorithmResultsNullB() throws InvalidSubmissionException {
        expectedEx.expect(NullPointerException.class);

        new AlgorithmResults(a, null, SPC.fromSubmission(a), SPC.fromSubmission(b));
    }

    @Test
    public void TestCreateAlgorithmResultsNullFinalA() throws InvalidSubmissionException {
        expectedEx.expect(NullPointerException.class);

        new AlgorithmResults(a, b, null, SPC.fromSubmission(b));
    }

    @Test
    public void TestCreateAlgorithmResultsNullFinalB() throws InvalidSubmissionException {
        expectedEx.expect(NullPointerException.class);

        new AlgorithmResults(a, b, SPC.fromSubmission(a), null);
    }
    
    @Test
    public void TestAlgorithmResultsGetPercentSimilarA() throws InvalidSubmissionException {
        AlgorithmResults test1 = new AlgorithmResults(a, b, SPC.fromSubmission(a), SPC.fromSubmission(b));

        assertEquals(0.0, test1.percentMatchedA().asDouble(), 0.0);
    }

    @Test
    public void TestAlgorithmResultsGetPercentSimilarB() throws InvalidSubmissionException {
        AlgorithmResults test1 = new AlgorithmResults(a, b, SPC.fromSubmission(a), SPC.fromSubmission(b));

        assertEquals(0.0, test1.percentMatchedB().asDouble(), 0.0);
    }

    @Test
    public void TestAlgorithmResultsPercentSimilarAEmpty() throws InvalidSubmissionException {
        AlgorithmResults test = new AlgorithmResults(empty, b, SPC.fromSubmission(empty), SPC.fromSubmission(b));

        assertEquals(0.0, test.percentMatchedA().asDouble(), 0.0);
    }

    @Test
    public void TestAlgorithmResultsGetPercentSimilarBEmpty() throws InvalidSubmissionException {
        AlgorithmResults test = new AlgorithmResults(a, empty, SPC.fromSubmission(a), SPC.fromSubmission(empty));

        assertEquals(0.0, test.percentMatchedB().asDouble(), 0.0);
    }

    @Test
    public void TestAlgorithmResultsGetPercentSimilarANonzero() throws InvalidSubmissionException {
        TokenList one = TokenList.cloneTokenList(SPC.fromSubmission(abcd).getDataCopy());
        one.get(0).setValid(false);
        TokenList two = TokenList.cloneTokenList(SPC.fromSubmission(abcd).getDataCopy());
        for(int i = 0; i < 2; i++) {
            two.get(i).setValid(false);
        }

        AlgorithmResults testOne = new AlgorithmResults(abcd, b, new PercentableTokenListDecorator(one), SPC.fromSubmission(b));
        AlgorithmResults testTwo = new AlgorithmResults(abcd, b, new PercentableTokenListDecorator(two), SPC.fromSubmission(b));

        assertEquals(0.25, testOne.percentMatchedA().asDouble(), 0.0);
        assertEquals(0.50, testTwo.percentMatchedA().asDouble(), 0.0);
    }

    @Test
    public void TestAlgorithmResultsGetPercentSimilarBNonzero() throws InvalidSubmissionException {
        TokenList one = TokenList.cloneTokenList(SPC.fromSubmission(abcd).getDataCopy());
        one.get(0).setValid(false);
        TokenList two = TokenList.cloneTokenList(SPC.fromSubmission(abcd).getDataCopy());
        for(int i = 0; i < 2; i++) {
            two.get(i).setValid(false);
        }

        AlgorithmResults testOne = new AlgorithmResults(a, abcd, SPC.fromSubmission(a), new PercentableTokenListDecorator(one));
        AlgorithmResults testTwo = new AlgorithmResults(a, abcd, SPC.fromSubmission(a), new PercentableTokenListDecorator(two));

        assertEquals(0.25, testOne.percentMatchedB().asDouble(), 0.0);
        assertEquals(0.50, testTwo.percentMatchedB().asDouble(), 0.0);
    }

    @Test
    public void TestBasicEquality() throws InvalidSubmissionException {
        AlgorithmResults one = new AlgorithmResults(a, b, SPC.fromSubmission(a), SPC.fromSubmission(b));
        AlgorithmResults two = new AlgorithmResults(a, b, SPC.fromSubmission(a), SPC.fromSubmission(b));

        assertEquals(one, two);
    }

    @Test
    public void TestBasicInequality() throws InvalidSubmissionException {
        AlgorithmResults one = new AlgorithmResults(a, b, SPC.fromSubmission(a), SPC.fromSubmission(b));
        AlgorithmResults two = new AlgorithmResults(a, abcd, SPC.fromSubmission(a), SPC.fromSubmission(abcd));

        assertNotEquals(one, two);
    }
}
