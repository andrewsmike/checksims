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

    public long getID()
    {
        return ID;
    }

    public File asFile()
    {
        return new File(path.getText());
    }
}
