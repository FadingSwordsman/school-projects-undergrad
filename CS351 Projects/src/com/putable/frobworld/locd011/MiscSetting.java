package com.putable.frobworld.locd011;

public class MiscSetting
{
	private final int ROCK_BUMP_PENALTY;
	
	public MiscSetting(int rockBumpPenalty)
	{
		this.ROCK_BUMP_PENALTY = rockBumpPenalty;
	}
	
	public int getRockBumpPenalty()
	{
		return ROCK_BUMP_PENALTY;
	}
}
