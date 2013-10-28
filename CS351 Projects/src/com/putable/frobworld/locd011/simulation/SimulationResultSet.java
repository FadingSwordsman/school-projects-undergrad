package com.putable.frobworld.locd011.simulation;

import java.util.LinkedList;
import java.util.List;

import com.putable.frobworld.locd011.beings.Frob;

/**
 * A SimulationResultSet allows us to group several SimulationResuls together, and calculate statistics on the set of runs.
 * 
 * @author David
 *
 */
public class SimulationResultSet
{
    private boolean recalculate = false;
    
    private List<SimulationResult> results = new LinkedList<SimulationResult>();
    private List<Integer> successfulRunSeeds = new LinkedList<Integer>();
    
    private int survivingFrobs = -1;
    private double metabolismAverage = -1;
    private double metabolismStandardDeviation = -1;   
    private List<Frob> survivingFrobList = new LinkedList<Frob>();

    private long averageRun = -1;
    private double averageLifeTime = -1;
    private int successfulRuns = 0;
    private int longestRunTime = -1;
    
    /**
     * Get the total number of Frobs surviving at the end of all of the simulations
     * @return
     */
    public int getSurvivingFrobs()
    {
	if(recalculate)
	    calculateStatistics();
	return survivingFrobs;
    }
    
    /**
     * Get the average metabolism of all surviving Frobs.
     * @return
     */
    public double getMetabolismAverage()
    {
	if(recalculate)
	    calculateStatistics();
	return metabolismAverage;
    }
    
    /**
     * Get the standard deviation of the metabolism of all surviving Frobs.
     * @return
     */
    public double getMetabolismStandardDeviation()
    {
	if(recalculate)
	    calculateStatistics();
	return metabolismStandardDeviation;
    }
    
    /**
     * Calculate and store the statistics related to surviving Frobs.
     */
    private void calculateSurvivorStats()
    {
	for(SimulationResult result : results)
	    survivingFrobList.addAll(result.getSurvivors());
	
	double[] stats = StatisticsUtility.calculateMetabolismStats(survivingFrobList);
	metabolismAverage = stats[0];
	metabolismStandardDeviation = stats[1];
	survivingFrobs = survivingFrobList.size();
    }

    
    /**
     * Add a SimulationResult to this group.
     * Since our statistic will be wrong at this point, flag the set for recalculation
     * @param result
     * @param seed
     */
    public void add(SimulationResult result, int seed)
    {
	results.add(result);
	if(result.isSuccess())
	{
	    successfulRuns++;
	    successfulRunSeeds.add(seed);
	}
	recalculate = true;
    }
    
    /**
     * Get the average time that all of the simulations ran
     * @return
     */
    public long getAverageRun()
    {
	if(recalculate)
	    calculateStatistics();
	return averageRun;
    }
    
    /**
     * Get the average of the average of all Frobs in the simulations.
     * @return
     */
    public double getAverageLifeTime()
    {
	if(recalculate)
	    calculateStatistics();
	return averageLifeTime;
    }
    
    /**
     * Get the number of runs which ended with a surviving Frob
     * @return
     */
    public int getSuccessfulRuns()
    {
	if(recalculate)
	    calculateStatistics();
	return successfulRuns;
    }
    
    /**
     * Get the longest runTime in the set of SimulationResults
     * @return
     */
    public int getLongestRunTime()
    {
	if(recalculate)
	    calculateStatistics();
	return longestRunTime;
    }
    
    /**
     * Calculate and store all of the statistics of interest.
     */
    private void calculateStatistics()
    {
	recalculate = false;
	averageLifeTime = 0;
	double tempAverage = 0;
	int currentItems = 0;
	for (SimulationResult result : results)
	{
	    int runTime = result.getRunTime();
	    tempAverage += runTime;
	    if(runTime > longestRunTime)
		longestRunTime = runTime;
	    currentItems++;
	    averageLifeTime += result.getAverageLife();
	}
	averageRun = Math.round(tempAverage/currentItems);
	averageLifeTime /= currentItems;
	calculateSurvivorStats();
    }
    
    public String toString()
    {
	StringBuilder sb = new StringBuilder();
	for(SimulationResult result : results)
	    sb.append("\n").append(result);
	sb.append("\nAverage run time was: ").append(getAverageRun());
	sb.append("\nAverage frob life was: ").append(getAverageLifeTime());
	sb.append("\nLongest run was: ").append(getLongestRunTime());
	sb.append("\nSuccessful runs: ").append(getSuccessfulRuns());
	if(getSuccessfulRuns() > 0)
	{
	    sb.append("\n\tTotal number of surviving Frobs: ").append(getSurvivingFrobs());
	    sb.append("\n\tAverage Frob metabolism: ").append(getMetabolismAverage()).append(" movements per day");
	    sb.append("\n\tFrob metabolism standard deviation: ").append(getMetabolismStandardDeviation());
	    sb.append("\n\tMetabolic rates of surviving Frobs:");
	    int tabs = 0;
	    int[] freqs = new int[40];
	    for(Frob survivor : survivingFrobList)
		freqs[survivor.getUpdatePeriod()]++;
	    for(int x = 0; x < freqs.length; x++)
		if(freqs[x] > 0)
		    sb.append("\n\t\t").append(x).append(" - ").append(freqs[x]).append(freqs[x] == 1 ? " Frob" : " Frobs");
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
