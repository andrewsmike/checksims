package net.lldp.checksims.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.lldp.checksims.algorithm.AlgorithmRegistry;
import net.lldp.checksims.algorithm.SimilarityDetector;
import net.lldp.checksims.parse.Percentable;
import net.lldp.checksims.ui.file.FileInputOptionAccordionList;

public class ChecksimsInitializer extends JPanel
{
    private final JComboBox<SimilarityDetector<? extends Percentable>> parsers;
    private final JButton checkSims;

    public ChecksimsInitializer(JFrame f) throws IOException
    {
        parsers = new JComboBox<>();
        checkSims = new JButton("CheckSims!");
        for (SimilarityDetector<? extends Percentable> s : AlgorithmRegistry.getInstance()
                .getSupportedImplementations())
        {
            parsers.addItem(s);
        }
        
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

        FileInputOptionAccordionList top = new FileInputOptionAccordionList(f);
        JPanel mid = new JPanel();
        JPanel bot = new JPanel();
        
        top.setBackground(new Color(0xA9, 0xB0, 0xB7)); // WPI colors
        mid.setBackground(new Color(0xA9, 0xB0, 0xB7)); // TODO make this static somewhere
        bot.setBackground(new Color(0xA9, 0xB0, 0xB7));

        mid.add(parsers);

        bot.add(checkSims);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(logo);
        add(top);
        add(mid);
        add(bot);
        
        checkSims.addActionListener(new RunChecksimsListener(this, parsers, top));
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
