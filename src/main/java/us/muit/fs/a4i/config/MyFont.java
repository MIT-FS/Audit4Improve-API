package us.muit.fs.a4i.config;

import java.awt.Color;
import java.awt.Font;

public class MyFont extends Font{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Color color;
	
	protected MyFont(Font font) {
		super(font);
	}
	
	protected MyFont(Font font, Color color) {
		super(font);
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}

}
