/**
 * 
 */
package us.muit.fs.a4i.model.entities;

import java.util.Collection;

/**
 * @author Isabel Román Interfaz para manejar los indicadores
 *
 */
public interface IndicatorI {
	/**
	 * <p>
	 * Estados posibles del indicador, indican el grado de atención que requiere por
	 * parte del analista
	 * </p>
	 * 
	 * @author Isabel Román
	 *
	 */
	public static enum IndicatorState {
		OK, WARNING, CRITICAL, UNDEFINED
	}

	/**
	 * Devuelve el estado en el que se encuentra este indicador
	 * 
	 * @return estado del indicador
	 */
	public IndicatorState getState();

	/**
	 * Devuelve el conjunto de métricas en las que se basa este indicador
	 * 
	 * @return colección de métricas en las que se basa el indicador
	 */
	public Collection<ReportItemI> getMetrics();

	/**
	 * Establece el conjunto de métricas en las que se basa este indicador
	 * 
	 * @param metrics conjunto de métricas en las que se basa el indicador
	 */
	public void setMetrics(Collection<ReportItemI> metrics);

	/**
	 * Establece el estado en el que se encuentra este indicador
	 * 
	 * @param state, estado del indicador
	 */
	public void setState(IndicatorState state);

}
