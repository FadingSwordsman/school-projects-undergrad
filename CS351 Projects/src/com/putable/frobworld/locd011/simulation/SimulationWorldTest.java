package com.putable.frobworld.locd011.simulation;

import org.junit.Test;

public class SimulationWorldTest
{
    @Test
    public void createWorldTest()
    {
	SimulationSettings simulationSettings = SimulationSettings.createSettings(1000);
	
	SimulationWorld world = new SimulationWorld(simulationSettings);
	world.run();
    }
}
