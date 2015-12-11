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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.ui.results.color.ColorGenerationAlgorithm;
import net.lldp.checksims.ui.results.color.OrangeGreenColorGenerationAlgorithm;
import net.lldp.checksims.ui.results.color.RedWhiteColorGenerationAlgorithm;
import net.lldp.checksims.util.data.Monad;

import static net.lldp.checksims.util.data.Monad.unwrap;
import static net.lldp.checksims.util.data.Monad.wrap;

public class SortableMatrixViewer extends JPanel
{
    private final SortableMatrix sm;
    private final ResultsInspector ins;

    private Map<Submission, List<MatrixElement>> highlightSelectorRows;
    private Map<Submission, List<MatrixElement>> highlightSelectorColumns;
    public static final int DEFAULT_ELEMENT_SIZE = 80;
    
    private Dimension preferredElementCount = new Dimension(1, 1);
    private double threshold;
    
    
    public SortableMatrixViewer(SortableMatrix sm)
    {
        this.sm = sm;
        ins = new ResultsInspector(){
            @Override
            public void handleResults(PairScore ps)
            {
                SubmissionPair sp = ps.getSubmissions();
                new IndividualInspectorWindow(sp.getA(), sp.getB(), ps);
            }
        };
        int size = updateMatrix(50) * DEFAULT_ELEMENT_SIZE; //TODO make this match default option
        setPreferredSize(new Dimension(size, size));
    }
    
    public void updateThreshold(double d)
    {
        threshold = d;
        int size = updateMatrix(d) * DEFAULT_ELEMENT_SIZE;
        setPreferredSize(new Dimension(size, size));
    }
    
    private int updateMatrix(double d)
    {
        highlightSelectorColumns = new HashMap<>();
        highlightSelectorRows = new HashMap<>();
        Submission[] subs = sm.getSubmissionsAboveThreshold(d);
        removeAll();
        int minWidth = subs.length;
        int minHeight = subs.length;
        if (preferredElementCount.width > minWidth)
        {
            minWidth = preferredElementCount.width;
        }
        if (preferredElementCount.height > minHeight)
        {
            minHeight = preferredElementCount.height;
        }
        setLayout(new GridLayout(minHeight, minWidth));
        
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
            if (subs.length < preferredElementCount.width)
            {
                for(int j=0; j<preferredElementCount.width-subs.length; j++)
                {
                    add(new BlankMatrixElement());
                }
            }
        }
        if (subs.length < preferredElementCount.height)
        {
            for(int j=0; j<(preferredElementCount.height-subs.length)*preferredElementCount.width; j++)
            {
                add(new BlankMatrixElement());
            }
        }
        return subs.length;
    }
    
    public static class MatrixElement extends JPanel implements MouseListener
    {
        private final PairScore ps;
        private final ResultsInspector ri;
        private final Monad<ColorGenerationAlgorithm> color;
        
        public MatrixElement(PairScore ps, ResultsInspector ri)
        {
            this.ps = ps;
            this.ri = ri;
            color = wrap(new RedWhiteColorGenerationAlgorithm());
            if (ps != null)
            {
                JLabel l = new JLabel(((int)(ps.getScore()*100))+"", SwingConstants.CENTER);
                this.add(l, BorderLayout.CENTER);
                setBackground(unwrap(color).getColorFromScore(ps.getScore()));
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
                this.color.set(new OrangeGreenColorGenerationAlgorithm());
            }
            else
            {
                this.color.set(new RedWhiteColorGenerationAlgorithm());
            }
            setBackground(unwrap(color).getColorFromScore(ps.getScore()));
        }

        public void setHorizontalHighlight(boolean b)
        {
            this.setVerticalHighlight(b);
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
                    ME.setHorizontalHighlight(true);
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

    public void padToSize(Dimension size)
    {
        Dimension nec = new Dimension(
                (int)size.getWidth() / DEFAULT_ELEMENT_SIZE,
                (int)size.getHeight() / DEFAULT_ELEMENT_SIZE
        );
        
        if (preferredElementCount.height != nec.height || preferredElementCount.width != nec.width)
        {
            preferredElementCount = nec;
            updateThreshold(threshold);
        }
    }
}
