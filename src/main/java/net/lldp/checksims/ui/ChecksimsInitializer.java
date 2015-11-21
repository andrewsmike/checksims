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
    private final JTextField submissionPath;
    private final JTextField archivePath;
    private final JButton submissionPathSelector;
    private final JButton archivePathSelector;
    private final JComboBox<SimilarityDetector<? extends Percentable>> parsers;
    private final JButton checkSims;

    public ChecksimsInitializer()
    {
        submissionPath = new JTextField("", 30);
        archivePath = new JTextField("", 30);
        submissionPathSelector = new JButton(" Other submission ");
        archivePathSelector = new JButton(" Other achive ");
        parsers = new JComboBox<>(); // populate with parsers later
        checkSims = new JButton("CheckSims!");
        for (SimilarityDetector<? extends Percentable> s : AlgorithmRegistry.getInstance()
                .getSupportedImplementations())
        {
            parsers.addItem(s);
        }

        JPanel subs = new JPanel();
        JPanel archive = new JPanel();
        JPanel mid = new JPanel();
        JPanel bot = new JPanel();

        subs.setBackground(new Color(0xA9, 0xB0, 0xB7));
        archive.setBackground(new Color(0xA9, 0xB0, 0xB7));
        mid.setBackground(new Color(0xA9, 0xB0, 0xB7));
        bot.setBackground(new Color(0xA9, 0xB0, 0xB7));

        subs.add(submissionPath, BorderLayout.CENTER);
        subs.add(submissionPathSelector, BorderLayout.EAST);

        archive.add(archivePath, BorderLayout.CENTER);
        archive.add(archivePathSelector, BorderLayout.EAST);

        mid.add(parsers);

        bot.add(checkSims);

        setLayout(new GridLayout(4, 1));
        add(subs);
        add(archive);
        add(mid);
        add(bot);

        submissionPathSelector.addActionListener(new PathSelectorListener(submissionPath));
        archivePathSelector.addActionListener(new PathSelectorListener(archivePath));
        checkSims.addActionListener(new RunChecksimsListener(this, parsers, submissionPath, archivePath));
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
