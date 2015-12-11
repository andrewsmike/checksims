package net.lldp.checksims.ui.results;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class ScrollViewer extends JPanel
{
    private final JScrollPane resultsView;
    private final JPanel sidebar;
    
    public ScrollViewer(SortableMatrixViewer results, JFrame toRevalidate)
    {
        resultsView = new JScrollPane(results);
        resultsView.addComponentListener(new ComponentListener(){

            @Override
            public void componentHidden(ComponentEvent arg0)
            { }

            @Override
            public void componentMoved(ComponentEvent arg0)
            { }

            @Override
            public void componentResized(ComponentEvent ce)
            {
                Dimension size = ce.getComponent().getSize();
                results.padToSize(size);
            }

            @Override
            public void componentShown(ComponentEvent arg0)
            { }
        });
        resultsView.setBackground(Color.black);
        sidebar = new JPanel();
        
        setPreferredSize(new Dimension(900, 631));
        setMinimumSize(new Dimension(900, 631));
        sidebar.setPreferredSize(new Dimension(200, 631));
        sidebar.setMaximumSize(new Dimension(200, 3000));
        resultsView.setMinimumSize(new Dimension(700, 631));
        resultsView.setPreferredSize(new Dimension(700, 631));
        
        sidebar.setBackground(Color.GRAY);
        
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        
        this.add(sidebar);
        this.add(resultsView);
        
        resultsView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        resultsView.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        resultsView.getVerticalScrollBar().setUnitIncrement(16);
        resultsView.getHorizontalScrollBar().setUnitIncrement(16);
        
        Integer[] presetThresholds = {90, 85, 80, 75, 70, 65, 60, 55, 50, 45, 40, 35, 30, 0};
        JComboBox<Integer> threshHold = new JComboBox<Integer>(presetThresholds);
        threshHold.setSelectedIndex(8);
        threshHold.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                   Integer item = (Integer)event.getItem();
                   results.updateThreshold(item / 100.0);
                   toRevalidate.revalidate();
                   toRevalidate.repaint();
                }
             }
        });
        
        JTextField student1 = new JTextField(15);
        JTextField student2 = new JTextField(15);
        
        KeyListener search = new KeyListener(){

            @Override
            public void keyPressed(KeyEvent e)
            { }

            @Override
            public void keyReleased(KeyEvent e)
            {
                if (student1.getText().length() > 0 && student2.getText().length() > 0)
                {
                    results.highlightMatching(student1.getText(), student2.getText());
                    toRevalidate.revalidate();
                    toRevalidate.repaint();
                }
                else if (student1.getText().length() > 0)
                {
                    results.highlightMatching(student1.getText());
                    toRevalidate.revalidate();
                    toRevalidate.repaint();
                }
                else if (student2.getText().length() > 0)
                {
                    results.highlightMatching(student2.getText());
                    toRevalidate.revalidate();
                    toRevalidate.repaint();
                }
            }

            @Override
            public void keyTyped(KeyEvent e)
            { }
            
        };
        
        student1.addKeyListener(search);
        student2.addKeyListener(search);
        
        sidebar.add(threshHold);
        sidebar.add(student1);
        sidebar.add(student2);
    }
}
