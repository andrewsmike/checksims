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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.lldp.checksims.parse.Percentable;
import net.lldp.checksims.parse.SubmissionPercentableCalculator;
import net.lldp.checksims.parse.token.TokenTypeMismatchException;
import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.util.data.Range;
import net.lldp.checksims.util.reflection.NamedInstantiable;

/**
 * Detect similarities between two submissions.
 *
 * NOTE that, in addition to the methods listed here, all plagiarism detectors MUST support a no-arguments getInstance()
 * method, and be contained in edu.wpi.checksims.algorithm or a subpackage thereof.
 *
 * This is required as reflection is used to automatically detect and instantiate all similarity detection algorithms
 * present at runtime.
 */
public abstract class SimilarityDetector<T extends Percentable> implements NamedInstantiable {
    /**
     * @return Default token type to be used for this similarity detector
     */
    public abstract SubmissionPercentableCalculator<T> getPercentableCalculator();

    /**
     * Apply a pairwise similarity detection algorithm.
     *
     * Token list types of A and B must match
     *
     * @param rft First submission to apply to
     * @param comt Second submission to apply to
     * @return Similarity results of comparing submissions A and B
     * @throws TokenTypeMismatchException Thrown on comparing two submissions with different token types
     * @throws InternalAlgorithmError Thrown on error detecting similarities
     */
    public abstract AlgorithmResults detectSimilarity(Pair<Submission, Submission> ab, T rft, T comt)
            throws TokenTypeMismatchException, InternalAlgorithmError;
    
    /**
     * Generate a mapping between regions in the documents.
     *
     * Only include extremely likely regions.
     *
     * @param res Results, with submission pair, to inspect
     * @return BiMap between extremely interesting regions of res.a to regions of res.b
     */
    public BiMap<Range,Range> getRegionMappings(AlgorithmResults res)
    {
        return HashBiMap.create();
    }

    @Override
    public String toString()
    {
        return getName();
    }

    /**
     * get the default glob pattern
     * @return the default glob pattern for the algorithm. defaults to * (all files)
     */
    public String getDefaultGlobPattern()
    {
        return "*";
    }
}
