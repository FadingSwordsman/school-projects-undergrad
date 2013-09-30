package com.putable.labs.lab6;

import com.putable.labs.lab6.interfaces.Fightable;

public class FightEngine {
	public static String fight(Fightable firstFighter, Fightable secondFighter)
	{
		StringBuilder synopsis = new StringBuilder();
		synopsis.append(firstFighter.getDescription())
				.append(" vs ")
				.append(secondFighter.getDescription());
		return synopsis.toString();
	}
}
