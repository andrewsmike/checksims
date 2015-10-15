package net.lldp.checksims.parse.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import net.lldp.checksims.util.data.Monad;
import static net.lldp.checksims.util.data.Monad.unwrap;
import static net.lldp.checksims.util.data.Monad.wrap;

public class OrderedCompareMap<K extends Comparable<K>, V> implements Map<K, V>
{
    final Set<K> keys = new HashSet<>();
    final Monad<ArrayList<Pair<K, V>>> backing = wrap(new ArrayList<>());
    final Monad<Boolean> sorted = wrap(false);
    
    public void sort()
    {
        if (!unwrap(sorted))
        {
            sorted.set(true);
            Collections.sort(unwrap(backing), new Comparator<Pair<K, V>>(){

                @Override
                public int compare(Pair<K, V> a, Pair<K, V> b)
                {
                    return a.getKey().compareTo(b.getKey());
                }
                
            });
        }
    }
    
    @Override
    public void clear()
    {
        keys.clear();
        backing.set(new ArrayList<>());
    }

    @Override
    public boolean containsKey(Object key)
    {
        return keys.contains(key);
    }

    @Override
    public boolean containsValue(Object val)
    {
        throw new RuntimeException("cannot search for value");
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet()
    {
        throw new RuntimeException("cannot get entry set");
    }

    @Override
    public V get(Object arg0)
    {
        throw new RuntimeException("cannoy get by key");
    }

    @Override
    public boolean isEmpty()
    {
        return keys.isEmpty();
    }

    @Override
    public Set<K> keySet()
    {
        throw new RuntimeException("cannot get a keyset");
    }

    @Override
    public V put(K key, V val)
    {
        if (containsKey(key))
        {
            return null;
        }
        if (unwrap(sorted))
        {
            throw new RuntimeException("cannot insert into an already sorted list");
        }
        keys.add(key);
        unwrap(backing).add(Pair.of(key, val));
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> arg0)
    {
        throw new RuntimeException("cannot insert in bulk");
    }

    @Override
    public V remove(Object arg0)
    {
        throw new RuntimeException("cannot remove individual elements");
    }

    @Override
    public int size()
    {
        return keys.size();
    }

    @Override
    public Collection<V> values()
    {
        throw new RuntimeException("cannot get collection of values");
    }

}
