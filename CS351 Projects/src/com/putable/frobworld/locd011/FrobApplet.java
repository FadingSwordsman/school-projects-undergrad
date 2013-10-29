package com.putable.frobworld.locd011;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.putable.frobworld.locd011.graphics.SimulationPanel;
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
    private final SimulationSettings settings = SimulationSettings.createSettings(25000);;
    
    private JLabel updateSpeed;
    private JLabel seed;
    private JTextField updateSpeedValue;
    private JTextField seedValue;
    private JButton startButton;

    public void init()
    {
	startButton = new JButton("Start");
	startButton.addActionListener(new ActionListener()
	{
	    @Override
	    public void actionPerformed(ActionEvent arg0)
	    {
		int seed = 0;
		boolean seedSet = !seedValue.getText().isEmpty();
		int updateSpeed = 30;
		boolean updateSpeedSet = !updateSpeedValue.getText().isEmpty();
		if(seedSet)
		    try
			{
				seed = Integer.parseInt(seedValue.getText());
			}
        		catch(NumberFormatException e)
        		{
        		    
        		    return;
        		}
		if (updateSpeedSet)
		    try
		    {
			seed = Integer.parseInt(updateSpeedValue.getText());
		    }
		    catch (NumberFormatException e)
		    {
			return;
		    }
		if(seedSet)
		    world = new SimulationWorld(settings, seed);
		else
		    world = new SimulationWorld(settings);
		worldPanel = new SimulationPanel(world);
		world.setPanel(worldPanel);
		startSimulation();
	    }
	});
	getContentPane().add(startButton, BorderLayout.NORTH);
	getContentPane().validate();
    }
    
    public void startSimulation()
    {
	getContentPane().remove(startButton);
	clear(getGraphics());
	getContentPane().add(worldPanel, BorderLayout.CENTER);
	getContentPane().validate();
	getContentPane().repaint();
	world.runSimulation();
    }
    
    private void clear(Graphics g)
    {
	g.clearRect(0, 0, getHeight(), getWidth());
    }
}