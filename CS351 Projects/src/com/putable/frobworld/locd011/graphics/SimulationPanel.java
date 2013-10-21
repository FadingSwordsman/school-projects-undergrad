package com.putable.frobworld.locd011.graphics;

import java.util.Deque;
import java.util.LinkedList;

import javax.swing.JPanel;

/**
 * A fancy SimulationPanel.
 * @author David
 *
 */
public class SimulationPanel extends JPanel implements Runnable
{
    private static final long serialVersionUID = 8763315806409185943L;

    private Deque<GraphicsDelta> updates;
    private Translation translation;
    
    /**
     * Start up the SimulationPanel.
     */
    public SimulationPanel()
    {
	super();
	updates = new LinkedList<GraphicsDelta>();
    }
    
    @Override
    public void run()
    {
	//Continuously check for changes:
	while(true)
	    if(!updates.isEmpty())
		updates.pop().updateMap(getGraphics(), getCoordinateTranslation());
    }
    
    /**
     * Calculate a Translation for the simulation panel.
     * @return
     */
    private Translation getCoordinateTranslation()
    {
	//TODO: Implement a way to clear translation on resize, translate i,j coordinates to x,y
	//TODO: Make it recalculate on resize
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
    
    //TODO: Implement actual drawing of items on the panel
    
    public interface Translation
    {
	public int[] translateCoordinates(int[] xyPair);
    }
}
