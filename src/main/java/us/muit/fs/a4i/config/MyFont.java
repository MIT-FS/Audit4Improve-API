package us.muit.fs.a4i.config;

import java.awt.Color;
import java.awt.Font;

public class MyFont extends Font{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Color of the font
	private Color color;
	
	/**
	 * Constructor of the MyFont class. Calls the constructor of
	 * the Font class and sets the color.
	 * 
	 * @param font the font to be set
	 * @param color the color to be set
	 */
	protected MyFont(Font font, Color color) {
		super(font);
		this.color = color;
	}
	
	/**
	 * Getter for color
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Setter for color
	 * @param color the color to be set/changed
	 */
	public void setColor(Color color) {
		this.color = color;
	}
}
