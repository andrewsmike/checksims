package net.lldp.checksims.ui.results.mview;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;

public abstract class SortableMatrixViewer extends JPanel
{
    public abstract void padToSize(Dimension size);

    public abstract void updateThreshold(double d);

    public abstract void highlightMatching(String text, String text2);

    public abstract void highlightMatching(String text);

    public abstract void setViewAt(Rectangle r);
}
