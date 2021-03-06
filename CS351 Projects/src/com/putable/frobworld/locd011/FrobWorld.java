package com.putable.frobworld.locd011;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;

import com.putable.frobworld.locd011.graphics.SimulationPanel;
import com.putable.frobworld.locd011.graphics.SimulationSpeedSlider;
import com.putable.frobworld.locd011.simulation.SimulationResult;
import com.putable.frobworld.locd011.simulation.SimulationResultSet;
import com.putable.frobworld.locd011.simulation.SimulationSettings;
import com.putable.frobworld.locd011.simulation.SimulationWorld;
import com.putable.frobworld.locd011.simulation.WorldSetting;

/**
 * A driver for FrobWorld artificial life simulator.
 * 
 * This version of Frobworld handles
 * @author David
 *
 */
public class FrobWorld
{
    /**
     * Run FrobWorld in some sort of batch mode, whether a runcount or runthese instance.
     * @param input
     */
    private static void runBatchMode(Scanner input)
    {
	List<Integer> seeds = new LinkedList<Integer>();
	String seed = "No input found";
	try
	{
	    while (input.hasNext())
	    {
		seed = input.nextLine();
		seeds.add(Integer.parseInt(seed));
	    }
	}
	catch (NumberFormatException e)
	{
	    System.err.println("'" + seed + "' is an invalid seed value!");
	    return;
	}
	if (seeds.size() >= 1)
	{
	    int firstLine = seeds.get(0);
	    if(firstLine == 0)
		runthese(seeds, true);
	    else if(firstLine > 0)
		runthis(seeds.get(0));
	}
    }

    /**
     * Process a one-lined runthis file.
     * @param runs
     */
    private static void runthis(int runs)
    {
	System.out.println("Running " + runs + " simulations.");
	List<Integer> seeds = new LinkedList<Integer>();
	Random r = new Random();
	for (int x = 0; x < runs; x++)
	    seeds.add(r.nextInt());
	runthese(seeds, false);
    }

    /**
     * Run a runthese file, or a runthis file after the seeds have been generated.
     * Run each Simulation on its own thread, for great justice. Avoid using all of the processors, unless
     * 		there is only one processor, in which case... I hope the kernel is friendly to your other activities.
     * @param seeds
     * @param stopAtZero
     */
    private static void runthese(List<Integer> seeds, boolean stopAtZero)
    {
	//Try not to completely overload everything with simulation processing:
	int maxThreads = Runtime.getRuntime().availableProcessors() >> 2;
	if(maxThreads == 0)
	    maxThreads = 1;
	Semaphore threadLimit = new Semaphore(maxThreads);
	List<SimulationThread> threads = new LinkedList<SimulationThread>();
	SimulationSettings settings = SimulationSettings.createSettings(25000);
	try
	{
	    boolean canEnd = false;
	    for (Integer seed : seeds)
	    {
		threadLimit.acquire();
		if (stopAtZero && seed == 0)
		{
		    if(canEnd)
			break;
		    else
		    {
			canEnd = true;
			continue;
		    }
		}
		SimulationThread nextThread = new SimulationThread(settings, seed, threadLimit);
		new Thread(nextThread).start();
		threads.add(nextThread);
	    }
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
	System.out.println(resultAggregate);
    }

    /**
     * Run an interactive simulation with the specified seed
     * @param seed
     */
    private static void runWithSeed(int seed)
    {
	SimulationSettings settings = SimulationSettings.createSettings(25000);
	SimulationWorld world = new SimulationWorld(settings, false, seed);
	JFrame container = new JFrame();
	SimulationPanel panel = new SimulationPanel(world);
	SimulationSpeedSlider slider = new SimulationSpeedSlider(2, 500, 30, panel);
	world.setPanel(panel);
	WorldSetting worldSetting = settings.getWorldSettings();
	int height = worldSetting.getWorldHeight();
	int width = worldSetting.getWorldWidth();
	container.setPreferredSize(new Dimension(width * 10 + 40, height * 10 + 40));
	container.getContentPane().add(panel, BorderLayout.CENTER);
	container.getContentPane().add(slider, BorderLayout.SOUTH);
	container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	container.pack();
	container.setResizable(false);
	container.setVisible(true);
	System.out.println(world.runSimulation());
    }

    /**
     * Run a single interactive simulation with a randomized seed 
     */
    private static void runInteractive()
    {
	runWithSeed(new Random().nextInt());
    }

    /**
     * A Simulation-Thread combination. Combines a SimulationWorld ant the Thread running it.
     * @author David
     *
     */
    private static class SimulationThread implements Runnable
    {
	private SimulationWorld world;
	private Thread thread;
	private Semaphore threadLimit;
	private int seed;

	/**
	 * Create this object, specifying the resource that limits the number of concurrent threads, settings, and seed.
	 * @param settings
	 * @param seed
	 * @param limit
	 */
	public SimulationThread(SimulationSettings settings, int seed, Semaphore limit)
	{
	    world = new SimulationWorld(settings, true, seed);
	    thread = new Thread(world);
	    this.seed = seed;
	    this.threadLimit = limit;
	}

	/**
	 * Get the seed that created this SimulationWorld
	 * @return
	 */
	public int getSeed()
	{
	    return seed;
	}

	/**
	 * Block for the simulation to complete, then return the result from that simulation.
	 * @return
	 */
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

	/**
	 * Start this thread. The calling thread needs to acquire a license before doing this, to respect the thread limit.
	 */
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
     * Run the FrobWorld program.
     * There are two primary modes of operation ((S.2.1.1))
     * 	Mode 1: Interactive mode. ((S.2.1.2))
     * 		Using its own window, FrobWorld runs, displaying
     * 		Frobs, Rocks, and Grasses in that window, with a random seed.
     * 		A specific seed can be entered simply by including the seed
     * 		number as the only argument to FrobWorld. ((S.2.1.3.4.1))
     * 	Mode 2: Batch mode. ((S.2.1.3.1))
     * 		FrobWorld is run without the graphical display. There are two
     * 		possible formats of inputs for batch mode.
     * 		The first, a 'runcount' input, is a single, non-zero,
     * 		positive integer ((S.2.1.3.3)) That number of runs is then
     * 		executed, and summary information is provided to stdout.
     * 		The second, a 'runthese' input, is a list of numbers starting
     * 		and ending with zero entries. In this case, FrobWorld runs through
     * 		a simulation for each line, using that line's number as its initial seed.
     * 		((S.2.1.3.4))
     * 		In each case, a summary of each of the runs, as well as a summary for
     * 		all of the runs, is output to stdout. ((S.2.1.3.5))
     * @param args
     */
    public static void main(String[] args)
    {
	if (args.length == 1)
	{
	    if (args[0].equals("batch"))
	    {
		Scanner input = new Scanner(System.in);
		runBatchMode(input);
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
	else if(args.length == 0)
	    runInteractive();
	else
	    System.err.println("Syntax:\n\tFrobWorld [batch]\n\tFrobWorld [run_count]");
    }
}
