package com.putable.frobworld.locd011.graphics;

import java.awt.Graphics;

import com.putable.frobworld.locd011.Liveable;
import com.putable.frobworld.locd011.graphics.SimulationPanel.Translation;

public class GraphicsDeltaHelper
{
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
		    next.getType().getRepresentation().drawItem(g, t);
		    
		}
	    }
	};
    }
}
