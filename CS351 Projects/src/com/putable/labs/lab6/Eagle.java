package com.putable.labs.lab6;

/**
 * This is a concrete {@code Eagle} class. An instance of this class is used
 * specifically to be compared to other living things by fact of who would win
 * in a fight. A fight outcome is based on the attributes of a {@code Eagle}
 * including weight and what they consume for food (i.e. meat, plants or both).
 * 
 * @author BKey
 * 
 */
public class Eagle {
	/**
	 * The {@code Consumption} enum, enumerates the possible type of feeder that
	 * a living thing is and gives a weight factor accordingly.
	 * 
	 * @author BKey
	 */
	public enum ConsumptionType {
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
		ConsumptionType(int weightFactor) {
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
		public int getWeightFactor() {
			return weightFactor;
		}
	}

	private double weight;
	private ConsumptionType consumptionType;

	/**
	 * The constructor of a {@code Eagle}.
	 * 
	 * @param weight
	 *            the {@code double} value that is the weight in lbs of the
	 *            {@code Eagle}
	 */
	public Eagle(double weight) {
		this.weight = weight;

		// Eagles are carnivores
		this.consumptionType = ConsumptionType.CARNIVORE;
	}

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
	public boolean winsFight(Object contender) {
		double thisScore;
		double contenderScore;

		// figure out what type of contender this is and cast to get weight and
		// consumption type...gah, that's ugly.
		if (contender instanceof Wolf) {
			Wolf wolfContender = (Wolf) contender;
			contenderScore = wolfContender.getConsumptionType()
					.getWeightFactor() * wolfContender.getWeight();
		} else if (contender instanceof Eagle) {
			Eagle eagleContender = (Eagle) contender;
			contenderScore = eagleContender.getConsumptionType()
					.getWeightFactor() * eagleContender.getWeight();
		} else if (contender instanceof Rabbit) {
			Rabbit rabbitContender = (Rabbit) contender;
			contenderScore = rabbitContender.getConsumptionType()
					.getWeightFactor() * rabbitContender.getWeight();
		} else {
			// I don't know what the crap this contender is...automatically run
			// away.
			return false;
		}

		thisScore = this.consumptionType.getWeightFactor() * this.weight;

		// compare scores
		if (thisScore > contenderScore)
			return true;
		else
			return false;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public ConsumptionType getConsumptionType() {
		return consumptionType;
	}

	public void setConsumptionType(ConsumptionType consumptionType) {
		this.consumptionType = consumptionType;
	}

	@Override
	public String toString() {
		return "Eagle";
	}
}
