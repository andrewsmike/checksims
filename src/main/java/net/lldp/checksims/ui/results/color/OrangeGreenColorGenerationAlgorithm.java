package net.lldp.checksims.ui.results.color;

import java.awt.Color;

import net.lldp.checksims.ui.HSLColor;

public class OrangeGreenColorGenerationAlgorithm implements ColorGenerationAlgorithm
{

    @Override
    public Color getColorFromScore(double score)
    {
        return new HSLColor(
                (float) (((1.0 - score) * 75.0) + 45.0)
               ,(float) (score * 100)
               ,(float) (100 - (score * 50))
        ).getRGB();
    }

}
