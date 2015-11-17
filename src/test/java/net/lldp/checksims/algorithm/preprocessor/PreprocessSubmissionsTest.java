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

import net.lldp.checksims.ChecksimsException;
import net.lldp.checksims.submission.ConcreteSubmission;
import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.util.completion.DefaultLoggerStatusLogger;
import net.lldp.checksims.util.completion.StatusLogger;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static net.lldp.checksims.testutil.SubmissionUtils.checkSubmissionCollections;
import static net.lldp.checksims.testutil.SubmissionUtils.submissionFromString;
import static net.lldp.checksims.testutil.SubmissionUtils.setFromElements;
import static java.util.Collections.*;
import static org.junit.Assert.*;

/**
 * Tests for PreprocessSubmissions class
 */
public class PreprocessSubmissionsTest {
    private Submission a;
    private Submission b;
    private Set<Submission> empty;
    private Set<Submission> oneSubmission;
    private Set<Submission> twoSubmissions;
    private SubmissionPreprocessor identity;
    private SubmissionPreprocessor renamer;
    private StatusLogger logger;

    @Before
    public void setUp() {
        a = submissionFromString("Submission A", "A");
        b = submissionFromString("Submission B", "B");
        logger = new DefaultLoggerStatusLogger();

        empty = new HashSet<>();
        oneSubmission = singleton(a);
        twoSubmissions = setFromElements(a, b);

        identity = new SubmissionPreprocessor() {
            @Override
            public Submission process(Submission submission) {
                return submission;
            }

            @Override
            public String getName() {
                return "identity";
            }
        };

        renamer = new SubmissionPreprocessor() {
            @Override
            public Submission process(Submission submission) {
                return new ConcreteSubmission("renamed " + submission.getName(), submission.getContentAsString());
            }

            @Override
            public String getName() {
                return "renamer";
            }
        };
    }

    @Test
    public void testEmptyReturnsEmpty() throws ChecksimsException {
        Collection<Submission> results = PreprocessSubmissions.process(identity, empty, logger);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testOneSubmissionIdentity() throws ChecksimsException {
        Collection<Submission> results = PreprocessSubmissions.process(identity, oneSubmission, logger);

        assertNotNull(results);
        assertEquals(results, oneSubmission);
    }

    @Test
    public void testOneSubmissionRename() throws ChecksimsException {
        Collection<Submission> results = PreprocessSubmissions.process(renamer, oneSubmission, logger);
        Submission expected = submissionFromString("renamed " + a.getName(), a.getContentAsString());

        checkSubmissionCollections(singleton(expected), results);
    }

    @Test
    public void testTwoSubmissionIdentity() throws ChecksimsException {
        Collection<Submission> results = PreprocessSubmissions.process(identity, twoSubmissions, logger);

        checkSubmissionCollections(twoSubmissions, results);
    }

    @Test
    public void testTwoSubmissionRename() throws ChecksimsException {
        Collection<Submission> results = PreprocessSubmissions.process(renamer, twoSubmissions, logger);
        Submission expectedA = submissionFromString("renamed " + a.getName(), a.getContentAsString());
        Submission expectedB = submissionFromString("renamed " + b.getName(), b.getContentAsString());
        List<Submission> expected = Arrays.asList(expectedA, expectedB);

        checkSubmissionCollections(expected, results);
    }
}
