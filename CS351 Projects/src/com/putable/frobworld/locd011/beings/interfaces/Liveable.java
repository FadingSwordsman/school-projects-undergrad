package com.putable.frobworld.locd011.beings.interfaces;

import com.putable.frobworld.locd011.beings.CollisionResult;
import com.putable.frobworld.locd011.graphics.GraphicsDelta;
import com.putable.pqueue.PQAble;

public interface Liveable extends PQAble, Placeable
{
    /**
     * Take a turn, and return a GraphicsDelta that updates the main screen depending
     * on what changed during the turn.
     * @return
     */
    public GraphicsDelta takeTurn();
    
    /**
     * Apply a collision initiated by this object, to this object.
     * Also keeps track of any graphical changes that might have occurred to the
     * 		object collided into.
     * @param result
     * @return
     */
    public boolean applyCollision(CollisionResult result);
    
    /**
     * Get the mass of this Liveable.
     * @return
     */
    public int getMass();
    
    /**
     * Return the next day that this object will move
     * @return
     */
    public int getNextMove();
    
    /**
     * Check to see if this liveable is no longer fulfilling its ability to live.
     * @return
     */
    public boolean isDead();
}
