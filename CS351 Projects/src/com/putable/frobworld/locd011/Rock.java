package com.putable.frobworld.locd011;

import com.putable.frobworld.locd011.graphics.Drawable;
import com.putable.frobworld.locd011.simulation.SimulationWorld;

public final class Rock extends AbstractPlaceable
{
	
	public Rock(SimulationWorld world)
	{
		super(PlaceType.ROCK, world);
	}

	@Override
	public CollisionResult collideInto(Frob collider)
	{
		return new CollisionResult(-getWorld().getSimulationSettings().getMiscSettings().getRockBumpPenalty(), false);
	}

	@Override
	public Drawable getRepresentation()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
