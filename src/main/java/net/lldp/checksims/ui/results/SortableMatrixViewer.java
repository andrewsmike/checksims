package net.lldp.checksims.ui.results;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.util.data.Monad;
import static net.lldp.checksims.util.data.Monad.wrap;
import static net.lldp.checksims.util.data.Monad.unwrap;

public class SortableMatrixViewer extends JPanel
{
    public final Monad<Double> threshold = wrap(0.7);
    private final SortableMatrix sm;
    
    
    public SortableMatrixViewer(SortableMatrix sm)
    {
        this.sm = sm;
        updateMatrix();
    }
    
    public void updateThreshold(double d)
    {
        threshold.set(d);
        //updateMatrix(); //TODO fix this
    }
    
    private void updateMatrix()
    {
        Submission[] subs = sm.getSubmissionsAboveThreshold(unwrap(threshold));
        removeAll();
        setLayout(new GridLayout(subs.length, subs.length));
        
        for(int i=0; i<subs.length; i++)
        {
            for(int j=0; j<subs.length; j++)
            {
                if (j == i)
                {
                    add(new BlankMatrixElement());
                }
                else
                {
                    add(new MatrixElement(sm.getPairForSubmissions(subs[i], subs[j])));
                }
            }
        }
    }
    
    public static class MatrixElement extends JPanel
    {
        private final PairScore ps;
        
        public MatrixElement(PairScore ps)
        {
            this.ps = ps;
            if (ps != null)
            {
                JLabel l = new JLabel(((int)(ps.getScore()*100))+"");
                this.add(l, BorderLayout.CENTER);
                Color bg = new Color(
                        (int)(255*ps.getScore())
                       ,255-(int)(60*ps.getScore())
                       ,(int)(255-(255*ps.getScore()))
                );
                setBackground(bg);
            }
        }

        public PairScore getPs()
        {
            return ps;
        }
    }
    
    public static class BlankMatrixElement extends MatrixElement
    {
        public BlankMatrixElement()
        {
            super(null);
            setBackground(Color.BLACK);
        }
    }    
}
