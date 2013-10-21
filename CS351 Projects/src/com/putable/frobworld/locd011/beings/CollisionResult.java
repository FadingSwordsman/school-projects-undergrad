package com.putable.frobworld.locd011.beings;

/**
 * A holder for collision results
 * Contains all pertinent information on what happens to the
 * 	initiator of a collision
 * @author david
 *
 */
public class CollisionResult
{
	private int massResult;
	private boolean moveAllowed;
	
	public CollisionResult(int healthResult, boolean moveAllowed)
	{
		this.massResult = healthResult;
		this.moveAllowed = moveAllowed;
	}

	/**
	 * The change of mass on the collidee
	 * @return
	 */
	public int getMassResult()
	{
		return massResult;
	}

	/**
	 * Notifies the Frob whether it can complete its attempted move
	 * @return
	 */
	public boolean isMoveAllowed()
	{
		return moveAllowed;
	}
}
