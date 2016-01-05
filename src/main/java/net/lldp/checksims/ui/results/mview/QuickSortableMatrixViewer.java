package net.lldp.checksims.ui.results.mview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.ui.results.IndividualInspectorWindow;
import net.lldp.checksims.ui.results.SortableMatrix;
import net.lldp.checksims.ui.results.color.ColorGenerationAlgorithm;
import net.lldp.checksims.ui.results.color.GreenBlueColorGenerationAlgorithm;
import net.lldp.checksims.ui.results.color.RedWhiteColorGenerationAlgorithm;
import net.lldp.checksims.util.data.Monad;

public class QuickSortableMatrixViewer extends SortableMatrixViewer
{
    final int HEADER = 40;
    final int CELL = 80;
    private final SortableMatrix sm;
    private List<Submission> subs = new ArrayList<>();
    private ColorGenerationAlgorithm color = new RedWhiteColorGenerationAlgorithm();
    private ColorGenerationAlgorithm colorHighlight = new GreenBlueColorGenerationAlgorithm();
    private int lx = 0;
    private int ly = 0;
    private int mx = 0;
    private int my = 0;
    private Dimension minimumDimension = new Dimension(1000, 1000);
    
    private final Monad<String> searchA = Monad.wrap("");
    private final Monad<String> searchB = Monad.wrap("");
    
    private final Comparator<Submission> submissionSorter = new Comparator<Submission>() {
        @Override
        public int compare(Submission a, Submission b)
        {
            boolean A = isTagged(a);
            boolean B = isTagged(b);
            if (A == B)
            {
                return b.getMaximumCopyScore().compareTo(a.getMaximumCopyScore());
            }
            else if (A)
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }
    };
    
    public QuickSortableMatrixViewer(SortableMatrix sm)
    {
        this.sm = sm;
        this.setPreferredSize(minimumDimension);
        this.addMouseListener(new MouseListener() {
            
            @Override
            public void mouseReleased(MouseEvent e)
            { }
            
            @Override
            public void mousePressed(MouseEvent e)
            { }
            
            @Override
            public void mouseExited(MouseEvent e)
            { }
            
            @Override
            public void mouseEntered(MouseEvent e)
            { }
            
            @Override
            public void mouseClicked(MouseEvent e)
            {
                int x = e.getX();
                int y = e.getY();
                
                Submission A = getSubmissionByCoord(x);
                Submission B = getSubmissionByCoord(y);
                if(A==B)
                {
                    //user clicked on black box
                }
                else if (A == null)
                {
                    //clicked on a row header
                    System.out.println("row: "+B.getName());
                }
                else if (B == null)
                {
                    //clicked on a column header
                    System.out.println("column: "+A.getName());
                }
                else
                {
                    System.out.println(A.getName() + " :: " + B.getName());
                    new IndividualInspectorWindow(A, B, sm.getPairForSubmissions(A, B));
                }
            }
        });
    }
    
    private boolean isTagged(Submission s)
    {
        boolean A = Monad.unwrap(searchA).equals("");
        boolean B = Monad.unwrap(searchB).equals("");
        if (!A && s.getName().contains(Monad.unwrap(searchA)))
        {
            return true;
        }
        if (!B && s.getName().contains(Monad.unwrap(searchB)))
        {
            return true;
        }
        return false;
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        subs.sort(submissionSorter);
        List<Submission> subs = Collections.unmodifiableList(this.subs);
        
        ((java.awt.Graphics2D) g).setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        int header_cur = HEADER;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, HEADER, HEADER);
        for(Submission s : subs)
        {
            if (!isTagged(s))
            {
                g.setColor(color.getColorFromScore(s.getMaximumCopyScore()));
            }
            else
            {
                g.setColor(colorHighlight.getColorFromScore(s.getMaximumCopyScore()));
            }
            g.fillRect(header_cur, 0, CELL, HEADER);
            g.setColor(Color.black);
            g.drawString(s.getName(), header_cur+2, 14);
            header_cur += CELL;
        }
        
        int cy = HEADER;
        for(Submission X : subs)
        {
            if (cy > ly && cy < my)
            {
                int cx = HEADER;
                for(Submission Y : subs)
                {
                    if (cx > lx && cx < mx)
                    {
                        if (!X.equals(Y))
                        {
                            double score = sm.getPairForSubmissions(X, Y).getScore();
                            if (isTagged(Y) || isTagged(X))
                            {
                                g.setColor(colorHighlight.getColorFromScore(score));
                            }
                            else
                            {
                                g.setColor(color.getColorFromScore(score));
                            }
                            g.fillRect(cx, cy, CELL, CELL);
                            g.setColor(Color.black);
                            g.drawString(((int)(100*score))+"", cx+2, cy+34);
                        }
                        else
                        {
                            g.setColor(Color.black);
                            g.fillRect(cx, cy, CELL, CELL);
                        }
                    }
                    cx += CELL;
                }
            }
            cy += CELL;
        }
        
        setFontRotated(g);
        header_cur = HEADER;
        for(Submission s : subs)
        {
            if (!isTagged(s))
            {
                g.setColor(color.getColorFromScore(s.getMaximumCopyScore()));
            }
            else
            {
                g.setColor(colorHighlight.getColorFromScore(s.getMaximumCopyScore()));
            }
            g.fillRect(0, header_cur, HEADER, CELL);
            g.setColor(Color.black);
            g.drawString(s.getName(), 14, header_cur+2);
            header_cur += CELL;
        }
    }

    @Override
    public void padToSize(Dimension size)
    {
        minimumDimension = size;
    }

    @Override
    public void updateThreshold(double d)
    {
        Submission[] s = sm.getSubmissionsAboveThreshold(d);
        if (s.length > 0)
        {
            subs = Arrays.asList(s);
            subs.sort(submissionSorter);
        }
        else
        {
            subs = new ArrayList<>();
        }
        
        if (s.length > 12)
        {
            this.setPreferredSize(new Dimension(s.length*CELL+HEADER, s.length*CELL+HEADER));
        }
        else
        {
            this.setPreferredSize(minimumDimension);
        }
    }

    @Override
    public void highlightMatching(String text, String text2)
    {
        searchA.set(text);
        searchB.set(text2);
    }

    @Override
    public void highlightMatching(String text)
    {
        searchA.set(text);
    }

    @Override
    public void setViewAt(Rectangle r)
    {
        lx = (int) (r.getX() - CELL*2);
        ly = (int) (r.getY() - CELL*2);
        mx = (int) (r.getX() + r.getWidth() + CELL*2);
        my = (int) (r.getY() + r.getHeight() + CELL*2);
    }
    
    private void setFontRotated(Graphics g)
    {
        Font font = new Font(null, Font.PLAIN, 14);    
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(90), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        g.setFont(rotatedFont);
    }
    
    private Submission getSubmissionByCoord(int x)
    {
        if (x < HEADER)
        {
            return null;
        }
        x -= HEADER;
        x /= CELL;
        return subs.get(x);
    }
}
