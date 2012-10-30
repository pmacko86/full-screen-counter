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
import javax.swing.border.EmptyBorder;

import com.aific.fullscreencounter.CounterFrame.GraphicalIndicatorEnum;


/**
 * The main class of the application that implements a full-screen counter
 * 
 * @author Peter Macko
 */
public class Main extends JFrame implements ActionListener {

	/// Serial version UID
	private static final long serialVersionUID = 1L;
	
	/// THe graphical indicators
	private static final GraphicalIndicatorEnum[] INDICATORS = new GraphicalIndicatorEnum[] {
		GraphicalIndicatorEnum.NONE,
		GraphicalIndicatorEnum.THERMOMETER
	};

	
	///
	/// The components
	///
	
	private JPanel panel;
	private JLabel topLabel;
	
	private JLabel startValueLabel;
	private JTextField startValueField;
	private JLabel goalValueLabel;
	private JTextField goalValueField;
	
	private JLabel appearanceTopLabel;
	private JLabel normalAppearanceLabel;
	private JPanel normalAppearancePanel;
	private JLabel normalAppearancePreviewLabel;
	private JButton normalAppearanceBackgroundButton;
	private JButton normalAppearanceForegroundButton;
	private JLabel reachedAppearanceLabel;
	private JPanel reachedAppearancePanel;
	private JLabel reachedAppearancePreviewLabel;
	private JButton reachedAppearanceBackgroundButton;
	private JButton reachedAppearanceForegroundButton;
	
	private JLabel graphicalIndicatorLabel;
	private JComboBox graphicalIndicatorCombo;
	private JLabel reachedTextLabel;
	private JTextField reachedTextField;

	private JLabel copyrightLabel;
	private JButton startButton;
	private JButton quitButton;
	

	/**
	 * Create the instance of the main window
	 */
	public Main() {
		super("Full Screen Counter");
		
		
		// Initialize the window

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		// Initialize the panel and the content pane

		panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panel, BorderLayout.CENTER);

		int gridy = 0;
		int gridw = 2;


		// Header

		topLabel = new JLabel("Counter Configuration:");
		topLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		topLabel.setFont(topLabel.getFont().deriveFont(Font.BOLD));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = gridw;
		c.weightx = 1;
		c.weighty = 0;
		panel.add(topLabel, c);
		c.weightx = 0;
		c.gridwidth = 1;

		gridy++;


		// Start and goal values

