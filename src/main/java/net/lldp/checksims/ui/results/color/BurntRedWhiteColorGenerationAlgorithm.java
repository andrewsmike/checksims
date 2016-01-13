package net.lldp.checksims.ui.results.color;

import java.awt.Color;

public class BurntRedWhiteColorGenerationAlgorithm implements ColorGenerationAlgorithm
{
    RedWhiteColorGenerationAlgorithm base = new RedWhiteColorGenerationAlgorithm();
    @Override
    public Color getColorFromScore(double score)
    {
        return base.getColorFromScore(score).darker().darker();
    }

}
