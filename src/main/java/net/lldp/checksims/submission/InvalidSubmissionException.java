package net.lldp.checksims.submission;

public class InvalidSubmissionException extends Exception
{
    public InvalidSubmissionException(Exception e)
    {
        super(e);
    }
}
