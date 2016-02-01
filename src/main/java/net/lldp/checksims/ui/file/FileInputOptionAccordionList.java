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
import javax.swing.JComponent;
import javax.swing.JFrame;

import net.lldp.checksims.ui.help.Direction;
import net.lldp.checksims.ui.help.DocumentationProviderPanel;
import net.lldp.checksims.ui.lib.BubbleUpEventDispatcher;

public class FileInputOptionAccordionList extends DocumentationProviderPanel
{
    public static final boolean SingleInput = false;
    private final SortedSet<FileInputOption> fios;
    private final JButton click; // TODO replace with fancy button
    private long nextID = 0;
    private final JFrame superParent;
    private final JComponent parent;
    private final Boolean multiselect;
    
    public FileInputOptionAccordionList(JFrame repack, JComponent parent, String type)
    {
        this(repack, parent, type, true);
    }
    
    public FileInputOptionAccordionList(JFrame f, JComponent selectors, String string, boolean b)
    {
        fios = new TreeSet<>(new Comparator<FileInputOption>(){
            @Override
            public int compare(FileInputOption a, FileInputOption b)
            {
                return (int) (a.getID() - b.getID());
            }
        });
        
        click = new JButton(string.toUpperCase() + ": Add a directory or turnin zip file");
        FileInputOptionAccordionList self = this;
        click.addMouseListener(new BubbleUpEventDispatcher(this, new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae)
            {
                click.setEnabled(multiselect);
                FileInputOption fio = new FileInputOption(self, nextID++, 50, 400);
                fio.addMouseListener(new BubbleUpEventDispatcher(self));
                fios.add(fio);
                Dimension d = parent.getPreferredSize();
                d.setSize(d.getWidth(), d.getHeight()+50);
                parent.setPreferredSize(d);
                
                repopulate();
                
                click.setText(string.toUpperCase() + ": Add another directory or turnin zip file");
            }
            
        }));
        
        superParent = f;
        parent = selectors;
        multiselect = b;
        
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
        Dimension d = parent.getPreferredSize();
        d.setSize(d.getWidth(), d.getHeight()-50);
        parent.setPreferredSize(d);
        click.setEnabled(true);
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

    @Override
    public Direction getDialogDirection()
    {
        return Direction.NORTH;
    }

    @Override
    public String getMessageContents()
    {
        return "Use these buttons to select the current class submissions and optionally the archived submissions and common code";
    }
}
