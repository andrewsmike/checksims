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

package net.lldp.checksims.submission;

import net.lldp.checksims.parse.token.SubmissionTokenizer;
import net.lldp.checksims.parse.token.Token;
import net.lldp.checksims.parse.token.TokenList;
import net.lldp.checksims.parse.token.TokenType;
import net.lldp.checksims.parse.token.tokenizer.Tokenizer;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static net.lldp.checksims.testutil.SubmissionUtils.submissionFromString;
import static org.junit.Assert.assertEquals;

/**
 * Tests for Submissions
 */
public class SubmissionTest {
    private Submission a;
    private Submission aTwo;
    private Submission aInval;

    @Before
    public void setUp() {
        a = submissionFromString("a", "a");
        aTwo = submissionFromString("a", "a");
        aInval = new ConcreteSubmission("a", "a");
    }

    @Test
    public void TestSubmissionEquality() {
        assertEquals(a, aTwo);
    }

    @Test
    public void TestBasicSubmissionOperations() {
        assertEquals("a", a.getName());
    }

    @Test
    public void TestValidityIgnoringSubmissionEquality() {
        Submission aIgnoring = new ValidityIgnoringSubmission(a, Tokenizer.getTokenizer(TokenType.CHARACTER));

        assertEquals(aIgnoring, aInval);
        assertEquals(aIgnoring, a);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testTokenListIsImmutable() throws InvalidSubmissionException {
        Submission s1 = submissionFromString("s1", "testtest");
        Submission s2 = submissionFromString("s2", "test");
        
        SubmissionTokenizer st = new SubmissionTokenizer(Tokenizer.getTokenizer(TokenType.CHARACTER));
        
        final TokenList aTokens = st.fromSubmission(s1).getImmutableDataCopy();
        final TokenList bTokens = st.fromSubmission(s2).getImmutableDataCopy();

        
        
        final Iterator<Token> aIt = aTokens.iterator();
        final Iterator<Token> bIt = bTokens.iterator();

        while(aIt.hasNext() && bIt.hasNext()) {
            final Token aTok = aIt.next();
            final Token bTok = bIt.next();
            if(aTok.equals(bTok)) {
                aTok.setValid(false);
                bTok.setValid(false);
            }
        }

        assertEquals(8, aTokens.numValid());
        assertEquals(4, bTokens.numValid());
    }
}
