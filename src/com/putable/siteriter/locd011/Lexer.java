package com.putable.siteriter.locd011;

import java.io.IOException;
import java.io.Reader;

/**
 * 
 * The interface for a Lexer
 * 
 * @author David
 * 
 * 
 */
public interface Lexer
{
    /**
     * Tokenize the given reader's char into a valid token
     * 
     * @param character
     * @param reader
     * @return
     * @throws IOException
     */
    public String tokenize(char character, Reader reader) throws IOException;

    /**
     * Return the representation of the possible Lexing
     * 
     * @return
     */
    public char getRepresentation();

    /**
     * Check if the given character potentially begins a valid lexing
     * 
     * @param character
     * @return
     */
    public boolean canStart(char character);
}