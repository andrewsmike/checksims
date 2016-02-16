package net.lldp.checksims.algorithm.syntaxtree.c;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.BiMap;

import net.lldp.checksims.algorithm.AlgorithmResults;
import net.lldp.checksims.algorithm.SimilarityDetector;
import net.lldp.checksims.algorithm.syntaxtree.ASTSimilarityDetector;
import net.lldp.checksims.parse.SubmissionPercentableCalculator;
import net.lldp.checksims.parse.ast.AST;
import net.lldp.checksims.parse.ast.SubmissionParser;
import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.util.data.Range;

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
        return ASTSimilarityDetector.detectSimilarity(ab, rft, comt, this);
    }

    @Override
    public BiMap<Range,Range> getRegionMappings(AlgorithmResults res)
    {
        SubmissionPercentableCalculator<AST> parser = getPercentableCalculator();
        AST l = parser.generateFromSubmission(res.a);
        AST r = parser.generateFromSubmission(res.b);

        return l.getRegionMappings(r);
    }

    @Override
    public String getDefaultGlobPattern()
    {
        return "*.c";
    }
}
