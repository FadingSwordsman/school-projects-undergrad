package com.putable.frobworld.locd011;

import com.putable.frobworld.locd011.simulation.SimulationSettings;

public class FrobWorld
{
    private static boolean batchMode = false;
    private static Integer batchRuns = null;
    private static Integer randomSeed = null;
    private static SimulationSettings settings;
    
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
		    batchRuns = Integer.parseInt(runNumber);
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
	    runWithSeed();
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
