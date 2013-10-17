package com.putable.frobworld.locd011.graphics;

import java.awt.Graphics;

import com.putable.frobworld.locd011.graphics.SimulationPanel.Translation;

public interface GraphicsDelta
{
    public void updateMap(Graphics g, Translation t);
}
