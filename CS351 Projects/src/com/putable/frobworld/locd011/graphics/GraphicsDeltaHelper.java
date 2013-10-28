package com.putable.frobworld.locd011.graphics;

import java.awt.Graphics;

import com.putable.frobworld.locd011.beings.interfaces.Liveable;
import com.putable.frobworld.locd011.beings.interfaces.Placeable;
import com.putable.frobworld.locd011.graphics.SimulationPanel.Translation;

/**
 * A factory for graphics deltas.
 * 
 * @author David
 */
public final class GraphicsDeltaHelper
{
    /**
     * There is no reason to instantiate this.
     */
    private GraphicsDeltaHelper()
    {
    }

    /**
     * Create a GraphicsDelta which removes the current object
     * 
     * @param location
     * @return
     */
    public static GraphicsDelta removeAt(final int[] location)
    {
	return new GraphicsDelta()
	{
	    @Override
	    public void updateMap(Graphics g, Translation t)
	    {
		int[] xyPairs = t.translateCoordinates(location);
		g.clearRect(xyPairs[0], xyPairs[1], xyPairs[2], xyPairs[3]);
	    }
	};
    }

    /**
     * Create a Graphics Delta that indicates no change
     * 
     * @return
     */
    public static GraphicsDelta nothing()
    {
	return new GraphicsDelta()
	{
	    @Override
	    public void updateMap(Graphics g, Translation t)
	    {
	    }
	};
    }
    
    /**
     * Move the specified object from a location to another, by clearing the previous location and drawing in the new one.
     * @param fromLocation
     * @param toLocation
     * @param object
     * @return
     */
    public static GraphicsDelta moveTo(final int[] fromLocation, final int[] toLocation, final Placeable object)
    {
	return new GraphicsDelta()
	{
	    private int[] outOf = fromLocation;
	    private int[] into = toLocation;
	    private Drawable representation = object.getRepresentation();
	    
	    @Override
	    public void updateMap(Graphics g, Translation t)
	    {
		int[] clear = t.translateCoordinates(outOf);
		g.clearRect(clear[0], clear[1], clear[2], clear[3]);
		representation.drawItem(g, t, into);
	    }
	};
    }

    /**
     * Create a graphics object which updates any changes in the specified Liveables.
     * Liveables
     * 
     * @param updates
     * @return
     */
    public static GraphicsDelta updateLiveables(final Liveable... updates)
    {
	return new GraphicsDelta()
	{
	    @Override
	    public void updateMap(Graphics g, Translation t)
	    {
		for (Liveable next : updates)
		{
		    int[] xyPairs = t.translateCoordinates(next.getLocation());
		    g.clearRect(xyPairs[0], xyPairs[1], xyPairs[2], xyPairs[3]);
		    next.getRepresentation().drawItem(g, t, next.getLocation());
		}
	    }
	};
    }

    /**
     * Append two or more deltas together, making them into a single delta
     * 		that simply executes them in order.
     * @param deltas
     * @return
     */
    public static GraphicsDelta append(final GraphicsDelta... deltas)
    {
	return new GraphicsDelta()
	{
	    @Override
	    public void updateMap(Graphics g, Translation t)
	    {
		for (GraphicsDelta next : deltas)
		    next.updateMap(g, t);
	    }
	};
    }
}
