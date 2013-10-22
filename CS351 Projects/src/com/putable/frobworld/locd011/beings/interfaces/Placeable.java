package com.putable.frobworld.locd011.beings.interfaces;

import com.putable.frobworld.locd011.beings.CollisionResult;
import com.putable.frobworld.locd011.beings.Frob;
import com.putable.frobworld.locd011.beings.PlaceType;

public interface Placeable
{
    /**
     * Set the internal location of this object
     * @param x
     * @param y
     */
    public void setLocation(int x, int y);
    /**
     * Get where the object thinks it is
     * @return
     */
    public int[] getLocation();
    /**
     * Get the PlaceType of this object
     * @return
     * 		PlaceType.ROCK if this is a Rock
     * 		PlaceType.GRASS if this is a Grass
     * 		PlaceType.FROB if this is a Frob.
     */
    public PlaceType getType();
    public CollisionResult collideInto(Frob frob);
}
