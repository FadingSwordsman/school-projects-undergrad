package com.putable.frobworld.locd011;

import com.putable.frobworld.locd011.graphics.Drawable;
import com.putable.frobworld.locd011.graphics.GraphicsDelta;
import com.putable.frobworld.locd011.simulation.SimulationWorld;

public class Grass extends AbstractPlaceable implements Liveable
{
	private int health;
	
	public Grass(SimulationWorld world)
	{
		super(PlaceType.GRASS, world);
	}

	@Override
	public CollisionResult collideInto(Frob collider)
	{
		return new CollisionResult(-health, true);
	}

	@Override
	public Drawable getRepresentation()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GraphicsDelta takeTurn()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void spawn()
	{
		
	}

	@Override
	public boolean applyCollision(CollisionResult result)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public int getHealth()
	{
		return health;
	}
}
