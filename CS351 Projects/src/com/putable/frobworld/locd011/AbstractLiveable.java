package com.putable.frobworld.locd011;

import com.putable.frobworld.locd011.simulation.SimulationWorld;
import com.putable.pqueue.PQAble;
import com.putable.pqueue.PQueue;

public abstract class AbstractLiveable extends AbstractPlaceable implements Liveable
{
    private int nextMovement;
    private PQueue queue = null;
    private int index = 0;
    private int mass;
    private int updatePeriod;
    private int birthMass;
    private boolean dead;

    public AbstractLiveable(PlaceType type, SimulationWorld world, int birthMass)
    {
	super(type, world);
	this.birthMass = birthMass;
	dead = false;
    }
    
    public AbstractLiveable(PlaceType type, SimulationWorld world, int[] location, int updatePeriod, int birthMass, int mass)
    {
	this(type, world, birthMass);
	this.updatePeriod = updatePeriod;
	setLocation(location[0], location[1]);
	this.mass = mass;
	world.createLiveable(this);
    }
    
    public int getNextMove()
    {
	return nextMovement;
    }
    
    protected void updateNextMove(int offset)
    {
	this.nextMovement += offset;
    }
    
    public int compareTo(PQAble p)
    {
	AbstractLiveable otherObject = (AbstractLiveable) p;
	int difference = otherObject.getNextMove() - getNextMove();
	if(difference == 0)
	    difference = -1;
	return difference;
    }
    
    public void setMass(int mass)
    {
	this.mass = mass;
    }
    
    public int getMass()
    {
	return mass;
    }
    
    @Override
    public void setPQueue(PQueue queue)
    {
	this.queue = queue;
    }
    
    @Override
    public PQueue getPQueue()
    {
	return queue;
    }

    @Override
    public int getIndex()
    {
	return index;
    }

    @Override
    public void setIndex(int index)
    {
	this.index = index;
    }
    
    public int getUpdatePeriod()
    {
	return updatePeriod;
    }
    
    public void setUpdatePeriod(int newUpdatePeriod)
    {
	this.updatePeriod = newUpdatePeriod;
    }
    
    public int getBirthMass()
    {
	return birthMass;
    }
    
    public boolean isDead()
    {
	return dead;
    }
    
    public void die()
    {
	dead = true;
    }
}
