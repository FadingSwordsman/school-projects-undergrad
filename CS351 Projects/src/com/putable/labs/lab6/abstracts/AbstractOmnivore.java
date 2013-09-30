package com.putable.labs.lab6.abstracts;

import com.putable.labs.lab6.enums.ConsumptionType;

public abstract class AbstractOmnivore extends AbstractAnimal {
	public AbstractOmnivore(double weight)
	{
		super(weight, ConsumptionType.OMNIVORE);
	}
}
