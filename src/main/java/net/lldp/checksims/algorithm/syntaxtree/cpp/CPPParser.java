package net.lldp.checksims.algorithm.syntaxtree.cpp;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.BiMap;

import net.lldp.checksims.algorithm.AlgorithmResults;
import net.lldp.checksims.algorithm.InternalAlgorithmError;
import net.lldp.checksims.algorithm.SimilarityDetector;
import net.lldp.checksims.util.data.Range;
import net.lldp.checksims.util.data.Real;
import net.lldp.checksims.parse.SubmissionPercentableCalculator;
import net.lldp.checksims.parse.ast.AST;
import net.lldp.checksims.parse.ast.SubmissionParser;
import net.lldp.checksims.parse.token.TokenTypeMismatchException;
import net.lldp.checksims.submission.Submission;

public class CPPParser extends SimilarityDetector<AST>
{
    public static CPPParser getInstance()
    {
        return new CPPParser(); // TODO: make a singleton later?
    }

    @Override
    public String getName()
    {
        return "cppparser";
    }

    @Override
    public SubmissionPercentableCalculator<AST> getPercentableCalculator()
    {
        return new SubmissionParser(new CPPSyntaxParser());
    }

    @Override
    public AlgorithmResults detectSimilarity(Pair<Submission, Submission> ab, AST rft, AST comt)
            throws TokenTypeMismatchException, InternalAlgorithmError
    {
        Real atb = rft.getPercentMatched(comt.getFingerprints());
        Real bta = comt.getPercentMatched(rft.getFingerprints());
        //System.out.println("Similarity: " + atb);
        return new AlgorithmResults(ab, atb, bta, this);
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
        return "*.cpp";
    }
}
