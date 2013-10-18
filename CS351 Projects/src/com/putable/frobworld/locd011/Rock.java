package com.putable.frobworld.locd011;

import com.putable.frobworld.locd011.graphics.Drawable;

public final class Rock extends AbstractPlaceable
{
	
	public Rock(SimulationWorld world)
	{
		super(PlaceType.ROCK, world);
		
	}

	@Override
	public CollisionResult collideInto(Frob collider)
	{
		return new CollisionResult(-world.getWorldSettings().getRockBumpPenalty(), false);
	}

	@Override
	public Drawable getRepresentation()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
