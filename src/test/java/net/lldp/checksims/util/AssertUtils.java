package net.lldp.checksims.util;

import static org.junit.Assert.*;

public class AssertUtils
{
    public static void betterStringEQAssert(String a, String b)
    {
        assertNotNull(a);
        assertNotNull(b);
        
        if (a.equals(b))
        {
            assertTrue(true);
            return;
        }
        fail("--- Strings found to be unequal (expected/actual) ---\n[" + a + "]\n[" + b + "]\n");
    }
}
