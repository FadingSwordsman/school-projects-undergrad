package com.putable.frobworld.locd011.simulation;

import com.putable.frobworld.locd011.MiscSetting;
import com.putable.frobworld.locd011.WorldSetting;

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
}
