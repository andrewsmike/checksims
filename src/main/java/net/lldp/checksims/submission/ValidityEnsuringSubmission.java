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

package net.lldp.checksims.submission;

import net.lldp.checksims.parse.Percentable;
import net.lldp.checksims.parse.token.TokenList;
import net.lldp.checksims.parse.token.ValidityEnsuringToken;
import net.lldp.checksims.parse.token.tokenizer.Tokenizer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Submission which enforces token validity - two tokens, even if invalid, are not considered equal.
 *
 * Decorates another submission and overrides equals()
 */
public final class ValidityEnsuringSubmission extends AbstractSubmissionDecorator {

    private final Set<String> flags = new HashSet<>();
    private final Map<Class<? extends Percentable>, Percentable> parsedTypes = new HashMap<>();
    private double real = 0;
    
    public ValidityEnsuringSubmission(Submission wrappedSubmission, Tokenizer tokenizer) {
        super(wrappedSubmission, tokenizer);
    }

    @Override
    public void invalidateCache()
    {
        parsedTypes.clear();
    }
    
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Submission)) {
            return false;
        }

        AbstractSubmissionDecorator otherSubmission =
                new ValidityEnsuringSubmission((Submission) other, getTokenizer());

        if(!otherSubmission.getTokenType().equals(this.getTokenType())
                || !otherSubmission.getName().equals(this.getName())
                || !(otherSubmission.getNumTokens() == this.getNumTokens())
                || !(otherSubmission.getContentAsString().equals(this.getContentAsString()))) {
            return false;
        }

        Supplier<TokenList> tokenListSupplier = () -> new TokenList(this.getTokenType());
        TokenList thisList = this.getContentAsTokens().stream()
                .map(ValidityEnsuringToken::new)
                .collect(Collectors.toCollection(tokenListSupplier));
        TokenList otherList = otherSubmission.getContentAsTokens().stream()
                .map(ValidityEnsuringToken::new)
                .collect(Collectors.toCollection(tokenListSupplier));

        return thisList.equals(otherList);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public <T extends Percentable> void addType(Class<T> clazz, T percentable)
    {
        parsedTypes.put(clazz, percentable);
    }

    @Override
    public boolean contains(Class<? extends Percentable> clazz)
    {
        return parsedTypes.containsKey(clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Percentable> T get(Class<T> clazz)
    {
        return (T) parsedTypes.get(clazz);
    }
    
    public void setFlag(String flagName)
    {
        flags.add(flagName);
    }
    
    public void unsetFlag(String flagName)
    {
        flags.remove(flagName);
    }
    
    public boolean testFlag(String flagName)
    {
        boolean result =flags.contains(flagName);
        return result;
    }

    @Override
    public void increaseScore(double r, double o)
    {
        real += r;
    }

    @Override
    public double getTotalCopyScore()
    {
        return real;
    }

    @Override
    public Double getMaximumCopyScore()
    {
        return 0d;
    }
}
