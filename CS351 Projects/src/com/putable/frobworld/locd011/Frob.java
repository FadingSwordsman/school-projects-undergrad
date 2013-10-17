package com.putable.frobworld.locd011;

import com.putable.frobworld.locd011.graphics.Drawable;
import com.putable.frobworld.locd011.graphics.GraphicsDelta;

public class Frob extends AbstractPlaceable implements Liveable
{
    public Frob()
    {
	super(PlaceType.FROB);
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

    @Override
    public boolean applyCollision(CollisionResult result)
    {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public CollisionResult collideInto()
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void spawn()
    {
	// TODO Auto-generated method stub
	
    }
}
