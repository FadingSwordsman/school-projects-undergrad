package com.putable.frobworld.locd011.simulation;

import java.util.LinkedList;
import java.util.List;

import com.putable.frobworld.locd011.beings.Frob;

/**
 * A holder for simulation results
 * @author David
 *
 */
public class SimulationResult
{
    private String mapResult;
    private LiveableStatus remainingObjects;
    private int simulationSeed;
    private int actualRunTime;
    
    private double averageMetabolism = -1;
    private double standardDeviationMetabolism = -1;
    
    private List<Frob> survivors = new LinkedList<Frob>();
    
    private boolean runSuccess;
    
    String resultString;
    
    private SimulationResult(String mapResult, LiveableStatus remainingObjects, int expectedRunTime, int actualRunTime, int simulationSeed)
    {
	this.mapResult = mapResult;
	this.remainingObjects = remainingObjects;
	this.actualRunTime = actualRunTime;
	this.simulationSeed = simulationSeed;
	runSuccess = actualRunTime >= expectedRunTime;
	if(runSuccess)
	    survivors = remainingObjects.getSurvivingFrobs();
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
    
    public double getSurvivingMetabolismAverage()
    {
	if(averageMetabolism < 0 && runSuccess)
    		calculateMetabolismStatistics();
	return averageMetabolism;
    }
    
    public double getSurvivingMetabolismDeviation()
    {
	if(standardDeviationMetabolism < 0 && runSuccess)
    		calculateMetabolismStatistics();
	return standardDeviationMetabolism;
    }
    
    private void calculateMetabolismStatistics()
    {
	double[] metabolismStats = StatisticsUtility.calculateMetabolismStats(survivors);
	averageMetabolism = metabolismStats[0];
	standardDeviationMetabolism = metabolismStats[1];
    }
    
    public int getRunTime()
    {
	return actualRunTime;
    }
    
    public boolean isSuccess()
    {
	return runSuccess;
    }
    
    public List<Frob> getSurvivors()
    {
	return survivors;
    }
    
    private void addArray(double[] array, StringBuilder toBuffer)
    {
	for(int x = 0; x < array.length; x++)
	    toBuffer.append('[').append(Math.round(array[x])).append(']');
    }
    
    public String mapResult()
    {
	return mapResult;
    }
    
    private String frobFocus(String prefixString, Frob frob)
    {
	StringBuilder builder = new StringBuilder();
	builder.append(prefixString).append("Frob ").append(frob.getId());
	builder.append(prefixString).append("\tAlive for ").append(frob.timeAlive()).append(" days");
	builder.append(prefixString).append("\tMetabolic Rate: ").append(frob.getUpdatePeriod()).append(" days per move");
	builder.append(prefixString).append("\tGenetic makeup:").append(prefixString).append("\t\t").append(frob.getGenome());
	builder.append(prefixString).append("\tChildren born: ").append(frob.getChildren().size());
	if(frob.getChildren().size() > 0)
	{
        	builder.append(prefixString).append("\tChildren surviving: ");
        	int counter = 0;
        	double avgLife = 0;
        	for(Frob child : frob.getChildren())
        	{
        	    if(!child.isDead())
        		counter++;
        	    avgLife += child.timeAlive();
        	}
        	avgLife /= frob.getChildren().size();
        	builder.append(counter);
        	builder.append(prefixString).append("\tAverage child life: ").append(avgLife).append(" days");
	}
	return builder.toString();
    }
    
    public String toString()
    {
	if(resultString == null)
	{
	    StringBuilder builder = new StringBuilder();
	    //builder.append(mapResult);
	    builder.append("Results for simulation starting on seed ").append(simulationSeed);
	    builder.append("\nThe simulation ended because ").append(runSuccess ? "the simulation max run time was reached at " : "all of the frobs died at ").append(actualRunTime).append(" days.");
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
	    if(isSuccess())
	    {
		boolean multiple = survivors.size() != 1;
		builder.append("\n\tThere ").append(multiple ? "were ": "was ").append(survivors.size()).append(multiple ? " Frobs": " Frob").append(" alive at simulation end.");
		builder.append("\n\t\tThe average surviving Frob moved once every ").append(getSurvivingMetabolismAverage()).append(" days.");
	    	builder.append("\n\t\tThe standard deviation for each surviving Frob's metabolic rate was ").append(getSurvivingMetabolismDeviation());
	    	builder.append("\n\t\tThe following Frobs were alive at simulation end: ");
	    	for(Frob survivor : survivors)
	    	    builder.append(frobFocus("\n\t\t\t", survivor));
	    }
	    Frob lastFrob = remainingObjects.getLastFrobToDie();
	    builder.append("\n\tThe last Frob to die was:").append(frobFocus("\n\t\t", lastFrob));
	    builder.append("\nGrass alive at simulation end: ").append(remainingObjects.getRemainingGrass());
	    resultString = builder.toString();
	}
	return resultString;
    }
}
