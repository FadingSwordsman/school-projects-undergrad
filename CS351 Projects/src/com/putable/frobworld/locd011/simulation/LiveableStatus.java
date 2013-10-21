package com.putable.frobworld.locd011.simulation;

public class LiveableStatus
{
    private int remainingFrobs;
    private int remainingGrass;
    
    public LiveableStatus()
    {
	remainingFrobs = 0;
	remainingGrass = 0;
    }
    
    public void updateFrobs(int offset)
    {
	remainingFrobs += offset;
    }
    
    public void updateGrass(int offset)
    {
	remainingGrass += offset;
    }
    
    public int getRemainingFrobs()
    {
	return remainingFrobs;
    }
    
    public int getRemainingGrass()
    {
	return remainingGrass;
    }
}
