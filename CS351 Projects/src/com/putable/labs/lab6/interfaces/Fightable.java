package com.putable.labs.lab6.interfaces;

public interface Fightable
{
    	/**
	 * This method decides if this animal is able to win a fight against the
	 * passed in contender. This is done with the highly scientific fight
	 * formula which gives a score to both fighters based on their specific
	 * {@code ConsumptionType} and weight. The score is given as follows: <br>
	 * score = weightFactor * weight <br>
	 * where weightFactor is 1 for {@code ConsumptionType.HERBIVORE}, 2 for
	 * {@code ConsumptionType.OMNIVORE} and 3 for
	 * {@code ConsumptionType.CARNIVORE}.
	 * 
	 * @param contender
	 *            the {@code Object} that is to fight this animal
	 * @return {@code true} if the score of this animal is greater than the
	 *         score of the contender, {@code false} otherwise.
	 */
	public boolean winsFight(Fightable contender);
	public boolean winsFight(Object contender);
	
	/**
	 * This method returns a scientific description of the given contender
	 * @return
	 */
	public String getDescription();
	
	/**
	 * This method returns the weight of the given contender
	 * @return
	 */
	public double getWeight();
	
	/**
	 * Returns the chance the the given animal will win in a fight
	 * @return
	 */
	public double getFightChance();
	
	/**
	 * This method returns the weight and a descriptor for that weight (g, oz, lb, etc.)
	 * @return
	 */
	public String getWeightDescription();
}
