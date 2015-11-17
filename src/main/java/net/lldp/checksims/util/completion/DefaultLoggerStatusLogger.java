package net.lldp.checksims.util.completion;

import net.lldp.checksims.util.data.Monad;

import org.slf4j.Logger;

import static net.lldp.checksims.util.data.Monad.wrap;
import static net.lldp.checksims.util.data.Monad.unwrap;

public class DefaultLoggerStatusLogger implements StatusLogger
{
    private final Monad<Logger> logs = wrap(null);
    
    public void setLogger(Logger l)
    {
        logs.set(l);
    }
    
    @Override
    public void logStatus(long completed, long total)
    {
        if (unwrap(logs) != null)
        {
            unwrap(logs).info("Processed " + completed + "/" + total + " tasks");
        }
    }

    @Override
    public void end()
    {
        // do nothing
    }

}
