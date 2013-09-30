package com.putable.labs.lab6.enums;

/**
 * The {@code Consumption} enum, enumerates the possible type of feeder that
 * a living thing is and gives a weight factor accordingly.
 * 
 * @author BKey
 */
public enum ConsumptionType
{
	// omnivore has a weightFactor of 2
	OMNIVORE(2),
	// carnivore has a weightFactor of 3
	CARNIVORE(3),
	// herbivore has a weightFactor of 1
	HERBIVORE(1);

	/**
	 * The constructor of a {@code ConsumptionType} enum.
	 * 
	 * @param weightFactor
	 *            the {@code int} value that is the weightFactor used in the
	 *            fight formula
	 */
	ConsumptionType(int weightFactor)
	{
		this.weightFactor = weightFactor;
	}

	private int weightFactor;

	/**
	 * Gets the {@code int} weight factor associated with this
	 * {@code ConsumptionType}.
	 * 
	 * @return the {@code int} weight factor associated with this
	 *         {@code ConsumptionType}
	 */
	public int getWeightFactor()
	{
		return weightFactor;
	}

}
