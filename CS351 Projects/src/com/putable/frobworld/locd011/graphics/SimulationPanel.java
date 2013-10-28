package com.putable.frobworld.locd011.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import com.putable.frobworld.locd011.beings.PlaceType;
import com.putable.frobworld.locd011.beings.interfaces.Placeable;
import com.putable.frobworld.locd011.simulation.SimulationWorld;
import com.putable.frobworld.locd011.simulation.WorldSetting;

/**
 * A fancy SimulationPanel.
 * 
 * @author David
 * 
 */
public class SimulationPanel extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 8763315806409185943L;
    private Iterable<GraphicsDelta> updates;
    private Translation translation;
    private boolean completedUpdate = false;
    private final SimulationWorld world;
    private boolean initialized = false;
    private List<Placeable> rocks = new LinkedList<Placeable>();
    
    private int waitTime = 1;
    
    private int[] statusText;

    /**
     * Start up the SimulationPanel.
     */
    public SimulationPanel(SimulationWorld world)
    {
	super();
	this.world = world;
    }

    /**
     * Set the list of changes to make
     * @param updates
     */
    public void setDeltaList(Iterable<GraphicsDelta> updates)
    {
	this.updates = updates;
    }

    /**
     * Flag all updates to this panel as complete.
     * @param completedUpdate
     */
    public void setCompletedUpdate(boolean completedUpdate)
    {
	this.completedUpdate = completedUpdate;
    }
    
    /**
     * Get the time that the containing object should wait for this one to update
     * @return
     */
    public int getWaitTime()
    {
	return waitTime;
    }
    
    /**
     * Set the time that the containing object should wait for this one to update
     * @param waitTime
     */
    public void setWaitTime(int waitTime)
    {
	this.waitTime = waitTime;
    }

    /**
     * Calculate a Translation for the simulation panel.
     * 
     * @return
     */
    private Translation getCoordinateTranslation()
    {
	if (translation == null)
	{
	    WorldSetting worldSetting = world.getSimulationSettings().getWorldSettings();
	    final int cellWidth = getWidth() / worldSetting.getWorldWidth();
	    final int cellHeight = getHeight() / worldSetting.getWorldHeight();
	    translation = new Translation()
	    {
		private int widthPerCell = cellWidth;
		private int heightPerCell = cellHeight;

		public int[] translateCoordinates(int[] xyPair)
		{
		    int x = widthPerCell * xyPair[0];
		    int y = heightPerCell * xyPair[1];
		    int width = widthPerCell - 1;
		    int height = heightPerCell - 1;
		    return new int[] { x, y, width, height };
		}
	    };
	    statusText = translation.translateCoordinates(new int[]{0, worldSetting.getWorldHeight() + 1});
	    statusText[2] = 0;
	    statusText[3] = statusText[1] - translation.translateCoordinates(new int[]{0,1})[1];
	}
	return translation;
    }
    
    @Override
    public void repaint()
    {
	if(world == null)
	    return;
	Graphics g = getGraphics();
	paintComponent(g);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
	Translation translator = getCoordinateTranslation();
	if(!initialized)
	{
        	WorldSetting settings = world.getSimulationSettings().getWorldSettings();
        	int height = settings.getWorldHeight();
        	int width = settings.getWorldWidth();
        	for (int x = 0; x < width; x++)
        	    for (int y = 0; y < height; y++)
        	    {
        		Placeable object = world.getPlaceableAt(new int[] { x, y });
        		if (object != null)
        		{
        		    object.getRepresentation().drawItem(g, translator, new int[] { x, y });
        		    if(object.getType() == PlaceType.ROCK)
        			rocks.add(object);
        		}
        	    }
        	initialized = true;
        	completedUpdate = true;
	}
	else
	    for(Placeable rock : rocks)
		rock.getRepresentation().drawItem(g, translator, rock.getLocation());
    }
    
    @Override
    public void paint(Graphics g)
    {
	if (updates != null)
	{
	    Translation coordinateTranslation = getCoordinateTranslation();
	    for (GraphicsDelta update : updates)
		update.updateMap(g, coordinateTranslation);
	    
	    updates = null;
	}
	if(initialized)
	    drawStatus(g);
	completedUpdate = true;
    }
    
    /**
     * Draw a simple status on the bottom of the panel
     * @param g
     */
    private void drawStatus(Graphics g)
    {
	g.clearRect(statusText[2], statusText[3], getWidth(), 40);
	g.setColor(Color.BLACK);
	g.drawString("Current Day: " + world.getDay(), statusText[0], statusText[1]);
	g.drawString(world.getLiveableStatus().toString(), statusText[0], statusText[1] + 14);
	g.drawString("Update speed: Once every " + getWaitTime() + " ms", statusText[0], statusText[1] + 28);
    }

    /**
     * Return whether this panel has completed the most recent update or not
     * @return
     */
    public boolean hasCompletedUpdate()
    {
	return completedUpdate;
    }

    /**
     * Translate i, j coordinates to an x, y, width, height format rectangle on this panel.
     * @author David
     *
     */
    public interface Translation
    {
	public int[] translateCoordinates(int[] xyPair);
    }

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
	if(world == null)
	    return;
	Graphics g = getGraphics();
	paintComponent(g);
	paint(g);
    }
}
