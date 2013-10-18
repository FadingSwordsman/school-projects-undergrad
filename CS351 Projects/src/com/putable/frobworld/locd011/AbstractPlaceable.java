package com.putable.frobworld.locd011;

import com.putable.frobworld.locd011.graphics.Drawable;
import com.putable.frobworld.locd011.simulation.SimulationWorld;

public abstract class AbstractPlaceable
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

	public abstract CollisionResult collideInto(Frob collider);

	public Drawable getRepresentation()
	{
		return type.getRepresentation();
	}
	
	public char getAscii()
	{
		return type.getAscii();
	}

	public int getId()
	{
		return id;
	}

	private int getNextId()
	{
		return nextId++;
	}

	public final PlaceType getType()
	{
		return type;
	}

	public final int[] getCoordinates()
	{
		return location;
	}
	
	public final SimulationWorld getWorld()
	{
		return world;
	}
}
