package com.putable.frobworld.locd011.beings;

import java.awt.Color;
import java.awt.Graphics;

import com.putable.frobworld.locd011.beings.interfaces.Placeable;
import com.putable.frobworld.locd011.graphics.Drawable;
import com.putable.frobworld.locd011.graphics.SimulationPanel.Translation;
import com.putable.frobworld.locd011.simulation.SimulationWorld;

/**
 * An enum to handle representing and evaluating types of Frobs, Rocks, and Grass
 * @author David
 *
 */
public enum PlaceType
{
    ROCK('R', rockRepresentation(), rockCreator()),
    FROB('F', frobRepresentation(), frobCreator()),
    GRASS('G', grassRepresentation(), grassCreator());
    
    //TODO: Implement Drawables for each of these types.
    private char asciiRepresentation;
    private Drawable representation;
    private Creator creator;
    
    private PlaceType(char ascii, Drawable image, Creator creator)
    {
    	this.asciiRepresentation = ascii;
    	this.representation = image;
    	this.creator = creator;
    }
    
    /**
     * Handle vanilla creation of this object for Genesis.
     * @param inWorld
     * @return
     */
    public Placeable create(SimulationWorld inWorld)
    {
	return creator.create(inWorld);
    }
    
    /**
     * Return an ascii representation of this object
     * @return
     */
    public char getAscii()
    {
    	return asciiRepresentation;
    }
    
    /**
     * Return a fancy, drawable representation of this object.
     * @return
     */
    public Drawable getRepresentation()
    {
    	//TODO: Implement this and stuff
    	return representation;
    }
    
    /**
     * Boilerplate for a Rock/Frob/Grass creator
     * @author David
     *
     */
    private interface Creator
    {
	public Placeable create(SimulationWorld inWorld);
    }
    
    /**
     * Create a creator that creates frobs
     * @return
     */
    private static Creator frobCreator()
    {
	return new Creator()
	{
	    public Placeable create(SimulationWorld inWorld)
	    {
		return new Frob(inWorld);
	    }
	};
    }
    
    /**
     * Create a creator that creates grass
     * @return
     */
    private static Creator grassCreator()
    {
	return new Creator()
	{
	    public Placeable create(SimulationWorld inWorld)
	    {
		return new Grass(inWorld);
	    }
	};
    }
    
    /**
     * Create a creator that creates rocks
     * @return
     */
    private static Creator rockCreator()
    {
	return new Creator()
	{
	    public Placeable create(SimulationWorld inWorld)
	    {
		return new Rock(inWorld);
	    }
	};
    }
    
    private static Drawable rockRepresentation()
    {
    	return new Drawable()
    	{
    		@Override
			public void drawItem(Graphics g, Translation t, int[] location)
			{
				int[] translatedLocation = t.translateCoordinates(location);
				g.setColor(Color.GRAY);
				g.fillRect(translatedLocation[0], translatedLocation[1], translatedLocation[2], translatedLocation[3]);
			}
    	};
    }
    
    private static Drawable frobRepresentation()
    {
    	return new Drawable()
    	{
    		@Override
			public void drawItem(Graphics g, Translation t, int[] location)
			{
				int[] translatedLocation = t.translateCoordinates(location);
				g.setColor(Color.RED);
				g.fillRect(translatedLocation[0], translatedLocation[1], translatedLocation[2], translatedLocation[3]);
			}
    	};
    }
    
    private static Drawable grassRepresentation()
    {
    	return new Drawable()
    	{
    		@Override
			public void drawItem(Graphics g, Translation t, int[] location)
			{
				int[] translatedLocation = t.translateCoordinates(location);
				g.setColor(Color.GREEN);
				g.fillRect(translatedLocation[0], translatedLocation[1], translatedLocation[2], translatedLocation[3]);
			}
    	};
    }
}
