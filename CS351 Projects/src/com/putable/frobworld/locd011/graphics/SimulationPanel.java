package com.putable.frobworld.locd011.graphics;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Deque;
import java.util.LinkedList;

import javax.swing.JPanel;

import com.putable.frobworld.locd011.simulation.SimulationWorld;

/**
 * A fancy SimulationPanel.
 * @author David
 *
 */
public class SimulationPanel extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 8763315806409185943L;

    private Deque<GraphicsDelta> updates;
    private Translation translation;
    private boolean initialized = false;
    private SimulationWorld world;
    
    /**
     * Start up the SimulationPanel.
     */
    public SimulationPanel(SimulationWorld world)
    {
	super();
	updates = new LinkedList<GraphicsDelta>();
	this.world = world;
    }
    
    public void addGraphicsDelta(GraphicsDelta toAdd)
    {
	updates.push(toAdd);
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
    public void paintComponent(Graphics g)
    {
	if(!initialized)
	{
	}
	else
	{
	    Translation coordinateTranslation = getCoordinateTranslation();
	    while(!updates.isEmpty())
		updates.pop().updateMap(g, coordinateTranslation);
	}
    }
    
    public interface Translation
    {
	public int[] translateCoordinates(int[] xyPair);
    }

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
	repaint();
    }
}
