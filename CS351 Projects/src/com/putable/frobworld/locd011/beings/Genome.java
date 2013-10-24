package com.putable.frobworld.locd011.beings;

import java.util.Random;

import com.putable.frobworld.locd011.simulation.SimulationWorld.Direction;

public class Genome
{
    private final static short DNA_BIRTH_MASS = 0,
	    DNA_BIRTH_PERCENT = 1,
	    DNA_UPDATE_PERIOD = 2,
	    DNA_NORTH_PREFS = 3,
	    DNA_EMPTY_OFFSET = 0,
	    DNA_ROCK_OFFSET = 1,
	    DNA_GRASS_OFFSET = 2,
	    DNA_FROB_OFFSET = 3,
	    DNA_SOUTH_PREFS = 7,
	    DNA_EAST_PREFS = 11,
	    DNA_WEST_PREFS = 15,
	    DNA_LENGTH = 19;
    
    private byte[] genome;
    
    public Genome(Random r)
    {
	genome = new byte[DNA_LENGTH];
	for(int x = 0; x < DNA_LENGTH; x++)
	    r.nextBytes(genome);
    }
    
    public short getBirthMass()
    {
	return positiveShort(genome[DNA_BIRTH_MASS]);
    }
    
    public short getBirthPercent()
    {
	return positiveShort(genome[DNA_BIRTH_PERCENT]);
    }
    
    public short getUpdatePeriod()
    {
	return positiveShort(genome[DNA_UPDATE_PERIOD]);
    }
    
    private short positiveShort(short fromShort)
    {
	short result = fromShort;
	if(result < 0)
	    result = (short)-result;
	return result;
    }
    
    public short getDirectionPrefs(Direction dir, PlaceType occupying)
    {
	int preferenceOffset = 0;
	if(occupying == null)
	    preferenceOffset = DNA_EMPTY_OFFSET;
	else
	{
	    switch(occupying)
	    {
		case ROCK:
		    preferenceOffset = DNA_ROCK_OFFSET;
		    break;
		case GRASS:
		    preferenceOffset = DNA_GRASS_OFFSET;
		    break;
		case FROB:
		    preferenceOffset = DNA_FROB_OFFSET;
		    break;
	    }
	}
	
	switch(dir)
	{
	    case NORTH:
		preferenceOffset += DNA_NORTH_PREFS;
		break;
	    case EAST:
		preferenceOffset += DNA_EAST_PREFS;
		break;
	    case SOUTH:
		preferenceOffset += DNA_SOUTH_PREFS;
		break;
	    case WEST:
	    	preferenceOffset += DNA_WEST_PREFS;
	    	break;
	}
	return positiveShort(genome[preferenceOffset]);
    }
}
