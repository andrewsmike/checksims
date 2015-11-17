package net.lldp.checksims.util.completion;

public interface StatusLogger
{
    void logStatus(long currentComplete, long total);

    void end(); 
}
