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
import java.awt.event.*;
import javax.swing.*;


/**
 * The full-screen counter frame
 * 
 * @author Peter Macko
 */
public class CounterFrame extends JFrame implements KeyListener {

	/// Serial version UID
	private static final long serialVersionUID = 1L;
	
	/// The initial counter value
	private int startValue;
	
	/// The goal counter value (the cutoff for the enhanced counter colors)
	private int goalVaue;
	
	/// The normal foreground color
	private Color normalForeground;
	
	/// The normal background color
	private Color normalBackground;
	
	/// The enhanced foreground color after the goal was reached
	private Color goalForeground;
	
	/// The enhanced background color after the goal was reached
	private Color goalBackground;
	
	/// The counter label
	private JLabel label;
	
	/// The goal graphics
	private Thermometer progressIndicator;
	
	/// The current counter value
	private int counter;
	
	/// The default font
	private Font defaultFont;
	
	/// The screen size
	private Dimension screenSize;


	/**
	 * Create the instance of the counter window
	 * 
	 * @param title The window title
	 */
	public CounterFrame(String title, int startValue, int goalValue,
			Color normalForeground, Color normalBackground,
			Color goalForeground, Color goalBackground) {
		
		super(title);
		
		this.startValue = startValue;
		this.goalVaue = goalValue;
		this.normalForeground = normalForeground;
		this.normalBackground = normalBackground;
		this.goalForeground = goalForeground;
		this.goalBackground = goalBackground;
		
		
		// Initialize
		
		counter = -1;
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		
		// Initialize the listeners
		
		addKeyListener(this);
		
		
		// Initialize the window

		setUndecorated(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setBackground(Color.BLACK);
		
		
		// Create the label
		
		label = new JLabel("", SwingConstants.CENTER);
		label.setForeground(Color.WHITE);
		label.setBackground(Color.BLACK);
		
		defaultFont = new Font(label.getFont().getName(),
				Font.PLAIN, 3 * screenSize.height / 4);
		label.setFont(defaultFont);
		
		
		// The progress indicator
		
		progressIndicator = new Thermometer(startValue, goalValue, screenSize.height);
		progressIndicator.setBackground(Color.BLACK);
		
		
		// Set the components
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(label, BorderLayout.CENTER);
		getContentPane().add(progressIndicator, BorderLayout.WEST);
		
		pack(); 
		
		
		// Full screen

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		if (gd.isFullScreenSupported()) {
			gd.setFullScreenWindow(this);
		}
		else {
			throw new RuntimeException("Full screen not supported");
		}
	}
	
	
	/**
	 * Start
	 */
	public void start() {
		
		setCounter(startValue);
		setVisible(true);
		
		progressIndicator.repaint();
	}
	
	
	/**
	 * Set the counter
	 * 
	 * @param c the new counter value
	 */
	private void setCounter(int c) {
		
		counter = c;
		progressIndicator.setValue(c);
		
		String text = counter >= 0 ? "" + counter : "";
		
		FontMetrics fm = label.getFontMetrics(defaultFont);
		int w = fm.stringWidth(text);
		int maxw = 9 * label.getWidth() / 10;
		if (w > maxw) {
			label.setFont(new Font(defaultFont.getName(), Font.PLAIN,
					(int) ((3 * screenSize.height / 4)
						* (maxw / (float) w))));
		}
		else {
			label.setFont(defaultFont);
		}
		label.setText(text);
		
		if (counter >= goalVaue) {
			setBackground(goalBackground);
			progressIndicator.setBackground(goalBackground);
			
			label.setBackground(goalBackground);
			label.setForeground(goalForeground);
		}
		else {
			setBackground(normalBackground);
			progressIndicator.setBackground(normalBackground);
			
			label.setBackground(normalBackground);
			label.setForeground(normalForeground);
		}
		
		repaint();
	}


	/**
	 * Handler for pressing a key
	 * 
	 * @param e the event
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		
		// Space bar - advance the counter 
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			setCounter(counter + 1);
		}
		
		// Backspace - decrease the counter 
		
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && counter > 0) {
			setCounter(counter - 1);
		}
	}


	/**
	 * Handler for releasing a key
	 * 
	 * @param e the event
	 */
	@Override
	public void keyReleased(KeyEvent e) {
	}


	/**
	 * Handler for tying a key
	 * 
	 * @param e the event
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}
}
