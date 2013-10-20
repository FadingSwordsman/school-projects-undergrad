package com.putable.frobworld.locd011;

import java.util.EnumSet;

import javax.xml.stream.Location;

import com.putable.frobworld.locd011.graphics.Drawable;
import com.putable.frobworld.locd011.graphics.GraphicsDelta;
import com.putable.frobworld.locd011.graphics.GraphicsDeltaHelper;
import com.putable.frobworld.locd011.simulation.GrassSetting;
import com.putable.frobworld.locd011.simulation.SimulationWorld;
import com.putable.frobworld.locd011.simulation.SimulationWorld.Direction;

public class Grass extends AbstractLiveable
{
    private GrassSetting settings;

    public Grass(SimulationWorld world)
    {
	super(PlaceType.GRASS, world, world.getSimulationSettings().getGrassSettings().getGrassBirthMass());
	settings = world.getSimulationSettings().getGrassSettings();
	setMass(settings.getGenesisMass());
	updateNextMove(settings.getGrassInitialUpdatePeriod());
    }
    
    public Grass(SimulationWorld world, int[] location, int updatePeriod, int mass)
    {
	super(PlaceType.GRASS, world, location, updatePeriod, world.getSimulationSettings().getGrassSettings().getGrassBirthMass(), mass);
	settings = world.getSimulationSettings().getGrassSettings();
    }

    @Override
    public CollisionResult collideInto(Frob collider)
    {
	getWorld().killLiveable(this);
	return new CollisionResult(-getMass(), true);
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
	int newMass = getMass();
	GraphicsDelta graphicsChange = GraphicsDeltaHelper.nothing();
	newMass -= (settings.getMassTax()*getUpdatePeriod())/(1000 + settings.getFixedOverhead());
	setMass(newMass);
	if(newMass <= 0)
	{
	    getWorld().killLiveable(this);
	    return GraphicsDeltaHelper.removeAt(getLocation());
	}
	if(newMass > getBirthMass())
	{
	    if(canSplit())
	    {
		Grass newGrass = doSplit();
		graphicsChange = GraphicsDeltaHelper.updateLiveables(this, newGrass);
		getWorld().createLiveable(newGrass);
	    }
	    else
	    {
		setMass(getBirthMass());
		int nextUpdate = getUpdatePeriod() << 1;
		if(nextUpdate > settings.getGrassMaxUpdatePeriod())
		    nextUpdate = settings.getGrassMaxUpdatePeriod();
		setUpdatePeriod(nextUpdate);
	    }
	}
	updateNextMove(getNextUpdate());
	return graphicsChange;
    }


    private boolean canSplit()
    {
	PlaceType[] adjacent = getWorld().getAdjacent(getLocation());
	int adjacentGrass = 0;
	int emptySpace = 0;
	for(PlaceType type : adjacent)
	{
	    if(type == null)
		emptySpace++;
	    else if(type == PlaceType.GRASS)
		adjacentGrass++;
	}
	return emptySpace > 0 && adjacentGrass < settings.getGrassCrowdLimit();
    }
    
    private Grass doSplit()
    {
	PlaceType[] adjacent = getWorld().getAdjacent(getLocation());
	EnumSet<Direction> nextPlaces = EnumSet.noneOf(Direction.class);
	int empty = 0;
	for(int x = 0; x < 4; x++)
	{
	    if(adjacent[x] == null)
	    {
		empty++;
		nextPlaces.add(Direction.getDirection(x));
	    }
	}
	Direction[] moves = new Direction[nextPlaces.size()];
	nextPlaces.toArray(moves);
	Direction nextPlace = moves[getWorld().getRandom().nextInt(moves.length)];
	int[] newLocation = nextPlace.getLocationFrom(getLocation());
	
	return new Grass(getWorld(), newLocation, empty, empty);
    }
    
    public int getNextUpdate()
    {
	return getWorld().getDay() + this.getUpdatePeriod();
    }
    
    @Override
    public boolean applyCollision(CollisionResult result)
    {
	return false;
    }
}
