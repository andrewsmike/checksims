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
 * Copyright (c) 2014-2015 Nicholas DeMarinis, Matthew Heon, Ted Meyer, and Dolan Murvihill
 */

package net.lldp.checksims.algorithm.smithwaterman;

import net.lldp.checksims.algorithm.AlgorithmResults;
import net.lldp.checksims.algorithm.InternalAlgorithmError;
import net.lldp.checksims.parse.token.PercentableTokenListDecorator;
import net.lldp.checksims.parse.token.SubmissionTokenizer;
import net.lldp.checksims.parse.token.TokenList;
import net.lldp.checksims.parse.token.TokenType;
import net.lldp.checksims.parse.token.TokenTypeMismatchException;
import net.lldp.checksims.parse.token.tokenizer.Tokenizer;
import net.lldp.checksims.submission.Submission;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static net.lldp.checksims.testutil.AlgorithmUtils.*;
import static net.lldp.checksims.testutil.SubmissionUtils.*;

/**
 * Tests for the Smith-Waterman Algorithm plagiarism detector
 */
public class SmithWatermanTest {
    private Submission empty;
    private Submission typeMismatch;
    private Submission oneToken;
    private Submission twoTokens;
    private Submission hello;
    private Submission world;
    private Submission helloWorld;
    private Submission helloWerld;
    private Submission helloLongPauseWorld;
    private Submission wrappedHelloPauseWorldIsWrapped;
    private TokenType MEH = TokenType.WHITESPACE;

    private SmithWaterman instance;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() {
        instance = SmithWaterman.getInstance();

        // whitepsace
        empty = submissionFromString("Empty", "");
        oneToken = submissionFromString("One Token", "hello");
        twoTokens = submissionFromString("Two Tokens", "hello world");
        
        //line
        typeMismatch = submissionFromString("Type Mismatch", "hello");
        
        //char
        hello = submissionFromString("Hello", "hello");
        world = submissionFromString("World", "world");
        helloWorld = submissionFromString("Hello World", "hello world");
        helloWerld = submissionFromString("Hello Werld", "hello werld");
        helloLongPauseWorld = submissionFromString("Hello World with Pause", "hello long pause world");
        wrappedHelloPauseWorldIsWrapped = submissionFromString("Wrapped Hello World with Pause", "wrapped hello random world is wrapped");
    }

    private AlgorithmResults runSmithWaterman(Submission a, Submission b, TokenType at, TokenType bt)
            throws TokenTypeMismatchException, InternalAlgorithmError 
    {
        if (bt != at)
        {
            throw new TokenTypeMismatchException("Tokenization of " + a + " and " + b + " do not match");
        }
        return instance.detectSimilarity(Pair.of(a, b),
                new SubmissionTokenizer(Tokenizer.getTokenizer(at)).fromSubmission(a),
                new SubmissionTokenizer(Tokenizer.getTokenizer(bt)).fromSubmission(b));
    }
    
    private TokenList tokens(Submission s, TokenType tt) 
    {
        return new SubmissionTokenizer(Tokenizer.getTokenizer(tt)).fromSubmission(s).getDataCopy();
    }
    
    // Tests for Smith-Waterman algorithm

    @Test
    public void TestNullSubmissionAThrowsException() throws Exception {
        expectedEx.expect(NullPointerException.class);

        runSmithWaterman(null, empty, MEH, TokenType.WHITESPACE);
    }

    @Test
    public void TestNullSubmissionBThrowsException() throws Exception {
        expectedEx.expect(NullPointerException.class);

        runSmithWaterman(empty, null, TokenType.WHITESPACE, MEH);
    }

    @Test(expected = TokenTypeMismatchException.class)
    public void TestTokenTypeMismatchThrowsException() throws Exception {
        runSmithWaterman(empty, typeMismatch, TokenType.WHITESPACE, TokenType.CHARACTER);
    }

    @Test
    public void TestTwoEmptySubmissionsAreNotSimilar() throws Exception {
        AlgorithmResults results = runSmithWaterman(empty, empty, TokenType.WHITESPACE, TokenType.WHITESPACE);

        checkResultsIdenticalSubmissions(results);
    }

    @Test
    public void TestOneEmptyOneNonEmptySubmissionsAreNotSimilar() throws Exception {
        AlgorithmResults results = runSmithWaterman(empty, oneToken, TokenType.WHITESPACE, TokenType.WHITESPACE);

        checkResultsNoMatch(results, empty, oneToken);
    }

    @Test
    public void TestIdenticalNonEmptySubmissionsAreIdentical() throws Exception {
        AlgorithmResults results = runSmithWaterman(oneToken, oneToken, TokenType.WHITESPACE, TokenType.WHITESPACE);

        checkResultsIdenticalSubmissions(results);
    }

