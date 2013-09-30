package com.putable.labs.lab6.interfaces;

import com.putable.labs.lab6.enums.ConsumptionType;
import com.putable.labs.lab6.enums.Unit;

public interface Fightable
{
	public boolean winsFight(ConsumptionType contender);
	public boolean winsFight(Object contender);
	
	public String getDescription();
	
	public double getWeight();
	public Unit getWeightMeasurement();
	
	public String getWeightDescription();
}
