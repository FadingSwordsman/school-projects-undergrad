package com.putable.frobworld.locd011.beings;

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
	//TODO: Implement this to make a drawable for a Frob.
	return null;
    }

    @Override
    public GraphicsDelta takeTurn()
    {
	// TODO: Implement the turn-taking of a Frob
	return null;
    }

    @Override
    public boolean applyCollision(CollisionResult result)
    {
	setMass(getMass() - result.getMassResult());
	attemptToDie();
	return result.isMoveAllowed();
    }

    @Override
    public CollisionResult collideInto(Frob collider)
    {
	setMass(getMass() - settings.getFrobHitPenalty());
	attemptToDie();
	return new CollisionResult(0, false);
    }
    
    /**
     * Handle logic for dying at varied places
     */
    private void attemptToDie()
    {
	if (getMass() <= 0)
	    die();
    }
}
