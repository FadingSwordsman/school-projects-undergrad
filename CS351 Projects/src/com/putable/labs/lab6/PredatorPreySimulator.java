package com.putable.labs.lab6;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The {@code PredatorPreySimulator} is an application that, based on the
 * created population of the Animal Kingdom, randomly chooses two animals and
 * prints out the outcome of a potential fight seen in the wild. <br>
 * The print statement must follow this format: <br>
 * ANIMAL1.toString() + "(" + GENUS_NAME + ")" + " " + WEIGHT + " vs. " +
 * ANIMAL2.toString() + "(" + GENUS_NAME + ")" + " " + WEIGHT + ": " + WEIGHT +
 * "lb " + WINNER.toString() + " wins!"<br>
 * where GENUS_NAME is the {@code String} value of the animal's genus name.
 * 
 * @author BKey
 * 
 */
public class PredatorPreySimulator {

	public static void main(String[] args) {
		List<Object> animalKingdom = new ArrayList<Object>();
		Object animal1, animal2;

		boolean animal1Wins;
		String output = "";

		createKingdom(animalKingdom);

		// randomly pick 2 NOTE: must set the seed to a value if you
		// want repeatability. I don't.
		Random prng = new Random();
		animal1 = animalKingdom.get(prng.nextInt(animalKingdom.size()));
		animal2 = animalKingdom.get(prng.nextInt(animalKingdom.size()));

		// compare and display results
		if (animal1 instanceof Wolf) {
			// Wolf has a genus of "Canis"
			if (animal2 instanceof Wolf) {
				animal1Wins = ((Wolf) animal1).winsFight(animal2);
				output = ((Wolf) animal1).toString() + "(Canis) "
						+ ((Wolf) animal1).getWeight() + " vs. "
						+ ((Wolf) animal2).toString() + "(Canis) "
						+ ((Wolf) animal2).getWeight() + ": ";
				if (animal1Wins)
					output += ((Wolf) animal1).getWeight() + "lb "
							+ ((Wolf) animal1).toString() + " wins!";
				else
					output += ((Wolf) animal2).getWeight() + "lb "
							+ ((Wolf) animal2).toString() + " wins!";
			} else if (animal2 instanceof Eagle) {
				animal1Wins = ((Wolf) animal1).winsFight(animal2);
				output = ((Wolf) animal1).toString() + "(Canis) "
						+ ((Wolf) animal1).getWeight() + " vs. "
						+ ((Eagle) animal2).toString() + "(Haliaeetus) "
						+ ((Eagle) animal2).getWeight() + ": ";
				if (animal1Wins)
					output += ((Wolf) animal1).getWeight() + "lb "
							+ ((Wolf) animal1).toString() + " wins!";
				else
					output += ((Eagle) animal2).getWeight() + "lb "
							+ ((Eagle) animal2).toString() + " wins!";
			} else if (animal2 instanceof Rabbit) {
				animal1Wins = ((Wolf) animal1).winsFight(animal2);
				output = ((Wolf) animal1).toString() + "(Canis) "
						+ ((Wolf) animal1).getWeight() + " vs. "
						+ ((Rabbit) animal2).toString() + "(Sylvilagus) "
						+ ((Rabbit) animal2).getWeight() + ": ";
				if (animal1Wins)
					output += ((Wolf) animal1).getWeight() + "lb "
							+ ((Wolf) animal1).toString() + " wins!";
				else
					output += ((Rabbit) animal2).getWeight() + "lb "
							+ ((Rabbit) animal2).toString() + " wins!";
			}

		} else if (animal1 instanceof Eagle) {
			// Eagle has a genus of "Haliaeetus"
			if (animal2 instanceof Wolf) {
				animal1Wins = ((Eagle) animal1).winsFight(animal2);
				output = ((Eagle) animal1).toString() + "(Haliaeetus) "
						+ ((Eagle) animal1).getWeight() + " vs. "
						+ ((Wolf) animal2).toString() + "(Canis) "
						+ ((Wolf) animal2).getWeight() + ": ";
				if (animal1Wins)
					output += ((Eagle) animal1).getWeight() + "lb "
							+ ((Eagle) animal1).toString() + " wins!";
				else
					output += ((Wolf) animal2).getWeight() + "lb "
							+ ((Wolf) animal2).toString() + " wins!";
			} else if (animal2 instanceof Eagle) {
				animal1Wins = ((Eagle) animal1).winsFight(animal2);
				output = ((Eagle) animal1).toString() + "(Haliaeetus) "
						+ ((Eagle) animal1).getWeight() + " vs. "
						+ ((Eagle) animal2).toString() + "(Haliaeetus) "
						+ ((Eagle) animal2).getWeight() + ": ";
				if (animal1Wins)
					output += ((Eagle) animal1).getWeight() + "lb "
							+ ((Eagle) animal1).toString() + " wins!";
				else
					output += ((Eagle) animal2).getWeight() + "lb "
							+ ((Eagle) animal2).toString() + " wins!";
			} else if (animal2 instanceof Rabbit) {
				animal1Wins = ((Eagle) animal1).winsFight(animal2);
				output = ((Eagle) animal1).toString() + "(Haliaeetus) "
						+ ((Eagle) animal1).getWeight() + " vs. "
						+ ((Rabbit) animal2).toString() + "(Sylvilagus) "
						+ ((Rabbit) animal2).getWeight() + ": ";
				if (animal1Wins)
					output += ((Eagle) animal1).getWeight() + "lb "
							+ ((Eagle) animal1).toString() + " wins!";
				else
					output += ((Rabbit) animal2).getWeight() + "lb "
							+ ((Rabbit) animal2).toString() + " wins!";
			}
		} else if (animal1 instanceof Rabbit) {
			// Rabbit has a genus of "Sylvilagus"
			if (animal2 instanceof Wolf) {
				animal1Wins = ((Rabbit) animal1).winsFight(animal2);
				output = ((Rabbit) animal1).toString() + "(Sylvilagus) "
						+ ((Rabbit) animal1).getWeight() + " vs. "
						+ ((Wolf) animal2).toString() + "(Canis) "
						+ ((Wolf) animal2).getWeight() + ": ";
				if (animal1Wins)
					output += ((Rabbit) animal1).getWeight() + "lb "
							+ ((Rabbit) animal1).toString() + " wins!";
				else
					output += ((Wolf) animal2).getWeight() + "lb "
							+ ((Wolf) animal2).toString() + " wins!";
			} else if (animal2 instanceof Eagle) {
				animal1Wins = ((Rabbit) animal1).winsFight(animal2);
				output = ((Rabbit) animal1).toString() + "(Sylvilagus) "
						+ ((Rabbit) animal1).getWeight() + " vs. "
						+ ((Eagle) animal2).toString() + "(Haliaeetus) "
						+ ((Eagle) animal2).getWeight() + ": ";
				if (animal1Wins)
					output += ((Rabbit) animal1).getWeight() + "lb "
							+ ((Rabbit) animal1).toString() + " wins!";
				else
					output += ((Eagle) animal2).getWeight() + "lb "
							+ ((Eagle) animal2).toString() + " wins!";
			} else if (animal2 instanceof Rabbit) {
				animal1Wins = ((Rabbit) animal1).winsFight(animal2);
				output = ((Rabbit) animal1).toString() + "(Sylvilagus) "
						+ ((Rabbit) animal1).getWeight() + " vs. "
						+ ((Rabbit) animal2).toString() + "(Sylvilagus) "
						+ ((Rabbit) animal2).getWeight() + ": ";
				if (animal1Wins)
					output += ((Rabbit) animal1).getWeight() + "lb "
							+ ((Rabbit) animal1).toString() + " wins!";
				else
					output += ((Rabbit) animal2).getWeight() + "lb "
							+ ((Rabbit) animal2).toString() + " wins!";
			}
		}

		System.out.println(output);
	}

	/**
	 * Create and fill the animal kingdom.
	 * 
	 * @param animalKingdom
	 *            the {@code ArrayList<Object>} that holds all of the animals in
	 *            the kingdom
	 */
	private static void createKingdom(List<Object> animalKingdom) {
		Wolf smallWolf = new Wolf(60.5);
		Wolf mediumWolf = new Wolf(87.2);
		Wolf largeWolf = new Wolf(105.7);
		Eagle smallEagle = new Eagle(3.3);
		Eagle largeEagle = new Eagle(21.0);
		Rabbit smallRabbit = new Rabbit(1.7);
		Rabbit mediumRabbit = new Rabbit(2.2);
		Rabbit largeRabbit = new Rabbit(10);

		animalKingdom.add(smallWolf);
		animalKingdom.add(mediumWolf);
		animalKingdom.add(largeWolf);
		animalKingdom.add(smallEagle);
		animalKingdom.add(largeEagle);
		animalKingdom.add(smallRabbit);
		animalKingdom.add(mediumRabbit);
		animalKingdom.add(largeRabbit);
	}
}
