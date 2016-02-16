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
 * Copyright (c) 2014-2015 Nicholas DeMarinis, Matthew Heon, and Dolan Murvihill
 */

package net.lldp.checksims.parse.token.tokenizer;

import net.lldp.checksims.parse.token.ConcreteToken;
import net.lldp.checksims.parse.token.TokenList;
import net.lldp.checksims.parse.token.TokenType;
import net.lldp.checksims.util.data.Range;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Split a file into tokens based on spaces.
 */
public final class WhitespaceTokenizer implements Tokenizer {
    private static WhitespaceTokenizer instance;

    private WhitespaceTokenizer() {}

    /**
     * @return Singleton instance of WhitespaceTokenizer
     */
    public static WhitespaceTokenizer getInstance() {
        if(instance == null) {
            instance = new WhitespaceTokenizer();
        }

        return instance;
    }

    /**
     * Split a string into whitespace-delineated tokens.
     *
     * @param string Input string
     * @return List of WHITESPACE tokens representing the input submission
     */
    @Override
    public TokenList splitString(String string) {
        checkNotNull(string);

        TokenList toReturn = new TokenList(this.getType());

        int start = 0;

        for (String t : string.split("\\s+")) {
            if (t.isEmpty())
                continue;

            int len = t.length() + 1; // Err... TODO
            toReturn.add(new ConcreteToken(t, new Range(start, start + len + 1), TokenType.WHITESPACE));
            start += len;
        }

        return toReturn;
    }

    @Override
    public TokenType getType() {
        return TokenType.WHITESPACE;
    }

    @Override
    public String toString() {
        return "Singleton instance of FileSpaceSplitter";
    }
}
