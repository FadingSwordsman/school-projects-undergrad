package com.putable.frobworld.locd011.graphics;

import java.util.Deque;
import java.util.LinkedList;

import javax.swing.JPanel;

public class SimulationPanel extends JPanel implements Runnable
{
    private static final long serialVersionUID = 8763315806409185943L;

    private Deque<GraphicsDelta> updates;
    private Translation translation;
    
    public SimulationPanel()
    {
	super();
	updates = new LinkedList<GraphicsDelta>();
    }
    
    @Override
    public void run()
    {
	while(true)
	{
	    if(!updates.isEmpty())
		updates.pop().updateMap(getGraphics(), getCoordinateTranslation());
	}
    }
    
    private Translation getCoordinateTranslation()
    {
	//TODO: Implement a way to clear translation on resize, translate i,j coordinates to x,y
	if(translation == null)
	{
	    translation = new Translation(){
		public int[] translateCoordinates(int[] xyPair)
		{
		    return xyPair;
		}
	    };
	}
	return translation;
    }
    
    
    
    public interface Translation
    {
	public int[] translateCoordinates(int[] xyPair);
    }
}
