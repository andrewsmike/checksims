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
package net.lldp.checksims.ui.lib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import net.lldp.checksims.ui.help.DocumentationProviderPanel;

/**
 * A listener that sends events up the UI tree
 * @author ted
 *
 */
public class BubbleUpEventDispatcher implements MouseListener
{
    private final DocumentationProviderPanel parent;
    private final ActionListener click;
    
    /**
     * default constructor that just takes a DPP for bubbling to
     * @param p the DPP
     */
    public BubbleUpEventDispatcher(DocumentationProviderPanel p)
    {
        this(p, null);
    }
    
    /**
     * Constructor that takes a DPP as well as an action listener, so that buttons can be over-ridden
     * @param p the DPP
     * @param l the action listener
     */
    public BubbleUpEventDispatcher(DocumentationProviderPanel p, ActionListener l)
    {
        this.parent = p;
        this.click = l;
    }


    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (parent.isHelpEnabled())
        {
            parent.dispatchEvent(e);
        }
        else
        {
            click.actionPerformed(new ActionEvent(e.getSource(), e.getID(), "click"));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        if (parent.isHelpEnabled())
        {
            parent.dispatchEvent(e);
        }
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        if (parent.isHelpEnabled())
        {
            parent.dispatchEvent(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (parent.isHelpEnabled())
        {
            parent.dispatchEvent(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (parent.isHelpEnabled())
        {
            parent.dispatchEvent(e);
        }
    }

}
