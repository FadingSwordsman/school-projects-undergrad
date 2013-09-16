package com.putable.siteriter.locd011;

import com.putable.siteriter.SDLParseException;

public class Utility
{
    protected static char undefinedChar = Character.toChars(0)[0];

    public static char processChar(int value) throws SDLParseException
    {
	if (Character.isDefined(value))
	{
	    char[] returnable = Character.toChars(value);
	    if (returnable.length == 1)
		return returnable[0];
	}
	// EOI
	if (value == -1)
	    return Character.toChars(0)[0];
	return Character.toChars(value)[0];
	//throw new SDLParseException("Error in stream! Could not lex <" + value + ">!");
    }
}
