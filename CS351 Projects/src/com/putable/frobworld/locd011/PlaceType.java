package com.putable.frobworld.locd011;

import com.putable.frobworld.locd011.graphics.Drawable;
import com.putable.frobworld.locd011.simulation.SimulationWorld;

public enum PlaceType
{
    ROCK('R', null, rockCreator()),
    FROB('F', null, frobCreator()),
    GRASS('G', null, grassCreator());
    
    private char asciiRepresentation;
    private Drawable representation;
    private Creator creator;
    
    private PlaceType(char ascii, Drawable image, Creator creator)
    {
    	this.asciiRepresentation = ascii;
    	this.representation = image;
    	this.creator = creator;
    }
    
    public Placeable create(SimulationWorld inWorld)
    {
	return creator.create(inWorld);
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
    
    private interface Creator
    {
	public Placeable create(SimulationWorld inWorld);
    }
    
    private static Creator frobCreator()
    {
	return new Creator()
	{
	    public Placeable create(SimulationWorld inWorld)
	    {
		return new Frob(inWorld);
	    }
	};
    }
    private static Creator grassCreator()
    {
	return new Creator()
	{
	    public Placeable create(SimulationWorld inWorld)
	    {
		return new Grass(inWorld);
	    }
	};
    }
    private static Creator rockCreator()
    {
	return new Creator()
	{
	    public Placeable create(SimulationWorld inWorld)
	    {
		return new Rock(inWorld);
	    }
	};
    }
}
