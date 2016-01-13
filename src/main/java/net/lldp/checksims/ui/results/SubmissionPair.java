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

import net.lldp.checksims.submission.Submission;

/**
 * 
 * @author ted
 *
 */
public class SubmissionPair
{
    private final Submission a, b;
    
    /**
     * Create a submission pair for two submissions
     * @param a one submission
     * @param b another submission
     */
    public SubmissionPair(Submission a, Submission b)
    {
        this.a = a;
        this.b = b;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((a == null) ? 0 : a.hashCode());
        result = prime * result + ((b == null) ? 0 : b.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SubmissionPair other = (SubmissionPair) obj;
        if (a == null)
        {
            if (other.a != null)
                return false;
        } else if (!a.equals(other.a))
            return false;
        if (b == null)
        {
            if (other.b != null)
                return false;
        } else if (!b.equals(other.b))
            return false;
        return true;
    }

    /**
     * @return the name of the first submission
     */
    public String getAName()
    {
        return a.getName();
    }
    
    /**
     * @return the name of the second submission
     */
    public String getBName()
    {
        return b.getName();
    }

    /**
     * TODO: make this take a formatter
     * @return a formatted submission string
     */
    public String getFormattedSubmissions()
    {
        return "<html>" + a.getName() + ": " +a.getMaximumCopyScore()+ "<br>" + b.getName() + ": " +b.getMaximumCopyScore()+ "</html>";
    }

    /**
     * @return the first submission
     */
    public Submission getA()
    {
        return a;
    }
    
    /**
     * @return the second submission
     */
    public Submission getB()
    {
        return b;
    }
}
