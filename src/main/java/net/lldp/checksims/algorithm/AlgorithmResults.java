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

import org.apache.commons.lang3.tuple.Pair;

import net.lldp.checksims.parse.Percentable;
import net.lldp.checksims.parse.Real;
import net.lldp.checksims.submission.Submission;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Results for a pairwise comparison algorithm.
 */
public class AlgorithmResults {
    // TODO consider making these private and adding getters
    public final Submission a;
    public final Submission b;
    public final Percentable percentableA;
    public final Percentable percentableB;
    private final Real percentMatchedA;
    private final Real percentMatchedB;

    /**
     * Construct results for a pairwise similarity detection algorithm.
     *
     * @param a First submission compared
     * @param b Second submission compared
     * @param percentableA Token list from submission A, with matched tokens set invalid
     * @param percentableB Token list from submission B, with matched tokens set invalid
     */
    public AlgorithmResults(Submission a, Submission b, Percentable percentableA, Percentable percentableB) {
        checkNotNull(a);
        checkNotNull(b);
        checkNotNull(percentableA);
        checkNotNull(percentableB);
        
        this.a = a;
        this.b = b;
        
        this.percentableA = percentableA;
        this.percentableB = percentableB;

        this.percentMatchedA = percentableA.getPercentageMatched();
        this.percentMatchedB = percentableB.getPercentageMatched();
        
    }

    public AlgorithmResults(Pair<Submission, Submission> ab, Percentable a, Percentable b)
    {
        this(ab.getLeft(), ab.getRight(), a, b);
    }

    /**
     * @return Percentage similarity of submission A to submission B. Represented as a double from 0.0 to 1.0 inclusive
     */
    public Real percentMatchedA() {
        return percentMatchedA;
    }

    /**
     * @return Percentage similarity of submission B to submission A. Represented as a double from 0.0 to 1.0 inclusive
     */
    public Real percentMatchedB() {
        return percentMatchedB;
    }

    @Override
    public String toString() {
        return "Similarity results for submissions named " + a.getName() + " and " + b.getName();
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof AlgorithmResults)) {
            return false;
        }

        AlgorithmResults otherResults = (AlgorithmResults)other;

        return this.a.equals(otherResults.a)
                && this.b.equals(otherResults.b)
                && this.percentableA.equals(otherResults.percentableA)
                && this.percentableB.equals(otherResults.percentableB);
    }

    @Override
    public int hashCode() {
        return a.hashCode() ^ b.hashCode();
    }

    public double getSimilarityPercent()
    {
        return percentMatchedB.asDouble();
    }

    public Percentable getPercentableA()
    {
        return percentableB;
    }
    
    public Percentable getPercentableB()
    {
        return percentableB;
    }

    public boolean identicalSubmissions()
    {
        checkNotNull(a);
        checkNotNull(b);
        
        return a.equals(b);
    }

    public AlgorithmResults inverse()
    {
        return new AlgorithmResults(b, a, percentableB, percentableA);
    }
}
