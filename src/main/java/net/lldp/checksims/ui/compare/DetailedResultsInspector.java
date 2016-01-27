package net.lldp.checksims.ui.compare;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.lldp.checksims.ui.ChecksimsColors;
import net.lldp.checksims.ui.compare.NiceTable.RowColor;
import net.lldp.checksims.ui.results.PairScore;
import net.lldp.checksims.ui.results.color.ColorGenerationAlgorithm;
import net.lldp.checksims.ui.results.color.RedWhiteColorGenerationAlgorithm;

public class DetailedResultsInspector implements ResultsInspector
{
    static final int STRIPE_SPACING = 10;
    private class DetailedResultsInspectorWindow extends JFrame
    {
        private final PairScore ps;

        public DetailedResultsInspectorWindow(PairScore ps)
        {
            JFrame self = this;
            this.ps = ps;
            this.build();
            this.pack();
            this.setVisible(true);
            
            self.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent arg0)
                {
                    synchronized (ps)
                    {
                        self.setVisible(false);
                        ps.notify();
                    }
                }
            });
        }

        private void build()
        {
            this.setPreferredSize(new Dimension(800, 600));
            
            JPanel header = new JPanel();
            JPanel body = new JPanel();
            
            header.setMinimumSize(new Dimension(800, 200));
            header.setPreferredSize(new Dimension(800, 200));
            if (ps.getScore() + ps.getInverseScore() > 1.6)
            {
                header.setBackground(ChecksimsColors.MATERIAL_RED);
            }
            else
            {
                header.setBackground(ChecksimsColors.MATERIAL_BLUE);
            }
            
            body.setBackground(ChecksimsColors.PRETTY_GREY);
            this.add(header, BorderLayout.NORTH);
            this.add(body, BorderLayout.CENTER);
            
            JLabel title = new JLabel("<html><center>Comparison<br />"
                    + this.ps.getSubmissions().getAName() + "<br /> & <br />"
                    + this.ps.getSubmissions().getBName() + "</center></html>");
            title.setFont(new Font("Arial", 0, 40));
            title.setMinimumSize(new Dimension(10, 200));
            header.add(Box.createHorizontalGlue());
            header.add(title);
            header.add(Box.createHorizontalGlue());
            body.setLayout(new GridLayout(1, 1));
            
            MaterialPanel bodyInfo = new MaterialPanel();
            bodyInfo.setBackground(ChecksimsColors.MATERIAL_WHITE);
            body.add(bodyInfo.getMaterialParent(), BorderLayout.CENTER);
            
            NiceTable results = new NiceTable(
                    createHeading(ps.getSubmissions().getAName(), ps.getSubmissions().getBName()),
                    createRows("similarity", "lines of code"),
                    createTableData(
                            new Object[]{(int)(ps.getScore()*100), (int)(ps.getInverseScore()*100)},
                            new Object[]{ps.getSubmissions().getA().getLinesOfCode(), ps.getSubmissions().getB().getLinesOfCode()}),
                    getColors(new RowColor(){

                        ColorGenerationAlgorithm color = new RedWhiteColorGenerationAlgorithm();
                        @Override
                        public boolean matchesRow(String row)
                        {
                            return row.equals("similarity");
                        }

                        @Override
                        public void drawCell(Graphics g, String s, int x, int y, int fontW, int fontH, int cellX, int cellY, int w, int h)
                        {
                            Double d = Double.parseDouble(s) / 100;
                            g.setColor(color.getColorFromScore(d));
                            int stripes = (2 * Math.max(w, h)) / (STRIPE_SPACING);
                            for(int i = 0; i < stripes; i++)
                            {
                                Stripe str = new Stripe(1, h - i*STRIPE_SPACING);
                                int[] l = str.boundBy(0, 0, w, h);
                                l[0] += cellX;
                                l[2] += cellX;
                                l[1] = cellY-l[1];
                                l[3] = cellY-l[3];
                                g.drawLine(l[0], l[1], l[2], l[3]);
                            }
                            
                            g.setColor(ChecksimsColors.MATERIAL_WHITE);
                            g.fillRoundRect(x-30, y-10-fontH, fontW+60, fontH+20, 4, 4);
                            g.setFont(new Font("Arial", Font.BOLD, fontH));
                            g.setColor(ChecksimsColors.PRETTY_GREY.darker());
                            g.drawString(s, x, y);
                        }
                        
                    }));
            
            bodyInfo.add(results, BorderLayout.CENTER);
        }

        private List<String> createHeading(Object ... items)
        {
            return Arrays.asList(items).stream().map(O -> O.toString()).collect(Collectors.toList());
        }
        
        private List<String> createRows(Object ... items)
        {
            return Arrays.asList(items).stream().map(O -> O.toString()).collect(Collectors.toList());
        }
        
        private List<List<String>> createTableData(Object[] ... items) {
            return Arrays.asList(items).stream().map(OA -> {
                return Arrays.asList(OA).stream().map(O -> O.toString()).collect(Collectors.toList());
            }).collect(Collectors.toList());
        }
        
        private List<RowColor> getColors(RowColor ... colors)
        {
            return Arrays.asList(colors);
        }
    }

    @Override
    public void handleResults(PairScore ps)
    {
        new DetailedResultsInspectorWindow(ps);
    }
    
    private class Stripe
    {
        // y = mx + b
        private final float m;
        private final int b;
        private Stripe(float m, int b)
        {
            this.m = m;
            this.b = b;
        }
        
        private int[] boundBy(int x, int y, int w, int h)
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
}
