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

package net.lldp.checksims.algorithm.linesimilarity;

import net.lldp.checksims.algorithm.AlgorithmResults;
import net.lldp.checksims.algorithm.InternalAlgorithmError;
import net.lldp.checksims.algorithm.SimilarityDetector;
import net.lldp.checksims.parse.SubmissionPercentableCalculator;
import net.lldp.checksims.parse.token.PercentableTokenListDecorator;
import net.lldp.checksims.parse.token.SubmissionTokenizer;
import net.lldp.checksims.parse.token.Token;
import net.lldp.checksims.parse.token.TokenList;
import net.lldp.checksims.parse.token.TokenType;
import net.lldp.checksims.parse.token.TokenTypeMismatchException;
import net.lldp.checksims.parse.token.tokenizer.Tokenizer;
import net.lldp.checksims.submission.Submission;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.tuple.Pair;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implements a line-by-line similarity checker.
 */
public final class LineSimilarityChecker implements SimilarityDetector<PercentableTokenListDecorator> { // TODO LINE
    private static LineSimilarityChecker instance;

    /**
     * Internal class for record-keeping - used to record a line at a specific location in a submission.
     */
    class SubmissionLine {
        public final int lineNum;
        public final Submission submission;

        SubmissionLine(int lineNum, Submission submission) {
            this.lineNum = lineNum;
            this.submission = submission;
        }

        @Override
        public String toString() {
            return "Line " + lineNum + " from submission with name " + submission.getName();
        }
    }

    private LineSimilarityChecker() {}

    // Singleton
    public static LineSimilarityChecker getInstance() {
        if(instance == null) {
            instance = new LineSimilarityChecker();
        }

        return instance;
    }

    @Override
    public String getName() {
        return "linecompare";
    }

    @Override
    public SubmissionPercentableCalculator<PercentableTokenListDecorator> getPercentableCalculator() {
        return new SubmissionTokenizer(Tokenizer.getTokenizer(TokenType.LINE));
    }

    /**
     * Detect similarities using line similarity comparator.
     *
     * @param a First submission to check
     * @param b Second submission to check
     * @return Results of the similarity detection
     * @throws TokenTypeMismatchException Thrown comparing two submissions with different token types
     * @throws InternalAlgorithmError Thrown on error obtaining a hash algorithm instance
     */
    @Override
    public AlgorithmResults detectSimilarity(Pair<Submission, Submission> ab,
            PercentableTokenListDecorator a,
            PercentableTokenListDecorator b)
            throws TokenTypeMismatchException, InternalAlgorithmError {
        checkNotNull(a);
        checkNotNull(b);

        //TokenList linesA = a.getContentAsTokens();
        //TokenList linesB = b.getContentAsTokens();
        //TokenList finalA = TokenList.cloneTokenList(linesA);
        //TokenList finalB = TokenList.cloneTokenList(linesB);

        /*
        if(!a.getTokenType().equals(b.getTokenType())) {
            throw new TokenTypeMismatchException("Token list type mismatch: submission " + a.getName() + " has type " +
                    linesA.type.toString() + ", while submission " + b.getName() + " has type "
                    + linesB.type.toString());
        } else
            */
        
        if(a.equals(b)) {
            a.getDataCopy().stream().forEach((token) -> token.setValid(false));
            b.getDataCopy().stream().forEach((token) -> token.setValid(false));
            return new AlgorithmResults(ab, a, b);
        }

        MessageDigest hasher;

        // Get a hashing instance
        try {
            hasher = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new InternalAlgorithmError("Error instantiating SHA-512 hash algorithm: " + e.getMessage());
        }

        // Create a line database map
        // Per-method basis to ensure we have no mutable state in the class
        Map<String, List<SubmissionLine>> lineDatabase = new HashMap<>();

        // Hash all lines in A, and put them in the lines database
        addLinesToMap(a.getDataCopy(), lineDatabase, ab.getLeft(), hasher);

        // Hash all lines in B, and put them in the lines database
        addLinesToMap(b.getDataCopy(), lineDatabase, ab.getRight(), hasher);

        // Number of matched lines contained in both
        int identicalLinesA = 0;
        int identicalLinesB = 0;

        // Check all the keys
        for(String key : lineDatabase.keySet()) {

            // If more than 1 line has the hash...
            if(lineDatabase.get(key).size() != 1) {
                int numLinesA = 0;
                int numLinesB = 0;

                // Count the number of that line in each submission
                for(SubmissionLine s : lineDatabase.get(key)) {
                    if(s.submission.equals(ab.getLeft())) {
                        numLinesA++;
                    } else if(s.submission.equals(ab.getRight())) {
                        numLinesB++;
                    } else {
                        throw new RuntimeException("Unreachable code!");
                    }
                }

                if(numLinesA == 0 || numLinesB == 0) {
                    // Only one of the submissions includes the line - no plagiarism here
                    continue;
                }

                // Set matches invalid
                for(SubmissionLine s : lineDatabase.get(key)) {
                    if(s.submission.equals(ab.getLeft())) {
                        a.getDataCopy().get(s.lineNum).setValid(false);
                    } else if(s.submission.equals(ab.getRight())) {
                        b.getDataCopy().get(s.lineNum).setValid(false);
                    } else {
                        throw new RuntimeException("Unreachable code!");
                    }
                }

                identicalLinesA += numLinesA;
                identicalLinesB += numLinesB;
            }
        }

        int invalTokensA = (int)a.getDataCopy().stream().filter((token) -> !token.isValid()).count();
        int invalTokensB = (int)b.getDataCopy().stream().filter((token) -> !token.isValid()).count();

        if(invalTokensA != identicalLinesA) {
            throw new InternalAlgorithmError("Internal error: number of identical tokens (" + identicalLinesA
                    + ") does not match number of invalid tokens (" + invalTokensA + ")");
        } else if(invalTokensB != identicalLinesB) {
            throw new InternalAlgorithmError("Internal error: number of identical tokens (" + identicalLinesB
                    + ") does not match number of invalid tokens (" + invalTokensB + ")");
        }

        return new AlgorithmResults(ab, a, b);
    }

    void addLinesToMap(TokenList lines, Map<String, List<SubmissionLine>> lineDatabase, Submission submitter,
                       MessageDigest hasher) {
        for(int i = 0; i < lines.size(); i++) {
            Token token = lines.get(i);

            String hash = Hex.encodeHexString(hasher.digest(token.getTokenAsString().getBytes()));

            if(lineDatabase.get(hash) == null) {
                lineDatabase.put(hash, new ArrayList<>());
            }

            SubmissionLine line = new SubmissionLine(i, submitter);
            lineDatabase.get(hash).add(line);
        }
    }

    @Override
    public String toString() {
        return "Sole instance of the Line Similarity Counter algorithm";
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof LineSimilarityChecker;
    }
}
