package us.muit.fs.a4i.model.entities;

import java.util.Collection;

/**
 * <p>Interfaz para la gestión de informes</p>
 * @author Isabel Román
 *
 */
public interface ReportI {
	/**
	 * <p>
	 * Tipos de informes, puede necesitarse cuando los algoritmos de cálculo de
	 * indicadores difieran según el tipo de informe
	 * </p>
	 * <p>
	 * Un informe sólo es de un tipo y no se puede modificar una vez establecido
	 * </p>
	 * 
	 */

	public static enum ReportType {
		REPOSITORY, DEVELOPER, PROJECT, ORGANIZATION
	}

	/**
	 * Consulta el tipo del informe
	 * 
	 * @return tipo del informe
	 */
	ReportI.ReportType getType();

	/**
	 * Obtiene el identificador de la entidad a la que se refiere el informe
	 * 
	 * @return Identificador unóvoco de la entidad a la que se refiere el informe en
	 *         el remoto
	 */
	String getEntityId();

	/**
	 * Consulta una métrica de un informe a partir del nombre
	 * 
	 * @param name Nombre de la métrica solicitada
	 * @return Métrica solicitada
	 */
	ReportItemI getMetricByName(String name);

	/**
	 * Obtiene todas las métricas del informe
	 * 
	 * @return Colecciónn de métricas que contiene el informe
	 */
	Collection<ReportItemI> getAllMetrics();

	/**
	 * Añade una métrica al informe
	 * 
	 * @param metric Nueva métrica
	 */
	void addMetric(ReportItemI metric);

	/**
	 * Obtiene un indicador del informe a partir del nombre del mismo
	 * 
	 * @param indicatorName Nombre del indicador consultado
	 * @return El indicador
	 */

	ReportItemI getIndicatorByName(String indicatorName);

	/**
	 * Obtiene todos los indicadores del informe
	 * 
	 * @return el conjunto de indicadores del informe
	 */
	Collection<ReportItemI> getAllIndicators();

	/**
	 * A�ade un indicador al informe
	 * 
	 * @param newIndicator nuevo indicador
	 */

	void addIndicator(ReportItemI newIndicator);

	/**
	 * Calcula un indicador a partir de su nombre y lo añade al informe Si se basa
	 * en métricas que no están aún incluidas en el informe las incluye
	 * 
	 * @param name Nombre del indicador que se quiere calcular
	 */

}