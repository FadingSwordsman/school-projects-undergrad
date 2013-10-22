package com.putable.frobworld.locd011.beings;

import com.putable.frobworld.locd011.graphics.Drawable;
import com.putable.frobworld.locd011.graphics.GraphicsDeltaHelper;
import com.putable.frobworld.locd011.simulation.SimulationWorld;

/**
 * It's not just a boulder, it's a rock.
 * @author David
 *
 */
public final class Rock extends AbstractPlaceable
{
    /**
     * Create a rock that the world can put somewhere.
     * @param world
     */
    public Rock(SimulationWorld world)
    {
	super(PlaceType.ROCK, world);
    }

    @Override
    public CollisionResult collideInto(Frob collider)
    {
	return new CollisionResult(-getWorld().getSimulationSettings().getMiscSettings().getRockBumpPenalty(), false, GraphicsDeltaHelper.nothing());
    }

    @Override
    public Drawable getRepresentation()
    {
	//TODO: Create a drawn representation of a rock.
	return null;
    }
}
