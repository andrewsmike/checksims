package net.lldp.checksims.ui.results;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.ui.HSLColor;

public class SortableMatrixViewer extends JPanel
{
    private final SortableMatrix sm;
    private final ResultsInspector ins;
    
    public static final int DEFAULT_ELEMENT_SIZE = 80;
    
    
    public SortableMatrixViewer(SortableMatrix sm)
    {
        this.sm = sm;
        ins = new ResultsInspector(){
            @Override
            public void handleResults(PairScore ps)
            {
                SubmissionPair sp = ps.getSubmissions();
                System.out.println(sp.getAName());
                System.out.println(sp.getBName());
            }
        };
        int size = updateMatrix(70) * DEFAULT_ELEMENT_SIZE;
        setPreferredSize(new Dimension(size, size));
    }
    
    public void updateThreshold(double d)
    {
        int size = updateMatrix(d) * DEFAULT_ELEMENT_SIZE;
        setPreferredSize(new Dimension(size, size));
    }
    
    private int updateMatrix(double d)
    {
        Submission[] subs = sm.getSubmissionsAboveThreshold(d);
        removeAll();
        if (subs.length == 0) {
            setLayout(new GridLayout(1,1));
            add(new BlankMatrixElement());
        } else {
            setLayout(new GridLayout(subs.length, subs.length));
        }
        
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
                    add(new MatrixElement(sm.getPairForSubmissions(subs[i], subs[j]), ins));
                }
            }
        }
        return subs.length;
    }
    
    public static class MatrixElement extends JPanel implements MouseListener
    {
        private final PairScore ps;
        private final ResultsInspector ri;
        
        public MatrixElement(PairScore ps, ResultsInspector ri)
        {
            this.ps = ps;
            this.ri = ri;
            if (ps != null)
            {
                JLabel l = new JLabel(((int)(ps.getScore()*100))+"", SwingConstants.CENTER);
                this.add(l, BorderLayout.CENTER);
                
                HSLColor hsl = new HSLColor(
                        (float) ((1.0 - ps.getScore()) * 60)
                       ,(float) (ps.getScore() * 100)
                       ,(float) (100 - (ps.getScore() * 50))
                );
                setBackground(hsl.getRGB());
                l.setForeground(Color.BLACK);
                this.addMouseListener(this);
            }
        }

        @Override
        public void mouseClicked(MouseEvent me)
        {
            ri.handleResults(ps);
        }

        @Override
        public void mouseEntered(MouseEvent arg0)
        { }

        @Override
        public void mouseExited(MouseEvent arg0)
        { }

        @Override
        public void mousePressed(MouseEvent arg0)
        { }

        @Override
        public void mouseReleased(MouseEvent arg0)
        { }
    }
    
    public static class BlankMatrixElement extends MatrixElement
    {
        public BlankMatrixElement()
        {
            super(null, null);
            setBackground(Color.BLACK);
        }
    }    
}
