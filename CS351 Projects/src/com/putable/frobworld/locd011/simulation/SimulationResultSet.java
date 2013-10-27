package com.putable.frobworld.locd011.simulation;

import java.util.LinkedList;
import java.util.List;

import com.putable.frobworld.locd011.beings.Frob;

public class SimulationResultSet
{
    private int survivingFrobs = -1;
    private double metabolismAverage = -1;
    private double metabolismStandardDeviation = -1;   
    private List<Frob> survivingFrobList = new LinkedList<Frob>();
    
    private class SimulationSeedPair
    {
	private SimulationResult result;
	private Integer seed;
	private boolean success;
	
	public SimulationSeedPair(SimulationResult result, Integer seed)
	{
	    this.result = result;
	    this.seed = seed;
	    success = result.isSuccess();
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
    
    private int getSurvivingFrobs()
    {
	if(survivingFrobs < 0)
	    calculateSurvivorStats();
	return survivingFrobs;
    }
    
    private double getMetabolismAverage()
    {
	if(metabolismAverage < 0)
	    calculateSurvivorStats();
	return metabolismAverage;
    }
    
    private double getMetabolismStandardDeviation()
    {
	if(metabolismStandardDeviation < 0)
	    calculateSurvivorStats();
	return metabolismStandardDeviation;
    }
    
    private void calculateSurvivorStats()
    {
	for(SimulationSeedPair result : results)
	    survivingFrobList.addAll(result.getResult().getSurvivors());
	
	double[] stats = StatisticsUtility.calculateMetabolismStats(survivingFrobList);
	metabolismAverage = stats[0];
	metabolismStandardDeviation = stats[1];
	survivingFrobs = survivingFrobList.size();
    }
    
    private List<SimulationSeedPair> results = new LinkedList<SimulationSeedPair>();
    private List<Integer> successfulRunSeeds = new LinkedList<Integer>();

    private long averageRun = -1;
    private double averageLifeTime = -1;
    private int successfulRuns = -1;
    private int longestRunTime = -1;
    
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
    
    public int getLongestRunTime()
    {
	if(longestRunTime < 0)
	    calculateStatistics();
	return longestRunTime;
    }
    
    private void calculateStatistics()
    {
	averageLifeTime = 0;
	successfulRuns = 0;
	double tempAverage = 0;
	int currentItems = 0;
	for (SimulationSeedPair result : results)
	{
	    int runTime = result.getResult().getRunTime();
	    tempAverage += runTime;
	    if(runTime > longestRunTime)
		longestRunTime = runTime;
	    currentItems++;
	    averageLifeTime += result.getResult().getAverageLife();
	    if (result.isSuccess())
	    {
		successfulRuns++;
		successfulRunSeeds.add(result.getSeed());
	    }
	}
	averageRun = Math.round(tempAverage/currentItems);
	averageLifeTime /= currentItems;
    }
    
    public String toString()
    {
	StringBuilder sb = new StringBuilder();
	for(SimulationSeedPair result : results)
	    sb.append("\n").append(result.getResult());
	sb.append("\nAverage run time was: ").append(getAverageRun());
	sb.append("\nAverage frob life was: ").append(getAverageLifeTime());
	sb.append("\nLongest run was: ").append(getLongestRunTime());
	sb.append("\nSuccessful runs: ").append(getSuccessfulRuns());
	if(getSuccessfulRuns() > 0)
	{
	    sb.append("\n\tTotal number of surviving Frobs: ").append(getSurvivingFrobs());
	    sb.append("\n\tAverage Frob metabolism: ").append(getMetabolismAverage()).append(" movements per day");
	    sb.append("\n\tFrob metabolism standard deviation: ").append(getMetabolismStandardDeviation());
	    sb.append("\n\tMetabolic rates of surviving Frobs:\n\t\t");
	    int tabs = 0;
	    int[] freqs = new int[40];
	    for(Frob survivor : survivingFrobList)
		freqs[survivor.getUpdatePeriod()]++;
	    for(int x = 0; x < freqs.length; x++)
		if(freqs[x] > 0)
		    sb.append('\n').append(x).append(',').append(freqs[x]);
	    sb.append("\n\tSuccessful run seeds: \n\t\t");
	    for(Integer successSeed : successfulRunSeeds)
	    {
		if(tabs > 5)
		{
		    tabs = 0;
		    sb.append("\n\t\t");
		}
		sb.append(successSeed).append("\t");
		tabs++;
	    }
	}
	return sb.toString();
    }
    
}
