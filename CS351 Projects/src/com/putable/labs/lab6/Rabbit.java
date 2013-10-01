package com.putable.labs.lab6;

import com.putable.labs.lab6.abstracts.AbstractHerbivore;

/**
 * This is a concrete {@code Rabbit} class. An instance of this class is used
 * specifically to be compared to other living things by fact of who would win
 * in a fight. A fight outcome is based on the attributes of a {@code Rabbit}
 * including weight and what they cKingdomonsume for food (i.e. meat, plants or both).
 * 
 * @author BKey
 * 
 */
public class Rabbit extends AbstractHerbivore {
	private String kingdom = "Sylvilagus";
	
	public Rabbit(double weight) {
		super(weight);
	}

	@Override
	public String getKingdom()
	{
	    return kingdom;
	}

	@Override
	public String toString() {
		return "Rabbit";
	}
}
