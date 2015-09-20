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
import net.lldp.checksims.util.AssertUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static net.lldp.checksims.testutil.SubmissionUtils.submissionFromString;
import static net.lldp.checksims.testutil.SubmissionUtils.setFromElements;
import static java.util.Collections.singleton;
import static org.junit.Assert.*;

/**
 * Tests for MatrixToCSVPrinter
 */
public class MatrixToCSVPrinterTest {
    private MatrixToCSVPrinter instance;
    private SimilarityMatrix twoByTwo;
    private SimilarityMatrix twoByThree;
    private SubmissionTokenizer st;
    
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() throws InternalAlgorithmError {
        instance = MatrixToCSVPrinter.getInstance();
        st = new SubmissionTokenizer(Tokenizer.getTokenizer(TokenType.CHARACTER));

        Submission abcd = submissionFromString("ABCD", "ABCD");
        Submission abcdefgh = submissionFromString("ABCDEFGH", "ABCDEFGH");

        TokenList abcdInval = TokenList.cloneTokenList(st.fromSubmission(abcd).getDataCopy());
        abcdInval.stream().forEach((token) -> token.setValid(false));
        TokenList abcdefghInval = TokenList.cloneTokenList(st.fromSubmission(abcdefgh).getDataCopy());
        for(int i = 0; i < 4; i++) {
            abcdefghInval.get(i).setValid(false);
        }

        AlgorithmResults abcdToAbcdefgh = new AlgorithmResults(abcd, abcdefgh,
                new PercentableTokenListDecorator(abcdInval),
                new PercentableTokenListDecorator(abcdefghInval));

        twoByTwo = SimilarityMatrix.generateMatrix(setFromElements(abcd, abcdefgh), singleton(abcdToAbcdefgh));

        Submission abxy = submissionFromString("ABXY", "ABXY");
        Submission xyz = submissionFromString("XYZ", "XYZ");
        Submission www = submissionFromString("WWW", "WWW");

        TokenList abxyInval = TokenList.cloneTokenList(st.fromSubmission(abxy).getDataCopy());
        for(int i = 2; i < 4; i++) {
            abxyInval.get(i).setValid(false);
        }
        TokenList xyzInval = TokenList.cloneTokenList(st.fromSubmission(xyz).getDataCopy());
        for(int i = 0; i < 2; i++) {
            xyzInval.get(i).setValid(false);
        }

        AlgorithmResults abxyToXyz = new AlgorithmResults(abxy, xyz,
                new PercentableTokenListDecorator(abxyInval),
                new PercentableTokenListDecorator(xyzInval));
        AlgorithmResults abxyToWww = new AlgorithmResults(abxy, www, st.fromSubmission(abxy), st.fromSubmission(www));
        AlgorithmResults xyzToWww = new AlgorithmResults(xyz, www, st.fromSubmission(xyz), st.fromSubmission(www));

        twoByThree = SimilarityMatrix.generateMatrix(setFromElements(abxy, xyz), setFromElements(www), setFromElements(abxyToXyz, abxyToWww, xyzToWww));
    }

    @Test
    public void TestNameIsCSV() {
        assertEquals("csv", instance.getName());
    }

    @Test
    public void TestPrinterNullThrowsException() throws Exception {
        expectedEx.expect(NullPointerException.class);

        instance.printMatrix(null);
    }

    @Test
    public void TestPrinterOnTwoByTwo() throws Exception {
        String expected = "NULL,\"ABCD\",\"ABCDEFGH\"\n\"ABCD\",1.00,1.00\n\"ABCDEFGH\",0.50,1.00\n";
        String actual = instance.printMatrix(twoByTwo);
        
        AssertUtils.betterStringEQAssert(expected, actual);
    }

    @Test
    public void TestPrinterOnThreeByTwo() throws Exception {
        String expected = "NULL,\"ABXY\",\"XYZ\",\"WWW\"\n\"ABXY\",1.00,0.50,0.00\n\"XYZ\",0.67,1.00,0.00\n";

        AssertUtils.betterStringEQAssert(expected, instance.printMatrix(twoByThree));
    }
}
