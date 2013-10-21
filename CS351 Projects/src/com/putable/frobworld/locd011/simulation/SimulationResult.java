package com.putable.frobworld.locd011.simulation;

public class SimulationResult
{
    String mapResult;
    LiveableStatus remainingObjects;
    int expectedRunTime;
    int actualRunTime;
    
    String resultString;
    
    private SimulationResult(String mapResult, LiveableStatus remainingObjects, int expectedRunTime, int actualRunTime)
    {
	this.mapResult = mapResult;
	this.remainingObjects = remainingObjects;
    }
    
    public static SimulationResult makeSimulationResult(SimulationWorld runSimulation)
    {
	return new SimulationResult(runSimulation.toString(), runSimulation.getLiveableStatus(),
		runSimulation.getSimulationSettings().getWorldSettings().getMaxSimulationLength(), runSimulation.getDay());
    }
    
    public String toString()
    {
	if(resultString == null)
	{
	    StringBuffer builder = new StringBuffer();
	    builder.append(mapResult);
	    builder.append("\nThe simulation ended because ").append(expectedRunTime == actualRunTime ? "the simulation max run time was reached." : "all of the frobs died at " + actualRunTime + " days.");
	    builder.append("\nFrobs alive at simulation end: ").append(remainingObjects.getRemainingFrobs());
	    builder.append("\nGrass alive at simulation end: ").append(remainingObjects.getRemainingGrass());
	    resultString = builder.toString();
	}
	return resultString;
    }
}
