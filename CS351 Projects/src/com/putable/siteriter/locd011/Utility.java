package com.putable.siteriter.locd011;

import com.putable.siteriter.SDLParseException;

public class Utility
{
    protected static char undefinedChar = Character.toChars(0)[0];

    public static char processChar(int value) throws SDLParseException
    {
	return (char)value;
    }
}
