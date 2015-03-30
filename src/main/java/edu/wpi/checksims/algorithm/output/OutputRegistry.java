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
 * Copyright (c) 2014-2015 Matthew Heon and Dolan Murvihill
 */

package edu.wpi.checksims.algorithm.output;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.wpi.checksims.ChecksimException;
import edu.wpi.checksims.util.reflection.ReflectiveInstantiator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Registry for valid output strategies
 */
public final class OutputRegistry {
    private final List<SimilarityMatrixPrinter> outputStrategies;

    private static OutputRegistry instance;

    private OutputRegistry() {
        List<SimilarityMatrixPrinter> instances = ReflectiveInstantiator.reflectiveInstantiator("edu.wpi.checksims.algorithm.output", SimilarityMatrixPrinter.class);

        // Get a list without duplicates
        // If it's a different size, then duplicates existed, which is bad
        // Throw a RuntimeException for that!
        ImmutableList<String> noDups = ImmutableSet.copyOf(instances.stream().map(SimilarityMatrixPrinter::getName).collect(Collectors.toList())).asList();
        if(noDups.size() < instances.size()) {
            throw new RuntimeException("Some algorithm names were not globally unique!");
        }

        if(instances.isEmpty()) {
            throw new RuntimeException("No providers for SimilarityMatrixPrinter present!");
        }

        // The algorithms list should not be changed after initialization
        this.outputStrategies = ImmutableList.copyOf(instances);
    }

    /**
     * @return Sole instance of the OutputRegistry singleton
     */
    public static OutputRegistry getInstance() {
        if(instance == null) {
            instance = new OutputRegistry();
        }

        return instance;
    }

    /**
     * Get an instance of the output strategy matching the given name
     *
     * @param name Name of output strategy to obtain
     * @return Instance of requested output strategy
     * @throws ChecksimException Thrown if requested output strategy does not exist or cannot be obtained
     */
    public SimilarityMatrixPrinter getOutputStrategy(String name) throws ChecksimException {
        String lowerName = name.toLowerCase();
        List<SimilarityMatrixPrinter> matchingStrategies = outputStrategies.stream().filter((strategy) -> strategy.getName().equals(lowerName)).collect(Collectors.toList());

        if(matchingStrategies.isEmpty()) {
            throw new ChecksimException("No output strategy found with name " + name);
        } else if(matchingStrategies.size() > 1) {
            throw new ChecksimException("Collision in output strategy names found!");
        }

        return matchingStrategies.get(0);
    }

    /**
     * @return A list of the names of all supported output strategies
     */
    public List<String> getAllOutputStrategyNames() {
        return outputStrategies.stream().map(SimilarityMatrixPrinter::getName).collect(Collectors.toList());
    }

    /**
     * The default output strategy is the Threshold Printer
     *
     * If the Threshold Printer is for some reason unavailable, the first available output strategy is used instead
     *
     * @return Instance of the default output strategy
     */
    public SimilarityMatrixPrinter getDefaultStrategy() {
        String thresholdPrinterName = SimilarityMatrixThresholdPrinter.getInstance().getName();

        try {
            return getOutputStrategy(thresholdPrinterName);
        } catch(ChecksimException e) {
            return outputStrategies.get(0);
        }
    }

    /**
     * @return Name of the default output strategy
     */
    public String getDefaultStrategyName() {
        return getDefaultStrategy().getName();
    }
}
