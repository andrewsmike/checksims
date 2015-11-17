package net.lldp.checksims.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import net.lldp.checksims.util.completion.StatusLogger;

public class ProgressBarStatusLogger implements StatusLogger
{
    private final JProgressBar progressBar;
    private final JFrame launcherWindow;
    private final JLabel percent;
    private final JLabel eta;
    
    private final long init;
    
    public ProgressBarStatusLogger(JProgressBar progressBar, JLabel percent, JLabel eta, JFrame launcherWindow)
    {
        this.progressBar = progressBar;
        this.launcherWindow = launcherWindow;
        this.eta = eta;
        this.percent = percent;
        
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
            long seconds = (elapsed * (total - currentComplete)) / (currentComplete * 1000);
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
            eta.setText(sb.toString());
        }
        
        progressBar.repaint();
    }

    @Override
    public void end()
    {
        launcherWindow.setVisible(false);
    }

}
