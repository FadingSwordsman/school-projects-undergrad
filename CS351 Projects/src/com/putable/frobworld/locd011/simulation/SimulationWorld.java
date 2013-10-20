package com.putable.frobworld.locd011.simulation;

import java.util.Random;

import com.putable.frobworld.locd011.AbstractPlaceable;
import com.putable.frobworld.locd011.Liveable;
import com.putable.frobworld.locd011.PlaceType;
import com.putable.frobworld.locd011.Placeable;
import com.putable.pqueue.PQueue;
import com.putable.pqueue.PQueueAdvanced;

public class SimulationWorld implements Runnable
{
    private PQueue interestings;
    private Placeable[][] grid;
    private int width, height, totalItems, totalSpace;
    private boolean batchMode;
    private final SimulationSettings settings;
    private Random prng;
    private int day;
    private int startingObjects = 0;
    private int grass = 0;
    private int frobs = 0;
    
    public SimulationWorld(SimulationSettings settings)
    {
	this(settings, false, new Random());
    }
    
    public SimulationWorld(SimulationSettings settings, boolean batchMode)
    {
	this(settings, batchMode, new Random());
    }
    
    public SimulationWorld(SimulationSettings settings, int randomSeed)
    {
	this(settings, false, new Random(randomSeed));
    }
    
    public SimulationWorld(SimulationSettings settings, boolean batchMode, int randomSeed)
    {
	this(settings, batchMode, new Random(randomSeed));
    }

    public SimulationWorld(SimulationSettings settings, boolean batchMode, Random prng)
    {
	this.settings = settings;
	this.batchMode = batchMode;
	this.prng = prng;
	Initializer init = new Initializer(this);
	init.initSimulation();
    }

    public SimulationResult runSimulation()
    {
	Initializer init = new Initializer(this);
	init.initSimulation();
	init = null;
	System.out.println(this);
	day = 0;
	while (day < settings.getWorldSettings().getMaxSimulationLength())
	{
	    Liveable nextThing = (Liveable)interestings.remove();
	    day = nextThing.getNextMove();
	    nextThing.takeTurn();
	    if(!nextThing.isDead())
		interestings.insert(nextThing);
	}
	return SimulationResult.makeSimulationResult(this);
    }

    public SimulationSettings getSimulationSettings()
    {
	return settings;
    }
    

    private class Initializer
    {
	private SimulationWorld world;
	
	public Initializer(SimulationWorld world)
	{
	    this.world = world;
	}
	
	public void initSimulation()
	{
	    world.interestings = new PQueueAdvanced();
	    world.width = settings.getWorldSettings().getWorldWidth();
	    world.height = settings.getWorldSettings().getWorldHeight();
	    world.totalSpace = width * height;
	    world.grid = new Placeable[height][width];
	    for (int x = 0; x < world.width; x++)
	    {
		world.put(x, 0, makeRock());
		world.put(x, world.height - 1, makeRock());
		world.addItem(2);
	    }
	    for (int y = 1; y < world.height - 1; y++)
	    {
		world.put(0, y, makeRock());
		world.put(world.width - 1, y, makeRock());
		world.addItem(2);
	    }
	    for (int x = 0; x < settings.getWorldSettings().getInitRocks(); x++)
	    {
		int[] location = getRandomLocation();
		world.put(location[0], location[1], makeRock());
		world.addItem();
	    }
	    for (int x = 0; x < settings.getWorldSettings().getInitGrasses(); x++)
	    {
		int[] location = getRandomLocation();
		Liveable grass = (Liveable)makeGrass();
		world.put(location[0], location[1], grass);
		world.createLiveable(grass);
	    }
	    for (int x = 0; x < settings.getWorldSettings().getInitFrobs(); x++)
	    {
		int[] location = getRandomLocation();
		Liveable frob = (Liveable)makeFrob();
		world.put(location[0], location[1], frob);
		world.createLiveable(frob);
	    }
	}
	
	private Placeable makeRock()
	{
	    return PlaceType.ROCK.create(world);
	}

	private Placeable makeFrob()
	{
	    return PlaceType.FROB.create(world);
	}

