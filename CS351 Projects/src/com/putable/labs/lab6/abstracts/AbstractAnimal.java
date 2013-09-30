package com.putable.labs.lab6.abstracts;

import com.putable.labs.lab6.enums.ConsumptionType;
import com.putable.labs.lab6.interfaces.Fightable;

public abstract class AbstractAnimal implements Fightable {
	private double weight;
	private ConsumptionType type;
	
	public AbstractAnimal(double weight, ConsumptionType type)
	{
		this.weight = weight;
		this.type = type;
	}

	protected abstract String getKingdom();
	
	@Override
	public boolean winsFight(ConsumptionType contender) {
		return false;
	}

	@Override
	public boolean winsFight(Object contender) {
		return false;
	}
	
	public double getWeightMeasurement()
	{
		return weight * type.getWeightFactor();
	}
	
	public String getWeightDescription()
	{
		return type.getWeightDecriptor(weight);
	}
	
	public double getWeight()
	{
		return weight;
	}
	
	public ConsumptionType getType()
	{
		return type;
	}
}
