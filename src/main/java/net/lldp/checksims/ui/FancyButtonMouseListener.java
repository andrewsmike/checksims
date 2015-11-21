package net.lldp.checksims.ui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

public class FancyButtonMouseListener implements MouseListener
{
    private final FancyButtonAction action;
    
    public FancyButtonMouseListener(FancyButtonAction action)
    {
        this.action = action;
    }

    @Override
    public void mouseClicked(MouseEvent me)
    {
        action.performAction();
    }

    @Override
    public void mouseEntered(MouseEvent me)
    {
        JComponent c = (JComponent) me.getSource();
        c.setBackground(Color.GREEN);
        c.setOpaque(true);
        c.repaint();
    }

    @Override
    public void mouseExited(MouseEvent me)
    {
        JComponent c = (JComponent) me.getSource();
        c.setOpaque(false);
        c.repaint();
    }

    @Override
    public void mousePressed(MouseEvent arg0)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent arg0)
    {
        // TODO Auto-generated method stub
        
    }

}
