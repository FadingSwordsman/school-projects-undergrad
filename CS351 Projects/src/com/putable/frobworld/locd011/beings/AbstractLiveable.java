package com.putable.frobworld.locd011.beings;

import com.putable.frobworld.locd011.beings.interfaces.Liveable;
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
	this.nextMovement = world.getDay() + updatePeriod;
	world.createLiveable(this);
    }
    
    @Override
    public int getNextMove()
    {
	return nextMovement;
    }
    
    protected void updateNextMove(int offset)
    {
	this.nextMovement += offset;
    }
    
    @Override
    public int compareTo(PQAble p)
    {
	AbstractLiveable otherObject = (AbstractLiveable) p;
	int difference = getNextMove() - otherObject.getNextMove();
	if(difference == 0)
	    difference = -1;
	return difference;
    }
    
    public void setMass(int mass)
    {
	this.mass = mass;
    }
    
    @Override
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
    
    @Override
    public boolean isDead()
    {
	return dead;
    }
    
    public void die()
    {
	dead = true;
	getWorld().killLiveable(this);
    }
}
