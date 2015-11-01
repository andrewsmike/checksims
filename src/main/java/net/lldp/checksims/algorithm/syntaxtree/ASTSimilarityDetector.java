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
        
        if(a > .99) {
            ab.getRight().increaseScore(10);
        } else if (a > .8) {
            ab.getRight().increaseScore(5);
        } else if (a > .7) {
            ab.getRight().increaseScore(1);
        }
        
        if(b > .99) {
            ab.getLeft().increaseScore(10);
        } else if (b > .8) {
            ab.getLeft().increaseScore(5);
        } else if (b > .7) {
            ab.getLeft().increaseScore(1);
        }

        ab.getLeft().increaseScore(b / 10);
        ab.getRight().increaseScore(a / 10);
        
        return new AlgorithmResults(ab, atb, bta);
    }
}
