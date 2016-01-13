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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import net.lldp.checksims.util.completion.StatusLogger;

/**
 * 
 * @author ted
 *
 */
public class ProgressBarStatusLogger implements StatusLogger
{
    private final JProgressBar progressBar;
    private final JFrame launcherWindow;
    private final JLabel percent;
    private final JLabel eta;
    private final JLabel elapsed;
    private final long init;
    
    /**
     * A status Logger for checksims that outputs to a Swing ProgressBar
     * @param progressBar the progress bar to update
     * @param percent the percent label to update
     * @param eta the eta label to update
     * @param elapsed the elapsed label to update
     * @param launcherWindow the frame to fiddle with
     */
    public ProgressBarStatusLogger(JProgressBar progressBar,
            JLabel percent, JLabel eta,
            JLabel elapsed, JFrame launcherWindow)
    {
        this.progressBar = progressBar;
        this.launcherWindow = launcherWindow;
        this.eta = eta;
        this.percent = percent;
        this.elapsed = elapsed;
        
        init = System.currentTimeMillis();
    }

    @Override
    public void logStatus(long currentComplete, long total)
    {
        long elapsed = System.currentTimeMillis() - init;
        progressBar.setMaximum((int) total);
        progressBar.setValue((int) currentComplete);
        percent.setText((((float)currentComplete)/ total) * 100 + "%");
        if (currentComplete == 0)
        {
            eta.setText("eta: na");
        }
        else
        {
            long msr = (elapsed * (total - currentComplete)) / (currentComplete);
            
            this.eta.setText("Estimated remaining time: " + timeHumanReadable(msr));
            this.elapsed.setText("Elapsed Time: "+ timeHumanReadable(elapsed));
        }
        
        progressBar.repaint();
    }

    @Override
    public void end()
    {
        launcherWindow.setVisible(false);
    }
    
    private final String timeHumanReadable(long ms)
    {
        long seconds = ms / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        StringBuilder sb = new StringBuilder();
        if (hours > 0)
        {
            sb.append(hours).append(" hours, ");
            minutes %= 60;
            seconds %= 3600;
        }
        
        if (minutes > 0)
        {
            sb.append(minutes).append(" minutes, ");
            seconds %= 60;
        }
        
        if (seconds > 0)
        {
            sb.append(seconds).append(" seconds, ");
        }
        
        return sb.toString();
    }

}
