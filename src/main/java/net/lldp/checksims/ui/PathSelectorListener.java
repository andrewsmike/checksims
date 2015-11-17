package net.lldp.checksims.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

public class PathSelectorListener implements ActionListener
{
    private final JTextField path;
    private final JFileChooser fc = new JFileChooser();

    public PathSelectorListener(JTextField path)
    {
        this.path = path;
        
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
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
