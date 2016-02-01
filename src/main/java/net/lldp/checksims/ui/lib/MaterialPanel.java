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

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A Jpanel that mimics google's material design look
 * @author ted
 *
 */
public class MaterialPanel extends JPanel
{
    private final DropShadowPanel parent = new DropShadowPanel();
    
    /**
     * Default constructor. Must not add directly, use {@link getMaterialParent}
     */
    public MaterialPanel()
    {
        parent.add(this, BorderLayout.NORTH);
        parent.setDistance(1);
    }
    
    /**
     * get the parent of this panel. use this to add to another element
     * @return
     */
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
