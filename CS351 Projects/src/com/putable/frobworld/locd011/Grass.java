package com.putable.frobworld.locd011;

import com.putable.frobworld.locd011.graphics.Drawable;
import com.putable.frobworld.locd011.graphics.GraphicsDelta;

public class Grass extends AbstractPlaceable implements Liveable
{
    public Grass()
    {
	super(PlaceType.GRASS);
    }
    
    @Override
    public CollisionResult collideInto()
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Drawable getRepresentation()
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public GraphicsDelta takeTurn()
    {
	// TODO Auto-generated method stub
	return null;
    }
    
    public void spawn()
    {
	
    }

    @Override
    public boolean applyCollision(CollisionResult result)
    {
	// TODO Auto-generated method stub
	return false;
    }
}
