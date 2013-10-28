package com.putable.frobworld.locd011.simulation;

import java.util.HashMap;

public class WorldSetting
{
    private final int WORLD_HEIGHT;
    private final int WORLD_WIDTH;
    private final int MAX_SIMULATION_LENGTH;
    private final int INIT_FROBS;
    private final int INIT_GRASSES;
    private final int INIT_ROCKS;

    /**
     * A list of parameters specific to the world itself, and its creation/existence
     * @param worldHeight
     * @param worldWidth
     * @param maxSimulationLength
     * @param initFrobs
     * @param initGrasses
     * @param initRocks
     */
    public WorldSetting(int worldHeight, int worldWidth, int maxSimulationLength, int initFrobs, int initGrasses, int initRocks)
    {
	WORLD_HEIGHT = worldHeight;
	WORLD_WIDTH = worldWidth;
	MAX_SIMULATION_LENGTH = maxSimulationLength;
	INIT_FROBS = initFrobs;
	INIT_GRASSES = initGrasses;
	INIT_ROCKS = initRocks;
    }
    
    public WorldSetting(HashMap<String, Integer> settingMap)
    {
	this(settingMap.get("WORLD_HEIGHT"), settingMap.get("WORLD_WIDTH"), settingMap.get("MAX_SIMULATION_LENGTH"),
		settingMap.get("INIT_FROBS"), settingMap.get("INIT_GRASSES"), settingMap.get("INIT_ROCKS"));
    }

    public int getWorldHeight()
    {
	return WORLD_HEIGHT;
    }

    public int getWorldWidth()
    {
	return WORLD_WIDTH;
    }

    public int getMaxSimulationLength()
    {
	return MAX_SIMULATION_LENGTH;
    }

    public int getInitFrobs()
    {
	return INIT_FROBS;
    }

    public int getInitGrasses()
    {
	return INIT_GRASSES;
    }

    public int getInitRocks()
    {
	return INIT_ROCKS;
    }
}