	private Placeable makeGrass()
	{
	    return PlaceType.GRASS.create(world);
	}
	
	private int[] getRandomLocation()
	{
	    if (totalItems >= totalSpace)
		throw new IllegalStateException("The grid is full");
	    int x = prng.nextInt(width - 2) + 1;
	    int y = prng.nextInt(height - 2) + 1;
	    while (grid[y][x] != null)
	    {
		x = prng.nextInt(width - 2) + 1;
		y = prng.nextInt(height - 2) + 1;
	    }
	    return new int[] { x, y };
	}
    }

    private void put(int x, int y, Placeable toPut)
    {
	grid[y][x] = toPut;
	if(toPut != null)
	    toPut.setLocation(x, y);
    }
    
    private void move(int x, int y, Liveable toMove)
    {
	int[] currentLocation = toMove.getLocation();
	put(currentLocation[0], currentLocation[1], null);
	put(x, y, toMove);
    }

    public void addItem()
    {
	addItem(1);
    }

    public void addItem(int number)
    {
	totalItems += number;
    }
    
    public void killLiveable(Liveable toKill)
    {
	int[] location = toKill.getLocation();
	put(location[0], location[1], null);
	interestings.delete(toKill);
	addItem(-1);
	updateTypes(toKill.getType(), -1);
    }
    
    public void createLiveable(Liveable toAdd)
    {
	interestings.insert(toAdd);
	addItem();
	updateTypes(toAdd.getType(), 1);
    }
    
    private void updateTypes(PlaceType type, int change)
    {
	switch(type)
	{
		case FROB:
		    frobs += change;
		    System.out.println("Added a frob! " + change);
		    break;
		case GRASS:
		    grass += change;
		    System.out.println("Added a grass! " + change);
		    break;
	}
    }
    
    public PlaceType[] getAdjacent(int[] location)
    {
	PlaceType[] adjacent = new PlaceType[4];
	int x = location[0], y = location[1];
	adjacent[0] = getPlaceableTypeAt(x, y - 1);
	adjacent[1] = getPlaceableTypeAt(x + 1, y);
	adjacent[2] = getPlaceableTypeAt(x, y + 1);
	adjacent[3] = getPlaceableTypeAt(x - 1, y);
	return adjacent;
    }
    
    private PlaceType getPlaceableTypeAt(int x, int y)
    {
	Placeable object = grid[y][x];
	PlaceType objectType = null;
	if(object != null)
	    objectType = object.getType();
	return objectType;
    }

    @Override
    public String toString()
    {
	StringBuffer sb = new StringBuffer();
	for(int y = 0; y < height; y++)
	{
	    for(int x = 0; x < width; x++)
	    {
		if(grid[y][x] != null)
		    sb.append(((AbstractPlaceable)grid[y][x]).getAscii());
		else
		    sb.append(' ');
	    }
	    if(y != grid.length - 1)
		sb.append("\n");
	}
	sb.append("\nRemaining Frobs: ").append(frobs);
	sb.append("\nRemaining Grass: ").append(grass);
	return sb.toString();
    }
    
    public enum Direction
    {
	NORTH(new int[]{0,-1}),
	EAST(new int[]{1,0}),
	SOUTH(new int[]{0,1}),
	WEST(new int[]{-1,0});
	
	private int[] delta;
	
	private Direction(int[] delta)
	{
	    this.delta = delta;
	}
	
	public static Direction getDirection(int arrayLocation)
	{
	    switch (arrayLocation)
	    {
	    case 0:
		return NORTH;
	    case 1:
		return EAST;
	    case 2:
		return SOUTH;
	    case 3:
		return WEST;
	    default:
		throw new IllegalStateException("Invalid direction specified!");
	    }
	}
	
	public int[] getLocationFrom(int[] location)
	{
	    int x = location[0] + delta[0];
	    int y = location[1] + delta[1];
	    return new int[]{x,y};
	}
    }

    public Random getRandom()
    {
	return prng;
    }
    
    public int getDay()
    {
	return day;
    }
    
    @Override
    public void run()
    {
	SimulationResult result = runSimulation();
	if (!batchMode)
	    System.out.println(result);
    }
}
