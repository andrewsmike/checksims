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
package net.lldp.checksims.ui.results.mview;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;

/**
 * 
 * @author ted
 *
 * An abstract class for representing sortable matricies.
 * Classes of this type are responsible for drawing the
 * sortable matrix they are tasked to draw
 */
public abstract class SortableMatrixViewer extends JPanel
{
    /**
     * @param size the minimum size of this panel.
     */
    public abstract void padToSize(Dimension size);

    /**
     * change the threshold for items that should be rendered
     * @param d the new threshold
     */
    public abstract void updateThreshold(double d);

    /**
     * Highlight all pairs of assignments matching either of these strings
     * @param text
     * @param text2
     */
    public abstract void highlightMatching(String text, String text2);

    /**
     * Highlight all assignments matching this string
     * @param text
     */
    public abstract void highlightMatching(String text);

    /**
     * set which region of this window should be drawn
     * @param r a bounding rectangle for which region needs to be drawn
     */
    public abstract void setViewAt(Rectangle r);
}
