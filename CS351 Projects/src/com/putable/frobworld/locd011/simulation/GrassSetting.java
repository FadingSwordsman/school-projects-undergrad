package com.putable.frobworld.locd011.simulation;

import java.util.HashMap;

public class GrassSetting extends AbstractLivingSetting
{
    private final int GRASS_BIRTH_MASS;
    private final int GRASS_INITIAL_UPDATE_PERIOD;
    private final int GRASS_CROWD_LIMIT;
    private final int GRASS_MAX_UPDATE_PERIOD;
    private final int GRASS_BIRTH_PERCENT;
    
    public GrassSetting(int fixedOverhead, int massTax, int genesisMass, int grassBirthMass, int grassInitialUpdatePeriod, int grassCrowdLimit, int grassMassUpdatePeriod, int grassBirthPercent)
    {
	super(fixedOverhead, massTax, genesisMass);
	GRASS_BIRTH_MASS = grassBirthMass;
	GRASS_INITIAL_UPDATE_PERIOD = grassInitialUpdatePeriod;
	GRASS_CROWD_LIMIT = grassCrowdLimit;
	GRASS_MAX_UPDATE_PERIOD = grassMassUpdatePeriod;
	GRASS_BIRTH_PERCENT = grassBirthPercent;
    }
    
    public GrassSetting(HashMap<String, Integer> settingMap)
    {
	this(settingMap.get("GRASS_FIXED_OVERHEAD"), settingMap.get("GRASS_MASS_TAX"), settingMap.get("GRASS_GENESIS_MASS"),
		settingMap.get("GRASS_BIRTH_MASS"), settingMap.get("GRASS_INITIAL_UPDATE_PERIOD"), settingMap.get("GRASS_CROWD_LIMIT"), settingMap
			.get("GRASS_MAX_UPDATE_PERIOD"), settingMap.get("GRASS_BIRTH_PERCENT"));
    }
    
    public int getGrassBirthMass()
    {
	return GRASS_BIRTH_MASS;
    }
    
    public int getGrassInitialUpdatePeriod()
    {
	return GRASS_INITIAL_UPDATE_PERIOD;
    }
    
    public int getGrassCrowdLimit()
    {
	return GRASS_CROWD_LIMIT;
    }
    
    public int getGrassMaxUpdatePeriod()
    {
	return GRASS_MAX_UPDATE_PERIOD;
    }
    
    public int getGrassBirthPercent()
    {
	return GRASS_BIRTH_PERCENT;
    }
    
}
