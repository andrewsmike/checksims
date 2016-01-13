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
package net.lldp.checksims.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import net.lldp.checksims.algorithm.AlgorithmRegistry;
import net.lldp.checksims.algorithm.SimilarityDetector;
import net.lldp.checksims.parse.Percentable;
import net.lldp.checksims.ui.file.FileInputOptionAccordionList;

/**
 * 
 * @author ted
 *
 * Main class for checksims GUI
 */
public class ChecksimsInitializer extends JPanel
{
    private final JButton checkSims;

    private ChecksimsInitializer(JFrame f) throws IOException
    {
        JList<SimilarityDetector<? extends Percentable>> list =
                new JList<SimilarityDetector<? extends Percentable>>(
                        new Vector<SimilarityDetector<? extends Percentable>>(
                                AlgorithmRegistry.getInstance().getSupportedImplementations())); // buy your onahole here baka!
        checkSims = new JButton("Run CheckSims!");
        
        InputStream stream = ChecksimsInitializer.class.getResourceAsStream("/net/lldp/checksims/ui/logo.png");
        BufferedImage logoIMG = ImageIO.read(stream);
        JPanel logo = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(logoIMG, 0, 0, null);
            }
        };
        logo.setMinimumSize(new Dimension(600, 175));
        logo.setMaximumSize(new Dimension(600, 175));
        logo.setPreferredSize(new Dimension(600, 175));


        JPanel selectors = new JPanel();
        FileInputOptionAccordionList subs = new FileInputOptionAccordionList(f, selectors, "source");
        FileInputOptionAccordionList archs = new FileInputOptionAccordionList(f, selectors, "archive");
        FileInputOptionAccordionList common = new FileInputOptionAccordionList(f, selectors, "common code", FileInputOptionAccordionList.SingleInput);
        JPanel bot = new JPanel();
        
        subs.setBackground(new Color(0xA9, 0xB0, 0xB7)); // WPI colors
        archs.setBackground(new Color(0xA9, 0xB0, 0xB7)); // WPI colors
        bot.setBackground(new Color(0xA9, 0xB0, 0xB7)); // TODO make constant

        bot.add(checkSims);
        
        JPanel UI = new JPanel();
        selectors.setMinimumSize(new Dimension(400, 175));
        selectors.setPreferredSize(new Dimension(400, 175));
        selectors.setLayout(new BoxLayout(selectors, BoxLayout.Y_AXIS));
        selectors.add(subs);
        selectors.add(archs);
        selectors.add(common);
        
        JPanel algorithm = new JPanel();
        algorithm.setMinimumSize(new Dimension(200, 175));
        algorithm.setPreferredSize(new Dimension(200, 175));
        algorithm.add(list);
        
        UI.setLayout(new BoxLayout(UI, BoxLayout.X_AXIS));
        UI.add(algorithm);
        UI.add(selectors);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(logo);
        add(UI);
        add(bot);
        
        checkSims.addActionListener(new RunChecksimsListener(this, list, subs, archs, common));
    }

    public static final JFrame f = new JFrame();
    
    public static void main(String ... args) throws IOException
    {
        f.setMinimumSize(new Dimension(600, 350));
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout(1, 1));
        f.add(new ChecksimsInitializer(f));
        f.pack();
        f.setVisible(true);
    }

}
