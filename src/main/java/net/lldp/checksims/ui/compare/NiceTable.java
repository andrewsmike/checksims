package net.lldp.checksims.ui.compare;

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
    
    public NiceTable(List<String> createHeading, List<String> createRows, List<List<String>> createTableData)
    {
        this(createHeading, createRows, createTableData, Collections.emptyList());
    }
    
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
    
    public interface RowColor
    {
        boolean matchesRow(String row);
        void drawCell(Graphics g, String s, int x, int y, int fontW, int fontH,  int cellX, int cellY, int w, int h);
    }
}