    @Test
    public void TestIdenticalNonEmptySubmissionsMoreThanOneTokenAreIdentical() throws Exception {
        AlgorithmResults results = runSmithWaterman(twoTokens, twoTokens, TokenType.WHITESPACE, TokenType.WHITESPACE);

        checkResultsIdenticalSubmissions(results);
    }

    @Test
    public void TestDifferentSubmissionsNoMatches() throws Exception {
        AlgorithmResults results = runSmithWaterman(hello, world, TokenType.CHARACTER, TokenType.CHARACTER);

        checkResultsNoMatch(results, hello, world);
    }

    @Test
    public void TestDifferentSubmissionsPartialOverlay() throws Exception {
        AlgorithmResults results = runSmithWaterman(helloWorld, hello, TokenType.CHARACTER, TokenType.CHARACTER);

        TokenList expectedHelloWorld = tokens(helloWorld, TokenType.CHARACTER);
        for(int i = 0; i < 5; i++) {
            expectedHelloWorld.get(i).setValid(false);
        }

        TokenList expectedHello = tokens(hello, TokenType.CHARACTER);
        expectedHello.stream().forEach((token) -> token.setValid(false));

        checkResults(results, helloWorld, hello,
                new PercentableTokenListDecorator(expectedHelloWorld),
                new PercentableTokenListDecorator(expectedHello));
    }

    @Test
    public void TestDifferentSubmissionsSameSizeInterruptedOverlay() throws Exception {
        AlgorithmResults results = runSmithWaterman(helloWorld, helloWerld, TokenType.CHARACTER, TokenType.CHARACTER);

        TokenList expectedHelloWorld = tokens(helloWorld, TokenType.CHARACTER);
        expectedHelloWorld.stream().forEach((token) -> token.setValid(false));
        expectedHelloWorld.get(7).setValid(true);

        TokenList expectedHelloWerld = tokens(helloWerld, TokenType.CHARACTER);
        expectedHelloWerld.stream().forEach((token) -> token.setValid(false));
        expectedHelloWerld.get(7).setValid(true);

        checkResults(results, helloWorld, helloWerld,
                new PercentableTokenListDecorator(expectedHelloWorld),
                new PercentableTokenListDecorator(expectedHelloWerld));
    }

    @Test
    public void TestDifferentSubmissionsTwoOverlays() throws Exception {
        AlgorithmResults results = runSmithWaterman(helloLongPauseWorld, helloWorld, TokenType.CHARACTER, TokenType.CHARACTER);

        TokenList expectedHelloLongPauseWorld = tokens(helloLongPauseWorld, TokenType.CHARACTER);
        for(int i = 0; i < 6; i++) {
            expectedHelloLongPauseWorld.get(i).setValid(false);
        }
        for(int i = 17; i < 22; i++) {
            expectedHelloLongPauseWorld.get(i).setValid(false);
        }

        TokenList expectedHelloWorld = tokens(helloWorld, TokenType.CHARACTER);
        expectedHelloWorld.stream().forEach((token) -> token.setValid(false));

        checkResults(results, helloLongPauseWorld, helloWorld,
                new PercentableTokenListDecorator(expectedHelloLongPauseWorld),
                new PercentableTokenListDecorator(expectedHelloWorld));
    }

    @Test
    public void TestDifferentSubmissionsTwoOverlaysWrapped() throws Exception {
        AlgorithmResults results = runSmithWaterman(helloLongPauseWorld, wrappedHelloPauseWorldIsWrapped, TokenType.CHARACTER, TokenType.CHARACTER);

        TokenList expectedHelloLongPauseWorld = tokens(helloLongPauseWorld, TokenType.CHARACTER);
        for(int i = 0; i < 6; i++) {
            expectedHelloLongPauseWorld.get(i).setValid(false);
        }
        for(int i = 16; i < 22; i++) {
            expectedHelloLongPauseWorld.get(i).setValid(false);
        }

        TokenList expectedWrappedHelloPauseWorldIsWrapped = tokens(wrappedHelloPauseWorldIsWrapped, TokenType.CHARACTER);
        for(int i = 8; i < 14; i++) {
            expectedWrappedHelloPauseWorldIsWrapped.get(i).setValid(false);
        }
        for(int i = 20; i < 26; i++) {
            expectedWrappedHelloPauseWorldIsWrapped.get(i).setValid(false);
        }

        checkResults(results, helloLongPauseWorld, wrappedHelloPauseWorldIsWrapped,
                new PercentableTokenListDecorator(expectedHelloLongPauseWorld),
                new PercentableTokenListDecorator(expectedWrappedHelloPauseWorldIsWrapped));
    }
}
