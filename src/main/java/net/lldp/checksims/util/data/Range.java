package net.lldp.checksims.util.data;

// Range in a submission. Indexed from zero, excludes end.
public class Range implements Comparable<Range> {

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

    @Override
    public int hashCode()
    {
        return start << 16 | end;
    }

    @Override
    public int compareTo(Range o)
    {
        if (this.start < o.start)
        {
            return -1;
        } else if (this.start > o.start)
        {
            return 1;
        } else
        {
            if (this.end < o.end)
                return -1;
            else if (this.end > o.end)
                return 1;
            else
                return 0;
        }
    }
    
    @Override
    public String toString()
    {
        return "(" + start + ":" + end + ")";
    }
}
