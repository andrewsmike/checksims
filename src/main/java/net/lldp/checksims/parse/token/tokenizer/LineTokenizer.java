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
import net.lldp.checksims.util.data.Monad;
import net.lldp.checksims.util.data.Range;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Splits a file on a line-by-line basis.
 */
public final class LineTokenizer implements Tokenizer {
    private static LineTokenizer instance;

    private LineTokenizer() {}

    /**
     * @return Singleton instance of LineTokenizer
     */
    public static LineTokenizer getInstance() {
        if(instance == null) {
            instance = new LineTokenizer();
        }

        return instance;
    }

    /**
     * Split string into newline-delineated tokens.
     *
     * @param string String to split
     * @return List of LINE tokens representing the input string
     */
    @Override
    public TokenList splitString(String string) {
        checkNotNull(string);

        TokenList toReturn = new TokenList(this.getType());

        if(string.isEmpty()) {
            return toReturn;
        }

        int start = 0;

        for (String l : string.split("\n")) {
            int len = l.length() + 1;
            toReturn.add(new ConcreteToken(l, new Range(start, start + len + 1), TokenType.LINE));
            start += len + 1;
        }

        return toReturn;
    }

    @Override
    public TokenType getType() {
        return TokenType.LINE;
    }

    @Override
    public String toString() {
        return "Singleton FileLineSplitter instance";
    }
}
