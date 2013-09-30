package com.putable.labs.lab6.abstracts;

import com.putable.labs.lab6.enums.ConsumptionType;

public abstract class AbstractCarnivore extends AbstractAnimal
{
	public AbstractCarnivore(double weight)
	{
		super(weight, ConsumptionType.CARNIVORE);
	}
}
