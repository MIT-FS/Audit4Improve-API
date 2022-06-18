/**
 * 
 */
package us.muit.fs.a4i.model.remote;

import java.util.List;

import us.muit.fs.a4i.exceptions.MetricException;

import us.muit.fs.a4i.model.entities.Metric;
import us.muit.fs.a4i.model.entities.ReportI;

/**
 * <p>Interfaz para desacoplar el mecanismo de obtenci�n de m�tricas del servidor remoto que se use como fuente de las mismas</p>
 * <p>Un conjunto de m�tricas es espec�fico para un tipo de entidad a informar: organizaci�n, proyecto, repositorio, desarrollador...</p>
 * <p>La identidad se refiere al identificador un�voco de la entidad sobre la que se quiere informar en el servidor remoto, la sem�ntica puede depender del tipo de entidad y del remoto</p>
 * @author IsabelRom�n
 *
 */
public interface RemoteEnquirer{
	
	/**
	 * <p>Construye el informe sobre la entidad indicada con las m�tricas por defecto</p>
	 * @param entityId Identificador un�voco en el remoto de la entidad sobre la que se quiere informar.
	 * @return El nuevo informe construido
	 */
	
	public ReportI buildReport(String entityId);
	/**
	 * <p>Consulta una m�trica espec�fica para una entidad concreta</p>
	 * @param metricName m�trica solicitada
	 * @param entityId Identificador un�voco en el remoto de la entidad sobre la que se consulta
	 * @return La nueva m�trica construida tras la consulta al remoto
	 * @throws MetricException Si la m�trica no est� definida
	 */
	public Metric getMetric(String metricName,String entityId) throws MetricException;

	/**
	 * <p>Devuelve las m�tricas que el objeto RemoteEnquirer concreto puede obtener del servidor remoto</p>
	 * @return El listado de los nombres de m�tricas definidas
	 */
	public List<String> getAvailableMetrics();
}
