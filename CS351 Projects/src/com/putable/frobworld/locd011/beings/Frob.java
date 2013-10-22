package com.putable.frobworld.locd011.beings;

import com.putable.frobworld.locd011.beings.interfaces.Placeable;
import com.putable.frobworld.locd011.graphics.GraphicsDelta;
import com.putable.frobworld.locd011.graphics.GraphicsDeltaHelper;
import com.putable.frobworld.locd011.simulation.FrobSetting;
import com.putable.frobworld.locd011.simulation.SimulationWorld;
import com.putable.frobworld.locd011.simulation.SimulationWorld.Direction;

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
    public GraphicsDelta takeTurn()
    {
	// TODO: Implement the turn-taking of a Frob
	payMassTax();
	if(isDead())
	    return GraphicsDeltaHelper.removeAt(getLocation());
	PlaceType[] surrounding = getWorld().getAdjacent(getLocation());
	Direction moveAttempt = selectDirection(surrounding);
	return attemptMove(moveAttempt);
    }
    
    private void payMassTax()
    {
	int currentMass = getMass();
	int newMass = currentMass - (settings.getMassTax() * getUpdatePeriod())/(1000 + settings.getFixedOverhead());
	setMass(newMass);
	attemptToDie();
    }
    
    private GraphicsDelta attemptMove(Direction inDirection)
    {
	int[] newLocation = inDirection.getLocationFrom(getLocation());
	Placeable atLocation = getWorld().getPlaceableAt(newLocation);
	GraphicsDelta change = GraphicsDeltaHelper.nothing();
	if(atLocation != null)
	{
	    CollisionResult collisionChange = atLocation.collideInto(this);
	    change = collisionChange.getGraphicsDelta();
	    if(!applyCollision(collisionChange))
		return change;
	}
	this.setLocation(newLocation[0], newLocation[1]);
	GraphicsDelta frobChange = GraphicsDeltaHelper.updateLiveables(this);
	return GraphicsDeltaHelper.append(change, frobChange);
    }

    @Override
    public boolean applyCollision(CollisionResult result)
    {
	setMass(getMass() - result.getMassResult());
	if(getMass() > getBirthMass())
	    setMass(getBirthMass());
	attemptToDie();
	return result.isMoveAllowed();
    }
    
    public Direction selectDirection(PlaceType[] surrounding)
    {
	int nextDirection = getWorld().getRandom().nextInt(4);
	return Direction.getDirection(nextDirection);
    }

    @Override
    public CollisionResult collideInto(Frob collider)
    {
	setMass(getMass() - settings.getFrobHitPenalty());
	attemptToDie();
	GraphicsDelta change = isDead() ? GraphicsDeltaHelper.updateLiveables(this) : GraphicsDeltaHelper.nothing();
	return new CollisionResult(0, false, change);
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
