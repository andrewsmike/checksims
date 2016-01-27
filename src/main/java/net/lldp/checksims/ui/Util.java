package net.lldp.checksims.ui;

import java.awt.Graphics;

public class Util
{
    public static int getWidth(String s, Graphics g)
    {
        return g.getFontMetrics().stringWidth(s);
    }
}
