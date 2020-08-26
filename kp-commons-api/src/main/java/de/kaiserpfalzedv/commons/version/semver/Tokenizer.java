/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-present Vincent DURMONT <vdurmont@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.kaiserpfalzedv.commons.version.semver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Utility class to convert a NPM requirement string into a list of tokens.
 */
public class Tokenizer {
    private static final Map<Semver.SemverType, Map<Character, Token>> SPECIAL_CHARS;

    static {
        SPECIAL_CHARS = new HashMap<>();

        for (Semver.SemverType type : Semver.SemverType.values()) {
            SPECIAL_CHARS.put(type, new HashMap<>());
        }

        for (TokenType tokenType : TokenType.values()) {
            if (tokenType.character != null) {
                for (Semver.SemverType type : Semver.SemverType.values()) {
                    if (tokenType.supports(type)) {
                        SPECIAL_CHARS.get(type).put(tokenType.character, new Token(tokenType));
                    }
                }
            }
        }
    }

    /**
     * Takes a NPM requirement string and creates a list of tokens by performing 3 operations:
     * - If the token is a version, it will add the version string
     * - If the token is an operator, it will add the operator
     * - It will insert missing "AND" operators for ranges
     *
     * @param requirement the requirement string
     * @param type        the version system used when tokenizing the requirement
     * @return the list of tokens
     */
    protected static List<Token> tokenize(String requirement, final Semver.SemverType type) {
        Map<Character, Token> specialChars = SPECIAL_CHARS.get(type);

        // Replace the tokens made of 2 chars
        if (type == Semver.SemverType.COCOAPODS) {
            requirement = requirement.replace("~>", "~");
        } else if (type == Semver.SemverType.NPM) {
            requirement = requirement.replace("||", "|");
        }
        requirement = requirement.replace("<=", "≤").replace(">=", "≥");


        LinkedList<Token> tokens = new LinkedList<>();
        Token previousToken = null;

        char[] chars = requirement.toCharArray();
        Token token = null;
        for (char c : chars) {
            if (c == ' ') continue;

            if (specialChars.containsKey(c)) {
                if (token != null) {
                    tokens.add(token);
                    previousToken = token;
                    token = null;
                }

                Token current = specialChars.get(c);
                if (current.type.isUnary() && previousToken != null && previousToken.type == TokenType.VERSION) {
                    // Handling the ranges like "≥1.2.3 <4.5.6" by inserting a "AND" binary operator
                    tokens.add(new Token(TokenType.AND));
                }

                tokens.add(current);
                previousToken = current;
            } else {
                if (token == null) {
                    token = new Token(TokenType.VERSION);
                }
                token.append(c);
            }
        }

        if (token != null) {
            tokens.add(token);
        }

        return tokens;
    }

    /**
     * The different types of tokens (unary operators, binary operators, delimiters and versions)
     */
    protected enum TokenType {
        // Unary operators: ~ ^ = < <= > >=
        TILDE('~', true, Semver.SemverType.COCOAPODS, Semver.SemverType.NPM),
        CARET('^', true, Semver.SemverType.NPM),
        EQ('=', true, Semver.SemverType.NPM),
        LT('<', true, Semver.SemverType.COCOAPODS, Semver.SemverType.NPM),
        LTE('≤', true, Semver.SemverType.COCOAPODS, Semver.SemverType.NPM),
        GT('>', true, Semver.SemverType.COCOAPODS, Semver.SemverType.NPM),
        GTE('≥', true, Semver.SemverType.COCOAPODS, Semver.SemverType.NPM),

        // Binary operators: - ||
        HYPHEN('-', false, Semver.SemverType.NPM),
        OR('|', false, Semver.SemverType.NPM),
        AND(null, false),

        // Delimiters: ( )
        OPENING('(', false, Semver.SemverType.NPM),
        CLOSING(')', false, Semver.SemverType.NPM),

        // Special
        VERSION(null, false);

        public final Character character;
        private final boolean unary;
        private final Semver.SemverType[] supportedTypes;

        TokenType(final Character character, final boolean unary, @SuppressWarnings("CdiInjectionPointsInspection") final Semver.SemverType... supportedTypes) {
            this.character = character;
            this.unary = unary;
            this.supportedTypes = supportedTypes;
        }

        public boolean isUnary() {
            return this.unary;
        }

        public boolean supports(final Semver.SemverType type) {
            for (Semver.SemverType t : this.supportedTypes) {
                if (t == type) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * A token in a requirement string. Has a type and a value if it is of type VERSION
     */
    protected static class Token {
        public final TokenType type;
        public String value;

        public Token(@SuppressWarnings("CdiInjectionPointsInspection") final TokenType type) {
            this(type, null);
        }

        public Token(final TokenType type, final String value) {
            this.type = type;
            this.value = value;
        }

        public void append(final char c) {
            if (value == null) value = "";
            value += c;
        }
    }
}
