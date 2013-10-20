package com.putable.frobworld.locd011.simulation;

import java.util.HashMap;

public class SimulationSettings
{
    private final WorldSetting worldSettings;
    private final MiscSetting miscSettings;
    private final FrobSetting frobSettings;
    private final GrassSetting grassSettings;

    public SimulationSettings(WorldSetting worldSettings, MiscSetting miscSettings, FrobSetting frobSettings, GrassSetting grassSettings)
    {
	this.worldSettings = worldSettings;
	this.miscSettings = miscSettings;
	this.frobSettings = frobSettings;
	this.grassSettings = grassSettings;
    }

    public WorldSetting getWorldSettings()
    {
	return worldSettings;
    }

    public MiscSetting getMiscSettings()
    {
	return miscSettings;
    }

    public FrobSetting getFrobSettings()
    {
	return frobSettings;
    }

    public GrassSetting getGrassSettings()
    {
	return grassSettings;
    }
    
    public static SimulationSettings createSettings(int runTime)
    {
	HashMap<String, Integer> settingMap = createSettingsMap(runTime);
	WorldSetting worldSettings = new WorldSetting(settingMap);
	MiscSetting miscSettings = new MiscSetting(settingMap);
	FrobSetting frobSettings = new FrobSetting(settingMap);
	GrassSetting grassSettings = new GrassSetting(settingMap);
	
	return new SimulationSettings(worldSettings, miscSettings, frobSettings, grassSettings);
    }
    
    private static HashMap<String, Integer> createSettingsMap(int runTime)
    {
	HashMap<String, Integer> settingsMap = new HashMap<String, Integer>();
	int WORLD_HEIGHT = 100,
	WORLD_WIDTH = 50,
	INIT_FROBS = 0,
	INIT_GRASSES = 250,
	INIT_ROCKS = 100;
	
	int ROCK_BUMP_PENALTY = 30;
	
	int FROB_FIXED_OVERHEAD = 2,
	FROB_MASS_TAX = 100,
	FROB_GENESIS_MASS = 100,
	DNA_MUTATION_ODDS = 20,
	FROB_HIT_PENALTY = 10;
	
	int GRASS_FIXED_OVERHEAD = 0,
	GRASS_MASS_TAX = -200,
	GRASS_GENESIS_MASS = 10,
	GRASS_BIRTH_MASS = 30,
	GRASS_INITIAL_UPDATE_PERIOD = 10,
	GRASS_CROWD_LIMIT = 2,
	GRASS_MAX_UPDATE_PERIOD = 100,
	GRASS_BIRTH_PERCENT = 40;
	
	settingsMap.put("MAX_SIMULATION_LENGTH", runTime);

	settingsMap.put("WORLD_HEIGHT", WORLD_HEIGHT);
	settingsMap.put("WORLD_WIDTH", WORLD_WIDTH);
	settingsMap.put("INIT_FROBS", INIT_FROBS);
	settingsMap.put("INIT_GRASSES", INIT_GRASSES);
	settingsMap.put("INIT_ROCKS", INIT_ROCKS);
	
	settingsMap.put("ROCK_BUMP_PENALTY", ROCK_BUMP_PENALTY);
	
	settingsMap.put("FROB_FIXED_OVERHEAD", FROB_FIXED_OVERHEAD);
	settingsMap.put("FROB_MASS_TAX", FROB_MASS_TAX);
	settingsMap.put("FROB_GENESIS_MASS", FROB_GENESIS_MASS);
	settingsMap.put("DNA_MUTATION_ODDS", DNA_MUTATION_ODDS);
	settingsMap.put("FROB_HIT_PENALTY", FROB_HIT_PENALTY);
	
	settingsMap.put("GRASS_FIXED_OVERHEAD", GRASS_FIXED_OVERHEAD);
	settingsMap.put("GRASS_MASS_TAX", GRASS_MASS_TAX);
	settingsMap.put("GRASS_GENESIS_MASS", GRASS_GENESIS_MASS);
	settingsMap.put("GRASS_BIRTH_MASS", GRASS_BIRTH_MASS);
	settingsMap.put("GRASS_INITIAL_UPDATE_PERIOD", GRASS_INITIAL_UPDATE_PERIOD);
	settingsMap.put("GRASS_CROWD_LIMIT", GRASS_CROWD_LIMIT);
	settingsMap.put("GRASS_MAX_UPDATE_PERIOD", GRASS_MAX_UPDATE_PERIOD);
	settingsMap.put("GRASS_BIRTH_PERCENT", GRASS_BIRTH_PERCENT);
	
	return settingsMap;
    }
}
