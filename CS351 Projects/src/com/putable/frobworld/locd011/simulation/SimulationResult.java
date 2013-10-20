package com.putable.frobworld.locd011.simulation;

public class SimulationResult
{
    String result;
    
    private SimulationResult(String... result)
    {
	StringBuffer resultBuffer = new StringBuffer();
	for(String resultPart : result)
	    resultBuffer.append(resultPart);
	this.result = resultBuffer.toString();
    }
    
    public static SimulationResult makeSimulationResult(SimulationWorld runSimulation)
    {
	return new SimulationResult(runSimulation.toString());
    }
    
    public String toString()
    {
	return result;
    }
}
