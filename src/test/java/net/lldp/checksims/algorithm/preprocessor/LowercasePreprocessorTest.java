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

import net.lldp.checksims.submission.Submission;
import org.junit.Before;
import org.junit.Test;

import static net.lldp.checksims.testutil.PreprocessorUtils.checkPreprocessSubmission;
import static net.lldp.checksims.testutil.PreprocessorUtils.checkPreprocessSubmissionIdentity;
import static net.lldp.checksims.testutil.SubmissionUtils.submissionFromString;

/**
 * Tests for the LowercasePreprocessor
 */
public class LowercasePreprocessorTest {
    private Submission emptyListCharacter;
    private Submission oneElementListCharacter;
    private Submission oneElementListCharacterIsLowerCase;
    private Submission oneElementListWhitespace;
    private Submission oneElementListWhitespaceIsLowerCase;
    private Submission oneElementListLine;
    private Submission oneElementListLineIsLowerCase;
    private Submission twoElementListCharacter;
    private Submission threeElementListCharacter;
    private LowercasePreprocessor instance;

    @Before
    public void setUp() {
        //char
        emptyListCharacter = submissionFromString("Empty Character submission", "");
        oneElementListCharacter = submissionFromString("One element character submission", "A");
        oneElementListCharacterIsLowerCase = submissionFromString("One element lowercase character submission", "a");
        twoElementListCharacter = submissionFromString("Two element character list", "He");
        threeElementListCharacter = submissionFromString("Three element character list", "HeL");
        
        //whitespace
        oneElementListWhitespace = submissionFromString("One element whitespace submission", "HELLO");
        oneElementListWhitespaceIsLowerCase = submissionFromString("One element lowercase whitespace submission", "hello");
        
        //line
        oneElementListLine = submissionFromString("One element line submission", "HELLO WORLD");
        oneElementListLineIsLowerCase = submissionFromString("One element lowercase line submission", "hello world");

        instance = LowercasePreprocessor.getInstance();
    }

    @Test
    public void TestLowercaseEmptyReturnsEmpty() throws Exception {
        checkPreprocessSubmissionIdentity(emptyListCharacter, instance);
    }

    @Test
    public void TestOneElementCharacterLowercase() throws Exception {
        checkPreprocessSubmission(oneElementListCharacter, oneElementListCharacter.getContentAsString().toLowerCase(), instance);
    }

    @Test
    public void TestOneElementCharacterLowercaseIdentity() throws Exception {
        checkPreprocessSubmissionIdentity(oneElementListCharacterIsLowerCase, instance);
    }

    @Test
    public void TestOneElementWhitespaceLowercase() throws Exception {
        checkPreprocessSubmission(oneElementListWhitespace, oneElementListWhitespace.getContentAsString().toLowerCase(), instance);
    }

    @Test
    public void TestOneElementWhitespaceLowercaseIdentity() throws Exception {
        checkPreprocessSubmissionIdentity(oneElementListWhitespaceIsLowerCase, instance);
    }

    @Test
    public void TestOneElementLineLowercase() throws Exception {
        checkPreprocessSubmission(oneElementListLine, oneElementListLine.getContentAsString().toLowerCase(), instance);
    }

    @Test
    public void TestOneElementLineLowercaseIdentity() throws Exception {
        checkPreprocessSubmissionIdentity(oneElementListLineIsLowerCase, instance);
    }

    @Test
    public void EnsureOrderingRemainsIdenticalTwoElements() throws Exception {
        checkPreprocessSubmission(twoElementListCharacter, twoElementListCharacter.getContentAsString().toLowerCase(), instance);
    }

    @Test
    public void EnsureOrderingRemainsIdenticalThreeElements() throws Exception {
        checkPreprocessSubmission(threeElementListCharacter, threeElementListCharacter.getContentAsString().toLowerCase(), instance);
    }
}
