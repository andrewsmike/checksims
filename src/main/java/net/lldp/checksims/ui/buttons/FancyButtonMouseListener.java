package net.lldp.checksims.ui.buttons;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

public class FancyButtonMouseListener implements MouseListener
{
    private final FancyButtonAction action;
    private final FancyButtonColorTheme colors;
    
    public FancyButtonMouseListener(JComponent on, FancyButtonAction action, FancyButtonColorTheme colors)
    {
        this.action = action;
        this.colors = colors;
        on.setOpaque(true);
        mouseExited(new MouseEvent(on, 0, 0, 0, 0, 0, 0, false));
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
        c.setBackground(colors.getHoverBackground());
        c.setForeground(colors.getHoverFont());
        c.repaint();
    }

    @Override
    public void mouseExited(MouseEvent me)
    {
        JComponent c = (JComponent) me.getSource();
        c.setForeground(colors.getUnhoverFont());
        c.setBackground(colors.getUnhoverBackground());
        c.repaint();
    }

    @Override
    public void mousePressed(MouseEvent me)
    {
        JComponent c = (JComponent) me.getSource();
        c.setBackground(colors.getPressBackground());
        c.setForeground(colors.getPressFont());
    }

    @Override
    public void mouseReleased(MouseEvent me)
    {
        JComponent c = (JComponent) me.getSource();
        c.setBackground(colors.getUnhoverBackground());
        c.setForeground(colors.getUnhoverFont());
    }

}
