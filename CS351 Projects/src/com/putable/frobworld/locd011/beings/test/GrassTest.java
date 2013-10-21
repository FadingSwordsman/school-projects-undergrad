package com.putable.frobworld.locd011.beings.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.putable.frobworld.locd011.beings.Grass;
import com.putable.frobworld.locd011.beings.PlaceType;
import com.putable.frobworld.locd011.simulation.GrassSetting;
import com.putable.frobworld.locd011.simulation.SimulationSettings;
import com.putable.frobworld.locd011.simulation.SimulationWorld;
import com.putable.frobworld.locd011.simulation.WorldSetting;

public class GrassTest
{
    private SimulationWorld world;
    private Grass underTest;
    int[][] locationPairs = new int[][]{{2,1}, {3,2}, {2,3}, {1,2}};
    
    @Before
    public void setUp()
    {
	//Make a world... We don't care about randomly generated anything.
	WorldSetting worldSettings = new WorldSetting(5, 5, 0, 0, 0, 0);
	GrassSetting grassSetting = new GrassSetting(0, -200, 10, 30, 10, 2, 100, 40);
	
	//Ideally, this wouldn't have to be instantiated, and could be mocked instead. This is a design flaw:
	SimulationSettings mockSettings = new SimulationSettings(worldSettings, null, null, grassSetting);
	world = new SimulationWorld(mockSettings);
	
	underTest = new Grass(world);
	underTest.setLocation(2,2);
	world.createLiveable(underTest);
    }
    
    /**
     * Simple birth test for grass
     */
    @Test
    public void testBirthSuccess()
    {
	int newMass = underTest.getBirthMass() - massGainedNextTurn(underTest);
	int newUpdate = underTest.getUpdatePeriod();
	underTest.setMass(underTest.getBirthMass());
	underTest.takeTurn();
	assertEquals(world.getLiveableStatus().getRemainingGrass(), 2);
	Grass newGrass = null;
	PlaceType[] adjacent = world.getAdjacent(underTest.getLocation());
	for(int x = 0; x < adjacent.length; x++)
	    if(adjacent[x] != null)
		newGrass = (Grass)world.getPlaceableAt(locationPairs[x]);
	assertEquals(underTest.getBirthMass(), newGrass.getBirthMass());
	int birthMass = calculateBirthMass(newMass);
	assertEquals(newMass - birthMass, underTest.getMass());
	assertEquals(birthMass, newGrass.getMass());
	assertEquals(newUpdate, underTest.getUpdatePeriod());
	assertEquals(world.getSimulationSettings().getGrassSettings().getGrassInitialUpdatePeriod(), newGrass.getUpdatePeriod());
    }
    
    /**
     * Simple failed birth test for grass
     */
    @Test
    public void testCrowdOutBirthFailure()
    {
	Grass crowder1 = new Grass(world);
	Grass crowder2 = new Grass(world);
	for(int x = 0; x < locationPairs.length; x++)
	{
	    crowder1.setLocation(locationPairs[x][0], locationPairs[x][1]);
	    world.createLiveable(crowder1);
	    for(int y = x + 1; y < locationPairs.length; y++)
	    {
		int oldUpdate = underTest.getUpdatePeriod();
		crowder2.setLocation(locationPairs[y][0], locationPairs[y][1]);
		world.createLiveable(crowder2);
		underTest.setMass(underTest.getBirthMass());
		underTest.takeTurn();
		assertEquals(3, world.getLiveableStatus().getRemainingGrass());
		assertEquals(Math.min(oldUpdate * 2, world.getSimulationSettings().getGrassSettings().getGrassMaxUpdatePeriod()),
			underTest.getUpdatePeriod());
		world.killLiveable(crowder2);
	    }
	    world.killLiveable(crowder1);
	}
    }
    
    private int massGainedNextTurn(Grass g)
    {
	int grassTax = world.getSimulationSettings().getGrassSettings().getMassTax();
	int fixedOverhead = world.getSimulationSettings().getGrassSettings().getFixedOverhead();
	return((grassTax*g.getUpdatePeriod())/(1000 + fixedOverhead));
    }
    
    private int calculateBirthMass(int oldMass)
    {
	int percent = world.getSimulationSettings().getGrassSettings().getGrassBirthPercent();
	return (percent * oldMass)/100;
    }
}
