package com.aific.fullscreencounter;

import java.awt.Color;
import java.awt.Graphics2D;


/**
 * An abstract graphical progress indicator that draws in the background
 * 
 * @author Peter Macko
 */
public interface BackgroundIndicator {

	
	/**
	 * Set the value
	 * 
	 * @param value the new value
	 */
	public void setValue(int value);
	
	
	/**
	 * Set the background color
	 * 
	 * @param color the color
	 */
	public void setBackground(Color color);
	
	
	/**
	 * Paint
	 * 
	 * @param g the graphics object
	 */
	public void paint(Graphics2D g);
}
