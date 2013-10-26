package com.putable.frobworld.locd011.simulation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

import com.putable.frobworld.locd011.beings.AbstractPlaceable;
import com.putable.frobworld.locd011.beings.Frob;
import com.putable.frobworld.locd011.beings.PlaceType;
import com.putable.frobworld.locd011.beings.interfaces.Liveable;
import com.putable.frobworld.locd011.beings.interfaces.Placeable;
import com.putable.frobworld.locd011.graphics.GraphicsDelta;
import com.putable.frobworld.locd011.graphics.SimulationPanel;
import com.putable.pqueue.PQueue;
import com.putable.pqueue.PQueueAdvanced;

/**
 * A SimulationWorld represents an instance of FrobWorld, and handles all
 * World-level events, such as genesis, rules of the overarching world, etc. For
 * batch mode, it implements Runnable, to allow multiple threads simulating at
 * the same time.
 * 
 * @author David
 * 
 */
public class SimulationWorld implements Runnable
{
    private PQueue interestings;
    private Placeable[][] grid;
    private int width, height;
    private boolean batchMode;
    private final SimulationSettings settings;
    private SimulationResult result;
    private Random prng;
    private int day;
    private JFrame outerPanel;
    private SimulationPanel panel;
    private Timer simulationUpdateTimer;
    private LiveableStatus liveablesRemaining = new LiveableStatus();

    /**
     * Make a simple, random, non-batch run:
     * 
     * @param settings
     */
    public SimulationWorld(SimulationSettings settings)
    {
	this(settings, false, new Random());
    }

    /**
     * Specify the batch run settings:
     * 
     * @param settings
     * @param batchMode
     */
    public SimulationWorld(SimulationSettings settings, boolean batchMode)
    {
	this(settings, batchMode, new Random());
    }

    /**
     * Select a specific random seed:
     * 
     * @param settings
     * @param randomSeed
     */
    public SimulationWorld(SimulationSettings settings, int randomSeed)
    {
	this(settings, false, new Random(randomSeed));
    }

    /**
     * Set batch mode, and a random seed:
     * 
     * @param settings
     * @param batchMode
     * @param randomSeed
     */
    public SimulationWorld(SimulationSettings settings, boolean batchMode, int randomSeed)
    {
	this(settings, batchMode, new Random(randomSeed));
    }

    /**
     * Create a SimulationWorld given the appropriate settings
     * 
     * @param settings
     * @param batchMode
     * @param prng
     */
    public SimulationWorld(SimulationSettings settings, boolean batchMode, Random prng)
    {
	this.settings = settings;
	this.batchMode = batchMode;
	this.prng = prng;
	Initializer init = new Initializer(this);
	init.initSimulation();
    }

    /**
     * Runs a simulation in its entirety. On finishing, returns a simulation
     * result.
     * 
     * @return A SimulationResult describing what happened in the simulation,
     *         and the state of the world at its end.
     */
    public SimulationResult runSimulation()
    {
	while (day < settings.getWorldSettings().getMaxSimulationLength() && liveablesRemaining.getRemainingFrobs() > 0)
	{
	    Iterable<GraphicsDelta> dailyChanges = runDay();
	    if (!batchMode)
	    {
		panel.setDeltaList(dailyChanges);
		updatePanel();
	    }
	}
	if(day >= settings.getWorldSettings().getMaxSimulationLength())
	{
	    while(interestings.size() > 0)
	    {
		Liveable next = (Liveable)interestings.remove();
		if(next.getType() == PlaceType.FROB)
		{
		    liveablesRemaining.addSurvivingFrob((Frob)next);
		}
	    }
		
	}
	result = SimulationResult.makeSimulationResult(this);
	return result;
    }

    private Iterable<GraphicsDelta> runDay()
    {
	List<GraphicsDelta> changes = new LinkedList<GraphicsDelta>();
	day = ((Liveable) interestings.top()).getNextMove();
	if (day > settings.getWorldSettings().getMaxSimulationLength())
	    return null;
	while (interestings.size() > 0 && ((Liveable) interestings.top()).getNextMove() == day)
	{
	    Liveable nextThing = (Liveable) interestings.remove();
	    if(nextThing.getNextMove() < day)
		throw new IllegalStateException("Goddammit");
	    int[] oldLocation = nextThing.getLocation();
	    changes.add(nextThing.takeTurn());
	    int[] newLocation = nextThing.getLocation();
	    put(oldLocation[0], oldLocation[1], null);
	    if (!nextThing.isDead())
	    {
		interestings.insert(nextThing);
		put(newLocation[0], newLocation[1], nextThing);
	    }
	}
	return changes;
    }