		startValueLabel = new JLabel("Start value:   ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridy;
		panel.add(startValueLabel, c);

		startValueField = new JTextField("0");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = gridy;
		panel.add(startValueField, c);

		gridy++;

		goalValueLabel = new JLabel("Goal:   ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridy;
		panel.add(goalValueLabel, c);

		goalValueField = new JTextField("50");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = gridy;
		panel.add(goalValueField, c);

		gridy++;
		

		// Appearance header

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = gridw;
		panel.add(new JLabel(" "), c);
		c.gridwidth = 1;

		gridy++;

		appearanceTopLabel = new JLabel("Counter Appearance:");
		appearanceTopLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		appearanceTopLabel.setFont(appearanceTopLabel.getFont().deriveFont(Font.BOLD));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = gridw;
		c.weightx = 1;
		c.weighty = 0;
		panel.add(appearanceTopLabel, c);
		c.weightx = 0;
		c.gridwidth = 1;

		gridy++;

		
		// Normal appearance

		normalAppearanceLabel = new JLabel("Before reaching the goal:   ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridy;
		panel.add(normalAppearanceLabel, c);
		
		normalAppearancePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = gridy;
		panel.add(normalAppearancePanel, c);
		
		normalAppearancePreviewLabel = new JLabel("0", SwingConstants.CENTER);
		normalAppearancePreviewLabel.setOpaque(true);
		normalAppearancePreviewLabel.setBackground(Color.BLACK);
		normalAppearancePreviewLabel.setForeground(Color.WHITE);
		normalAppearancePreviewLabel.setMinimumSize(new Dimension(75, 25));
		normalAppearancePreviewLabel.setPreferredSize(new Dimension(75, 25));
		normalAppearancePreviewLabel.setFont(normalAppearancePreviewLabel.getFont().deriveFont(Font.BOLD));
		normalAppearancePanel.add(normalAppearancePreviewLabel);
		
		normalAppearancePanel.add(new JLabel(" "));

		normalAppearanceForegroundButton = new JButton("Text Color");
		normalAppearanceForegroundButton.addActionListener(this);
		normalAppearancePanel.add(normalAppearanceForegroundButton);

		normalAppearanceBackgroundButton = new JButton("Background Color");
		normalAppearanceBackgroundButton.addActionListener(this);
		normalAppearancePanel.add(normalAppearanceBackgroundButton);

		gridy++;
		
		
		// Goal reached appearance

		reachedAppearanceLabel = new JLabel("After reaching the goal:   ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridy;
		panel.add(reachedAppearanceLabel, c);
		
		reachedAppearancePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = gridy;
		panel.add(reachedAppearancePanel, c);
		
		reachedAppearancePreviewLabel = new JLabel("50", SwingConstants.CENTER);
		reachedAppearancePreviewLabel.setOpaque(true);
		reachedAppearancePreviewLabel.setBackground(Color.WHITE);	// new Color(170, 170, 255)
		reachedAppearancePreviewLabel.setForeground(new Color(75, 75, 255));	// new Color(0, 96, 0)
		reachedAppearancePreviewLabel.setMinimumSize(new Dimension(75, 25));
		reachedAppearancePreviewLabel.setPreferredSize(new Dimension(75, 25));
		reachedAppearancePreviewLabel.setFont(reachedAppearancePreviewLabel.getFont().deriveFont(Font.BOLD));
		reachedAppearancePanel.add(reachedAppearancePreviewLabel);
		
		reachedAppearancePanel.add(new JLabel(" "));

		reachedAppearanceForegroundButton = new JButton("Text Color");
		reachedAppearanceForegroundButton.addActionListener(this);
		reachedAppearancePanel.add(reachedAppearanceForegroundButton);

		reachedAppearanceBackgroundButton = new JButton("Background Color");
		reachedAppearanceBackgroundButton.addActionListener(this);
		reachedAppearancePanel.add(reachedAppearanceBackgroundButton);

		gridy++;
		
		
		// Graphical indicator

		graphicalIndicatorLabel = new JLabel("Graphical Indicator:   ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridy;
		panel.add(graphicalIndicatorLabel, c);

		graphicalIndicatorCombo = new JComboBox(INDICATORS);
		graphicalIndicatorCombo.setSelectedItem(GraphicalIndicatorEnum.THERMOMETER);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = gridy;
		panel.add(graphicalIndicatorCombo, c);

		gridy++;
		
		
		// Goal reached text announcement
		
		reachedTextLabel = new JLabel("Goal Reached Text: ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridy;
		panel.add(reachedTextLabel, c);

		reachedTextField = new JTextField("Goal Reached!");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = gridy;
		panel.add(reachedTextField, c);

		gridy++;


		//
		// Help
		//

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = gridw;
		c.weighty = 1;
		panel.add(new JLabel(" "), c);
		c.gridwidth = 1;

		gridy++;

		JLabel usageLabelHeader = new JLabel("Usage:");
		usageLabelHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		usageLabelHeader.setFont(usageLabelHeader.getFont().deriveFont(Font.BOLD));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = gridw;
		c.weightx = 1;
		c.weighty = 0;
		panel.add(usageLabelHeader, c);
		c.weightx = 0;
		c.gridwidth = 1;

		gridy++;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = gridw;
		c.weightx = 1;
		c.weighty = 0;
		panel.add(new JLabel(
				"<html><body><ul>"
				+ "<li><i>Space</i> &ndash; Increment the counter</li>"
				+ "<li><i>Backspace</i> &ndash; Decrement the counter</li>"
				+ "<li><i>Escape</i> &ndash; Exit the program</li>"
				+ "</body></ul></html>"), c);
		c.weightx = 0;
		c.gridwidth = 1;

		gridy++;


		//
		// Finish the main form
		//

		/*c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = gridy;
		c.gridwidth = gridw;
		c.weighty = 1;
		panel.add(new JLabel(" "), c);
		c.gridwidth = 1;

		gridy++;*/
		
		
		// Bottom buttons and the copyright label

		JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		Box buttonBox = new Box(BoxLayout.X_AXIS);

		startButton = new JButton("Start");
		startButton.addActionListener(this);

		quitButton = new JButton("Quit");
		quitButton.addActionListener(this);

		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.add(new JSeparator(), BorderLayout.NORTH);

		buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
		buttonBox.add(startButton);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(quitButton);
		
		copyrightLabel = new JLabel("  Full Screen Counter, (c) Peter Macko, 2012");
		Color lc = copyrightLabel.getForeground();
		Color bg = getBackground();
		copyrightLabel.setForeground(new Color((lc.getRed() + bg.getRed()) / 2,
				(lc.getGreen() + bg.getGreen()) / 2, (lc.getBlue() + bg.getBlue()) / 2));
		
		buttonPanel.add(copyrightLabel, java.awt.BorderLayout.WEST);
		buttonPanel.add(buttonBox, java.awt.BorderLayout.EAST);


		// Finish the window
		
		pack();
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);
		
		setMinimumSize(getSize());
	}


	/**
	 * The main entry point to the application
	 * 
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {


		// Set-up platform-specific properties

		try {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
		}
		catch (Exception e) {
			// do a silent failover
		}
		
		
		// Show the main window
		
		Main frame = new Main();
		frame.setVisible(true);
	}


	/**
	 * Handler for the performed actions
	 * 
	 * @param e the action event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == normalAppearanceForegroundButton) {
			Color c = JColorChooser.showDialog(this, "Choose the text color",
					normalAppearancePreviewLabel.getForeground());
			if (c != null) normalAppearancePreviewLabel.setForeground(c);
		}
		
		if (e.getSource() == normalAppearanceBackgroundButton) {
			Color c = JColorChooser.showDialog(this, "Choose the background color",
					normalAppearancePreviewLabel.getBackground());
			if (c != null) normalAppearancePreviewLabel.setBackground(c);
		}
		
		if (e.getSource() == reachedAppearanceForegroundButton) {
			Color c = JColorChooser.showDialog(this, "Choose the text color after the goal was reached",
					reachedAppearancePreviewLabel.getForeground());
			if (c != null) reachedAppearancePreviewLabel.setForeground(c);
		}
		
		if (e.getSource() == reachedAppearanceBackgroundButton) {
			Color c = JColorChooser.showDialog(this, "Choose the background color after the goal was reached",
					reachedAppearancePreviewLabel.getBackground());
			if (c != null) reachedAppearancePreviewLabel.setBackground(c);
		}
		
		if (e.getSource() == startButton) {
			
			int startValue;
			int goalValue;
			
			try {
				startValue = Integer.parseInt(startValueField.getText());
				if (!startValueField.getText().equals("" + startValue)) {
					throw new NumberFormatException();
				}
			}
			catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "The start value must be an integer",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			try {
				goalValue = Integer.parseInt(goalValueField.getText());
				if (!goalValueField.getText().equals("" + goalValue)) {
					throw new NumberFormatException();
				}
			}
			catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "The goal value must be an integer",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (startValue >= goalValue) {
				JOptionPane.showMessageDialog(this, "The goal value must be larger than the start value",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			CounterFrame cf = new CounterFrame("Full Screen Counter", startValue, goalValue,
					normalAppearancePreviewLabel.getForeground(),
					normalAppearancePreviewLabel.getBackground(),
					reachedAppearancePreviewLabel.getForeground(),
					reachedAppearancePreviewLabel.getBackground(),
					(GraphicalIndicatorEnum) graphicalIndicatorCombo.getSelectedItem(),
					reachedTextField.getText());
			
			dispose();
			cf.start();
		}
		
		if (e.getSource() == quitButton) {
			dispose();
		}
	}
}
