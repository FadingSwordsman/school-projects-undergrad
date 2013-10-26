package com.putable.frobworld;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import com.putable.frobworld.locd011.simulation.SimulationResult;
import com.putable.frobworld.locd011.simulation.SimulationResultSet;
import com.putable.frobworld.locd011.simulation.SimulationSettings;
import com.putable.frobworld.locd011.simulation.SimulationWorld;

public class FrobWorld
{
    private static void runBatchMode()
    {
	Scanner input = new Scanner(System.in);
	SimulationSettings settings = SimulationSettings.createSettings(250000);
	Semaphore threadLimit = new Semaphore(30);
	String seed = "No input found";
	int step = Integer.MAX_VALUE >> 20;
	int start = step * 5;
	int end = step * 4;
	for (int i = start; i < Integer.MAX_VALUE; i += step)
	{
		List<SimulationThread> threads = new LinkedList<SimulationThread>();
	    try
	    {
		/*
		 * while (input.hasNext()) {
		 */
		for (int x = i; x > end; x--)
		{
		    // seed = input.nextLine();
		    threadLimit.acquire();
		    int nextSeed = x;
		    if (nextSeed == 0)
			break;
		    SimulationThread nextThread = new SimulationThread(settings, nextSeed, threadLimit);
		    new Thread(nextThread).start();
		    threads.add(nextThread);
		}
	    }
	    catch (NumberFormatException e)
	    {
		System.err.println("Invalid runthese file at: " + seed);
		return;
	    }
	    catch (InterruptedException e)
	    {
		System.err.println("Threading problem");
		return;
	    }
	    SimulationResultSet resultAggregate = new SimulationResultSet();
	    for (SimulationThread thread : threads)
	    {
		resultAggregate.add(thread.getResult(), thread.getSeed());
	    }
	    try
	    {
		File temp = new File("FrobBatch" + i + "to" + end); 
			//File.createTempFile("Frob", "batch");
		temp.createNewFile();
		FileWriter output = new FileWriter(temp);
		output.append(resultAggregate.toString());
		output.flush();
		output.close();
		System.out.println("Wrote to " + temp.getAbsolutePath());
	    }
	    catch (IOException e)
	    {
	    }
	    end = i;
	}
    }

    private static void runWithSeed(int seed)
    {
	SimulationSettings settings = SimulationSettings.createSettings(25000);
	SimulationWorld world = new SimulationWorld(settings, false, seed);
	System.out.println(world.runSimulation());
    }

    private static void runInteractive()
    {
	runWithSeed(new Random().nextInt());
    }

    private static class SimulationThread implements Runnable
    {
	private SimulationWorld world;
	private Thread thread;
	private Semaphore threadLimit;
	private int seed;

	public SimulationThread(SimulationSettings settings, int seed, Semaphore limit)
	{
	    world = new SimulationWorld(settings, true, seed);
	    thread = new Thread(world);
	    this.seed = seed;
	    this.threadLimit = limit;
	}

	public int getSeed()
	{
	    return seed;
	}

	public void join()
	{
	    try
	    {
		thread.join();
	    }
	    catch (InterruptedException e)
	    {
	    }
	}

	public SimulationResult getResult()
	{
	    try
	    {
		thread.join();
	    }
	    catch (InterruptedException e)
	    {
	    }
	    return world.getResult();
	}

	public void run()
	{
	    thread.start();
	    try
	    {
		thread.join();
	    }
	    catch (InterruptedException e)
	    {
	    }
	    threadLimit.release();
	}
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
	if (args.length > 0)
	{
	    if (args[0].equals("batch"))
	    {
		runBatchMode();
		return;
	    }
	    try
	    {
		int seed = Integer.parseInt(args[0]);
		runWithSeed(seed);
		return;
	    }
	    catch (NumberFormatException e)
	    {
		System.out.println("Invalid argument " + args[0]);
		return;
	    }
	}
	runInteractive();
    }
}
