package net.lldp.checksims.util.data;

// Range in a submission. Indexed from zero, excludes end.
public class Range {

    public final int start, end;
    
    public Range()
    {
        this(0, 0);
    }
    
    public Range(int start, int end)
    {
        this.start = start;
        this.end = end;
    }
    
    public int length()
    {
        return end - start;
    }
    
    public boolean includes(Range other)
    {
        return other.start >= start && other.end <= end;
    }
    
    public boolean disjoint(Range other)
    {
        return other.start >= end || other.end <= start;
    }
}
