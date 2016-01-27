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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import net.lldp.checksims.parse.Percentable;

/**
 * Concrete implementation of the Submission interface.
 *
 * Intended to be the only concrete implementation of Submission that is not a decorator.
 */
public final class ConcreteSubmission implements Submission {
    private final Set<String> flags = new HashSet<>();
    private final String content;
    private final String name;
    private final Map<Class<? extends Percentable>, Percentable> parsedTypes = new HashMap<>();
    private double sortingScore;
    private double maximumScore;
    
    
    /**
     * Construct a new Concrete Submission with given name and contents.
     *
     * Token content should be the result of tokenizing the string content of the submission with some tokenizer. This
     * invariant is maintained throughout the project, but not enforced here for performance reasons. It is thus
     * possible to create a ConcreteSubmission with Token contents not equal to tokenized String contents. This is not
     * recommended and will most likely break, at the very least, Preprocessors.
     *
     * @param name Name of new submission
     * @param content Content of submission, as string
     * @param tokens Content of submission, as token
     */
    public ConcreteSubmission(String name, String content) {
        checkNotNull(name);
        checkArgument(!name.isEmpty(), "Submission name cannot be empty");
        checkNotNull(content);

        this.name = name;
        this.content = content;
        this.sortingScore = 0;
    }

    @Override
    public String getContentAsString() {
        return content;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "A submission with name " + name + " and " + getContentAsString().length() + " bytes";
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        
        if(!(other instanceof ConcreteSubmission)) {
            return false;
        }

        Submission otherSubmission = (Submission)other;

        return otherSubmission.getName().equals(this.name)
                && otherSubmission.getContentAsString().equals(this.content);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Compare two Submissions, using natural ordering by name.
     *
     * Note that the natural ordering of ConcreteSubmission is inconsistent with equality. Ordering is based solely on
     * the name of a submission; two submissions with the same name, but different contents, will have compareTo()
     * return 0, but equals() return false
     *
     * @param other Submission to compare to
     * @return Integer indicating relative ordering of the submissions
     */
    @Override
    public int compareTo(Submission other) {
        return this.name.compareTo(other.getName());
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

    @Override
    public void invalidateCache()
    {
        parsedTypes.clear();
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
    public void increaseScore(double r, double orig)
    {
        if (orig > maximumScore)
        {
            maximumScore = orig;
        }
        sortingScore += r;
    }

    @Override
    public double getTotalCopyScore()
    {
        return sortingScore;
    }

    @Override
    public Double getMaximumCopyScore()
    {
        return maximumScore;
    }

    @Override
    public int getLinesOfCode()
    {
        return StringUtils.countMatches(getContentAsString(), "\n") + 1;
    }
}
