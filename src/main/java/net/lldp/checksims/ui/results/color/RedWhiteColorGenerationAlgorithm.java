package net.lldp.checksims.ui.results.color;

import java.awt.Color;

import net.lldp.checksims.ui.HSLColor;

public class RedWhiteColorGenerationAlgorithm implements ColorGenerationAlgorithm
{

    @Override
    public Color getColorFromScore(double score)
    {
        return new HSLColor(
                (float) ((1.0 - score) * 60)
               ,(float) (score * 100)
               ,(float) (100 - (score * 50))
        ).getRGB();
    }

}
