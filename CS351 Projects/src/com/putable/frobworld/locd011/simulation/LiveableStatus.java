package com.putable.frobworld.locd011.simulation;

/**
 * Hold the status of Liveables in the SimulationWorld
 * @author David
 *
 */
public class LiveableStatus
{
    private int remainingFrobs;
    private int remainingGrass;
    
    /**
     * In the beginning, there was nothing...
     */
    public LiveableStatus()
    {
	remainingFrobs = 0;
	remainingGrass = 0;
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
    
    /**
     * Return the number of current Frobs
     * @return
     */
    public int getRemainingFrobs()
    {
	return remainingFrobs;
    }
    
    /**
     * Return the number of current Grass
     * @return
     */
    public int getRemainingGrass()
    {
	return remainingGrass;
    }
}
