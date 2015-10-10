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

package net.lldp.checksims.algorithm.similaritymatrix.output;

import net.lldp.checksims.algorithm.AlgorithmResults;
import net.lldp.checksims.algorithm.InternalAlgorithmError;
import net.lldp.checksims.algorithm.similaritymatrix.SimilarityMatrix;
import net.lldp.checksims.parse.token.PercentableTokenListDecorator;
import net.lldp.checksims.parse.token.SubmissionTokenizer;
import net.lldp.checksims.parse.token.TokenList;
import net.lldp.checksims.parse.token.TokenType;
import net.lldp.checksims.parse.token.tokenizer.Tokenizer;
import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.testutil.SubmissionUtils;
import net.lldp.checksims.util.AssertUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;

/**
 * Tests for MatrixThresholdPrinter
 */
public class MatrixThresholdPrinterTest {
    private MatrixThresholdPrinter instance;
    private SimilarityMatrix noSignificant;
    private SimilarityMatrix oneSignificant;
    private SimilarityMatrix oneHalfSignificant;
    private SimilarityMatrix twoSignificant;
    private SubmissionTokenizer st;
    
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() throws InternalAlgorithmError {
        Submission abcd = SubmissionUtils.submissionFromString("ABCD", "ABCD");
        Submission xyz = SubmissionUtils.submissionFromString("XYZ", "XYZ");
        Submission abcde = SubmissionUtils.submissionFromString("ABCDE", "ABCDE");
        Submission a = SubmissionUtils.submissionFromString("A", "A");

        st = new SubmissionTokenizer(Tokenizer.getTokenizer(TokenType.CHARACTER));
        instance = MatrixThresholdPrinter.getInstance();

        AlgorithmResults abcdToXyz = new AlgorithmResults(abcd, xyz,
                st.fromSubmission(abcd),
                st.fromSubmission(xyz));

        noSignificant = SimilarityMatrix.generateMatrix(SubmissionUtils.setFromElements(abcd, xyz), singleton(abcdToXyz));

        TokenList abcdInval = st.fromSubmission(abcd).getDataCopy();
        abcdInval.stream().forEach((token) -> token.setValid(false));
        TokenList abcdeInval = st.fromSubmission(abcde).getDataCopy();
        for(int i = 0; i < 4; i++) {
            abcdeInval.get(i).setValid(false);
        }

        AlgorithmResults abcdToAbcde = new AlgorithmResults(abcd, abcde, 
                new PercentableTokenListDecorator(abcdInval),
                new PercentableTokenListDecorator(abcdeInval));

        oneSignificant = SimilarityMatrix.generateMatrix(SubmissionUtils.setFromElements(abcd, abcde), singleton(abcdToAbcde));

        abcd.invalidateCache();
        TokenList abcdInval2 = st.fromSubmission(abcd).getDataCopy();
        abcdInval2.get(0).setValid(false);
        TokenList aInval = st.fromSubmission(a).getDataCopy();
        aInval.get(0).setValid(false);

        AlgorithmResults abcdToA = new AlgorithmResults(abcd, a,
                new PercentableTokenListDecorator(abcdInval2),
                new PercentableTokenListDecorator(aInval));

        oneHalfSignificant = SimilarityMatrix.generateMatrix(SubmissionUtils.setFromElements(abcd, a), singleton(abcdToA));

        Submission efgh = SubmissionUtils.submissionFromString("EFGH", "EFGH");
        Submission fghijk = SubmissionUtils.submissionFromString("FGHIJK", "FGHIJK");
        Submission e = SubmissionUtils.submissionFromString("E", "E");

        efgh.invalidateCache();
        fghijk.invalidateCache();
        TokenList efghInval1 = st.fromSubmission(efgh).getDataCopy();
        for(int i = 1; i < 4; i++) {
            efghInval1.get(i).setValid(false);
        }
        TokenList fghijkInval = st.fromSubmission(fghijk).getDataCopy();
        for(int i = 0; i < 3; i++) {
            fghijkInval.get(i).setValid(false);
        }

        AlgorithmResults efghToF = new AlgorithmResults(efgh, fghijk,
                new PercentableTokenListDecorator(efghInval1),
                new PercentableTokenListDecorator(fghijkInval));

        efgh.invalidateCache();
        e.invalidateCache();
        TokenList efghInval2 = st.fromSubmission(efgh).getDataCopy();
        efghInval2.get(0).setValid(false);
        TokenList eInval = st.fromSubmission(e).getDataCopy();
        eInval.get(0).setValid(false);

        AlgorithmResults efghToE = new AlgorithmResults(efgh, e,
                new PercentableTokenListDecorator(efghInval2),
                new PercentableTokenListDecorator(eInval));

        fghijk.invalidateCache();
        e.invalidateCache();
        
        AlgorithmResults fToE = new AlgorithmResults(fghijk, e,
                st.fromSubmission(fghijk),
                st.fromSubmission(e));

        twoSignificant = SimilarityMatrix.generateMatrix(SubmissionUtils.setFromElements(efgh, fghijk), singleton(e), SubmissionUtils.setFromElements(efghToF, efghToE, fToE));
    }

    @Test
    public void TestPrintNull() throws Exception {
        expectedEx.expect(NullPointerException.class);

        instance.printMatrix(null);
    }

    @Test
    public void TestNameIsThreshold() {
        assertEquals("threshold", instance.getName());
    }

    @Test
    public void TestPrintNoSignificant() throws Exception {
        String expected = "No significant matches found.\n";

        assertEquals(expected, instance.printMatrix(noSignificant));
    }

    @Test
    public void TestPrintOneSignificant() throws Exception {
        String expected = "Found match of 100% (inverse match 80%) between submissions \"ABCD\" and \"ABCDE\"\n";

        AssertUtils.betterStringEQAssert(expected, instance.printMatrix(oneSignificant));
    }

    @Test
    public void TestPrintOneHalfSignificant() throws Exception {
        String expected = "Found match of 100% (inverse match 25%) between submissions \"A\" and \"ABCD\"\n";

        AssertUtils.betterStringEQAssert(expected, instance.printMatrix(oneHalfSignificant));
    }

    @Test
    public void TestPrintTwoSignificant() throws Exception {
        String expected = "Found match of 100% (inverse match 25%) between submissions \"E\" and \"EFGH\"\n";
        expected += "Found match of 75% (inverse match 50%) between submissions \"EFGH\" and \"FGHIJK\"\n";

        AssertUtils.betterStringEQAssert(expected, instance.printMatrix(twoSignificant));
    }
}
