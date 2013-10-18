package com.putable.frobworld.locd011;

/**
 * A holder for collision results
 * Contains all pertinent information on what happens to the
 * 	initiator of a collision
 * @author david
 *
 */
public class CollisionResult
{
	private int healthResult;
	private boolean moveAllowed;
	
	public CollisionResult(int healthResult, boolean moveAllowed)
	{
		this.healthResult = healthResult;
		this.moveAllowed = moveAllowed;
	}

	public int getHealthResult()
	{
		return healthResult;
	}

	public boolean isMoveAllowed()
	{
		return moveAllowed;
	}
}
