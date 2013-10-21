package com.putable.frobworld.locd011.graphics;

import java.awt.Graphics;

import com.putable.frobworld.locd011.graphics.SimulationPanel.Translation;

/**
 * An item which is able to draw itself onto a Graphics object.
 * @author David
 */
public interface Drawable
{
    /**
     * Draw this item on the Graphics object.
     * @param g
     * @param t
     */
    public void drawItem(Graphics g, Translation t);
}
