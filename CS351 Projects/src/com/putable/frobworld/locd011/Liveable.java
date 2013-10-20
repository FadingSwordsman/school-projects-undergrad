package com.putable.frobworld.locd011;

import com.putable.frobworld.locd011.graphics.GraphicsDelta;
import com.putable.pqueue.PQAble;

public interface Liveable extends PQAble, Placeable
{
    public GraphicsDelta takeTurn();
    public boolean applyCollision(CollisionResult result);
    public int getMass();
    public int getNextMove();
    public boolean isDead();
}
