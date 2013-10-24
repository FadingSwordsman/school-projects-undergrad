package com.putable.frobworld.locd011.beings;

import java.util.Random;

import com.putable.frobworld.locd011.beings.interfaces.Placeable;
import com.putable.frobworld.locd011.graphics.GraphicsDelta;
import com.putable.frobworld.locd011.graphics.GraphicsDeltaHelper;
import com.putable.frobworld.locd011.simulation.FrobSetting;
import com.putable.frobworld.locd011.simulation.SimulationWorld;
import com.putable.frobworld.locd011.simulation.SimulationWorld.Direction;

public class Frob extends AbstractLiveable
{
    private FrobSetting settings;
    private Genome gene;

    public Frob(SimulationWorld world)
    {
	super(PlaceType.FROB, world, world.getSimulationSettings().getFrobSettings().getGenesisMass());
	Random randomizer = world.getRandom();
	settings = world.getSimulationSettings().getFrobSettings();
	setMass(settings.getGenesisMass());
	gene = new Genome(randomizer);
	setUpdatePeriod(gene.getUpdatePeriod());
	this.updateNextMove(getUpdatePeriod());
    }

    @Override
    public GraphicsDelta takeTurn()
    {
	// TODO: Implement the turn-taking of a Frob
	payMassTax();
	if (isDead())
	    return GraphicsDeltaHelper.removeAt(getLocation());
	PlaceType[] surrounding = getWorld().getAdjacent(getLocation());
	Direction moveAttempt = selectDirection(surrounding);
	GraphicsDelta moveDelta = attemptMove(moveAttempt);
	return isDead() ? GraphicsDeltaHelper.removeAt(getLocation()) : moveDelta;
    }

    private void payMassTax()
    {
	int currentMass = getMass();
	int newMass = currentMass - (settings.getMassTax() * getUpdatePeriod()) / (1000 + settings.getFixedOverhead());
	setMass(newMass);
	attemptToDie();
    }

    private GraphicsDelta attemptMove(Direction inDirection)
    {
	int[] newLocation = inDirection.getLocationFrom(getLocation());
	Placeable atLocation = getWorld().getPlaceableAt(newLocation);
	GraphicsDelta change = GraphicsDeltaHelper.moveTo(getLocation(), newLocation, this);
	if (atLocation != null)
	{
	    CollisionResult collisionChange = atLocation.collideInto(this);
	    change = collisionChange.getGraphicsDelta();
	    if (!applyCollision(collisionChange))
		return change;
	}
	setLocation(newLocation[0], newLocation[1]);
	updateNextMove(getUpdatePeriod());
	return change;
    }

    @Override
    public boolean applyCollision(CollisionResult result)
    {
	setMass(getMass() - result.getMassResult());
	if (getMass() > getBirthMass())
	    setMass(getBirthMass());
	attemptToDie();
	return result.isMoveAllowed();
    }

    public Direction selectDirection(PlaceType[] surrounding)
    {
	int currentPreference = 0;
	short[] preferences = new short[surrounding.length];
	for(int x = 0; x < surrounding.length; x++)
	{
	    PlaceType inLocation = surrounding[x];
	    preferences[x] = gene.getDirectionPrefs(Direction.getDirection(x), inLocation);
	    currentPreference += preferences[x];
	}
	int selection = getWorld().getRandom().nextInt(currentPreference);
	for(int x = 0; x < preferences.length; x++)
	{
	    selection -= preferences[x];
	    if(selection < 0)
		return Direction.getDirection(x);
	}
	throw new IllegalStateException("Frob selected a direction out of bounds! Choice was " + selection + " but highest choice was " + currentPreference);
    }

    @Override
    public CollisionResult collideInto(Frob collider)
    {
	setMass(getMass() - settings.getFrobHitPenalty());
	attemptToDie();
	GraphicsDelta change = isDead() ? GraphicsDeltaHelper.removeAt(getLocation()) : GraphicsDeltaHelper.nothing();
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
