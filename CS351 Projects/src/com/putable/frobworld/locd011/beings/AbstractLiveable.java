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

    public AbstractLiveable(PlaceType type, SimulationWorld world)
    {
	super(type, world);
	dead = false;
    }

    public AbstractLiveable(PlaceType type, SimulationWorld world, int birthMass)
    {
	super(type, world);
	nextMovement = world.getDay();
	this.birthMass = birthMass;
	dead = false;
    }

    public AbstractLiveable(PlaceType type, SimulationWorld world, int[] location, int updatePeriod, int birthMass, int mass)
    {
	this(type, world, birthMass);
	this.updatePeriod = updatePeriod;
	setLocation(location[0], location[1]);
	this.mass = mass;
	nextMovement = world.getDay();
	dead = false;
    }

    @Override
    public int getNextMove()
    {
	return nextMovement;
    }

    /**
     * Add the specified number to this object's next movement
     * @param offset
     */
    public void updateNextMove(int offset)
    {
	if(queue != null)
	    throw new IllegalStateException("Cannot update move while on a queue!");
	this.nextMovement += offset;
    }

    @Override
    public int compareTo(PQAble p)
    {
	AbstractLiveable otherObject = (AbstractLiveable) p;
	int difference = getNextMove() - otherObject.getNextMove();
	if (difference == 0)
	    difference = -1;
	return difference;
    }

    /**
     * Set the mass of this Liveable
     * @param mass
     */
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

    /**
     * Get the update period of this Liveable
     * @return
     */
    public int getUpdatePeriod()
    {
	return updatePeriod;
    }

    /**
     * Change the update period of this liveable
     * @param newUpdatePeriod
     */
    public void setUpdatePeriod(int newUpdatePeriod)
    {
	this.updatePeriod = newUpdatePeriod;
    }
    
    /**
     * Set the birthmass of this liveable
     * @param birthMass
     */
    public void setBirthMass(int birthMass)
    {
	this.birthMass = birthMass;
    }

    /**
     * Get the current birthmass of this liveable
     * @return
     */
    public int getBirthMass()
    {
	return birthMass;
    }

    @Override
    public boolean isDead()
    {
	return dead;
    }

    /**
     * Kill this Liveable and notify the world.
     */
    public void die()
    {
	dead = true;
	getWorld().killLiveable(this);
    }
}
