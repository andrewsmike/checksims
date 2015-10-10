package net.lldp.checksims.parse.ast;

public class Monad<T>
{
    private T t;
    
    private Monad(T t)
    {
        this.t = t;
    }
    
    public static <T> Monad<T> wrap(T t)
    {
        return new Monad<>(t);
    }
    
    public static <T> T unwrap(Monad<T> m)
    {
        return m.t;
    }
    
    public void set(T t)
    {
        this.t = t;
    }
}
