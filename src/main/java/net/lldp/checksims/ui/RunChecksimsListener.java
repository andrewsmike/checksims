package net.lldp.checksims.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import net.lldp.checksims.ChecksimsCommandLine;
import net.lldp.checksims.ChecksimsConfig;
import net.lldp.checksims.ChecksimsException;
import net.lldp.checksims.ChecksimsRunner;
import net.lldp.checksims.algorithm.SimilarityDetector;
import net.lldp.checksims.algorithm.similaritymatrix.output.MatrixPrinter;
import net.lldp.checksims.parse.Percentable;
import net.lldp.checksims.ui.file.FileInputOptionAccordionList;
import net.lldp.checksims.ui.results.GraphicalMatrixPrinter;

public class RunChecksimsListener implements ActionListener
{
    private final ChecksimsInitializer uiPanel;
    private final FileInputOptionAccordionList submissionPaths;
    private final FileInputOptionAccordionList archivePaths;
    private final JComboBox<SimilarityDetector<? extends Percentable>> selection;

    public RunChecksimsListener(ChecksimsInitializer checksimsInitializer,
            JComboBox<SimilarityDetector<? extends Percentable>> parsers, 
            FileInputOptionAccordionList submissionPaths, FileInputOptionAccordionList archivePaths)
    {
        this.submissionPaths = submissionPaths;
        this.archivePaths = archivePaths;
        this.selection = parsers;

        uiPanel = checksimsInitializer;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        ((JButton)ae.getSource()).setEnabled(false);
        
        //TODO check conditions!
        ChecksimsConfig conf = new ChecksimsConfig();
        conf.ignoreInvalid();
        conf.setOutputPrinters(new HashSet<MatrixPrinter>(){{
            add(new GraphicalMatrixPrinter());
        }});
        
        conf.setAlgorithm((SimilarityDetector<?>) selection.getSelectedItem());
        try
        {
            Set<File> files = submissionPaths.getFileSet();
            if (files != null && files.size() > 0)
            {
                conf.setSubmissions(ChecksimsCommandLine.getSubmissions(files, "*", false, false));
            }
            else
            {
                ((JButton)ae.getSource()).setEnabled(true);
                throw new ChecksimsException("missing files");
            }
        }
        catch (IOException | ChecksimsException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        
        try
        {
            Set<File> files = archivePaths.getFileSet();
            if (files != null && files.size() > 0)
            {
                conf.setArchiveSubmissions(ChecksimsCommandLine.getSubmissions(files, "*", false, false));
            }
            else
            {
                ((JButton)ae.getSource()).setEnabled(true);
                throw new ChecksimsException("missing files");
            }
        }
        catch (IOException | ChecksimsException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        
        JProgressBar progressBar = new JProgressBar(0, 100);
        JLabel percent = new JLabel();
        JLabel eta = new JLabel();
        progressBar.setValue(0);
        
        uiPanel.removeAll();
        uiPanel.add(progressBar);
        uiPanel.add(percent);
        uiPanel.add(eta);
        
        uiPanel.revalidate();
        uiPanel.repaint();
        
        
        conf.setStatusLogger(new ProgressBarStatusLogger(progressBar, percent, eta, ChecksimsInitializer.f));
        
        new Thread() {
            @Override
            public void run() {
                try
                {
                    Map<String, String> output = ChecksimsRunner.runChecksims(conf);
                    for(String strategy : output.keySet()) {
                        System.out.println("\n\n");
                        System.out.println("Output from " + strategy + "\n");
                        System.out.println(output.get(strategy));
                    }
                }
                catch (ChecksimsException e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
        
    }

}
