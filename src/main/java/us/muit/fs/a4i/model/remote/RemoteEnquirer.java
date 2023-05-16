/**
 * 
 */
package us.muit.fs.a4i.model.remote;

import java.util.List;

import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItemI;

/**
 * <p>
 * Interfaz para desacoplar el mecanismo de obtención de métricas del servidor
 * remoto que se use como fuente de las mismas
 * </p>
 * <p>
 * Un conjunto de métricas es específico para un tipo de entidad a informar:
 * organización, proyecto, repositorio, desarrollador...
 * </p>
 * <p>
 * La identidad se refiere al identificador unívoco de la entidad sobre la que
 * se quiere informar en el servidor remoto, la semántica puede depender del
 * tipo de entidad y del remoto
 * </p>
 * 
 * @author Isabel Román
 *
 */
public interface RemoteEnquirer {

	public static enum RemoteType {
		GITHUB
	}

	/**
	 * <p>
	 * Construye el informe sobre la entidad indicada con las métricas por defecto
	 * </p>
	 * 
	 * @param entityId Identificador unívoco en el remoto de la entidad sobre la que
	 *                 se quiere informar.
	 * @return El nuevo informe construido
	 */

	public ReportI buildReport(String entityId);

	/**
	 * <p>
	 * Consulta una métrica específica para una entidad concreta
	 * </p>
	 * 
	 * @param metricName métrica solicitada
	 * @param entityId   Identificador unívoco en el remoto de la entidad sobre la
	 *                   que se consulta
	 * @return La nueva métrica construida tras la consulta al remoto
	 * @throws MetricException Si la métrica no esta definida
	 */
	public ReportItemI getMetric(String metricName, String entityId) throws MetricException;

	/**
	 * <p>
	 * Devuelve las métricas que el objeto RemoteEnquirer concreto puede obtener del
	 * servidor remoto
	 * </p>
	 * 
	 * @return El listado de los nombres de métricas definidas
	 */
	public List<String> getAvailableMetrics();

}
