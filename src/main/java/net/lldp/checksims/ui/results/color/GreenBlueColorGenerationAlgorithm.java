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
package net.lldp.checksims.ui.results.color;

import java.awt.Color;

import net.lldp.checksims.ui.HSLColor;

/**
 * 
 * @author ted
 *
 */
public class GreenBlueColorGenerationAlgorithm implements ColorGenerationAlgorithm
{

    @Override
    public Color getColorFromScore(double score)
    {
        return new HSLColor(
                (float) ((score * 120) + 120)
               ,(float) (50)
               ,(float) (50)
        ).getRGB();
    }

}
