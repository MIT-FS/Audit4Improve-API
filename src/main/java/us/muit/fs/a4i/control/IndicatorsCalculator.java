/**
 * <p>Algoritmos para el c�lculo de indicadores espec�ficos al tipo de informe</p>
 */
package us.muit.fs.a4i.control;

import us.muit.fs.a4i.exceptions.IndicatorException;
import us.muit.fs.a4i.model.entities.ReportI;

/**
 * 
 * <p>
 * Define los métodos para calcular cada indicador y añadirlo al informe
 * </p>
 * <p>
 * Puede hacerse uno a uno o todos a la vez
 * </p>
 * <p>
 * Las clases que la implementen serán específias para un tipo de informe
 * </p>
 * 
 * @author Isabel Román
 *
 */
public interface IndicatorsCalculator {
	/**
	 * <p>
	 * Calcula el indicador con el nombre que se pasa y lo incluye en el informe Si
	 * las métricas que necesita no están en el informe las busca y las añade
	 * </p>
	 * 
	 * @param indicatorName Nombre del indicador a calcular
	 * @param reportManager Gestor del Informe sobre el que realizar el cálculo
	 * @throws IndicatorException Si el indicador no está definido en la calculadora
	 */

	public void calcIndicator(String indicatorName, ReportManagerI reportManager) throws IndicatorException;

	/**
	 * <p>
	 * Calcula todos los indicadores configurados para el tipo de informe que se
	 * pasa. Debe verificar primero que el tipo de informe que se pasa es correcto
	 * </p>
	 * 
	 * @param reportManager Informe sobre el que realizar el c�lculo
	 * @throws IndicatorException Si el tipo del informe no coincide con el de la
	 *                            calculadora
	 */
	public void calcAllIndicators(ReportManagerI reportManager) throws IndicatorException;

	/**
	 * Devuelve el tipo de informe que maneja esta calculadora de indicadores
	 * 
	 * @return El tipo de informes
	 */
	public ReportI.ReportType getReportType();
}