    private void updatePanel()
    {
	panel.setCompletedUpdate(false);
	simulationUpdateTimer.start();
	try
	{
	    while (!panel.hasCompletedUpdate())
		Thread.sleep(1);
	}
	catch (InterruptedException e)
	{
	}
	simulationUpdateTimer.stop();
    }

    /**
     * An initializer for the simulation. This is an internal class for a number
     * of reasons. First, many of these setting should not be changeable outside
     * of this class. Second, it's easier to drop all references to a single
     * object instead of all of the things involved separately.
     * 
     * @author David
     * 
     */
    private class Initializer
    {
	private SimulationWorld world;
	int totalItems, totalSpace;

	/**
	 * Create an initializer for the given SimulationWorld.
	 * 
	 * @param world
	 */
	public Initializer(SimulationWorld world)
	{
	    this.world = world;
	}

	/**
	 * Initialize the SimulationWorld, placing all of the appropriate Rocks,
	 * Grass, and Frobs.
	 */
	public void initSimulation()
	{
	    world.interestings = new PQueueAdvanced();
	    world.width = settings.getWorldSettings().getWorldWidth();
	    world.height = settings.getWorldSettings().getWorldHeight();
	    totalSpace = width * height;
	    world.grid = new Placeable[height][width];
	    for (int x = 0; x < world.width; x++)
	    {
		world.put(x, 0, makeRock());
		world.put(x, world.height - 1, makeRock());
		addItem(2);
	    }
	    for (int y = 1; y < world.height - 1; y++)
	    {
		world.put(0, y, makeRock());
		world.put(world.width - 1, y, makeRock());
		addItem(2);
	    }
	    for (int x = 0; x < settings.getWorldSettings().getInitRocks(); x++)
	    {
		int[] location = getRandomLocation();
		world.put(location[0], location[1], makeRock());
		addItem();
	    }
	    for (int x = 0; x < settings.getWorldSettings().getInitGrasses(); x++)
	    {
		int[] location = getRandomLocation();
		Liveable grass = (Liveable) makeGrass();
		world.put(location[0], location[1], grass);
		world.createLiveable(grass);
	    }
	    for (int x = 0; x < settings.getWorldSettings().getInitFrobs(); x++)
	    {
		int[] location = getRandomLocation();
		Liveable frob = (Liveable) makeFrob();
		world.put(location[0], location[1], frob);
		world.createLiveable(frob);
	    }
	    if (!batchMode)
		initializeGraphics(world);
	}

	private void initializeGraphics(SimulationWorld world)
	{
	    outerPanel = new JFrame();
	    outerPanel.setPreferredSize(new Dimension(width * 10, height * 10));
	    world.panel = new SimulationPanel(world);
	    outerPanel.getContentPane().add(panel, BorderLayout.CENTER);
	    outerPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    outerPanel.pack();
	    outerPanel.setResizable(false);
	    outerPanel.setVisible(true);
	    world.simulationUpdateTimer = new Timer(1, panel);
	    world.simulationUpdateTimer.setRepeats(false);
	}

	/**
	 * A helper for creating Rocks
	 * 
	 * @return A default instance of rock.
	 */
	private Placeable makeRock()
	{
	    return PlaceType.ROCK.create(world);
	}

	/**
	 * A helper for creating Frobs
	 * 
	 * @return A default version of a Frob, specified in S.9.1.5
	 */
	private Placeable makeFrob()
	{
	    return PlaceType.FROB.create(world);
	}

	/**
	 * A helper for creating Grass
	 * 
	 * @return A default version of a Grass, specified in S.9.1.4
	 */
	private Placeable makeGrass()
	{
	    return PlaceType.GRASS.create(world);
	}

	/**
	 * A helper to keep track of items
	 */
	public void addItem()
	{
	    addItem(1);
	}

	/**
	 * Another helper, still for keeping track of items.
	 * 
	 * @param number
	 */
	public void addItem(int number)
	{
	    totalItems += number;
	}

	/**
	 * Generate the next random location, if there is one.
	 * 
	 * @throws IllegalStateException
	 *             If there are no more spaces.
	 * @return The next randomly generated location
	 */
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

    /**
     * Get the settings for this simulation run
     * 
     * @return A SimulationSettings object representing the current simulation
     *         settings.
     */
    public SimulationSettings getSimulationSettings()
    {
	return settings;
    }

