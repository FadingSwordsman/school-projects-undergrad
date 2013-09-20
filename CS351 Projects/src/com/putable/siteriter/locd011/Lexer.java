package com.putable.siteriter.locd011;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public interface Lexer
{
    /**
     * Lex the next Token(s) from the reader, beginning with starting.
     * @param reader
     * @param starting
     * @return
     * @throws IOException
     */
    public List<Tuple<Token, String>> lex(Reader reader, char starting) throws IOException;

    /**
     * Checks to see if a given character is a valid start point for this Lexer.
     * @param toCheck
     * @return
     */
    public boolean canStart(char toCheck);
}
