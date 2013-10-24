package com.putable.frobworld.locd011.simulation;

/**
 * A holder for simulation results
 * @author David
 *
 */
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
    
    /**
     * Make a SimulationResult for a completed SimulationWorld.
     * This could be called from outside of the SimulationWorld!
     * @param runSimulation
     * @return
     */
    public static SimulationResult makeSimulationResult(SimulationWorld runSimulation)
    {
	return new SimulationResult(runSimulation.toString(), runSimulation.getLiveableStatus(),
		runSimulation.getSimulationSettings().getWorldSettings().getMaxSimulationLength(), runSimulation.getDay());
    }
    
    //TODO: Implement operations (Add, average, etc) on sets of these items.
    
    public String toString()
    {
	if(resultString == null)
	{
	    StringBuffer builder = new StringBuffer();
	    builder.append(mapResult);
	    builder.append("\nThe simulation ended because ").append(expectedRunTime == actualRunTime ? "the simulation max run time was reached at " + actualRunTime + " days." : "all of the frobs died at " + actualRunTime + " days.");
	    builder.append("\nFrobs alive at simulation end: ").append(remainingObjects.getRemainingFrobs());
	    builder.append("\nGrass alive at simulation end: ").append(remainingObjects.getRemainingGrass());
	    resultString = builder.toString();
	}
	return resultString;
    }
}
