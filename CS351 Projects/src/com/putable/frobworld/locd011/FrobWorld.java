package com.putable.frobworld.locd011;

import com.putable.frobworld.locd011.simulation.SimulationResult;
import com.putable.frobworld.locd011.simulation.SimulationSettings;
import com.putable.frobworld.locd011.simulation.SimulationWorld;


public class FrobWorld
{
    private static boolean batchMode = false;
    private static Integer randomSeed = null;
    
    private static void simpleRun(int randomSeed)
    {
	SimulationSettings runSettings = SimulationSettings.createSettings(25000);
	SimulationWorld simulation = new SimulationWorld(runSettings, randomSeed);
	simulation.run();
	SimulationResult results = simulation.getResult();
	
    }
    
    private static void runInteractive()
    {
	
    }
    
    private static void runWithSeed()
    {
	
    }
    
    private static void runBatchMode()
    {
	
    }
    
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
	int numberRuns = 1;
	for(int x = 0; x < args.length; x++)
	{
	    if(args[x].equals("batch"))
	    {
		batchMode = true;
		if(args.length < x+2)
		{
		    System.out.println("No number of runs specified");
		    return;
		}
		String runNumber = args[x+1];
		try
		{
		     numberRuns = Integer.parseInt(runNumber);
		}
		catch(NumberFormatException e)
		{
		    System.out.println("Invalid number of runs: " + runNumber);
		    return;
		}
		x++;
	    }
	    else if(randomSeed == null)
	    {
		try
		{
		    randomSeed = Integer.parseInt(args[x]);
		}
		catch(NumberFormatException e)
		{
		    System.out.println("Invalid random seed: " + args[x]);
		    return;
		}
	    }
	}
	if(randomSeed != null && !batchMode)
	{
	    simpleRun(randomSeed);
	}
	else if(batchMode && randomSeed == null)
	{
	    runBatchMode();
	}
	else if(randomSeed == null && !batchMode)
	{
	    runInteractive();
	}
	else
	{
	    System.out.println("Random seed and batch mode are not compatible options.");
	}
	
    }
}
