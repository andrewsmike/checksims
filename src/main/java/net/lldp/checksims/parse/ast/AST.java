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
 * Copyright (c) 2015 Ted Meyer and Michael Andrews
 */
package net.lldp.checksims.parse.ast;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import net.lldp.checksims.parse.Percentable;
import net.lldp.checksims.util.data.Monad;
import net.lldp.checksims.util.data.Real;
import net.lldp.checksims.util.data.Range;


import static net.lldp.checksims.util.data.Monad.unwrap;
import static net.lldp.checksims.util.data.Monad.wrap;

/**
 * This is where the magic happens
 * @author ted
 *
 * The AST structure for all parsers to target
 */
public class AST implements Percentable
{
    private final String tag;
    private final Set<AST> asts;
    private final Set<Integer> hashes;
    private final Integer hashCode;
    private final Map<Integer, AST> fingerprints;
    private final Monad<AST> parent = wrap(null);
    private final Range interval; // Range of document under this AST.
    
    // scoring heuristics?
    public final Integer size;
    private final Integer depth;
    
    /**
     * Creates an AST with variadic children
     * @param tag the name/tag of the AST
     * @param children an array or variadic number of children
     */
    public AST(String tag, AST ... children)
    {
        this(tag, new Range(), Arrays.asList(children).stream());
    }
    
    /**
     * Creates an AST with variadic children, document region.
     * @param tag the name/tag of the AST
     * @param children an array or variadic number of children
     * @param interval Range in submission.
     */
    public AST(String tag, Range interval, AST ... children)
    {
        this(tag, interval, Arrays.asList(children).stream());
    }
    
    /**
     * Creates an AST from a stream of children
     * @param tag the name/tag of the AST
     * @param children a stream containing children to add to the AST
     */
    public AST(String tag, Stream<AST> children)
    {
        this(tag, new Range(), children);
    }
    
    /**
     * Creates an AST from a stream of children
     * @param tag the name/tag of the AST
     * @param children a stream containing children to add to the AST
     * @param interval Range in submission.
     */
    public AST(String tag, Range interval, Stream<AST> children)
    {
        this.tag = tag;
        this.asts = new HashSet<>();
        this.hashes = new HashSet<>();
        this.fingerprints = new HashMap<>();
        this.interval = interval;
        
        Monad<Integer> size = wrap(0);
        Monad<Integer> depth = wrap(0);
        
        children.forEach(A -> {
            asts.add(A);
            hashes.add(A.hashCode());
            size.set(unwrap(size) + A.size);
            depth.set(A.depth > unwrap(depth) ? A.depth: unwrap(depth));
            A.parent.set(this);
        });
        
        this.size = unwrap(size)+1;
        this.depth = unwrap(depth)+1;
        hashCode = _hashCode();
    }
    
    /**
     * MUST BE CALLED AFTER GENERATION
     */
    public AST cacheFingerprinting()
    {
        fingerprint(wrap(fingerprints));
        return this;
    }
    
    /** 
     * @return gets a mapping for all nodes in this tree of all fingerprints
     */
    public Map<Integer, AST> getFingerprints()
    {
        return Collections.unmodifiableMap(fingerprints);
    }
    
    /**
     * Store fingerprints in the provided Map
     * @param fpdb a monad of a map in which the fingerprints will be stored
     */
    public void fingerprint(final Monad<Map<Integer, AST>> fpdb)
    {
        AST t = 
        unwrap(fpdb).put(hashCode(), this); // yes this may overwrite collisions
                                            // no we do not care
        
        if (t != null && !equals(t)) // OK maybe we do care...
        {
            System.out.println("HASH COLLISION!!!  mapsize("+unwrap(fpdb).size()+")");
        }
        
        asts.stream().forEach(A -> A.fingerprint(fpdb));
    }
    
    /**
     * given a mapping of another tree's fingerprints, generate a similarity score
     * @param fpdb the map of the other tree's fingerprints
     * @return A percentage representing the amount of similarity between the two trees
     */
    public Real getPercentMatched(Map<Integer, AST> fpdb)
    {
        Real result = null;
        if (equals(fpdb.get(hashCode())))
        {
            result = new Real(size, size);
        }
        else
        {
            result = new Real(0, size);
        }
        
        for(AST t : asts)
        {
            result = result.scoreSummation(t.getPercentMatched(fpdb));
        }
        
        return result;
    }

    /**
     * Get the range covered by this AST node.
     * @return Range of submission covered by AST.
     */
    public Range getRange()
    {
        return interval;
    }

    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return hashCode;
    }
    
    /*
     * AUTO GENERATED BY ECLIPSE
     * @see java.lang.Object#hashCode()
     */
    private int _hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hashes == null) ? 0 : hashes.hashCode());
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
        return result;
    }

    /*
     * AUTO GENERATED BY ECLIPSE
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AST other = (AST) obj;
        if (hashes == null)
        {
            if (other.hashes != null)
                return false;
        } else if (!hashes.equals(other.hashes))
            return false;
        if (tag == null)
        {
            if (other.tag != null)
                return false;
        } else if (!tag.equals(other.tag))
            return false;
        return true;
    }

    @Override
    public Real getPercentageMatched()
    {
        throw new RuntimeException("cannot evaluate getPercentMatched()");
    }

    /**
     * @return the parent of the AST, or null if it has no parent
     */
    public AST getParent()
    {
        return unwrap(parent);
    }
    
    /**
     * @return the AST tag.
     */
    public String getTag()
    {
        return tag;
    }
    
    /**
     * @return a collection of the children of this AST node
     */
    public Collection<AST> getChildren()
    {
        return Collections.unmodifiableSet(asts);
    }
}
