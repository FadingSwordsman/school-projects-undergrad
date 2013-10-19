package com.putable.frobworld.locd011.simulation;

import com.putable.frobworld.locd011.Liveable;
import com.putable.frobworld.locd011.Placeable;
import com.putable.pqueue.PQueue;

public class SimulationWorld
{
	private PQueue interestings;
	private Placeable[][] grid;

	private final SimulationSettings settings;

	public SimulationWorld(SimulationSettings settings)
	{
		this.settings = settings;
	}

	public SimulationResult runSimulation()
	{
		int day = 0;
		while(day < settings.getMiscSettings().getMaxRunTime())
		{
			// =(
			Liveable nextThing = (Liveable)interestings.remove();
			day = nextThing.getNextMove();
		}
		return SimulationResult.makeResult(this);
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
