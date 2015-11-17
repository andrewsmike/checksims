package net.lldp.checksims.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.lldp.checksims.algorithm.AlgorithmRegistry;
import net.lldp.checksims.algorithm.SimilarityDetector;
import net.lldp.checksims.parse.Percentable;

public class ChecksimsInitializer extends JPanel
{
    private final JTextField path;
    private final JButton pathSelector;
    private final JComboBox<SimilarityDetector<? extends Percentable>> parsers;
    private final JButton checkSims;

    public ChecksimsInitializer()
    {
        path = new JTextField("/home/ted/checksims_samples/python", 30); // default empty
        pathSelector = new JButton(" ... "); // select path standard ellipses
        parsers = new JComboBox<>(); // populate with parsers later
        checkSims = new JButton("CheckSims!");
        for (SimilarityDetector<? extends Percentable> s : AlgorithmRegistry.getInstance()
                .getSupportedImplementations())
        {
            parsers.addItem(s);
        }

        JPanel top = new JPanel();
        JPanel mid = new JPanel();
        JPanel bot = new JPanel();
        
        top.setBackground(new Color(0xA9, 0xB0, 0xB7));
        mid.setBackground(new Color(0xA9, 0xB0, 0xB7));
        bot.setBackground(new Color(0xA9, 0xB0, 0xB7));

        top.add(path, BorderLayout.CENTER);
        top.add(pathSelector, BorderLayout.EAST);

        mid.add(parsers);

        bot.add(checkSims);

        setLayout(new GridLayout(3, 1));
        add(top);
        add(mid);
        add(bot);

        pathSelector.addActionListener(new PathSelectorListener(path));
        checkSims.addActionListener(new RunChecksimsListener(this, parsers, path));
    }

    
    public static final JFrame f = new JFrame();
    
    public static void main(String ... args) throws IOException
    {
        InputStream stream = ChecksimsInitializer.class.getResourceAsStream("/net/lldp/checksims/ui/logo.png");
        BufferedImage logoIMG = ImageIO.read(stream);
        
        f.setPreferredSize(new Dimension(600, 350));
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout(2, 1));

        JPanel logo = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(logoIMG, 0, 0, null);
            }
        };

        f.add(logo);
        f.add(new ChecksimsInitializer());

        f.pack();
        f.setVisible(true);
    }

}
