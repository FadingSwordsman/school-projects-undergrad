package com.putable.frobworld.locd011;

import com.putable.frobworld.locd011.graphics.GraphicsDelta;

public interface Liveable extends Placeable
{
    public GraphicsDelta takeTurn();
    public boolean applyCollision(CollisionResult result);
    public void spawn();
}
