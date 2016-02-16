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

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableSet;

import net.lldp.checksims.algorithm.AlgorithmResults;
import net.lldp.checksims.algorithm.InternalAlgorithmError;
import net.lldp.checksims.algorithm.similaritymatrix.SimilarityMatrix;
import net.lldp.checksims.util.data.Range;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Print all results over a certain threshold.
 */
public final class MatrixRegionPrinter implements MatrixPrinter {
    private static MatrixRegionPrinter instance;

    private static final double threshold = 0.7;

    private MatrixRegionPrinter() {}

    /**
     * @return Singleton instance of MatrixRegionPrinter
     */
    public static MatrixRegionPrinter getInstance() {
        if(instance == null) {
            instance = new MatrixRegionPrinter();
        }

        return instance;
    }

    /**
     * Print full text and regions of significant results in a similarity matrix.
     *
     * @param matrix Matrix to print
     * @return Body and regions of matched submissions
     * @throws InternalAlgorithmError Thrown on internal error processing matrix
     */
    @Override
    public String printMatrix(SimilarityMatrix matrix) throws InternalAlgorithmError {
        checkNotNull(matrix);

        StringBuilder builder = new StringBuilder();

        ImmutableSet<AlgorithmResults> results = matrix.getBaseResults();

        Set<AlgorithmResults> filteredBelowThreshold = results.stream()
                .filter((result) -> result.percentMatchedA().gtEQ(threshold) || result.percentMatchedB().gtEQ(threshold))
                .collect(Collectors.toCollection(HashSet::new));

        if(filteredBelowThreshold.isEmpty()) {
            builder.append("No significant matches found.\n");
        }

        // For each match, get regions, print.
        for (AlgorithmResults r : filteredBelowThreshold) {
            builder.append("Found ordered match (");
            builder.append(r.a.getName());
            builder.append(",");
            builder.append(r.b.getName());
            builder.append(") at ");
            builder.append(r.getSimilarityPercent());
            builder.append("%.\n");
            builder.append("Printing first submission with regions:\n");

            printRegions(builder, r);
        }

        return builder.toString();
    }

    private void printRegions(StringBuilder b, AlgorithmResults r) {

        // Get regions.
        BiMap<Range,Range> mappings = r.getRegionMappings();

        List<Range> t = mappings.keySet().stream().sorted().collect(Collectors.toList());

        String sub = Arrays.asList(r.a.getContentAsString().split("\n"))
                .stream()
                .filter(A -> !macroLine(A))
                .collect(Collectors.joining("\n"));
        sub = sub.replace('\r', ' ');

        int len = sub.length();
        int s = 0, e;
        Range n;
        do
        {
            if (t.size() > 0) {
                n = t.get(0);
                if (n.start >= s)
                    e = n.start;
                else
                    e = n.end;
            } else {
                n = null;
                e = len;
            }

            b.append(sub.substring(s, e));

            if (n == null)
                break;

            b.append("[#").append(mappings.get(n).toString()).append("# ");
            b.append(sub.substring(e, e + n.length()));
            b.append("]");

            t.remove(0);

            s = e + n.length();
        } while (true);
    }

    public static boolean macroLine(String s) {
        s = s.trim();
        return s.length() > 0 && s.codePointAt(0) == '#';
    }

    @Override
    public String getName() {
        return "region";
    }

    @Override
    public String toString() {
        return "region";
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof MatrixRegionPrinter;
    }
}
