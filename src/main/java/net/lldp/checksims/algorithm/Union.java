package net.lldp.checksims.algorithm;

public class Union<A, B>
{
    public final A a;
    public final B b;
    
    private Union (A a, B b) {
        this.a = a;
        this.b = b;
    }
    
    public static <A, B> Union<A,B> unionA(A a)
    {
        return new Union<>(a, null);
    }
    
    public static <A, B> Union<A,B> unionB(B b)
    {
        return new Union<>(null, b);
    }
}
