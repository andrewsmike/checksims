package net.lldp.checksims.ui.results.color;

import java.awt.Color;

public interface ColorGenerationAlgorithm
{
    Color getColorFromScore(double score);
}
