package com.putable.frobworld.locd011.simulation;

import java.util.List;

public class SimulationResultSet
{
    private List<SimulationResult> results;
    private List<Integer> seeds;

    private int averageRun;
    private int averageLifeTime;
    
    public void add(SimulationResult result, int seed)
    {
	results.add(result);
	seeds.add(seed);
    }
    
    public String toString()
    {
	StringBuilder sb = new StringBuilder();
	for(SimulationResult result : results)
	    sb.append("\n").append(result);
	return sb.toString();
    }
    
}
