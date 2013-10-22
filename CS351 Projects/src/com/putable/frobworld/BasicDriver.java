package com.putable.frobworld;

import java.util.Random;

import com.putable.frobworld.locd011.simulation.SimulationResult;
import com.putable.frobworld.locd011.simulation.SimulationSettings;
import com.putable.frobworld.locd011.simulation.SimulationWorld;

/**
 * A driver for FrobWorld.
 * 
 * @author David
 * 
 */
public class BasicDriver {
	/**
	 * Run the simulation simply, using the given seed to create the PRNG for
	 * the simulation
	 * 
	 * @param randomSeed
	 */
	private static void simpleRun(int randomSeed) {
		SimulationSettings runSettings = SimulationSettings
				.createSettings(25000);
		SimulationWorld simulation = new SimulationWorld(runSettings,
				randomSeed);
		simulation.run();
		SimulationResult results = simulation.getResult();
		System.out.println(results);
	}

	/**
	 * Create a random run.
	 */
	private static void randomRun() {
		Random r = new Random();
		int seed = r.nextInt();
		simpleRun(seed);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			int randomSeed;
			try {
				randomSeed = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input!");
				return;
			}
			simpleRun(randomSeed);
		} else
			randomRun();
	}
}
