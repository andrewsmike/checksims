package net.lldp.checksims.algorithm.syntaxtree.c;

import org.apache.commons.lang3.tuple.Pair;

import net.lldp.checksims.algorithm.AlgorithmResults;
import net.lldp.checksims.algorithm.SimilarityDetector;
import net.lldp.checksims.algorithm.syntaxtree.ASTSimilarityDetector;
import net.lldp.checksims.parse.SubmissionPercentableCalculator;
import net.lldp.checksims.parse.ast.AST;
import net.lldp.checksims.parse.ast.SubmissionParser;
import net.lldp.checksims.submission.Submission;

public class CParser extends SimilarityDetector<AST>
{
    public static CParser getInstance()
    {
        return new CParser(); // TODO: make a singleton later?
    }
    
    @Override
    public String getName()
    {
        return "cparser";
    }

    @Override
    public SubmissionPercentableCalculator<AST> getPercentableCalculator()
    {
        return new SubmissionParser(new CSyntaxParser());
    }

    @Override
    public AlgorithmResults detectSimilarity(Pair<Submission, Submission> ab, AST rft, AST comt)
    {
        return ASTSimilarityDetector.detectSimilarity(ab, rft, comt);
    }
    
    @Override
    public String getDefaultGlobPattern()
    {
        return "*.c";
    }
}
