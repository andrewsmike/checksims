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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

/**
 * 
 * @author ted
 *
 */
public class PathSelectorListener implements ActionListener
{
    private final JTextField path;
    private final JFileChooser fc = new JFileChooser();

    /**
     * An action listener to open a file selector
     * @param path the field where the selected path is written
     */
    public PathSelectorListener(JTextField path)
    {
        this.path = path;
        
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setCurrentDirectory(new java.io.File("."));
        fc.setDialogTitle("title");
        fc.setAcceptAllFileFilterUsed(false);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        int returnVal = fc.showOpenDialog(path);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            //This is where a real application would open the file.
            path.setText(file.getAbsolutePath());
        }
    }

}
