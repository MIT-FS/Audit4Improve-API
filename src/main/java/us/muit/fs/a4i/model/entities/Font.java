package us.muit.fs.a4i.model.entities;
import java.awt.Color;
import java.util.logging.Logger;


public class Font {
	private static Logger log = Logger.getLogger(Font.class.getName());
	private Color color;
	private java.awt.Font font;
	/**
	 * Todos los valores por defecto
	 */
	public Font() {
		this.color = Color.black;
		this.font = new java.awt.Font ("Serif", java.awt.Font.PLAIN , 10);
	}
	/**
	 * Personalizando el color
	 * @param color color
	 */
	public Font (String color) {
		this.font = new java.awt.Font ("Serif", java.awt.Font.PLAIN , 10);
		setColor(color);
	}
	/**
	 * Todos los Valores personalizados salvo el estilo
	 * @param family tipo de letra	
	 * @param size tamaño
	 * @param color color
	 */
	public Font (String family, int size, String color) {
		log.info("Creando fuente con valores familia "+family+" tamano "+size+" color "+color);
		this.font=new java.awt.Font (family.toLowerCase(), java.awt.Font.PLAIN , size);
		setColor(color);	
		log.info("Se ha creado fuente con el tipo "+font.getFamily()+" y el color "+color);
	}
	/**
	 * Todos los Valores personalizados
	 * @param family tipo de letra
	 * @param format formato
	 * @param size tamaño
	 * @param color color
	 */
	public Font (String family, int style, int size, String color) {
		this.font=new java.awt.Font (family.toLowerCase(), style , size);
		setColor(color);
	}

	public Color getColor() {
		return this.color;
	}
	public java.awt.Font getFont() {
		return this.font;
	}
	
	private void setColor(String color) {
		switch(color.toLowerCase()){
		case "red","rojo":
			this.color=Color.RED;
			break;
		case "green","verde":
			this.color=Color.GREEN;
			break;
		case "blue","azul":
			this.color=Color.BLUE;
		    break;
		case "orange","naranja":
			this.color=Color.ORANGE;
		    break;
		default:
			this.color=Color.BLACK;
		}			
	}
	
}