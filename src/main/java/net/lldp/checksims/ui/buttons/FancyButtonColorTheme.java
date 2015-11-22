package net.lldp.checksims.ui.buttons;

import java.awt.Color;

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

    public Color getHoverBackground()
    {
        return hoverBackground;
    }

    public Color getHoverFont()
    {
        return hoverFont;
    }

    public Color getUnhoverBackground()
    {
        return unhoverBackground;
    }

    public Color getUnhoverFont()
    {
        return unhoverFont;
    }

    public Color getPressBackground()
    {
        return pressBackground;
    }

    public Color getPressFont()
    {
        return pressFont;
    }

    public Color getUnpressBackground()
    {
        return unpressBackground;
    }

    public Color getUnpressFont()
    {
        return unpressFont;
    }
    
    public static FancyButtonColorTheme CLOSE = new FancyButtonColorTheme(
            Color.red,
            Color.white,
            Color.gray,
            Color.black,
            Color.red.darker(),
            Color.white,
            Color.gray,
            Color.black);
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
