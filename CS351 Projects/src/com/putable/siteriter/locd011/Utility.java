package com.putable.siteriter.locd011;

import com.putable.siteriter.SDLParseException;

/**
 * A Utility class for simpler static methods.
 * @author David
 *
 */
public class Utility
{
    /**
     * Handle all int to char processing by converting it directly 
     * @param value
     * @return
     * @throws SDLParseException
     */
    public static char processChar(int value) throws SDLParseException
    {
	return (char) value;
    }
}
