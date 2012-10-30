/*
 * Full Screen Counter
 *
 * Copyright (c) 2012, Peter Macko <pmacko@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *   
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */


package com.aific.fullscreencounter;

import java.awt.*;

import javax.swing.*;


/**
 * The goal thermometer component
 * 
 * @author Peter Macko
 */
public class Thermometer extends JComponent {

	private static final long serialVersionUID = 1L;
	
	private static final int Y_GOAL  = 140;
	private static final int Y_EMPTY = 790;
	
	private ImageIcon emptyIcon;
	private ImageIcon reachedIcon;
	private double iconAspectRatio;
	
	private Image emptyScaledImage;
	private Image reachedScaledImage;
	private MediaTracker imageMediaTracker;
	
	private Dimension desiredSize;
	
	private int value;
	private int minValue;
	private int maxValue;
	
	
	/**
	 * Create an instance of class Thermometer
	 * 
	 * @param minValue the minimum value
	 * @param maxValue the maximum value
	 * @param height the desired height
	 */
	public Thermometer(int minValue, int maxValue, int height) {
		
		this.value    = minValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
		
		if (minValue >= maxValue) {
			throw new IllegalArgumentException("minValue >= maxValue");
		}
		
		
		// Load the resources
		
		emptyIcon = new ImageIcon(Thermometer.class.getResource("Goal Thermometer - Empty.png"));
		reachedIcon = new ImageIcon(Thermometer.class.getResource("Goal Thermometer - Goal.png"));
		iconAspectRatio = emptyIcon.getIconWidth()  / (double) emptyIcon.getIconHeight();

		
		// Compute the dimensions
		
		int scaledWidth = (int) (emptyIcon.getIconWidth() * (height / (double) emptyIcon.getIconHeight()));
		desiredSize = new Dimension(scaledWidth, height);
		
		
		// Scale the resource images
		
		emptyScaledImage   = emptyIcon  .getImage().getScaledInstance(desiredSize.width, desiredSize.height, Image.SCALE_SMOOTH);
		reachedScaledImage = reachedIcon.getImage().getScaledInstance(desiredSize.width, desiredSize.height, Image.SCALE_SMOOTH);
		
		imageMediaTracker = new MediaTracker(this);
		imageMediaTracker.addImage(emptyScaledImage, 0);
		imageMediaTracker.addImage(reachedScaledImage, 0);

		try {
			imageMediaTracker.waitForAll();
		}
		catch(InterruptedException e) {}
		

		// Set the component size
		
		setMinimumSize  (desiredSize);
		setPreferredSize(desiredSize);
		setMaximumSize  (desiredSize);
	}
	
	
	/**
	 * Set the value
	 * 
	 * @param value the new value
	 */
	public void setValue(int value) {
		this.value = value;
		invalidate();
	}

	
	/**
	 * Paint the component
	 * 
	 * @param g the graphics context
	 */
	@Override
	protected void paintComponent(Graphics g) {
		
		int width = getWidth();
		int height = getHeight();
		
		if (isOpaque()) {
			g.setClip(0, 0, width, height);
			g.setColor(getBackground());
			g.fillRect(0, 0, width, height);
		}
		
		
		// Wait for the images
		
		while (!imageMediaTracker.checkAll()) {
			Thread.yield();
		}
		

		// Compute the destination size and location
		
		int dw = desiredSize.width;
		int dh = desiredSize.height;
		if (dw > width) {
			dw = width;
			dh = (int) (dw / iconAspectRatio);
		}
		if (dh > height) {
			dh = height;
			dw = (int) (dh * iconAspectRatio);
		}
		
		int dx = (width  - dw) / 2;
		int dy = (height - dh) / 2;

		
		// Compute the split between the two images
		
		double p = (value - minValue) / (double) (maxValue - minValue);
		if (p < 0) p = 0;
		
		int d_goal  = (int) (dh * (Y_GOAL  / (double) emptyIcon.getIconHeight()));
		int d_empty = (int) (dh * (Y_EMPTY / (double) emptyIcon.getIconHeight()));
		int d_scale = d_empty - d_goal;
		
		int d_split = (int) Math.round(d_empty - (p * d_scale));
		if (d_split < 0) d_split = 0;
		
		int s_split = (int) Math.round((Y_EMPTY - (p * (Y_EMPTY - Y_GOAL))) * (desiredSize.height / (double) emptyIcon.getIconHeight()));
		if (s_split < 0) s_split = 0;
		
		
		// Draw the images

		if (d_split >= 1) {
			g.setClip(dx, dy, dw, d_split);
			g.drawImage(emptyScaledImage, dx, dy, dx + dw, dy + dh, 0, 0, desiredSize.width, desiredSize.height, null);
			//g.drawImage(emptyScaledImage, dx, dy, dx + dw, dy + d_split, 0, 0, desiredSize.width, s_split, null);
		}
		
		if (d_split < dh) {
			g.setClip(dx, dy + d_split, dw, dh - d_split);
			g.drawImage(reachedScaledImage, dx, dy, dx + dw, dy + dh, 0, 0, desiredSize.width, desiredSize.height, null);
			//g.drawImage(reachedScaledImage, dx, dy + d_split, dx + dw, dy + dh, 0, s_split, desiredSize.width, desiredSize.height, null);
		}
	}
	
	
	/**
	 * Private image observer
	 */
	
}
