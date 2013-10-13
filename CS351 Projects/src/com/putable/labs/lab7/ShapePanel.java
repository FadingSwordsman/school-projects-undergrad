package com.putable.labs.lab7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ShapePanel extends JPanel
{
    private static final long serialVersionUID = -7450960793776661568L;
    private List<Shape> shapes;
    private int maxSize;

    private ShapePanel(List<Shape> shapes, int maxSize)
    {
	this.shapes = shapes;
	this.maxSize = maxSize;
    }

    @Override
    public void paintComponent(Graphics g)
    {
	Random r = new Random();
	int center = getHeight() / 2;
	g.drawRect(center, center, 1, 1);
	for (Shape shape : shapes)
	    shape.draw(g, r.nextInt(maxSize), center);
    }

    /**
     * The enum {@link Shape} defines the 3 shapes that this {@link ShapePanel}
     * can draw.
     * 
     * @author BKey
     * 
     */
    private enum Shape
    {
	SQUARE(new ShapeGen()
	{
	    @Override
	    public void drawShape(Graphics g, int size, int center)
	    {
		int halfLength = size / 2;
		int upper = center + halfLength;
		int lower = center - halfLength;
		int[] xpoints = { lower, upper, upper, lower };
		int[] ypoints = { upper, upper, lower, lower };
		Polygon square = new Polygon(xpoints, ypoints, 4);
		g.setColor(Color.BLUE);
		g.drawPolygon(square);
	    }
	}),
	CIRCLE(new ShapeGen()
	{
	    @Override
	    public void drawShape(Graphics g, int size, int center)
	    {
		g.setColor(Color.RED);
		g.drawArc(center - size / 2, center - size / 2, size, size, 0, 360);
	    }
	}),
	TRIANGLE(new ShapeGen()
	{
	    @Override
	    public void drawShape(Graphics g, int size, int center)
	    {
		int[] xpoints = { center, center + size / 2, center - size / 2 };
		int ypos = (int) (size / Math.sqrt(3));
		int yneg = (int) (size / (2 * Math.sqrt(3)));
		int[] ypoints = { center - ypos, center + yneg, center + yneg };
		Polygon triangle = new Polygon(xpoints, ypoints, 3);
		g.setColor(Color.GREEN);
		g.drawPolygon(triangle);
	    }
	});
	private ShapeGen generator;

	private Shape(ShapeGen generator)
	{
	    this.generator = generator;
	}

	/**
	 * Boilerplate for drawing a Shape of a given type
	 * 
	 * @param g
	 *            The Graphic object associated from the panel you want to
	 *            draw on
	 * @param size
	 *            The size of an appropriate dimension
	 * @param center
	 *            What the center of the shape should be, both X and Y
	 */
	private void draw(Graphics g, int size, int center)
	{
	    generator.drawShape(g, size, center);
	}

	/**
	 * Creates an appropriate shape using the appropriate graphics object.
	 * 
	 * @author David
	 * 
	 */
	private interface ShapeGen
	{
	    public void drawShape(Graphics g, int size, int center);
	}
    }

    /**
     * Uses the Graphic library to create randomly sized shapes of specified
     * type. Triangles, Squares, and Circles are all allowable.
     * 
     * @param args
     *            is an array of {@code Strings} which hold the names of shapes
     *            to draw.
     */
    public static void main(String[] args)
    {
	List<Shape> shapesToDraw = new ArrayList<Shape>();
	String currentShapeName = "";
	if (args.length <= 0)
	    System.out.println("Please provide arguments as to what shape(s) (Square, Circle or Triangle) you would like to draw.");
	else
	{
	    try
	    {
		// fill the list
		for (int i = 0; i < args.length; i++)
		{
		    // can't switch on Strings in Java < 1.7... Gah.
		    currentShapeName = args[i];
		    shapesToDraw.add(Shape.valueOf(currentShapeName.toUpperCase()));
		}
		JFrame mainFrame = new JFrame("Shape Artist");
		int maxSize = 500;
		JPanel shapePanel = new ShapePanel(shapesToDraw, maxSize);
		shapePanel.setPreferredSize(new Dimension(maxSize, maxSize));
		// add the JPanel to the pane
		mainFrame.getContentPane().add(shapePanel, BorderLayout.CENTER);
		// clean up
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
	    }
	    catch (IllegalArgumentException e)
	    {
		System.out.println("This program only draws Squares, Circles or Triangles. Sorry.");
		System.exit(0);
	    }
	}
    }
}
