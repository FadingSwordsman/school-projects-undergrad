package com.putable.frobworld.locd011.beings;

import com.putable.frobworld.locd011.beings.interfaces.Placeable;
import com.putable.frobworld.locd011.graphics.Drawable;
import com.putable.frobworld.locd011.simulation.SimulationWorld;

/**
 * An AbstractPlaceable for not implementing a huge number of functions over and
 * over again
 * 
 * @author David
 * 
 */
public abstract class AbstractPlaceable implements Placeable
{
    private final int id;
    private static int nextId = 0;
    private final PlaceType type;
    private int[] location;
    private final SimulationWorld world;

    public AbstractPlaceable(PlaceType type, SimulationWorld world)
    {
	this.type = type;
	id = getNextId();
	this.world = world;
    }

    public AbstractPlaceable(PlaceType type, SimulationWorld world, int[] location)
    {
	this(type, world);
	this.location = location;
    }

    /**
     * Calculate a collisionResult that should be applied to the Frob attempting
     * to move into this spot
     * 
     * @param collider
     * @return
     */
    public abstract CollisionResult collideInto(Frob collider);

    /**
     * Return a drawable representation for this object.
     * 
     * @return
     */
    public Drawable getRepresentation()
    {
	return type.getRepresentation();
    }

    /**
     * Return the ASCII representation of this object for CLI enthusiasts
     * @return
     */
    public char getAscii()
    {
	return type.getAscii();
    }

    /**
     * Return the id of this Placeable
     * @return
     */
    public int getId()
    {
	return id;
    }

    /**
     * Get the next available ID number for a Placeable.
     * @return
     */
    private int getNextId()
    {
	return nextId++;
    }

    public final PlaceType getType()
    {
	return type;
    }

    public final void setLocation(int x, int y)
    {
	location = new int[] { x, y };
    }

    public final int[] getLocation()
    {
	return location;
    }

    public final SimulationWorld getWorld()
    {
	return world;
    }
    
    @Override
    public boolean equals(Object o)
    {
	AbstractPlaceable obj;
	if(o instanceof AbstractPlaceable)
	    obj = (AbstractPlaceable)o;
	else
	    return false;
	return obj.getId() == getId();
    }
}
