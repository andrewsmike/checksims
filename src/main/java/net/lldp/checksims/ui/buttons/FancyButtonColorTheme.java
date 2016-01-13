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

import java.awt.Color;

/**
 * Color themes for fancy buttons. These are DLC, you need to buy
 * @author ted
 *
 */
public class FancyButtonColorTheme
{
    private final Color hoverBackground;
    private final Color hoverFont;
    
    private final Color unhoverBackground;
    private final Color unhoverFont;
    
    private final Color pressBackground;
    private final Color pressFont;
    
    private final Color unpressBackground;
    private final Color unpressFont;
    
    /**
     * Create a colorTheme
     * @param hb hover background
     * @param hf hover text color
     * @param uhb mouse away background
     * @param uhf mouse away text color
     * @param pb press background
     * @param pf press text color
     * @param upb release background
     * @param upf release text color
     */
    public FancyButtonColorTheme(
            Color hb,  Color hf,
            Color uhb, Color uhf,
            Color pb,  Color pf,
            Color upb, Color upf)
                    
    {
        this.hoverBackground = hb;
        this.hoverFont = hf;
        this.unhoverBackground = uhb;
        this.unhoverFont = uhf;
        
        this.pressBackground = pb;
        this.pressFont = pf;
        this.unpressBackground = upb;
        this.unpressFont = upf;
    }

    /**
     * @return hover background color
     */
    public Color getHoverBackground()
    {
        return hoverBackground;
    }

    /**
     * @return hover text color
     */
    public Color getHoverFont()
    {
        return hoverFont;
    }

    /**
     * @return mouse away background color
     */
    public Color getUnhoverBackground()
    {
        return unhoverBackground;
    }

    /**
     * @return mouse away text color
     */
    public Color getUnhoverFont()
    {
        return unhoverFont;
    }

    /**
     * @return press background color
     */
    public Color getPressBackground()
    {
        return pressBackground;
    }

    /**
     * @return press text color
     */
    public Color getPressFont()
    {
        return pressFont;
    }

    /**
     * @return release background color
     */
    public Color getUnpressBackground()
    {
        return unpressBackground;
    }

    /**
     * @return reease text color
     */
    public Color getUnpressFont()
    {
        return unpressFont;
    }
    
    /**
     * Nice Looking style for a close button
     */
    public static FancyButtonColorTheme CLOSE = new FancyButtonColorTheme(
            Color.red,
            Color.white,
            Color.gray,
            Color.black,
            Color.red.darker(),
            Color.white,
            Color.gray,
            Color.black);
    
    /**
     * Nice looking stule for a browse button
     */
    public static FancyButtonColorTheme BROWSE = new FancyButtonColorTheme(
            Color.green.darker().darker(),
            Color.white,
            Color.gray,
            Color.black,
            Color.green.darker().darker().darker(),
            Color.white,
            Color.gray,
            Color.black);
}
