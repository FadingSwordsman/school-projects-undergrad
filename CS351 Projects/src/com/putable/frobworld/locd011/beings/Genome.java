package com.putable.frobworld.locd011.beings;

import java.util.Random;

import com.putable.frobworld.locd011.simulation.SimulationWorld.Direction;

/**
 * A class for holding genomes, and making the information in that genome available to whatever needs it
 * @author David
 *
 */
public class Genome
{
    /**
     * Static, constant settings for indexing into the genome
     */
    public final static byte DNA_BIRTH_MASS = 0,
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
    
    /**
     * Create a new, random genome
     * @param r
     */
    public Genome(Random r)
    {
	genome = new byte[DNA_LENGTH];
	for(int x = 0; x < DNA_LENGTH; x++)
	    r.nextBytes(genome);
    }
    
    /**
     * Create a new genome from another, possibly mutating bits 
     * @param r
     * @param parentGenome
     * @param mutationOdds
     */
    public Genome(Random r, Genome parentGenome, int mutationOdds)
    {
	genome = new byte[DNA_LENGTH];
	for(int i = 0; i < DNA_LENGTH; i++)
	{
	    byte nextStrand = parentGenome.getByteAt(i);
	    int mutationChance = r.nextInt(mutationOdds);
	    if(mutationChance == 0)
	    {
		byte mask = mutateRandomBitMask(r);
		nextStrand = (byte)(nextStrand ^ mask);
	    }
	    genome[i] = nextStrand;
	    
	}
    }
    
    /**
     * Return the raw value for BirthMass
     * @return
     */
    public short getBirthMass()
    {
	return getRawValue(DNA_BIRTH_MASS);
    }
    
    /**
     * Return the raw value for BirthPercent
     * @return
     */
    public short getBirthPercent()
    {
	return getRawValue(DNA_BIRTH_PERCENT);
    }
    
    /**
     * Return the raw value for UpdatePeriod
     * @return
     */
    public short getUpdatePeriod()
    {
	return getRawValue(DNA_UPDATE_PERIOD);
    }
    
    /**
     * Make sure the result is positive (Adjust it from the negative value a full byte returns)
     * @param fromShort
     * @return
     */
    private short positiveShort(short fromShort)
    {
	short result = fromShort;
	if(result < 0)
	    result = (short)(result + 256);
	return result;
    }
    
    /**
     * Return the raw value for direction preferences
     * @param dir
     * @param occupying
     * @return
     */
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
	return getRawValue(preferenceOffset);
    }
    
    /**
     * Choose a random bit to mutate, and make a mask for it
     * @param r
     * @return
     */
    private byte mutateRandomBitMask(Random r)
    {
	int randomByte = r.nextInt(8);
	return (byte)(1 << randomByte);
    }
    
    /**
     * Return the byte at the specified index
     * @param i
     * @return
     */
    private byte getByteAt(int i)
    {
	return genome[i];
    }
    
    /**
     * Return a partially processed value for the given index
     * @param index
     * @return
     */
    public short getRawValue(int index)
    {
	return positiveShort(genome[index]);
    }
    
    /**
     * Return the length of the genome
     * @return
     */
    public static int getGenomeLength()
    {
	return DNA_LENGTH;
    }
    
    @Override
    public String toString()
    {
	StringBuilder sb = new StringBuilder();
	for(int x = 0; x < DNA_LENGTH; x++)
	    sb.append('[').append(getRawValue(x)).append(']');
	return sb.toString();
    }
}
