package us.muit.fs.a4i.model.entities;

import java.awt.Color;

public class Font {
	
	private String color;
	private java.awt.Font font;
	
	public Font (String color, java.awt.Font font) {
		this.color = color;
		this.font = font;
	}
	public Font() {
		this.color = Color.black.toString();
		this.font = new java.awt.Font ("Serif", java.awt.Font.PLAIN , 10);
	}
	public Font(java.awt.Font font) {
		this.color = Color.black.toString();
		this.font = font;
	}
	public String getColor() {
		return color;
	}
	public java.awt.Font getFont() {
		return font;
	}
	public String getName() {
		return font.getName();
	}
	public Integer getSize() {
		return font.getSize();
	}

}