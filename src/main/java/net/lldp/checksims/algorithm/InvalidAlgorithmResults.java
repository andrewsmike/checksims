package net.lldp.checksims.algorithm;

import org.apache.commons.lang3.tuple.Pair;

import net.lldp.checksims.parse.Percentable;
import net.lldp.checksims.submission.Submission;

public class InvalidAlgorithmResults extends AlgorithmResults
{

    public InvalidAlgorithmResults(Pair<Submission, Submission> ab,
                                   Percentable a, Percentable b,
                                   SimilarityDetector<? extends Percentable> algorithm)
    {
        super(ab, a, b, algorithm);
    }

    @Override
    public boolean isValid()
    {
        return false;
    }
}