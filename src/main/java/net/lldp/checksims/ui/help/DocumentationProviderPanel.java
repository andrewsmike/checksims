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
package net.lldp.checksims.ui.help;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.lldp.checksims.ui.lib.Stripe;

/**
 * A subtype of JPanel which can be used to represent locations where help can occur
 * @author ted
 *
 */
public abstract class DocumentationProviderPanel extends JPanel implements DocumentationProvider, MouseListener
{
    private boolean helpMode = false;
    private boolean helpDrawing = false;
    static final int STRIPE_SPACING = 20;
    
    /**
     * default constructor that gets called on every instantiation
     */
    public DocumentationProviderPanel()
    {
        this.addMouseListener(this);
        DocumentationProviderRegistry.addSelf(this);
    }

    @Override
    public void enableHelpMode()
    {
        helpMode = true;
    }

    @Override
    public void disableHelpMode()
    {
        helpMode = false;
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        if (helpMode)
        {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            int stripes = (2 * Math.max(getWidth(), getHeight())) / (STRIPE_SPACING);
            for(int i = 0; i < stripes; i++)
            {
                g.setColor(getColor(((float)i) / ((float)stripes)));
                Stripe str = new Stripe(1, getHeight() - i*STRIPE_SPACING);
                int[] l = str.boundBy(0, 0, getWidth(), getHeight());
                g2.draw(new Line2D.Float(l[0], l[1], l[2], l[3]));
            }
            g2.setColor(Color.red);
            g2.drawRect(1, 1, getWidth()-1, getHeight()-1);
        }
    }
    
    private Color getColor(float i)
    {
        if (helpDrawing)
        {
            return new Color((int)(255 * i), (int)(255 - (255 * i)), 0, 128);
        }
        else
        {
            return new Color(128, 128, 128, 32);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        JOptionPane.showMessageDialog(null, this.getMessageContents());
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        if (helpMode)
        {
            ;
        }
        helpDrawing = true;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent arg0)
    {
        helpDrawing = false;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent arg0)
    { }

    @Override
    public void mouseReleased(MouseEvent arg0)
    { }

    @Override
    public boolean isHelpEnabled()
    {
        return helpMode;
    }
}
