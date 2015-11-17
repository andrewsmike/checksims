package net.lldp.checksims.ui.results;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.lldp.checksims.algorithm.similaritymatrix.SimilarityMatrix;
import net.lldp.checksims.submission.Submission;

public class SortableMatrix
{
    private final Submission[] submissions;
    private final Map<SubmissionPair, PairScore> scores;
    
    public SortableMatrix(SimilarityMatrix matrix)
    {
        Set<Submission> submissions = new HashSet<>();
        scores = new HashMap<>();
        
        matrix.getBaseResults().forEach(AR -> {
            submissions.add(AR.a);
            submissions.add(AR.b);
            
            SubmissionPair a2b = new SubmissionPair(AR.a, AR.b);
            SubmissionPair b2a = new SubmissionPair(AR.b, AR.a);
            
            PairScore a2bPS = new PairScore(a2b, AR.percentMatchedA().asDouble(), AR.percentMatchedB().asDouble());
            PairScore b2aPS = new PairScore(b2a, AR.percentMatchedB().asDouble(), AR.percentMatchedA().asDouble());

            scores.put(a2b, a2bPS);
            scores.put(b2a, b2aPS);
        });
        
        this.submissions = new Submission[submissions.size()];
        int counter = 0;
        for(Submission s : submissions)
        {
            this.submissions[counter++] = s;
        }
        
        Arrays.sort(this.submissions, new Comparator<Submission>(){
            @Override
            public int compare(Submission o1, Submission o2)
            {
                return (int) ((o1.getMaximumCopyScore() - o2.getMaximumCopyScore()) * 1000);
            }
        });
    }
    
    private int getLowestIndexAbove(double score)
    {
        int mod = submissions.length;
        int check = 0;
        
        double[] r = new double[mod];
        for(int i=0;i<mod;i++)r[i]=submissions[i].getMaximumCopyScore();
        
        while(mod > 0 && check > 0)
        {
            double max = submissions[check].getMaximumCopyScore();
            if (score > max)
            {
                check += (mod /= 2);
            }
            else if (score < max)
            {
                check -= (mod /= 2);
            }
            else
            {
                return check;
            }
        }
        return check>0?check+1:0; //TODO maybe -1? or +1?
    }
    
    public Submission[] getSubmissionsAboveThreshold(double thresh)
    {
        int indx = getLowestIndexAbove(thresh);
        Submission[] result = new Submission[submissions.length - indx];
        for(int i=0; i<result.length; i++)
        {
            result[i] = submissions[indx+i];
        }
        
        Arrays.sort(result, new Comparator<Submission>(){
            @Override
            public int compare(Submission o1, Submission o2) {
                return (int) ((o2.getTotalCopyScore() - o1.getTotalCopyScore())*1000);
            }
        });
        
        return result;
    }
    
    public PairScore getPairForSubmissions(Submission a, Submission b)
    {
        return getPairForSubmissions(new SubmissionPair(a, b));
    }
    
    public PairScore getPairForSubmissions(SubmissionPair sp)
    {
        return scores.get(sp);
    }
}
