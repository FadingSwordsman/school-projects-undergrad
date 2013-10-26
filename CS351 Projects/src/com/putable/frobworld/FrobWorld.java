package com.putable.frobworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.putable.frobworld.locd011.simulation.SimulationResult;
import com.putable.frobworld.locd011.simulation.SimulationResultSet;
import com.putable.frobworld.locd011.simulation.SimulationSettings;
import com.putable.frobworld.locd011.simulation.SimulationWorld;

public class FrobWorld
{
    private static void runBatchMode()
    {
	StringReader input = new StringReader(System.in.toString());
	BufferedReader bufferIn = new BufferedReader(input);
	SimulationSettings settings = SimulationSettings.createSettings(250000);
	List<SimulationThread> threads = new LinkedList<SimulationThread>();
	try
	{
	    while(bufferIn.ready())
	    {
        	    int nextSeed = Integer.parseInt(bufferIn.readLine());
        	    if(nextSeed == 0)
        		break;
        	    SimulationThread nextThread = new SimulationThread(settings, nextSeed);
        	    nextThread.start();
        	    threads.add(nextThread);
	    }
	}
	catch(IOException e)
	{
	    System.out.println("Something went wrong!");
	    e.printStackTrace();
	    return;
	}
	SimulationResultSet resultAggregate = new SimulationResultSet();
	
	for(SimulationThread thread : threads)
	{
	    resultAggregate.add(thread.getResult(), thread.getSeed());
	}
    }
    
    private static void runWithSeed(Random r)
    {
	SimulationSettings settings = SimulationSettings.createSettings(25000);
	SimulationWorld world = new SimulationWorld(settings, false, r);
	System.out.println(world.runSimulation());
    }
    
    private static void runInteractive()
    {
	runWithSeed(new Random());
    }
    
    private static class SimulationThread
    {
	private SimulationWorld world;
	private Thread thread;
	private int seed;
	
	public SimulationThread(SimulationSettings settings, int seed)
	{
	    world = new SimulationWorld(settings, true, seed);
	    thread = new Thread(world);
	    this.seed = seed;
	}
	
	public void start()
	{
	    thread.start();
	}
	
	public int getSeed()
	{
	    return seed;
	}
	
	public SimulationResult getResult()
	{
	    try
	    {
		thread.join();
	    }
	    catch(InterruptedException e)
	    {}
	    return world.getResult();
	}
	
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
	if(args.length > 0)
	{
	    if(args[0].equals("batch"))
	    {
		runBatchMode();
		return;
	    }
	    try
	    {
		int seed = Integer.parseInt(args[0]);
		runWithSeed(new Random(seed));
	    }
	    catch(NumberFormatException e)
	    {
		System.out.println("Invalid argument " + args[0]);
	    }
	}
	runInteractive();
    }
}
