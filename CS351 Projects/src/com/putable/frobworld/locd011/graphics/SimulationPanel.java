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
    
    private int[] statusText;

    /**
     * Start up the SimulationPanel.
     */
    public SimulationPanel(SimulationWorld world)
    {
	super();
	this.world = world;
    }

    public void setDeltaList(Iterable<GraphicsDelta> updates)
    {
	this.updates = updates;
    }

    public void setCompletedUpdate(boolean completedUpdate)
    {
	this.completedUpdate = completedUpdate;
    }

    /**
     * Calculate a Translation for the simulation panel.
     * 
     * @return
     */
    private Translation getCoordinateTranslation()
    {
	// TODO: Implement a way to clear translation on resize, translate i,j
	// coordinates to x,y
	// TODO: Make it recalculate on resize
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
    
    // TODO: Implement actual drawing of items on the panel
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
	drawStatus(g);
	completedUpdate = true;
    }
    
    private void drawStatus(Graphics g)
    {
	g.clearRect(statusText[2], statusText[3], getWidth(), getHeight());
	g.setColor(Color.BLACK);
	g.drawString(world.getLiveableStatus().toString(), statusText[0], statusText[1]);
    }

    public boolean hasCompletedUpdate()
    {
	return completedUpdate;
    }

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
