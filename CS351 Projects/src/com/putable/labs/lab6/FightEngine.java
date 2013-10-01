package com.putable.labs.lab6;

import com.putable.labs.lab6.interfaces.Fightable;

public class FightEngine
{
    /**
     * Calculates and returns a synopsis for the result of a given fight
     * 
     * @param firstFighter
     * @param secondFighter
     * @return
     */
    public static String fight(Fightable firstFighter, Fightable secondFighter)
    {
	StringBuilder synopsis = new StringBuilder();
	synopsis.append(firstFighter.toString())
		.append("(" + firstFighter.getDescription())
		.append(") ")
		.append(firstFighter.getWeight())
		.append(" vs ")
		.append(secondFighter.toString())
		.append("(" + secondFighter.getDescription())
		.append(") ")
		.append(secondFighter.getWeight())
		.append(": ");
	if (firstFighter.winsFight(secondFighter))
	    synopsis.append(winnerString(firstFighter));
	else
	    synopsis.append(winnerString(secondFighter));
	return synopsis.toString();
    }

    private static String winnerString(Fightable fighter)
    {
	StringBuilder winner = new StringBuilder();
	winner.append(fighter.getWeightDescription())
		.append(" ")
		.append(fighter.toString())
		.append(" wins!");
	return winner.toString();
    }
}
