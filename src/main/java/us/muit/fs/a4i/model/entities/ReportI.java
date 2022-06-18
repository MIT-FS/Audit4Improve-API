package us.muit.fs.a4i.model.entities;

import java.util.Collection;
import java.util.List;

import us.muit.fs.a4i.control.IndicatorsCalculator;
import us.muit.fs.a4i.exceptions.IndicatorException;

public interface ReportI {
	/**
	 * <p>Tipos de informes, puede necesitarse cuando los algoritmos de cálculo de indicadores difieran según el tipo de informe</p>
	 * <p>Un informe sólo es de un tipo y no se puede modificar una vez establecido</p>
	 * 
	 */

	public static enum Type{
    	REPOSITORY,
    	DEVELOPER,
    	PROJECT,
    	ORGANIZATION
    }
	
	ReportI.Type getType();
	
	String getId();
	
	Metric getMetricByName(String name);
	
	Collection<Metric> getAllMetrics();
	
	void addMetric(Metric met);
	
	Indicator getIndicatorByName(String name);
	
	Collection<Indicator> getAllIndicators();
	
	void addIndicator(Indicator ind);
	
	/**
	 * Consulta una métrica de un informe a partir del nombre
	 * @param name Nombre de la métrica solicitada
	 * @return Métrica solicitada
	 */

	/**
	 * Obtiene todas las métricas del informe
	 * @return Colleción de métricas que contiene el informe
	 */

    /**
     * Añade una métrica al informe
     * @param met Nueva métrica
     */

	/**
	 * Obtiene un indicador del informe a partir del nombre del mismo
	 * @param name Nombre del indicador consultado
	 * @return El indicador
	 */


	/**
	 * Añade un indicador al informe
	 * @param ind Nuevo indicador
	 */


	/**
	 * Calcula un indicador a partir de su nombre y lo añade al informe
	 * Si se basa en métricas que no están aún incluidas en el informe las incluye
	 * @param name Nombre del indicador que se quiere calcular
	 */


	/**

	
    /**
     * Obtiene el identificador de la entidad a la que se refiere el informe
     * @return Identificador unívoco de la entidad a la que se refiere el informe en el remoto
     */

	/**
	 * Establece la calculadora de indicadores, debe ser específica para el tipo de informe
	 * @param calc calculadora a utilizar para el cálculo de indicadores
	 * @throws IndicatorException Si el tipo de la calculadora no coincide con el tipo de informe
	 */
	




}
