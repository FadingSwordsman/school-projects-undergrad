package com.putable.frobworld.locd011.beings;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.putable.frobworld.locd011.beings.interfaces.Placeable;
import com.putable.frobworld.locd011.graphics.Drawable;
import com.putable.frobworld.locd011.graphics.GraphicsDelta;
import com.putable.frobworld.locd011.graphics.GraphicsDeltaHelper;
import com.putable.frobworld.locd011.graphics.SimulationPanel.Translation;
import com.putable.frobworld.locd011.simulation.FrobSetting;
import com.putable.frobworld.locd011.simulation.SimulationWorld;
import com.putable.frobworld.locd011.simulation.SimulationWorld.Direction;

public class Frob extends AbstractLiveable
{
    private FrobSetting settings;
    private Genome gene;
    private int birthPercent;
    private int birthdate;
    private int deathdate = -1;
    private List<Frob> children = new LinkedList<Frob>();

    public Frob(SimulationWorld world)
    {
	super(PlaceType.FROB, world, world.getSimulationSettings().getFrobSettings().getGenesisMass());
	Random randomizer = world.getRandom();
	settings = world.getSimulationSettings().getFrobSettings();
	setMass(settings.getGenesisMass());
	gene = new Genome(randomizer);
	setBirthMass((gene.getBirthMass() >> 1) + 20);
	birthPercent = (gene.getBirthPercent() * 100) / 255;
	setUpdatePeriod((gene.getUpdatePeriod() % 32) + 5);
	updateNextMove(getUpdatePeriod());
	birthdate = 0;
    }

    public Frob(SimulationWorld world, Genome gene, int newMass)
    {
	super(PlaceType.FROB, world, gene.getBirthMass());
	settings = world.getSimulationSettings().getFrobSettings();
	this.gene = gene;
	setMass(newMass);
	setUpdatePeriod((gene.getUpdatePeriod() % 32) + 5);
	updateNextMove(getWorld().getRandom().nextInt(getUpdatePeriod()) + 1);
	birthdate = world.getDay();
    }

    @Override
    public GraphicsDelta takeTurn()
    {
	payMassTax();
	if (isDead())
	    return GraphicsDeltaHelper.removeAt(getLocation());
	int[] previousLocation = getLocation();
	PlaceType[] surrounding = getWorld().getAdjacent(previousLocation);
	Direction moveAttempt = selectDirection(surrounding);
	GraphicsDelta moveDelta = attemptMove(moveAttempt, previousLocation);
	return isDead() ? GraphicsDeltaHelper.removeAt(getLocation()) : moveDelta;
    }

    private void payMassTax()
    {
	int currentMass = getMass();
	int newMass = currentMass - ((settings.getMassTax() * getUpdatePeriod()) / (1000) + settings.getFixedOverhead());
	setMass(newMass);
	attemptToDie();
    }

    private GraphicsDelta attemptMove(Direction inDirection, int[] previousLocation)
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
	    if (atLocation.getType() == PlaceType.GRASS)
	    {
		if (getMass() > getBirthMass())
		    setMass(getBirthMass());
		change = GraphicsDeltaHelper.append(change, attemptSplit(previousLocation));
	    }
	}
	setLocation(newLocation[0], newLocation[1]);
	updateNextMove(getUpdatePeriod());
	return change;
    }

    private GraphicsDelta attemptSplit(int[] location)
    {
	if (getMass() >= getBirthMass())
	{
	    int birthMass = (getMass() * this.birthPercent) / 100;
	    SimulationWorld world = getWorld();
	    Frob newFrob = new Frob(world, new Genome(getWorld().getRandom(), gene, settings.getDnaMutationOdds()), birthMass);
	    children.add(newFrob);
	    newFrob.setLocation(location[0], location[1]);
	    world.createLiveable(newFrob);
	    setMass(getMass() - birthMass);
	    return GraphicsDeltaHelper.updateLiveables(newFrob);
	}
	return GraphicsDeltaHelper.nothing();
    }

    @Override
    public boolean applyCollision(CollisionResult result)
    {
	setMass(getMass() - result.getMassResult());
	attemptToDie();
	return result.isMoveAllowed();
    }

    public Direction selectDirection(PlaceType[] surrounding)
    {
	int currentPreference = 0;
	int[] preferences = new int[surrounding.length];
	for (int x = 0; x < surrounding.length; x++)
	{
	    PlaceType inLocation = surrounding[x];
	    preferences[x] = gene.getDirectionPrefs(Direction.getDirection(x), inLocation);
	    currentPreference += preferences[x];
	}
	int selection = getWorld().getRandom().nextInt(currentPreference);
	for (int x = 0; x < preferences.length; x++)
	{
	    selection -= preferences[x];
	    if (selection < 0)
		return Direction.getDirection(x);
	}
	throw new IllegalStateException("Frob selected a direction out of bounds! Choice was " + selection + " but highest choice was " + currentPreference);
    }

    @Override
    public Drawable getRepresentation()
    {
	int tempBirthMass = getBirthMass();
	final int birthMass = tempBirthMass > 0 ? tempBirthMass : 1;
	final int mass = getMass() > birthMass ? birthMass : getMass();
	return new Drawable()
	{
	    @Override
	    public void drawItem(Graphics g, Translation t, int[] location)
	    {
		if (birthMass > 0)
		{
		    int[] drawingCoordinates = t.translateCoordinates(location);
		    int width = (mass * drawingCoordinates[2]) / birthMass;
		    if (width < 1)
			width = 1;
		    int height = (mass * drawingCoordinates[3]) / birthMass;
		    if (height < 1)
			height = 1;
		    int x = drawingCoordinates[0] + ((drawingCoordinates[2] - width) >> 1);
		    int y = drawingCoordinates[1] + ((drawingCoordinates[3] - height) >> 1);
		    g.setColor(Color.RED);
		    g.fillOval(x, y, width, height);
		}
	    }
	};
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
    
    public void die()
    {
	deathdate = getWorld().getDay();
	super.die();
    }

    public int timeAlive()
    {
	if(!isDead())
	    return getWorld().getDay() - birthdate;
	return deathdate - birthdate;
    }

    public List<Frob> getChildren()
    {
	return children;
    }

    public Genome getGenome()
    {
	return gene;
    }
}
