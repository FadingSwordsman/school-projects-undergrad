package com.putable.frobworld.locd011.simulation;

import java.util.HashMap;

/**
 * A list of settings that don't really fit anywhere else
 * @author David
 *
 */
public class MiscSetting
{
	private final int ROCK_BUMP_PENALTY;
	
	public MiscSetting(int rockBumpPenalty)
	{
		this.ROCK_BUMP_PENALTY = rockBumpPenalty;
	}
	
	public MiscSetting(HashMap<String, Integer> settingMap)
	{
	    this(settingMap.get("ROCK_BUMP_PENALTY"));
	}
	
	public int getRockBumpPenalty()
	{
		return ROCK_BUMP_PENALTY;
	}
}
