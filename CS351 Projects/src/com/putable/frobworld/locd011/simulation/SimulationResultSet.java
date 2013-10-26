package com.putable.frobworld.locd011.simulation;

import java.util.LinkedList;
import java.util.List;

public class SimulationResultSet
{
    private class SimulationSeedPair
    {
	private SimulationResult result;
	private Integer seed;
	private boolean success;
	
	public SimulationSeedPair(SimulationResult result, Integer seed)
	{
	    this.result = result;
	    this.seed = seed;
	    success = result.actualRunTime >= result.expectedRunTime;
	}
	
	public SimulationResult getResult()
	{
	    return result;
	}
	
	public Integer getSeed()
	{
	    return seed;
	}
	
	public boolean isSuccess()
	{
	    return success;
	}
    }
    
    private List<SimulationSeedPair> results = new LinkedList<SimulationSeedPair>();
    private List<Integer> successfulRunSeeds = new LinkedList<Integer>();

    private long averageRun = -1;
    private double averageLifeTime = -1;
    private int successfulRuns = -1;
    
    public void add(SimulationResult result, int seed)
    {
	results.add(new SimulationSeedPair(result, seed));
    }
    
    public long getAverageRun()
    {
	if(averageRun < 0)
	    calculateStatistics();
	return averageRun;
    }
    
    public double getAverageLifeTime()
    {
	if(averageLifeTime < 0)
	    calculateStatistics();
	return averageLifeTime;
    }
    
    public int getSuccessfulRuns()
    {
	if(successfulRuns < 0)
	    calculateStatistics();
	return successfulRuns;
    }
    
    private void calculateStatistics()
    {
	averageLifeTime = 0;
	successfulRuns = 0;
	double tempAverage = 0;
	int currentItems = 0;
	for (SimulationSeedPair result : results)
	{
	    tempAverage += result.getResult().getRunTime();
	    currentItems++;
	    averageLifeTime += result.getResult().getAverageLife();
	    if (result.isSuccess())
		successfulRunSeeds.add(result.getSeed());
	}
	averageRun = Math.round(tempAverage/currentItems); 
    }
    
    public String toString()
    {
	StringBuilder sb = new StringBuilder();
	for(SimulationSeedPair result : results)
	    sb.append("\n").append(result.getResult());
	sb.append("\nAverage run time was: ").append(getAverageRun());
	sb.append("\nAverage frob life was: ").append(getAverageLifeTime());
	sb.append("\nSuccessful runs: ").append(getSuccessfulRuns());
	if(getSuccessfulRuns() > 0)
	{
	    sb.append("\n\tSuccessful run seeds: ");
	    for(Integer successSeed : successfulRunSeeds)
		sb.append(successSeed).append("\t");
	}
	return sb.toString();
    }
    
}
