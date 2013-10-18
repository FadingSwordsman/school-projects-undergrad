package com.putable.frobworld.locd011;

import com.putable.frobworld.locd011.graphics.Drawable;
import com.putable.frobworld.locd011.graphics.GraphicsDelta;
import com.putable.frobworld.locd011.simulation.SimulationWorld;

public class Frob extends AbstractPlaceable implements Liveable
{
	private int health; 
	
	public Frob(SimulationWorld world)
	{
		super(PlaceType.FROB, world);
		health = 10;
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

	@Override
	public boolean applyCollision(CollisionResult result)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CollisionResult collideInto(Frob collider)
	{
		health -= collider.getHealth();
		return new CollisionResult(0, false);
	}

	@Override
	public void spawn()
	{
		// TODO Auto-generated method stub

	}
	
	public int getHealth()
	{
		return health;
	}
}
