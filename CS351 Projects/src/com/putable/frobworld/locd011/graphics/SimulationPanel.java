package com.putable.frobworld.locd011.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import com.putable.frobworld.locd011.beings.interfaces.Placeable;
import com.putable.frobworld.locd011.simulation.SimulationWorld;
import com.putable.frobworld.locd011.simulation.WorldSetting;

/**
 * A fancy SimulationPanel.
 * @author David
 *
 */
public class SimulationPanel extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 8763315806409185943L;

    private Iterable<GraphicsDelta> updates;
    private Translation translation;
    private boolean initialized = false;
    private boolean completedUpdate = false; 
    private final SimulationWorld world;
    
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
			WorldSetting worldSetting = world.getSimulationSettings().getWorldSettings();
			final int cellWidth = getWidth() / worldSetting.getWorldWidth();
			final int cellHeight = getHeight() / worldSetting.getWorldHeight();
		    translation = new Translation(){
		    	private int widthPerCell = cellWidth;
		    	private int heightPerCell = cellHeight;
		    	
				public int[] translateCoordinates(int[] xyPair)
				{
				    int x = widthPerCell * xyPair[0] + 1;
				    int y = heightPerCell * xyPair[1] + 1;
				    int width = widthPerCell - 1;
				    int height = heightPerCell - 1;
				    return new int[]{x, y, width, height};
				}
		    };
		}
		return translation;
    }
    
    //TODO: Implement actual drawing of items on the panel
    public void paintComponent(Graphics g)
    {
    	completedUpdate = false;
		if(!initialized)
		{
			WorldSetting settings = world.getSimulationSettings().getWorldSettings();
			Translation translator = getCoordinateTranslation();
			int height = settings.getWorldHeight();
			int width = settings.getWorldWidth();
			for(int x = 0; x < width; x++)
			{
				int[] top = translator.translateCoordinates(new int[]{x,0});
				g.setColor(Color.black);
				g.drawLine(top[0]-1, 0, top[0]-1, getHeight() - 1);
				for(int y = 0; y < height; y++)
				{
					Placeable object = world.getPlaceableAt(new int[]{x,y});
					if(object != null)
						object.getRepresentation().drawItem(g, translator, new int[]{x,y});
				}
			}
			g.setColor(Color.BLACK);
			for(int y = 0; y < height; y++)
			{
				int[] ys = translator.translateCoordinates(new int[]{0,y});
				g.drawLine(0, ys[1] - 1, getWidth() - 1, ys[1] - 1);
			}
		}
		else
		{
		    Translation coordinateTranslation = getCoordinateTranslation();
		    for(GraphicsDelta update : updates)
		    	update.updateMap(g, coordinateTranslation);
		}
		completedUpdate = true;
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
    	repaint();
    }
}