    /**
     * Place the specified object at the appropriate x, y coordinates
     * 
     * @param x
     * @param y
     * @param toPut
     */
    private void put(int x, int y, Placeable toPut)
    {
	grid[y][x] = toPut;
	if (toPut != null)
	    toPut.setLocation(x, y);
    }

    /**
     * Kill a Liveable in the event it is eaten by a Frob, keeps running into
     * rocks, or can't find anything to eat.
     * 
     * @param toKill
     *            The Liveable which should no longer be referenced by this
     *            object.
     */
    public void killLiveable(Liveable toKill)
    {
	int[] location = toKill.getLocation();
	put(location[0], location[1], null);
	if(toKill.getType() == PlaceType.FROB)
		liveablesRemaining.addFrobDeath(((Frob)toKill).timeAlive());
	if (interestings == toKill.getPQueue())
	    interestings.delete(toKill);
	updateTypes(toKill.getType(), -1);
    }

    /**
     * Notify the world that a new Liveable now exists, and should be tracked
     * from now on.
     * 
     * @param toAdd
     *            A Liveable object spawned somewhere not on this board.
     */
    public void createLiveable(Liveable toAdd)
    {
	interestings.insert(toAdd);
	updateTypes(toAdd.getType(), 1);
	int[] newLocation = toAdd.getLocation();
	put(newLocation[0], newLocation[1], toAdd);
    }

    /**
     * Update the liveablesRemaining object with the specified information
     * 
     * @param type
     *            The PlaceType that corresponds with the updated information
     * @param change
     *            The change in that information
     */
    private void updateTypes(PlaceType type, int change)
    {
	switch (type)
	{
	    case FROB:
		liveablesRemaining.updateFrobs(change);
		break;
	    case GRASS:
		liveablesRemaining.updateGrass(change);
		break;
	    default:
		break;
	}
    }

    /**
     * Get the type of objects at all of the locations adjacent to the specified
     * one.
     * 
     * @param location
     * @return
     */
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

    /**
     * Get the type of the placeable at a certain location
     * 
     * @param x
     * @param y
     * @return
     */
    private PlaceType getPlaceableTypeAt(int x, int y)
    {
	Placeable object = grid[y][x];
	PlaceType objectType = null;
	if (object != null)
	    objectType = object.getType();
	return objectType;
    }

    @Override
    public String toString()
    {
	StringBuffer sb = new StringBuffer();
	for (int y = 0; y < height; y++)
	{
	    for (int x = 0; x < width; x++)
	    {
		if (grid[y][x] != null)
		    sb.append(((AbstractPlaceable) grid[y][x]).getAscii());
		else
		    sb.append(' ');
	    }
	    if (y != grid.length - 1)
		sb.append("\n");
	}
	return sb.toString();
    }

    /**
     * A handler for directions for the SimulationWorld.
     * 
     * @author David
     * 
     */
    public enum Direction
    {
	NORTH(new int[] { 0, -1 }), EAST(new int[] { 1, 0 }), SOUTH(new int[] { 0, 1 }), WEST(new int[] { -1, 0 });
	private int[] delta;

	private Direction(int[] delta)
	{
	    this.delta = delta;
	}

	/**
	 * Get a direction from the index in an adjacency list.
	 * 
	 * @param arrayLocation
	 * @return
	 */
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

	/**
	 * Translate the direction from the given location into a new location
	 * 
	 * @param location
	 * @return
	 */
	public int[] getLocationFrom(int[] location)
	{
	    int x = location[0] + delta[0];
	    int y = location[1] + delta[1];
	    return new int[] { x, y };
	}
    }

    /**
     * Return the current Random object
     * 
     * @return
     */
    public Random getRandom()
    {
	return prng;
    }

    /**
     * Return the current, or final day in the SimulationWorld
     * 
     * @return
     */
    public int getDay()
    {
	return day;
    }

    /**
     * Return a LiveableStatus representing the statistics of living
     * Frobs/Grasses at the current or final time.
     * 
     * @return
     */
    public LiveableStatus getLiveableStatus()
    {
	return liveablesRemaining;
    }

    /**
     * Get the placeable at a specific location
     * 
     * @param location
     * @return
     */
    public Placeable getPlaceableAt(int[] location)
    {
	return grid[location[1]][location[0]];
    }

    @Override
    public void run()
    {
	SimulationResult result = runSimulation();
	this.result = result;
    }

    /**
     * Return the result of this simulation if it has already completed, or null
     * otherwise.
     * 
     * @return
     */
    public SimulationResult getResult()
    {
	if (result != null)
	    return result;
	return null;
    }
}
