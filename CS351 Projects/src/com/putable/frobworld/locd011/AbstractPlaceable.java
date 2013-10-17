package com.putable.frobworld.locd011;

import com.putable.frobworld.locd011.graphics.Drawable;

public abstract class AbstractPlaceable
{
    private int id;
    private static int nextId = 0;
    private PlaceType type;
    private int[] location;
    
    public AbstractPlaceable(PlaceType type)
    {
	this.type = type;
	id = getNextId();
    }
    
    public abstract CollisionResult collideInto();
    public abstract Drawable getRepresentation();
    public int getId()
    {
	return id;
    }
    
    private int getNextId()
    {
	return nextId++;
    }
    
    public PlaceType getType()
    {
	return type;
    }
    
    public int[] getCoordinates()
    {
	return location;
    }
}
