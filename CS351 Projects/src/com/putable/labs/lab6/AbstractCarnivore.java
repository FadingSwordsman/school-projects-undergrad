package com.putable.labs.lab6;

import com.putable.labs.lab6.enums.ConsumptionType;

public class AbstractCarnivore
{
	protected double weight;
	protected ConsumptionType type = ConsumptionType.CARNIVORE;
	
	public AbstractCarnivore(double weight)
	{
		this.weight = weight;
	}
}
