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
 * Copyright (c) 2014-2016 Ted Meyer, Nicholas DeMarinis, Matthew Heon, and Dolan Murvihill
 */
package net.lldp.checksims.ui.results;

/**
 * 
 * @author ted
 *
 */
public class PairScore
{
    private final SubmissionPair submissions;
    private final double score;
    private final double inverseScore;
    
    /**
     * Both scores between two submissions
     * @param subs the submissions
     * @param score the similarity a->b
     * @param inverse the similarity b->a
     */
    public PairScore(SubmissionPair subs, double score, double inverse)
    {
        this.submissions = subs;
        this.score = score;
        this.inverseScore = inverse;
    }

    /**
     * get the submissions for this score
     * @return the submissions
     */
    public SubmissionPair getSubmissions()
    {
        return submissions;
    }

    /**
     * @return get the score (a->b) for this pair
     */
    public double getScore()
    {
        return score;
    }

    /**
     * @return get the score (b->a) for this pair
     */
    public double getInverseScore()
    {
        return inverseScore;
    }

    /**
     * @return format this pair
     */
    public String getFormattedSubmissions()
    {
        return submissions.getFormattedSubmissions();
    }
}
