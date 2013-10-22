package com.putable.frobworld.locd011.graphics;

import java.awt.Graphics;

import com.putable.frobworld.locd011.beings.interfaces.Liveable;
import com.putable.frobworld.locd011.graphics.SimulationPanel.Translation;

/**
 * A factory for graphics deltas.
 * @author David
 */
public final class GraphicsDeltaHelper
{
    /**
     * There is no reason to instantiate this.
     */
    private GraphicsDeltaHelper()
    {}
    
    /**
     * Create a GraphicsDelta which removes the current object
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
     * @return
     */
    public static GraphicsDelta nothing()
    {
		return new GraphicsDelta()
		{
		    @Override
		    public void updateMap(Graphics g, Translation t)
		    {}
		};
    }
    
    /**
     * Create a graphics object which updates the interaction between two Liveables
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
				for(Liveable next : updates)
				{
				    int[] xyPairs = t.translateCoordinates(next.getLocation());
				    g.clearRect(xyPairs[0], xyPairs[1], xyPairs[2], xyPairs[3]);
				    next.getType().getRepresentation().drawItem(g, t, next.getLocation());
				}
		    }
		};
    }
    
    public static GraphicsDelta move(final Liveable toMove, final int[] oldCoordinate)
    {
    	return new GraphicsDelta()
    	{
			@Override
			public void updateMap(Graphics g, Translation t) {
				int[] oldCoordinates = t.translateCoordinates(oldCoordinate);
				g.clearRect(oldCoordinates[0], oldCoordinates[1], oldCoordinates[2], oldCoordinates[3]);
				toMove.getRepresentation().drawItem(g, t, toMove.getLocation());
			}
    	};
    }
    
    public static GraphicsDelta append(final GraphicsDelta... deltas)
    {
		return new GraphicsDelta()
		{
		    @Override
		    public void updateMap(Graphics g, Translation t)
		    {
				for(GraphicsDelta next : deltas)
					next.updateMap(g, t);
		    }
		};
    }
}
