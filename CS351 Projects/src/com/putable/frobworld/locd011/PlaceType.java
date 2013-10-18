package com.putable.frobworld.locd011;

import com.putable.frobworld.locd011.graphics.Drawable;

public enum PlaceType
{
    ROCK('!', null),
    FROB('@', null),
    GRASS('^', null);
    
    private char asciiRepresentation;
    private Drawable representation;
    
    private PlaceType(char ascii, Drawable image)
    {
    	this.asciiRepresentation = ascii;
    	this.representation = image;
    }
    
    public char getAscii()
    {
    	return asciiRepresentation;
    }
    
    public Drawable getRepresentation()
    {
    	//TODO: Implement this and stuff
    	return representation;
    }
}
