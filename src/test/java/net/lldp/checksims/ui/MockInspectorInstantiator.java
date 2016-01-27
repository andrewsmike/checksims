package net.lldp.checksims.ui;

import org.junit.Test;

import net.lldp.checksims.submission.ConcreteSubmission;
import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.ui.compare.DetailedResultsInspector;
import net.lldp.checksims.ui.results.PairScore;
import net.lldp.checksims.ui.results.SubmissionPair;

public class MockInspectorInstantiator
{

    @Test
    public void test()
    {
        Submission A = new ConcreteSubmission("A_very_long_localized_name", "SUGAR CONTENT");
        Submission B = new ConcreteSubmission("Another Very Long Name", "SUGAR CONTENT");
        
        PairScore ps = new PairScore(new SubmissionPair(A, B), 0.855, 0.843);
        
        new DetailedResultsInspector().handleResults(ps);
        
        synchronized(ps)
        {
            try
            {
                ps.wait();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

}
