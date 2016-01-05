package net.lldp.checksims.ui.results;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import net.lldp.checksims.algorithm.InternalAlgorithmError;
import net.lldp.checksims.algorithm.similaritymatrix.SimilarityMatrix;
import net.lldp.checksims.algorithm.similaritymatrix.output.MatrixPrinter;
import net.lldp.checksims.ui.results.mview.QuickSortableMatrixViewer;

public class GraphicalMatrixPrinter implements MatrixPrinter
{

    @Override
    public String getName()
    {
        return "GraphicalMatrixPrinter";
    }

    @Override
    public String printMatrix(SimilarityMatrix matrix) throws InternalAlgorithmError
    {
        SortableMatrix sm = new SortableMatrix(matrix);
        
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(true);
        
        f.add(new ScrollViewer(new QuickSortableMatrixViewer(sm), f), BorderLayout.CENTER);
        f.pack();
        
        f.setVisible(true);
        
        return "see window for details";
    }

}
