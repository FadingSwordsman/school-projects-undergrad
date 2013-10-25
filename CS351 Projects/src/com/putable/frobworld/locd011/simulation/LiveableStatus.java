package com.putable.frobworld.locd011.simulation;

import java.util.LinkedList;
import java.util.List;

import com.putable.frobworld.locd011.beings.Frob;

/**
 * Hold the status of Liveables in the SimulationWorld
 * @author David
 *
 */
public class LiveableStatus
{
    private int remainingFrobs;
    private int remainingGrass;
    private List<Integer> timeAlive;
    private List<Frob> livingFrobs;
    
    //Singletons to make calculating statistics quicker:
    private Double averageLife;
    private Integer longestLife;
    private Integer shortestLife;
    private Integer numberFrobsTotal;
    
    /**
     * In the beginning, there was nothing...
     */
    public LiveableStatus()
    {
	remainingFrobs = 0;
	remainingGrass = 0;
	timeAlive = new LinkedList<Integer>();
	livingFrobs = new LinkedList<Frob>();
    }
    
    /**
     * Change the number of Frobs in the world, whether through death or birth
     * @param offset
     */
    public void updateFrobs(int offset)
    {
	remainingFrobs += offset;
    }
    
    /**
     * Change the number of Grass in the world.
     * @param offset
     */
    public void updateGrass(int offset)
    {
	remainingGrass += offset;
    }
    
    public void addFrobDeath(int lifeLength)
    {
	timeAlive.add(lifeLength);
    }
    
    /**
     * Return the number of current Frobs
     * @return
     */
    public int getRemainingFrobs()
    {
	return remainingFrobs;
    }
    
    public void addSurvivingFrob(Frob frob)
    {
	timeAlive.add(frob.timeAlive());
	livingFrobs.add(frob);
    }
    
    /**
     * Return the number of current Grass
     * @return
     */
    public int getRemainingGrass()
    {
	return remainingGrass;
    }
    
    public double getFrobLifeAverage()
    {
	if(averageLife == null)
	    createStats();
	return averageLife;
    }
    
    public int getLongestLivedFrob()
    {
	if(longestLife == null)
	    createStats();
	return longestLife;
    }
    
    public int getShortestLivedFrob()
    {
	if(shortestLife == null)
	    createStats();
	return shortestLife;
    }
    
    private void createStats()
    {
	longestLife = shortestLife = timeAlive.get(0);
	numberFrobsTotal = timeAlive.size();
	averageLife = 0.0;
	
	for(Integer lifespan : timeAlive)
	{
	    if(lifespan > longestLife)
		longestLife = lifespan;
	    else if(lifespan < shortestLife)
		shortestLife = lifespan;
	    averageLife += lifespan;
	}
	averageLife /= numberFrobsTotal;
    }
}
