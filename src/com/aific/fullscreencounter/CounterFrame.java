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
	
	
	/**
	 * The graphical indicators
	 */
	public enum GraphicalIndicatorEnum {
		
		NONE { 
			@Override
			public String toString() { return "(none)"; }
		},
		
		THERMOMETER { 
			@Override
			public String toString() { return "Thermometer"; }
		}
	}
	
	
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
	
	/// The "goal reached" announcement text
	private String goalReachedText;
	
	/// The goal graphical progress indicator
	private GraphicalIndicator graphicalIndicator;
	
	/// The panel with the labels
	JPanel labelPanel;
	
	/// The counter label
	private JLabel label;
	
	/// The counter label
	private JLabel topLabel;
	
	/// The counter label
	private JLabel bottomLabel;
	
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
	 * @param startValue The initial counter value
	 * @param goalValue The goal counter value (the cutoff for the enhanced counter colors)
	 * @param normalForeground The normal foreground color
	 * @param normalBackground The normal background color
	 * @param goalForeground The enhanced foreground color after the goal was reached
	 * @param goalBackground The enhanced background color after the goal was reached
	 * @param indicator The goal graphical progress indicator
	 * @param goalReachedText The "goal reached" announcement text
	 */
	public CounterFrame(String title, int startValue, int goalValue,
			Color normalForeground, Color normalBackground,
			Color goalForeground, Color goalBackground,
			GraphicalIndicatorEnum indicator,
			String goalReachedText) {
		
		super(title);
		
		this.startValue = startValue;
		this.goalVaue = goalValue;
		this.normalForeground = normalForeground;
		this.normalBackground = normalBackground;
		this.goalForeground = goalForeground;
		this.goalBackground = goalBackground;
		this.goalReachedText = goalReachedText;
		
		if (this.goalReachedText == null) this.goalReachedText = "";
		
		
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
		
		
		// The graphical indicator
		
		switch (indicator) {
		case THERMOMETER:
			graphicalIndicator = new Thermometer(startValue, goalValue, screenSize.height);
			break;
		case NONE:
		default:
			graphicalIndicator = null;
		}
		
		if (graphicalIndicator != null) {
			graphicalIndicator.setBackground(Color.BLACK);
			graphicalIndicator.setOpaque(true);
		}
		
		
		// The top and bottom labels
		
		topLabel = new JLabel(" ", SwingConstants.CENTER);
		topLabel.setForeground(Color.WHITE);
		topLabel.setBackground(Color.BLACK);
		
		bottomLabel = new JLabel(" ", SwingConstants.CENTER);
		bottomLabel.setForeground(Color.WHITE);
		bottomLabel.setBackground(Color.BLACK);

		Font f = new Font(label.getFont().getName(),
				Font.PLAIN, 1 * screenSize.height / 8);
		topLabel.setFont(f);
		bottomLabel.setFont(f);

		
		// Set the components
		
		labelPanel = new JPanel(new BorderLayout());
		labelPanel.setBackground(getBackground());
		labelPanel.setOpaque(true);
		labelPanel.add(label, BorderLayout.CENTER);
		labelPanel.add(topLabel, BorderLayout.NORTH);
		labelPanel.add(bottomLabel, BorderLayout.SOUTH);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(labelPanel, BorderLayout.CENTER);
		if (graphicalIndicator != null) getContentPane().add(graphicalIndicator, BorderLayout.WEST);
		
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
		
		if (graphicalIndicator != null) graphicalIndicator.repaint();
	}
	
	
	/**
	 * Set the colors
	 * 
	 * @param foreground the foreground color
	 * @param background the background color
	 */
	private void setColors(Color foreground, Color background) {
		
		setBackground(background);
		labelPanel.setBackground(background);
		if (graphicalIndicator != null) graphicalIndicator.setBackground(background);
		
		label.setBackground(background);
		label.setForeground(foreground);
		
		topLabel.setBackground(background);
		topLabel.setForeground(foreground);
		
		bottomLabel.setBackground(background);
		bottomLabel.setForeground(foreground);
	}
	
	
	/**
	 * Set the counter
	 * 
	 * @param c the new counter value
	 */
	private void setCounter(int c) {
		
		counter = c;
		if (graphicalIndicator != null) graphicalIndicator.setValue(c);
		
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
			setColors(goalForeground, goalBackground);
			bottomLabel.setText("".equals(goalReachedText) ? " " : goalReachedText);
		}
		else {
			setColors(normalForeground, normalBackground);
			bottomLabel.setText(" ");
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
		
		// Escape - exit
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			gd.setFullScreenWindow(null);
			this.setBounds(0, 0, screenSize.width, screenSize.height);
			
			if (JOptionPane.showConfirmDialog(this, "Are you sure to exit the counter?",
					getTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null) == JOptionPane.YES_OPTION) {
				setVisible(false);
				dispose();
			}
			else {
				gd.setFullScreenWindow(this);
			}
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
