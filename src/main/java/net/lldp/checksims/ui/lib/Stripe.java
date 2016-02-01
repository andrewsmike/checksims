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

/**
 * A class for doing some simple line intersection math for drawing fancy looking diagonal lines
 * @author ted
 *
 */
public class Stripe
{
    private final float m;
    private final int b;
    
    /**
     * Create a line given a slope and a y intercept (y = mx + b)
     * @param m the slope of the line
     * @param b the y intercept of the line
     */
    public Stripe(float m, int b)
    {
        this.m = m;
        this.b = b;
    }
    
    /**
     * find both locations where this line intersects the given rectangle
     * @param x the X coordinate of the lowest right hand corner
     * @param y the Y coordinate of the lowest right hand corner
     * @param w the width of the rectangle
     * @param h the height of the rectangle
     * @return an array where elements 0 and 1 are one pair of X,Y coordinates, and elements 2 and 3 are the other pair of X,Y coordinates
     * This will return [0,0,0,0] on failure
     */
    public int[] boundBy(int x, int y, int w, int h)
    {
        int[] result = new int[4];
        float[][] i = {{0, 0}, {0, 0}, {0, 0}, {0, 0}};
        i[0][1] = y;
        i[1][0] = x;
        i[2][1] = y+h;
        i[3][0] = x+w;
        i[0][0] = (i[0][1] - b)/m;
        i[1][1] = (i[1][0] * m) + b;
        i[2][0] = (i[2][1] - b)/m;
        i[3][1] = (i[3][0] * m) + b;
        int k = 0;
        int p = 0;
        int m = 4;
        if (i[0][0] == i[1][0] && i[0][1] == i[1][1])
        {
            result[0] = (int) i[0][0];
            result[1] = (int) i[0][1];
            k+=2;
            p+=2;
        }
        if (i[2][0] == i[3][0] && i[2][1] == i[3][1])
        {
            result[2] = (int) i[2][0];
            result[3] = (int) i[2][1];
            m -= 2;
        }
        for(;p<m&&k<4;p++)
        {
            if (i[p][0] >=x && i[p][0] <= x+w)
            {
                if (i[p][1] >= y && i[p][1] <= y+h)
                {
                    result[k] = (int) i[p][0];
                    result[k+1] = (int) i[p][1];
                    k+=2;
                }
            }
        }
        return result;
    }
}