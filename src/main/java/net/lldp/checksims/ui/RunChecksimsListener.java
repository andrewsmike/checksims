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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JProgressBar;

import net.lldp.checksims.ChecksimsCommandLine;
import net.lldp.checksims.ChecksimsConfig;
import net.lldp.checksims.ChecksimsException;
import net.lldp.checksims.ChecksimsRunner;
import net.lldp.checksims.algorithm.SimilarityDetector;
import net.lldp.checksims.algorithm.preprocessor.SubmissionPreprocessor;
import net.lldp.checksims.algorithm.similaritymatrix.output.MatrixPrinter;
import net.lldp.checksims.parse.Percentable;
import net.lldp.checksims.ui.file.FileInputOptionAccordionList;
import net.lldp.checksims.ui.results.GraphicalMatrixPrinter;

/**
 * 
 * @author ted
 *
 */
public class RunChecksimsListener implements ActionListener
{
    private final ChecksimsInitializer uiPanel;
    private final FileInputOptionAccordionList submissionPaths;
    private final FileInputOptionAccordionList archivePaths;
    private final FileInputOptionAccordionList commonCode;
    private final JList<SimilarityDetector<? extends Percentable>> selection;

    /**
     * a listener that will run checksims when the target is acted upon
     * @param checksimsInitializer the initializer frame
     * @param list the list of algorithms of which one is selected
     * @param submissionPaths the file accordion for submissions
     * @param archivePaths the file accordion for archives
     * @param commonCode the file accordion for common code
     */
    public RunChecksimsListener(ChecksimsInitializer checksimsInitializer,
            JList<SimilarityDetector<? extends Percentable>> list, 
            FileInputOptionAccordionList submissionPaths,
            FileInputOptionAccordionList archivePaths,
            FileInputOptionAccordionList commonCode)
    {
        this.submissionPaths = submissionPaths;
        this.archivePaths = archivePaths;
        this.commonCode = commonCode;
        this.selection = list;

        uiPanel = checksimsInitializer;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        new Thread() {
            @Override
            public void run() {
                try
                {
                    ((JButton)ae.getSource()).setEnabled(false);
                    
                    //TODO check conditions!
                    final ChecksimsConfig conf = new ChecksimsConfig();
                    
                    JProgressBar progressBar = new JProgressBar(0, 100);
                    JProgressBar overallStatus = new JProgressBar(0, 8);
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
                    
                    
                    conf.setStatusLogger(new ProgressBarStatusLogger(progressBar, percent, eta, elapsed, uiPanel.getWindow()));
                    tickProgress(overallStatus, message, "creating UI display");
                    
                    conf.ignoreInvalid();
                    conf.setOutputPrinters(new HashSet<MatrixPrinter>(){{
                        add(new GraphicalMatrixPrinter());
                    }});
                    tickProgress(overallStatus, message, "loading compilers");
                    
                    conf.setAlgorithm((SimilarityDetector<?>) selection.getSelectedValue());
    
                    tickProgress(overallStatus, message, "loading submissions (this may take a while)");
                    Set<File> all = new HashSet<>();
                    try
                    {
                        Set<File> files = submissionPaths.getFileSet();
                        all.addAll(files);
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
                        uiPanel.UhOhException(e, "Invalid Submission Directory");
                        return;
                    }
                    tickProgress(overallStatus, message, "loading archived submissions (this may take a while)");
                    
                    try
                    {
                        Set<File> files = archivePaths.getFileSet();
                        all.addAll(files);
                        if (files != null && files.size() > 0)
                        {
                            conf.setArchiveSubmissions(ChecksimsCommandLine.getSubmissions(
                                    files, conf.getAlgorithm().getDefaultGlobPattern(), true, false));
                        }
                    }
                    catch (IOException | ChecksimsException e)
                    {
                        uiPanel.UhOhException(e, "Invalid Archive Directory");
                        return;
                    }
                    
                    tickProgress(overallStatus, message, "Converting Preprocessor for Common Code");
                    
                    try
                    {
                        Set<File> files = commonCode.getFileSet();
                        if (files != null && files.size() > 0)
                        {
                            List<SubmissionPreprocessor> preprops = files.stream().map(f -> {
                                try
                                {
                                    return ChecksimsCommandLine.getCommonCodeRemoval(
                                                f.getAbsolutePath(),
                                                all,
                                                conf.getAlgorithm().getDefaultGlobPattern());
                                }
                                catch (ChecksimsException | IOException e)
                                {
                                    return null;
                                }
                            }).collect(Collectors.toList());
                            conf.setPreprocessors(preprops);
                        }
                    }
                    catch (Exception e)
                    {
                        uiPanel.UhOhException(e, "Invalid Common Code Directory");
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
                                uiPanel.UhOhException(e);
                            }
                        }
                    }.start();
                }
                catch (Exception e)
                {
                    uiPanel.UhOhException(e);
                }
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
