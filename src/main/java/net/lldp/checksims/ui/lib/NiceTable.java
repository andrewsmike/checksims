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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.swing.JPanel;

import net.lldp.checksims.ui.ChecksimsColors;
import net.lldp.checksims.ui.Util;

/**
 * A class for drawing nice looking tables in a jpanel
 * @author ted
 *
 */
public class NiceTable extends JPanel
{
    private final List<String> createHeading;
    private final List<String> createRows;
    private final List<List<String>> createTableData;
    private final List<RowColor> colorSchemes;
    
    private final int WIDTH;
    private final int HEIGHT;
    
    private final int CELL_HEIGHT = 150;
    private final int CELL_WIDTH;
    private final int MARGIN = 10;
    private final int FONT_SIZE = 16;
    
    private final RowColor defaultRowColor = new RowColor() {

        @Override
        public boolean matchesRow(String row)
        {
            return true;
        }

        @Override
        public void drawCell(Graphics g, String s, int x, int y, int fontW, int fontH, int cellX, int cellY, int w, int h)
        {
            g.setColor(ChecksimsColors.PRETTY_GREY.darker());
            g.drawString(s, x, y);
        }
    };
    
    /**
     * Create a default nice table with a list of column headings, row titles, and the data that goes inside.
     * @param createHeading a list of Strings that represents headings
     * @param createRows a list of Strings that represents row titles
     * @param createTableData a 2D list of strings for the table contents
     */
    public NiceTable(List<String> createHeading, List<String> createRows, List<List<String>> createTableData)
    {
        this(createHeading, createRows, createTableData, Collections.emptyList());
    }
    
    /**
     * Create a nice table with all the default table information, as well as custom row colorings
     * @param color A list of RowColorings see @RowColor class for details
     */
    public NiceTable(List<String> createHeading, List<String> createRows, List<List<String>> createTableData, List<RowColor> color)
    {
        this.createHeading = createHeading;
        this.createRows = createRows;
        this.createTableData = createTableData;
        this.colorSchemes = color;
        
        Graphics g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
        g.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        int maxHeading = createHeading
                .stream()
                .map(S -> Util.getWidth(S, g))
                .reduce(0, (A,B) -> {
                    return Math.max(A, B);
                });
        int maxRow = createRows
                .stream()
                .map(S -> Util.getWidth(S, g))
                .reduce(0, (A,B) -> {
                    return Math.max(A, B);
                });
        CELL_WIDTH = Math.max(maxHeading, Math.max(maxRow, 200)) + 2*MARGIN;
        
        this.WIDTH = CELL_WIDTH * (1 + createHeading.size()) + MARGIN*2;
        this.HEIGHT = CELL_HEIGHT * (1 + createRows.size()) + MARGIN*2;
        
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        g.setColor(ChecksimsColors.PRETTY_GREY);
        
        for(int i=createRows.size()+1; --i>0;)
        {
            if (i == 1) {
                g.setColor(ChecksimsColors.PRETTY_GREY.darker());
            }
            g.drawLine(MARGIN, MARGIN+CELL_HEIGHT*i, WIDTH-MARGIN, MARGIN+CELL_HEIGHT*i);
        }
        g.setColor(ChecksimsColors.PRETTY_GREY);
        for(int i=createHeading.size()+1; --i>0;)
        {
            if (i == 1) {
                g.setColor(ChecksimsColors.PRETTY_GREY.darker());
            }
            g.drawLine(MARGIN+i*CELL_WIDTH, MARGIN, MARGIN+i*CELL_WIDTH, HEIGHT-MARGIN);
        }
        
        g.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        int ybase = MARGIN + CELL_HEIGHT;
        for(String s : createRows)
        {
            int y = ybase + (CELL_HEIGHT + FONT_SIZE) / 2;
            int x = MARGIN + (CELL_WIDTH - Util.getWidth(s, g)) / 2;
            g.drawString(s, x, y);
            ybase += CELL_HEIGHT;
        }
        
        int xbase = MARGIN + CELL_WIDTH;
        for(String s : createHeading)
        {
            int x = xbase + (CELL_WIDTH - Util.getWidth(s, g)) / 2;
            int y = MARGIN + (CELL_HEIGHT + FONT_SIZE) / 2;
            g.drawString(s, x, y);
            xbase += CELL_WIDTH;
        }
        
        
        g.setFont(new Font("Arial", 0, FONT_SIZE));
        ybase = MARGIN + CELL_HEIGHT;
        Iterator<String> rows = createRows.iterator();
        for(List<String> ss : createTableData)
        {
            xbase = MARGIN + CELL_WIDTH;
            String row = rows.next();
            for (String s : ss)
            {
                int x = xbase + (CELL_WIDTH - Util.getWidth(s, g)) / 2;
                int y = ybase + (CELL_HEIGHT + FONT_SIZE) / 2;
                Optional<RowColor> maybe = colorSchemes.stream().filter(C -> C.matchesRow(row)).findFirst();
                if (maybe.isPresent())
                {
                    maybe.get().drawCell(g, s, x, y, Util.getWidth(s, g), FONT_SIZE, xbase, ybase+CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
                }
                else
                {
                    defaultRowColor.drawCell(g, s, x, y, Util.getWidth(s, g), FONT_SIZE, xbase, ybase+CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
                }
                xbase += CELL_WIDTH;
            }
            ybase += CELL_HEIGHT;
        }
    }
    
    /**
     * An interface for representing an object that optionally draws the cells for matching rows
     * @author ted
     *
     */
    public interface RowColor
    {
        /**
         * @param row the title of the row
         * @return whether this RowColor should be drawing the cells in the row
         */
        boolean matchesRow(String row);
        
        /**
         * Draw a custom cell
         * @param g the graphics context
         * @param s the String for the cell
         * @param x the lower right X coordinate for the text
         * @param y the lower right Y coordinate for the text
         * @param fontW the font width
         * @param fontH the font height
         * @param cellX the lower right X coordinate for the cell
         * @param cellY the lower right Y coordinate for the cell
         * @param w the width of the cell
         * @param h the height of the cell
         */
        void drawCell(Graphics g, String s, int x, int y, int fontW, int fontH,  int cellX, int cellY, int w, int h);
    }
}
