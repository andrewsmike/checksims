package net.lldp.checksims.ui.results.color;

import java.awt.Color;

import net.lldp.checksims.ui.HSLColor;

public class GreenBlueColorGenerationAlgorithm implements ColorGenerationAlgorithm
{

    @Override
    public Color getColorFromScore(double score)
    {
        return new HSLColor(
                (float) ((score * 120) + 120)
               ,(float) (50)
               ,(float) (50)
        ).getRGB();
    }

}
