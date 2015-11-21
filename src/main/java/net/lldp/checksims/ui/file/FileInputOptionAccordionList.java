package net.lldp.checksims.ui.file;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FileInputOptionAccordionList extends JPanel
{
    private final SortedSet<FileInputOption> fios;
    private final JButton click; // TODO replace with fancy button
    private long nextID = 0;
    private final JFrame superParent;
    
    public FileInputOptionAccordionList(JFrame repack)
    {
        fios = new TreeSet<>(new Comparator<FileInputOption>(){
            @Override
            public int compare(FileInputOption a, FileInputOption b)
            {
                return (int) (a.getID() - b.getID());
            }
        });
        
        click = new JButton("Add Source Directory");
        FileInputOptionAccordionList self = this;
        click.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae)
            {
                fios.add(new FileInputOption(self, nextID++, 50, 400));
                
                repopulate();
            }
            
        });
        
        superParent = repack;
        
        repopulate();
    }
    
    private void repopulate()
    {
        removeAll();
        setLayout(new GridLayout(fios.size()+1, 1));
        for(FileInputOption fio : fios)
        {
            add(fio);
        }
        add(click);
        Dimension newsize = new Dimension(400, 50 * (fios.size()+1));
        setPreferredSize(newsize);
        superParent.pack();
    }
    
    public void remove(FileInputOption fio)
    {
        fios.remove(fio);
        repopulate();
    }

    public Set<File> getFileSet()
    {
        Set<File> result = new HashSet<>();
        for(FileInputOption fio : fios)
        {
            File f = fio.asFile();
            if (f == null)
            {
                return null;
            }
            result.add(f);
        }
        return result;
    }
}
