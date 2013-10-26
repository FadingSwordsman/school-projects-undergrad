package com.putable.frobworld.locd011.simulation;

import com.putable.frobworld.locd011.beings.Frob;

/**
 * A holder for simulation results
 * @author David
 *
 */
public class SimulationResult
{
    String mapResult;
    LiveableStatus remainingObjects;
    int simulationSeed;
    int expectedRunTime;
    int actualRunTime;
    
    String resultString;
    
    private SimulationResult(String mapResult, LiveableStatus remainingObjects, int expectedRunTime, int actualRunTime, int simulationSeed)
    {
	this.mapResult = mapResult;
	this.remainingObjects = remainingObjects;
	this.expectedRunTime = expectedRunTime;
	this.actualRunTime = actualRunTime;
	this.simulationSeed = simulationSeed;
    }
    
    /**
     * Make a SimulationResult for a completed SimulationWorld.
     * This could be called from outside of the SimulationWorld!
     * @param runSimulation
     * @return
     */
    public static SimulationResult makeSimulationResult(SimulationWorld runSimulation)
    {
	SimulationResult result = new SimulationResult(runSimulation.toString(), runSimulation.getLiveableStatus(),
		runSimulation.getSimulationSettings().getWorldSettings().getMaxSimulationLength(), runSimulation.getDay(), runSimulation.getSeed());
	return result;
    }
    
    public double getAverageLife()
    {
	return remainingObjects.getFrobLifeAverage();
    }
    
    public int getRunTime()
    {
	return actualRunTime;
    }
    
    private void addArray(double[] array, StringBuffer toBuffer)
    {
	for(int x = 0; x < array.length; x++)
	    toBuffer.append('[').append(Math.round(array[x])).append(']');
    }
    
    public String toString()
    {
	if(resultString == null)
	{
	    StringBuffer builder = new StringBuffer();
	    //builder.append(mapResult);
	    builder.append("Results for simulation starting on seed ").append(simulationSeed);
	    builder.append("\nThe simulation ended because ").append(expectedRunTime < actualRunTime ? "the simulation max run time was reached at " + actualRunTime + " days." : "all of the frobs died at " + actualRunTime + " days.");
	    builder.append("\nFrobs alive at simulation end: ").append(remainingObjects.getRemainingFrobs());
	    builder.append("\nThere were a total of ").append(remainingObjects.getTotalFrobsEver()).append(" born or created.");
	    builder.append("\n\tAverage Frob life-span: ").append(remainingObjects.getFrobLifeAverage()).append(" days.");
	    builder.append("\n\tLongest Frob life: ").append(remainingObjects.getLongestLivedFrob()).append(" days.");
	    builder.append("\n\tShortest Frob life: ").append(remainingObjects.getShortestLivedFrob()).append(" days.");
	    builder.append("\n\tFrob life standard deviation: ").append(remainingObjects.getFrobDeviation());
	    builder.append("\n\tThe average genome was:\n\t\t");
	    addArray(remainingObjects.getAverageGenome(), builder);
	    builder.append("\n\tDeviation per gene was:\n\t\t");
	    addArray(remainingObjects.getGenomeDeviation(), builder);
	    Frob lastFrob = remainingObjects.lastFrobAlive();
	    builder.append("\n\tThe last Frob to die lived ").append(lastFrob.timeAlive()).append(" days.");
	    builder.append("\n\tIt had ").append(lastFrob.getChildren().size()).append(" children.");
	    builder.append("\n\t\tIts genome was: \n\t\t\t").append(lastFrob.getGenome());
	    builder.append("\nGrass alive at simulation end: ").append(remainingObjects.getRemainingGrass());
	    resultString = builder.toString();
	}
	return resultString;
    }
}
