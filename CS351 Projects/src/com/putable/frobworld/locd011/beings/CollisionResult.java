package com.putable.frobworld.locd011.beings;

import com.putable.frobworld.locd011.graphics.GraphicsDelta;

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
	private GraphicsDelta change;
	
	public CollisionResult(int healthResult, boolean moveAllowed, GraphicsDelta change)
	{
		this.massResult = healthResult;
		this.moveAllowed = moveAllowed;
		this.change = change;
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
	
	public GraphicsDelta getGraphicsDelta()
	{
	    return change;
	}
}
