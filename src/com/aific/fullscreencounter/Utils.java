package com.aific.fullscreencounter;

import java.awt.Color;


/**
 * Miscellaneous utilities
 * 
 * @author Peter Macko
 */
public class Utils {

	/**
	 * Add an alpha value to a color
	 * 
	 * @param color the base color
	 * @param alpha the new alpha value
	 * @return the new color
	 */
	public static Color withAlpha(Color color, int alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}
}
