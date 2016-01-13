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
package net.lldp.checksims.ui.file;

import java.awt.Dimension;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.lldp.checksims.ui.buttons.FancyButtonAction;
import net.lldp.checksims.ui.buttons.FancyButtonColorTheme;
import net.lldp.checksims.ui.buttons.FancyButtonMouseListener;

/**
 * A simple UI for file picking with a cancel, view, and browse display
 * used in accordians for batch file picking
 * @author ted
 *
 */
public class FileInputOption extends JPanel
{
    private static class FieldEditorAction implements FancyButtonAction
    {
        private final JTextField path;
        private final JFileChooser fc;
        public FieldEditorAction(JTextField path)
        {
            this.path = path;
            
            fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.setCurrentDirectory(new java.io.File("."));
            fc.setDialogTitle("title");
            fc.setAcceptAllFileFilterUsed(false);
        }
        
        @Override
        public void performAction()
        {
            int returnVal = fc.showOpenDialog(path);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                path.setText(file.getAbsolutePath());
            }
        }
        
    }
    
    private final long ID;
    private final JTextField path;
    
    /**
     * create a default FileInputOption UI
     * @param parent the parent accordian
     * @param ID the numeric ID of this option
     * @param height the height of this option
     * @param width the width of this option
     */
    public FileInputOption(FileInputOptionAccordionList parent, long ID, int height, int width)
    {
        this.ID = ID;
        FileInputOption self = this;
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        
        JLabel close = new JLabel(" x ", SwingConstants.CENTER);
        JLabel browse = new JLabel(" ... ", SwingConstants.CENTER);
        path = new JTextField();
        
        path.setEditable(false);
        
        close.setPreferredSize(new Dimension(height, height));
        browse.setPreferredSize(new Dimension(height, height));
        close.setMinimumSize(new Dimension(height, height));
        browse.setMinimumSize(new Dimension(height, height));
        close.setMaximumSize(new Dimension(height, height));
        browse.setMaximumSize(new Dimension(height, height));
        path.setPreferredSize(new Dimension(width-2*height, height));
        setPreferredSize(new Dimension(width, height));
        
        add(close);
        add(path);
        add(browse);
        
        browse.addMouseListener(new FancyButtonMouseListener(browse, new FieldEditorAction(path), FancyButtonColorTheme.BROWSE));
        close.addMouseListener(new FancyButtonMouseListener(close, new FancyButtonAction(){
            @Override
            public void performAction()
            {
                parent.remove(self);
            }
        }, FancyButtonColorTheme.CLOSE));
    }

    /**
     * @return the ID of this FileInputOption
     */
    public long getID()
    {
        return ID;
    }

    /**
     * get the file from this option
     * @return get the file that was returned by the picker
     */
    public File asFile()
    {
        return new File(path.getText());
    }
}
