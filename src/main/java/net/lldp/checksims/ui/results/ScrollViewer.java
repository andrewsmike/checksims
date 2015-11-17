package net.lldp.checksims.ui.results;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ScrollViewer extends JPanel implements ChangeListener
{
    private final JScrollPane resultsView;
    private final JPanel sidebar;
    private final SortableMatrixViewer results;
    private final JFrame toRevalidate;
    
    public ScrollViewer(SortableMatrixViewer results, JFrame toRevalidate)
    {
        this.results = results;
        this.toRevalidate = toRevalidate;
        
        resultsView = new JScrollPane(results);
        sidebar = new JPanel();
        
        setPreferredSize(new Dimension(900, 631));
        sidebar.setPreferredSize(new Dimension(200, 631));
        resultsView.setPreferredSize(new Dimension(700, 631));
        
        sidebar.setBackground(Color.GRAY);
        
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        
        this.add(sidebar);
        this.add(resultsView);
        
        resultsView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        resultsView.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        resultsView.getVerticalScrollBar().setUnitIncrement(16);
        
        JSlider threshHold = new JSlider(JSlider.VERTICAL, 0, 100, 70);
        
        threshHold.addChangeListener(this);
        threshHold.setMajorTickSpacing(10);
        threshHold.setPaintTicks(true);
        
        sidebar.add(threshHold);
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            float thresh = (float)source.getValue();
            results.updateThreshold(thresh / 100.0);
            toRevalidate.revalidate();
            toRevalidate.repaint();
        }
    }
}
