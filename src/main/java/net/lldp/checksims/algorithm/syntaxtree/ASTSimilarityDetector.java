package net.lldp.checksims.algorithm.syntaxtree;

import org.apache.commons.lang3.tuple.Pair;

import net.lldp.checksims.algorithm.AlgorithmResults;
import net.lldp.checksims.algorithm.InvalidAlgorithmResults;
import net.lldp.checksims.parse.ast.AST;
import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.util.data.Real;

public abstract class ASTSimilarityDetector
{
    /**
     * see #SimilarityDetector.detectSimilarity
     */
    public static AlgorithmResults detectSimilarity(Pair<Submission, Submission> ab, AST rft, AST comt) {
        if (ab.getLeft().testFlag("invalid") || ab.getRight().testFlag("invalid"))
        {
            return new InvalidAlgorithmResults(ab, new Real(0), new Real(0));
        }
        Real atb = rft.getPercentMatched(comt.getFingerprints());
        Real bta = comt.getPercentMatched(rft.getFingerprints());

        double b = atb.asDouble();
        double a = bta.asDouble();
        
        double d_tmp = a;
        Submission s_tmp = ab.getRight();
        s_tmp.increaseScore(0, d_tmp);
        s_tmp.increaseScore(Math.pow(5, d_tmp*10), d_tmp);
        /*
        if(d_tmp > .99) {
            s_tmp.increaseScore(100000, d_tmp);
        } else if (d_tmp > .9){
            s_tmp.increaseScore(10000, d_tmp);
        } else if (d_tmp > .8) {
            s_tmp.increaseScore(1000, d_tmp);
        } else if (d_tmp > .7) {
            s_tmp.increaseScore(100, d_tmp);
        } else if (d_tmp > .6) {
            s_tmp.increaseScore(10, d_tmp);
        }
        */
        
        d_tmp = b;
        s_tmp = ab.getLeft();
        s_tmp.increaseScore(0, d_tmp);
        s_tmp.increaseScore(Math.pow(5, d_tmp*10), d_tmp);
        /*
        if(d_tmp > .99) {
            s_tmp.increaseScore(100000, d_tmp);
        } else if (d_tmp > .9){
            s_tmp.increaseScore(10000, d_tmp);
        } else if (d_tmp > .8) {
            s_tmp.increaseScore(1000, d_tmp);
        } else if (d_tmp > .7) {
            s_tmp.increaseScore(100, d_tmp);
        } else if (d_tmp > .6) {
            s_tmp.increaseScore(10, d_tmp);
        }
        */
        
        return new AlgorithmResults(ab, atb, bta);
    }
}
