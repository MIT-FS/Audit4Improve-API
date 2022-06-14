package us.muit.fs.a4i.model.entities;

import java.util.Collection;
import java.util.List;

import us.muit.fs.a4i.control.IndicatorsCalculator;
import us.muit.fs.a4i.exceptions.IndicatorException;

public interface ReportI {
	/**
	 * <p>Tipos de informes, puede necesitarse cuando los algoritmos de c�lculo de indicadores difieran seg�n el tipo de informe</p>
	 * <p>Un informe s�lo es de un tipo y no se puede modificar una vez establecido</p>
	 * 
	 */

	public static enum Type{
    	REPOSITORY,
    	DEVELOPER,
    	PROJECT,
    	ORGANIZATION
    }
	
	ReportI.Type getType();
	
	String getEntityId();
	
	Metric getMetricByName(String name);
	
	Collection<Metric> getAllMetrics();
	
	void addMetric(Metric met);
	
	Indicator getIndicatorByName(String name);
	
	Collection<Indicator> getAllIndicators();
	
	void addIndicator(Indicator ind);
	
	/**
	 * Consulta una m�trica de un informe a partir del nombre
	 * @param name Nombre de la m�trica solicitada
	 * @return M�trica solicitada
	 */

	/**
	 * Obtiene todas las m�tricas del informe
	 * @return Colleci�n de m�tricas que contiene el informe
	 */

    /**
     * A�ade una m�trica al informe
     * @param met Nueva m�trica
     */

	/**
	 * Obtiene un indicador del informe a partir del nombre del mismo
	 * @param name Nombre del indicador consultado
	 * @return El indicador
	 */


	/**
	 * A�ade un indicador al informe
	 * @param ind Nuevo indicador
	 */


	/**
	 * Calcula un indicador a partir de su nombre y lo a�ade al informe
	 * Si se basa en m�tricas que no est�n a�n incluidas en el informe las incluye
	 * @param name Nombre del indicador que se quiere calcular
	 */


	/**

	
    /**
     * Obtiene el identificador de la entidad a la que se refiere el informe
     * @return Identificador un�voco de la entidad a la que se refiere el informe en el remoto
     */

	/**
	 * Establece la calculadora de indicadores, debe ser espec�fica para el tipo de informe
	 * @param calc calculadora a utilizar para el c�lculo de indicadores
	 * @throws IndicatorException Si el tipo de la calculadora no coincide con el tipo de informe
	 */
	




}