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

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.lldp.checksims.algorithm.similaritymatrix.SimilarityMatrix;
import net.lldp.checksims.submission.Submission;

/**
 * 
 * @author ted
 *
 */
public class SortableMatrix
{
    private final Submission[] submissions;
    private final Map<SubmissionPair, PairScore> scores;
    
    /**
     * Create a sortable matrix of submissions,
     * can be sorted on multiple queries, and does lookups
     * @param matrix the similarity matrix to be created from
     */
    public SortableMatrix(SimilarityMatrix matrix)
    {
        Set<Submission> submissions = new HashSet<>();
        scores = new HashMap<>();
        
        matrix.getBaseResults().forEach(AR -> {
            submissions.add(AR.a);
            submissions.add(AR.b);
            
            SubmissionPair a2b = new SubmissionPair(AR.a, AR.b);
            SubmissionPair b2a = new SubmissionPair(AR.b, AR.a);
            
            PairScore a2bPS = new PairScore(a2b, AR.percentMatchedA().asDouble(), AR.percentMatchedB().asDouble());
            PairScore b2aPS = new PairScore(b2a, AR.percentMatchedB().asDouble(), AR.percentMatchedA().asDouble());

            scores.put(a2b, a2bPS);
            scores.put(b2a, b2aPS);
        });
        
        this.submissions = new Submission[submissions.size()];
        int counter = 0;
        for(Submission s : submissions)
        {
            this.submissions[counter++] = s;
        }
        
        Arrays.sort(this.submissions, new Comparator<Submission>(){
            @Override
            public int compare(Submission o1, Submission o2)
            {
                return (int) ((o1.getMaximumCopyScore() - o2.getMaximumCopyScore()) * 1000);
            }
        });
    }
    
    private int getLowestIndexAbove(double score)
    {
        int check = 0;
        while(check < submissions.length && submissions[check].getMaximumCopyScore() <= score) {
            check++;
        }
        return check;
    }
    
    /**
     * get submissions that meet threshold criteria
     * @param thresh the minimum threshold a submission must be above
     * @return all submissions with a match above the threshold
     */
    public Submission[] getSubmissionsAboveThreshold(double thresh)
    {
        int indx = getLowestIndexAbove(thresh);
        Submission[] result = new Submission[submissions.length - indx];
        for(int i=0; i<result.length; i++)
        {
            result[i] = submissions[indx+i];
        }
        
        Arrays.sort(result, new Comparator<Submission>(){
            @Override
            public int compare(Submission o1, Submission o2) {
                return (int) ((o2.getMaximumCopyScore() - o1.getMaximumCopyScore())*1000);
            }
        });
        
        return result;
    }
    
    /**
     * get the score for two submissions
     * @param a first submission
     * @param b second submission
     * @return the score between a and b (not to be confused with the score between b and a)
     */
    public PairScore getPairForSubmissions(Submission a, Submission b)
    {
        return getPairForSubmissions(new SubmissionPair(a, b));
    }
    
    /**
     * see {@code getPairForSubmissions}
     * @param sp a submission pair
     * @return the score for these submissions
     */
    public PairScore getPairForSubmissions(SubmissionPair sp)
    {
        return scores.get(sp);
    }
}
