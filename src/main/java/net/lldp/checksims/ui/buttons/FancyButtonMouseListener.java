/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License (the "License").
 * You may not use this file except in compliance with the License.
 *
 * See LICENSE.txt included in this distribution for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at LICENSE.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 * Copyright (c) 2014-2016 Ted Meyer, Nicholas DeMarinis, Matthew Heon, and Dolan Murvihill
 */
package net.lldp.checksims.ui.buttons;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

/**
 * For only 60 easy payments of $39.99, you can turn any JComponent into a button!
 * @author ted
 *
 */
public class FancyButtonMouseListener implements MouseListener
{
    private final FancyButtonAction action;
    private final FancyButtonColorTheme colors;
    
    /**
     * @param on the component to turn in
     * @param action the action listener
     * @param colors the color theme of the button
     */
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
