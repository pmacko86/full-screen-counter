package com.aific.fullscreencounter;

//import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;


/**
 * The star indicator
 * 
 * @author Peter Macko
 */
public class StarIndicator implements BackgroundIndicator {
	
	private Dimension size;
	private Color background;
	
	private int value;
	private int minValue;
	private int maxValue;
	
	private ArrayList<Star> stars;
	
	private int starMinRadius;
	private int starMaxRadius;
	private int starVariations;
	private double starMinDist;
	
	private ImageIcon starIcon;
	private ImageIcon starShiningIcon;
	
	private Image[] starScaledImages;
	private Image[] starShiningScaledImages;
	private MediaTracker imageMediaTracker;
	
	
	/**
	 * Create an instance of class StarIndicator
	 * 
	 * @param component the component
	 * @param minValue the minimum value
	 * @param maxValue the maximum value
	 * @param size the size
	 */
	public StarIndicator(JComponent component, int minValue, int maxValue, Dimension size) {
		
		this.stars = new ArrayList<StarIndicator.Star>();
		
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.size = size;
		this.background = Color.BLACK;
		
		starMinRadius  = size.height / 60;
		starMaxRadius  = size.height / 25;
		starVariations = starMaxRadius - starMinRadius + 1;
		starMinDist    = starMaxRadius * 2.5;
		
		starIcon = new ImageIcon(StarIndicator.class.getResource("Star - Normal.png"));
		starShiningIcon = new ImageIcon(StarIndicator.class.getResource("Star - Shining.png"));
		
		starScaledImages = new Image[starVariations];
		starShiningScaledImages = new Image[starVariations];
		
		imageMediaTracker = new MediaTracker(component);

		for (int i = 0; i < starScaledImages.length; i++) {
			int r = starMinRadius + i;
			
			starScaledImages[i] = starIcon.getImage()
					.getScaledInstance(2 * r, 2 * r, Image.SCALE_SMOOTH);
			starShiningScaledImages[i] = starShiningIcon.getImage()
					.getScaledInstance(2 * r, 2 * r, Image.SCALE_SMOOTH);
			
			imageMediaTracker.addImage(starScaledImages[i], 0);
			imageMediaTracker.addImage(starShiningScaledImages[i], 0);
		}

		try {
			imageMediaTracker.waitForAll();
		}
		catch(InterruptedException e) {}

		setValue(this.minValue);
	}


	/**
	 * Set the value
	 * 
	 * @param value the new value
	 */
	@Override
	public void setValue(int value) {
		this.value = value;
		
		if (stars.size() < value) {
			for (int i = stars.size(); i < value; i++) {
				
				int x, y;
				int tries = 0;
				
				do {
					x = (int) (Math.random() * (size.width  - 2 * starMaxRadius));
					y = (int) (Math.random() * (size.height - 2 * starMaxRadius));
					if (tries++ > 500) return;	// Too many stars!
				}
				while (!isFarEnough(x, y));
				
				stars.add(new Star(x, y, (int) (Math.random() * starVariations)));
			}
		}
	}
	
	
	/**
	 * Set the background color
	 * 
	 * @param color the color
	 */
	@Override
	public void setBackground(Color color) {
		this.background = color;
	}

	
	/**
	 * Paint
	 * 
	 * @param g the graphics object
	 */
	@Override
	public void paint(Graphics2D g) {
		
		Composite defaultComposite = g.getComposite();
		
		g.setClip(0, 0, size.width, size.height);
		
		g.setColor(background);
		g.fillRect(0, 0, size.width, size.height);
		
		
		// Wait for the images
		
		while (!imageMediaTracker.checkAll()) {
			Thread.yield();
		}

		
		// Paint the stars
		
		for (int i = 0; i < Math.min(stars.size(), value); i++) {
			stars.get(i).paint(g);
		}
		
		
		// Finish
		
		g.setComposite(defaultComposite);
	}
	
	
	/**
	 * Determine if the given coordinates are a minimum distance away from all
	 * of the already placed stars.
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * @return true if it is far enough
	 */
	private boolean isFarEnough(int x, int y) {
		
		for (Star s : stars) {
			double d = Math.sqrt((x - s.x) * (x - s.x) + (y - s.y) * (y - s.y));
			if (d < starMinDist) return false;
		}
		
		return true;
	}

	
	/**
	 * A star
	 */
	protected class Star {
		
		public static final int RADIUS = 5;
		public static final int MIN_DISTANCE = 25;

		public int x;
		public int y;
		public int variation;
		
		
		/**
		 * Create an instance of class Star
		 * 
		 * @param x the X coordinate
		 * @param y the Y coordinate
		 * @param variation the variation
		 */
		public Star(int x, int y, int variation) {
			this.x = x;
			this.y = y;
			this.variation = variation;
		}

		
		/**
		 * Paint
		 * 
		 * @param g the graphics object
		 */
		public void paint(Graphics2D g) {
			
			Image image = starScaledImages[variation];
			
			if (value >= maxValue) {
				image = starShiningScaledImages[variation];
			}
			
			//g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

			g.drawImage(image, x, y, null);
		}
	}
}
