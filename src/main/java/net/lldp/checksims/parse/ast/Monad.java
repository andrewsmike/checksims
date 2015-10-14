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

/**
 * 
 * @author ted
 *
 * A class for wrapping types for times when the final
 * property is both required by method contract, but
 * makes code significantly more annoying to write.
 *
 * @param <T> The wrapped type of this monad
 */
public class Monad<T>
{
    private T t;
    
    private Monad(T t)
    {
        this.t = t;
    }
    
    /**
     * Create a monad from an object
     * @param t the object to wrap in a monad
     * @return a monad wrapping this object
     */
    public static <T> Monad<T> wrap(T t)
    {
        return new Monad<>(t);
    }
    
    /**
     * Extract value from monad
     * @param m a monad to unwrap
     * @return the value the monad is wrapping
     */
    public static <T> T unwrap(Monad<T> m)
    {
        return m.t;
    }
    
    /**
     * Change the value of a monad
     * @param t the new value of the monad
     */
    public void set(T t)
    {
        this.t = t;
    }
}
