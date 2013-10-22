package com.putable.labs.lab9;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimationPanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 6487823903895721745L;

	/**
	 * A ball object, which bounces around on our screen
	 */
	private Ball bouncingBall = new Ball(1, 1, 250, 250);
	private Timer actionTimer;
		
	/**
	 * Create a new AnimationPanel, and start the Timer
	 */
	public AnimationPanel()
	{
		actionTimer = new Timer(1, this);
		actionTimer.start();
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		g.clearRect(0, 0, getWidth(), getHeight());
		bouncingBall.draw(g);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		bouncingBall.update(getHeight(), getWidth());
		repaint();
	}
	
	/**
	 * The driver for this animation.
	 * 
	 * @param args
	 *            none. This driver shouldn't take any arguments.
	 */
	public static void main(String[] args) {
		JFrame mainFrame = new JFrame("Bouncing Ball");
		int maxSize = 500;
		JPanel animation = new AnimationPanel();

		animation.setPreferredSize(new Dimension(maxSize, maxSize));

		// add the JPanel to the pane
		mainFrame.getContentPane().add(animation, BorderLayout.CENTER);

		// clean up
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
	}
	
	/**
	 * A class for a ball that bounces around
	 * @author locd011
	 *
	 */
	private class Ball
	{
		private final int WIDTH = 50;
		private final int HEIGHT = 50;
		
		private int x = -1;
		private int y = -1;
		
		private int yUpdates = 0;
		private int xUpdateEvery = 5;
		
		private int rateX;
		private int rateY;
		
		/**
		 * Create a new Ball at the specified start area, with given rates
		 * @param rateX
		 * @param rateY
		 * @param startX
		 * @param startY
		 */
		public Ball(int rateX, int rateY, int startX, int startY)
		{
			this.rateX = rateX;
			this.rateY = rateY;
			x = startX;
			y = startY;
		}
		
		/**
		 * Update the location of this ball
		 * @param boundX
		 * @param boundY
		 */
		public void update(int boundX, int boundY)
		{
			if(y <= 0 || y + HEIGHT >= boundY - 1)
				rateY = -rateY;
			if(x <= 0 || x + WIDTH >= boundX - 1)
				rateX = -rateX;
			
			updateLocation();
		}
		
		/**
		 * Do the actual update
		 */
		private void updateLocation()
		{
			yUpdates++;
			y += rateY;
			if(yUpdates % xUpdateEvery == 0)
				x += rateX;
		}
		
		/**
		 * Draw this object on the specified component
		 * @param g
		 */
		public void draw(Graphics g)
		{
			g.setColor(Color.RED);
			g.fillOval(x, y, WIDTH, HEIGHT);
		}
	}
}
