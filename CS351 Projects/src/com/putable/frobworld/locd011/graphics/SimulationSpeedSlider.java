package com.putable.frobworld.locd011.graphics;

import javax.swing.JSlider;

public class SimulationSpeedSlider extends JSlider
{
    private static final long serialVersionUID = 8534977044449808468L;

    private SimulationPanel panel;
    
    public SimulationSpeedSlider(int min, int max, int value, SimulationPanel panel)
    {
	super(min, max, value);
	this.panel = panel;
    }
    
    public void fireStateChanged()
    {
	super.fireStateChanged();
	panel.setWaitTime(getValue());
    }
}
