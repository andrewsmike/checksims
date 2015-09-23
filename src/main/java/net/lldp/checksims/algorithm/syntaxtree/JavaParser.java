package net.lldp.checksims.algorithm.syntaxtree;

import org.apache.commons.lang3.tuple.Pair;

import net.lldp.checksims.algorithm.AlgorithmResults;
import net.lldp.checksims.algorithm.InternalAlgorithmError;
import net.lldp.checksims.algorithm.SimilarityDetector;
import net.lldp.checksims.parse.Real;
import net.lldp.checksims.parse.SubmissionPercentableCalculator;
import net.lldp.checksims.parse.ast.PercentableAST;
import net.lldp.checksims.parse.ast.SubmissionParser;
import net.lldp.checksims.parse.token.TokenTypeMismatchException;
import net.lldp.checksims.submission.Submission;

public class JavaParser implements SimilarityDetector<PercentableAST>
{
    public static JavaParser getInstance()
    {
        return new JavaParser(); // TODO: make a singleton later?
    }
    
    @Override
    public String getName()
    {
        return "Java Specific Syntax Tree Builder";
    }

    @Override
    public SubmissionPercentableCalculator<PercentableAST> getPercentableCalculator()
    {
        return new SubmissionParser(new JavaSyntaxParser());
    }

    @Override
    public AlgorithmResults detectSimilarity(Pair<Submission, Submission> ab, PercentableAST rft, PercentableAST comt)
            throws TokenTypeMismatchException, InternalAlgorithmError
    {
        
        Real atb = rft.getPercent(comt);
        Real bta = comt.getPercent(rft);
        
        return new AlgorithmResults(ab, atb, bta);
    }

}
