package com.putable.frobworld.locd011;

import javax.swing.JApplet;

import com.putable.frobworld.locd011.graphics.SimulationPanel;
import com.putable.frobworld.locd011.graphics.SimulationSpeedSlider;
import com.putable.frobworld.locd011.simulation.SimulationSettings;
import com.putable.frobworld.locd011.simulation.SimulationWorld;

public class FrobApplet extends JApplet
{
    /**
     * 
     */
    private static final long serialVersionUID = -7559962497197529318L;

    private SimulationWorld world;
    private SimulationPanel worldPanel;
    private SimulationSpeedSlider speedSlider;

    public void init()
    {
	SimulationSettings settings = SimulationSettings.createSettings(25000);
	world = new SimulationWorld(settings, this);
	worldPanel = world.getPanel();
	speedSlider = new SimulationSpeedSlider(1,500,25,worldPanel);
	getContentPane().add(worldPanel);
	getContentPane().add(speedSlider);
	worldPanel.setBounds(0,0, getContentPane().getWidth(), getContentPane().getHeight());
	worldPanel.setWaitTime(5);
	worldPanel.repaint();
	world.runSimulation();
    }
}