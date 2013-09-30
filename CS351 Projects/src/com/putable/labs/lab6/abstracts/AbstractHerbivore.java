package com.putable.labs.lab6.abstracts;

import com.putable.labs.lab6.enums.ConsumptionType;

public abstract class AbstractHerbivore extends AbstractAnimal
{
	public AbstractHerbivore(double weight)
	{
		super(weight, ConsumptionType.HERBIVORE);
	}
}
