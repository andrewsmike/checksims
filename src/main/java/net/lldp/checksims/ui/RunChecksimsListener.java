package net.lldp.checksims.ui;

import java.awt.BorderLayout;
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
        new Thread() {
            @Override
            public void run() {
                ((JButton)ae.getSource()).setEnabled(false);
                
                //TODO check conditions!
                ChecksimsConfig conf = new ChecksimsConfig();
                
                JProgressBar progressBar = new JProgressBar(0, 100);
                JProgressBar overallStatus = new JProgressBar(0, 7);
                JLabel percent = new JLabel("Percent");
                JLabel eta = new JLabel("Estimated Time Remaining: NaN");
                JLabel elapsed = new JLabel("Elapsed Time: 0s");
                JLabel message = new JLabel("initializing");
                progressBar.setValue(0);
                overallStatus.setValue(0);
                
                uiPanel.removeAll();
                uiPanel.add(progressBar);
                uiPanel.add(percent);
                uiPanel.add(eta);
                uiPanel.add(elapsed);
                uiPanel.add(message);
                uiPanel.add(overallStatus, BorderLayout.SOUTH);
                
                tickProgress(overallStatus, message, "initializing");
                
                
                conf.setStatusLogger(new ProgressBarStatusLogger(progressBar, percent, eta, elapsed, ChecksimsInitializer.f));
                tickProgress(overallStatus, message, "creating UI display");
                
                conf.ignoreInvalid();
                conf.setOutputPrinters(new HashSet<MatrixPrinter>(){{
                    add(new GraphicalMatrixPrinter());
                }});
                tickProgress(overallStatus, message, "loading compilers");
                
                conf.setAlgorithm((SimilarityDetector<?>) selection.getSelectedItem());

                tickProgress(overallStatus, message, "loading submissions (this may take a while)");
                try
                {
                    Set<File> files = submissionPaths.getFileSet();
                    if (files != null && files.size() > 0)
                    {
                        conf.setSubmissions(ChecksimsCommandLine.getSubmissions(
                                files, conf.getAlgorithm().getDefaultGlobPattern(), true, false));
                    }
                    else
                    {
                        ((JButton)ae.getSource()).setEnabled(true);
                        throw new ChecksimsException("missing files");
                    }
                }
                catch (IOException | ChecksimsException e)
                {
                    message.setText("Could not process files - "+e.getMessage());
                    return;
                }
                tickProgress(overallStatus, message, "loading archived submissions (this may take a while)");
                
                try
                {
                    Set<File> files = archivePaths.getFileSet();
                    if (files != null && files.size() > 0)
                    {
                        conf.setArchiveSubmissions(ChecksimsCommandLine.getSubmissions(
                                files, conf.getAlgorithm().getDefaultGlobPattern(), true, false));
                    }
                }
                catch (IOException | ChecksimsException e)
                {
                    message.setText("Could not process files - "+e.getMessage());
                    return;
                }
                tickProgress(overallStatus, message, "Comparing Student submissions");
                
                new Thread() {
                    @Override
                    public void run() {
                        try
                        {
                            Map<String, String> output = ChecksimsRunner.runChecksims(conf);
                            tickProgress(overallStatus, message, "done");
                            for(String strategy : output.keySet()) {
                                System.out.println("\n\n");
                                System.out.println("Output from " + strategy + "\n");
                                System.out.println(output.get(strategy));
                            }
                        }
                        catch (ChecksimsException e)
                        {
                            message.setText(e.getMessage());
                        }
                    }
                }.start();
            }
        }.start();   
    }

    private void tickProgress(JProgressBar progress, JLabel jl, String message)
    {
        try
        {
            Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        progress.setValue(progress.getValue()+1);
        jl.setText(message);
        uiPanel.revalidate();
        uiPanel.repaint();
        uiPanel.setVisible(true);
    }
}
