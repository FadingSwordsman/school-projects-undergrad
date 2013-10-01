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
	public boolean winsFight(Fightable contender) {
		double canWin = weight*type.getWeightFactor();
		return canWin > contender.getFightChance();
	}
	
	@Override
	public double getFightChance()
	{
	    return weight * type.getWeightFactor();
	}

	@Override
	public boolean winsFight(Object contender) {
		return false;
	}
	
	@Override
	public String getWeightDescription()
	{
		return type.getWeightDecriptor(weight);
	}
	
	@Override
	public String getDescription()
	{
	    return getKingdom();
	}
	
	@Override
	public double getWeight()
	{
		return weight;
	}
	
	public ConsumptionType getType()
	{
		return type;
	}
}
