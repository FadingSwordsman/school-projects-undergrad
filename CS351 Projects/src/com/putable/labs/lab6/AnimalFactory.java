package com.putable.labs.lab6;

import java.util.List;

import com.putable.labs.lab6.abstracts.AbstractAnimal;

public class AnimalFactory {
	
	private static List<AbstractAnimal> animals;
	
	public static AbstractAnimal getAnimal(int animalNumber)
	{
		if(animals == null)
		{
			animals = new ArrayList<AbstractAnimal>(8);
			Wolf smallWolf = new Wolf(60.5);
			Wolf mediumWolf = new Wolf(87.2);
			Wolf largeWolf = new Wolf(105.7);
			Eagle smallEagle = new Eagle(3.3);
			Eagle largeEagle = new Eagle(21.0);
			Rabbit smallRabbit = new Rabbit(1.7);
			Rabbit mediumRabbit = new Rabbit(2.2);
			Rabbit largeRabbit = new Rabbit(10);

			animals.add(smallWolf);
			animals.add(mediumWolf);
			animals.add(largeWolf);
			animals.add(smallEagle);
			animals.add(largeEagle);
			animals.add(smallRabbit);
			animals.add(mediumRabbit);
			animals.add(largeRabbit);
		}
		
		return animals.get(Math.abs(animalNumber % animals.size()));
	}
}
