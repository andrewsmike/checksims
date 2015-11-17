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
import net.lldp.checksims.algorithm.similaritymatrix.SimilarityMatrix;
import net.lldp.checksims.parse.token.PercentableTokenListDecorator;
import net.lldp.checksims.parse.token.SubmissionTokenizer;
import net.lldp.checksims.parse.token.TokenList;
import net.lldp.checksims.parse.token.TokenType;
import net.lldp.checksims.parse.token.tokenizer.Tokenizer;
import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.util.AssertUtils;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static net.lldp.checksims.testutil.SubmissionUtils.submissionFromString;
import static net.lldp.checksims.testutil.SubmissionUtils.setFromElements;
import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;

/**
 * Tests for MatrixToHTMLPrinter
 */
public class MatrixToHTMLPrinterTest {
    private MatrixToHTMLPrinter instance;
    private SimilarityMatrix noSignificant;
    private SimilarityMatrix oneSignificant;
    private SimilarityMatrix oneHalfSignificant;
    private SimilarityMatrix twoSignificant;
    private SubmissionTokenizer st;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        instance = MatrixToHTMLPrinter.getInstance();
        st = new SubmissionTokenizer(Tokenizer.getTokenizer(TokenType.CHARACTER));

        Submission abcd = submissionFromString("ABCD", "ABCD");
        Submission xyz = submissionFromString("XYZ", "XYZ");
        Submission abcde = submissionFromString("ABCDE", "ABCDE");
        Submission a = submissionFromString("A", "A");

        AlgorithmResults abcdToXyz = new AlgorithmResults(abcd, xyz, st.fromSubmission(abcd), st.fromSubmission(xyz));

        noSignificant = SimilarityMatrix.generateMatrix(setFromElements(abcd, xyz), singleton(abcdToXyz));

        TokenList abcdInval = TokenList.cloneTokenList(st.fromSubmission(abcd).getDataCopy());
        abcdInval.stream().forEach((token) -> token.setValid(false));
        TokenList abcdeInval = TokenList.cloneTokenList(st.fromSubmission(abcde).getDataCopy());
        for(int i = 0; i < 4; i++) {
            abcdeInval.get(i).setValid(false);
        }

        AlgorithmResults abcdToAbcde = new AlgorithmResults(abcd, abcde,
                new PercentableTokenListDecorator(abcdInval),
                new PercentableTokenListDecorator(abcdeInval));

        oneSignificant = SimilarityMatrix.generateMatrix(setFromElements(abcd, abcde), singleton(abcdToAbcde));

        TokenList abcdInval2 = TokenList.cloneTokenList(st.fromSubmission(abcd).getDataCopy());
        abcdInval2.get(0).setValid(false);
        TokenList aInval = TokenList.cloneTokenList(st.fromSubmission(a).getDataCopy());
        aInval.get(0).setValid(false);

        AlgorithmResults abcdToA = new AlgorithmResults(abcd, a,
                new PercentableTokenListDecorator(abcdInval2),
                new PercentableTokenListDecorator(aInval));

        oneHalfSignificant = SimilarityMatrix.generateMatrix(setFromElements(abcd, a), singleton(abcdToA));

        Submission efgh = submissionFromString("EFGH", "EFGH");
        Submission fghijk = submissionFromString("FGHIJK", "FGHIJK");
        Submission e = submissionFromString("E", "E");

        TokenList efghInval1 = TokenList.cloneTokenList(st.fromSubmission(efgh).getDataCopy());
        for(int i = 1; i < 4; i++) {
            efghInval1.get(i).setValid(false);
        }
        TokenList fghijkInval = TokenList.cloneTokenList(st.fromSubmission(fghijk).getDataCopy());
        for(int i = 0; i < 3; i++) {
            fghijkInval.get(i).setValid(false);
        }

        AlgorithmResults efghToF = new AlgorithmResults(efgh, fghijk,
                new PercentableTokenListDecorator(efghInval1),
                new PercentableTokenListDecorator(fghijkInval));

        TokenList efghInval2 = TokenList.cloneTokenList(st.fromSubmission(efgh).getDataCopy());
        efghInval2.get(0).setValid(false);
        TokenList eInval = TokenList.cloneTokenList(st.fromSubmission(e).getDataCopy());
        eInval.get(0).setValid(false);

        AlgorithmResults efghToE = new AlgorithmResults(efgh, e,
                new PercentableTokenListDecorator(efghInval2),
                new PercentableTokenListDecorator(eInval));

        AlgorithmResults fToE = new AlgorithmResults(fghijk, e, st.fromSubmission(fghijk), st.fromSubmission(e));

        twoSignificant = SimilarityMatrix.generateMatrix(setFromElements(efgh, fghijk), singleton(e), setFromElements(efghToF, efghToE, fToE));
    }

    @Test
    public void TestPrintNull() throws Exception {
        expectedEx.expect(NullPointerException.class);

        instance.printMatrix(null);
    }

    @Test
    public void TestNameIsHTML() {
        assertEquals("html", instance.getName());
    }

    @Test
    public void TestPrintOneHalfSignificant() throws Exception {
        InputStream expectedStream = this.getClass().getResourceAsStream("expected_3.html");
        String expected = IOUtils.toString(expectedStream, StandardCharsets.UTF_8);

        AssertUtils.betterStringEQAssert(expected, instance.printMatrix(oneHalfSignificant));
    }

    @Test
    public void TestPrintTwoSignificant() throws Exception {
        InputStream expectedStream = this.getClass().getResourceAsStream("expected_4.html");
        String expected = IOUtils.toString(expectedStream, StandardCharsets.UTF_8);

        AssertUtils.betterStringEQAssert(expected, instance.printMatrix(twoSignificant));
    }
}
