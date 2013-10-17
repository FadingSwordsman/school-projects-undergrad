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
}
