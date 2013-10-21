package com.putable.frobworld.locd011.graphics;

import java.awt.Graphics;

import com.putable.frobworld.locd011.graphics.SimulationPanel.Translation;

/**
 * An object which indicates something in the simulation has changed, and that the
 * 	panel should be updated in interactive mode.
 * @author David
 *
 */
public interface GraphicsDelta
{
    /**
     * Update the displayed panel with the specified changes.
     * @param g
     * @param t
     */
    public void updateMap(Graphics g, Translation t);
}
