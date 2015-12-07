package net.lldp.checksims.ui.results;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.ui.HSLColor;

public class SortableMatrixViewer extends JPanel
{
    private final SortableMatrix sm;
    private final ResultsInspector ins;

    private Map<Submission, List<MatrixElement>> highlightSelectorRows;
    private Map<Submission, List<MatrixElement>> highlightSelectorColumns;
    public static final int DEFAULT_ELEMENT_SIZE = 80;
    
    
    public SortableMatrixViewer(SortableMatrix sm)
    {
        this.sm = sm;
        ins = new ResultsInspector(){
            @Override
            public void handleResults(PairScore ps)
            {
                SubmissionPair sp = ps.getSubmissions();
                String students = sp.getBName() + " // " + sp.getAName();
                JOptionPane.showMessageDialog(null,
                        "<html>Comparison for students: <br> "
                                +students+"<br>matched at "
                                +ps.getScore()*100+"% :: "+ps.getInverseScore()*100
                                +"%</html>", students, JOptionPane.INFORMATION_MESSAGE);
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
        highlightSelectorColumns = new HashMap<>();
        highlightSelectorRows = new HashMap<>();
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
                    if (highlightSelectorColumns.get(subs[i]) == null)
                    {
                        highlightSelectorColumns.put(subs[i], new LinkedList<>());
                    }
                    if (highlightSelectorRows.get(subs[j]) == null)
                    {
                        highlightSelectorRows.put(subs[j], new LinkedList<>());
                    }
                    MatrixElement res = new MatrixElement(sm.getPairForSubmissions(subs[i], subs[j]), ins);
                    add(res);
                    highlightSelectorColumns.get(subs[i]).add(res);
                    highlightSelectorRows.get(subs[j]).add(res);
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
                this.setToolTipText(ps.getFormattedSubmissions());
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

        public void setVerticalHighlight(boolean b)
        {
            if (b)
            {
                this.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.red),
                        BorderFactory.createLineBorder(Color.black)));
            }
            else
            {
                this.setBorder(null);
            }
        }
    }
    
    public static class BlankMatrixElement extends MatrixElement
    {
        public BlankMatrixElement()
        {
            super(null, null);
            setBackground(Color.BLACK);
        }
    }

    public void highlightMatching(String textA)
    {
        for(Submission k : highlightSelectorColumns.keySet())
        {
            highlightSelectorColumns.get(k).stream().forEach(ME -> {
                ME.setVerticalHighlight(false);
            });
        }
        
        for(Submission k : highlightSelectorColumns.keySet())
        {
            if (k.getName().contains(textA))
            {
                highlightSelectorColumns.get(k).stream().forEach(ME -> {
                    ME.setVerticalHighlight(true);
                });
                highlightSelectorRows.get(k).stream().forEach(ME -> {
                    ME.setVerticalHighlight(true);
                });
            }
        }
    }
    
    public void highlightMatching(String textA, String textB)
    {
        for(Submission k : highlightSelectorColumns.keySet())
        {
            highlightSelectorColumns.get(k).stream().forEach(ME -> {
                ME.setVerticalHighlight(false);
            });
        }
        
        
        for(Submission k : highlightSelectorColumns.keySet())
        {
            if (k.getName().contains(textA))
            {
                for(MatrixElement me : highlightSelectorColumns.get(k))
                {
                    if (me.ps.getSubmissions().getBName().contains(textB))
                    {
                        me.setVerticalHighlight(true);
                    }
                }
            }
        }
        
        for(Submission k : highlightSelectorColumns.keySet())
        {
            if (k.getName().contains(textB))
            {
                for(MatrixElement me : highlightSelectorColumns.get(k))
                {
                    if (me.ps.getSubmissions().getBName().contains(textA))
                    {
                        me.setVerticalHighlight(true);
                    }
                }
            }
        }
    }
}
