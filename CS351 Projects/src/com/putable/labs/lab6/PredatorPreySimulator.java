package com.putable.labs.lab6;

import java.util.Random;

import com.putable.labs.lab6.interfaces.Fightable;

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
		Fightable animal1, animal2;

		// randomly pick 2 NOTE: must set the seed to a value if you
		// want repeatability. I don't.
		Random prng = new Random();
		animal1 = AnimalFactory.getAnimal(prng.nextInt());
		animal2 = AnimalFactory.getAnimal(prng.nextInt());

		System.out.println(FightEngine.fight(animal1, animal2));
	}
}
