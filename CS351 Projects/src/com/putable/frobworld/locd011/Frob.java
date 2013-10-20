package com.putable.frobworld.locd011;

import com.putable.frobworld.locd011.graphics.Drawable;
import com.putable.frobworld.locd011.graphics.GraphicsDelta;
import com.putable.frobworld.locd011.simulation.FrobSetting;
import com.putable.frobworld.locd011.simulation.SimulationWorld;

public class Frob extends AbstractLiveable
{ 
	private FrobSetting settings;
	
	public Frob(SimulationWorld world)
	{
		super(PlaceType.FROB, world, world.getSimulationSettings().getFrobSettings().getGenesisMass());
		settings = world.getSimulationSettings().getFrobSettings();
		setMass(settings.getGenesisMass());
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
		return false;
	}

	@Override
	public CollisionResult collideInto(Frob collider)
	{
		setMass(getMass() - settings.getFrobHitPenalty());
		if(getMass() <= 0)
		    die();
		return new CollisionResult(0, false);
	}
	
	public void die()
	{
	    super.die();
	    getWorld().killLiveable(this);
	}
}
