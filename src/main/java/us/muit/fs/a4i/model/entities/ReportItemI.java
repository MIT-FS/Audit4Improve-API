package us.muit.fs.a4i.model.entities;

import java.util.Date;

public interface ReportItemI<T> {
	
	
	/**
	 * Consulta el nombre de la métrica
	 * @return Nombre de la métrica
	 */
	public String getName();
	
	/**
	 * Consulta el valor de la métrica
	 * @return Medida
	 */
	public T getValue();
	
	/**
	 * Consulta cuando se obtuvo la métrica
	 * @return Fecha de consulta de la métrica
	 */
	public Date getDate();
	
	/**
	 * Obtiene la descripción de la métrica
	 * @return Descripción del significado de la métrica
	 */
	public String getDescription();
	
	/**
	 * Consulta la fuente de información
	 * @return Origen de la medida
	 */
	public String getSource();
	
	/**
	 * Consulta las unidades de medida
	 * @return la unidad usada en la medida
	 */
	public String getUnit();
	
	// getIndicator no existe, no sabemos si hay que crearla o como
	
	/***
	 * Establece la fuente de la información para la medida
	 * @param source fuente de información origen
	 */
	public void setSource(String source);
}