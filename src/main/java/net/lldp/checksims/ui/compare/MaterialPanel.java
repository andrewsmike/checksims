package net.lldp.checksims.ui.compare;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MaterialPanel extends JPanel
{
    private final DropShadowPanel parent = new DropShadowPanel();
    
    public MaterialPanel()
    {
        parent.add(this, BorderLayout.NORTH);
        parent.setDistance(1);
    }
    
    public JComponent getMaterialParent()
    {
        JScrollPane jsp = new JScrollPane(parent);
        jsp.setOpaque(false);
        jsp.getViewport().setOpaque(false);
        jsp.setBorder(BorderFactory.createEmptyBorder());
        jsp.setViewportBorder(null);
        jsp.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
        jsp.getHorizontalScrollBar().setPreferredSize(new Dimension(0,0));
        return jsp;
    }
}
