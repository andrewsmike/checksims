/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License (the "License").
 * You may not use this file except in compliance with the License.
 *
 * See LICENSE.txt included in this distribution for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at LICENSE.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 * Copyright (c) 2014-2016 Ted Meyer, Nicholas DeMarinis, Matthew Heon, and Dolan Murvihill
 */
package net.lldp.checksims.ui.compare;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.lldp.checksims.submission.Submission;
import net.lldp.checksims.ui.results.PairScore;
import net.lldp.checksims.ui.results.color.ColorGenerationAlgorithm;
import net.lldp.checksims.ui.results.color.RedWhiteColorGenerationAlgorithm;

/**
 * 
 * @author ted
 *
 */
public class IndividualInspectorWindow extends JFrame
{
    private final JPanel main = new JPanel();
    
    /**
     * TODO; scrap this class, write a better inspector
     * @param a
     * @param b
     * @param ps
     */
    public IndividualInspectorWindow(Submission a, Submission b, PairScore ps)
    {
        main.setLayout(new GridLayout(2, 1));
        
        JPanel student1 = new JPanel();
        JPanel student2 = new JPanel();
        
        student1.setLayout(new GridLayout(1, 2));
        student2.setLayout(new GridLayout(1, 2));
        
        ColorGenerationAlgorithm cga = new RedWhiteColorGenerationAlgorithm();
        
        student2.setBackground(cga.getColorFromScore(ps.getInverseScore()));
        student1.setBackground(cga.getColorFromScore(ps.getScore()));
        
        student1.add(new JLabel(a.getName()));
        student1.add(colorSet(ps.getScore(), cga));
        
        student2.add(new JLabel(b.getName()));
        student2.add(colorSet(ps.getInverseScore(), cga));
        
        this.add(main, BorderLayout.CENTER);
        main.add(student1);
        main.add(student2);
        
        this.setResizable(false);
        this.setPreferredSize(new Dimension(300, 80));
        this.setMinimumSize(new Dimension(300, 80));
        this.pack();
        this.setVisible(true);
    }
    
    private JLabel colorSet(double score, ColorGenerationAlgorithm cga)
    {
        JLabel l = new JLabel(""+score);
        l.setBackground(cga.getColorFromScore(score));
        return l;
    }
}
