package net.lldp.checksims.ui.results;

public class PairScore
{
    private final SubmissionPair submissions;
    private final double score;
    private final double inverseScore;
    
    public PairScore(SubmissionPair subs, double score, double inverse)
    {
        this.submissions = subs;
        this.score = score;
        this.inverseScore = inverse;
    }

    public SubmissionPair getSubmissions()
    {
        return submissions;
    }

    public double getScore()
    {
        return score;
    }

    public double getInverseScore()
    {
        return inverseScore;
    }
}
