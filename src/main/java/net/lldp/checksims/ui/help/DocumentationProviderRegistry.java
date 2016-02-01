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
 * Copyright (c) 2014-2016 Ted Meyer, Nicholas DeMarinis, Matthew Heon, and Dolan Murvihill
 */
package net.lldp.checksims.ui.help;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A central collection site for all Documentation providers, so that they may all be toggled on and off at once
 * @author ted
 *
 */
public class DocumentationProviderRegistry
{
    private static Set<DocumentationProvider> dps = new HashSet<>();
    
    /**
     * add a documentation provider
     * @param dp
     */
    public static void addSelf(DocumentationProvider dp)
    {
        dps.add(dp);
    }

    /**
     * get all documentation providers
     * @return
     */
    public static Set<DocumentationProvider> getAll()
    {
        return Collections.unmodifiableSet(dps);
    }
}
