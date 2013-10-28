package com.putable.frobworld.locd011.graphics;

import javax.swing.JSlider;

/**
 * A slider for controlling the speed of the associated simulation.
 * @author David
 *
 */
public class SimulationSpeedSlider extends JSlider
{
    private static final long serialVersionUID = 8534977044449808468L;

    private SimulationPanel panel;
    
    /**
     * Create this slider, and associate it with the specified SimulationPanel
     * @param min
     * @param max
     * @param value
     * @param panel
     */
    public SimulationSpeedSlider(int min, int max, int value, SimulationPanel panel)
    {
	super(min, max, value);
	this.panel = panel;
	fireStateChanged();
    }
    
    public void fireStateChanged()
    {
	super.fireStateChanged();
	panel.setWaitTime(getValue());
    }
}
