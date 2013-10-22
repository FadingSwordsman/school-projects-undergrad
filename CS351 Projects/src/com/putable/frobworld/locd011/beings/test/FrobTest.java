package com.putable.frobworld.locd011.beings.test;

import org.junit.Before;
import org.junit.Test;

import com.putable.frobworld.locd011.beings.Frob;
import com.putable.frobworld.locd011.simulation.FrobSetting;
import com.putable.frobworld.locd011.simulation.MiscSetting;
import com.putable.frobworld.locd011.simulation.SimulationWorld;

public class FrobTest
{
    SimulationWorld world;
    FrobSetting settings;
    
    Frob underTest;
    
    @Before
    public void setUp()
    {
	settings = new FrobSetting(0, 0, 0, 0, 0);
	MiscSetting misc = new MiscSetting(10);
    }
    
    @Test
    public void moveTest()
    {
	
    }
}
