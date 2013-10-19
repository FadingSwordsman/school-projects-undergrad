package com.putable.frobworld.locd011;

public class MiscSetting
{
	private final int ROCK_BUMP_PENALTY;
	private final int MAX_RUN_TIME;
	
	public MiscSetting(int rockBumpPenalty, int maxRunTime)
	{
		this.ROCK_BUMP_PENALTY = rockBumpPenalty;
		this.MAX_RUN_TIME = maxRunTime;
	}
	
	public int getRockBumpPenalty()
	{
		return ROCK_BUMP_PENALTY;
	}
	
	public int getMaxRunTime()
	{
		return MAX_RUN_TIME;
	}
}
