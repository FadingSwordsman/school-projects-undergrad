package com.putable.frobworld.locd011.simulation;

import com.putable.frobworld.locd011.Placeable;
import com.putable.pqueue.PQueue;

public class SimulationWorld
{
	private PQueue Moveables;
	private Placeable[][] grid;

	private final SimulationSettings settings;

	public SimulationWorld(SimulationSettings settings)
	{
		this.settings = settings;
	}

	public SimulationResult runSimulation()
	{
		return null;
	}

	public SimulationSettings getSimulationSettings()
	{
		return settings;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for(int x = 0; x < grid.length; x++)
			sb.append("--");
		String separator = sb.toString();
		return separator;
	}
}
