/**
 * 
 */
package us.muit.fs.a4i.control;

import us.muit.fs.a4i.exceptions.ReportNotDefinedException;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.persistence.ReportFormaterI;

/**
 * <p>
 * Interfaz con los métodos disponibles para manejar informes
 * </p>
 * <p>
 * No depende del sistema de persistencia utilizado
 * </p>
 * <p>
 * No depende del tipo de remoto del que se obtienen las métricas
 * </p>
 * <p>
 * No depende del modo de calcular los indicadores
 * </p>
 * <p>
 * En las primeras versiones sólo se leen desde remotos y se guardarán los
 * informes localmente
 * </p>
 * <p>
 * Versiones posteriores permitirán leer y modificar informes
 * </p>
 * 
 * @author Isabel Román
 *
 */

public interface ReportManagerI {

	/**
	 * <p>
	 * Recupera el informe que se está manejando
	 * </p>
	 * 
	 * @return Devuelve el informe manejado
	 */
	public ReportI getReport();

	public void addMetric(String metricName);

	public ReportItemI getMetric(String metricName);

	public void addIndicator(String indicatorName);

	public void getIndicator(String indicatorName);

	public void saveReport() throws ReportNotDefinedException;

	public void deleteReport();

	/**
	 * <p>
	 * Establece el formateador a usar
	 * </p>
	 * 
	 * @param formater El gestor de formato a utilizar
	 */
	public void setFormater(ReportFormaterI formater);

	/**
	 * <p>
	 * Crea un informe para la entidad indicada como parámetro, según las reglas del
	 * RemoteBuilder Establecido
	 * </p>
	 * <p>
	 * El id debe identificar unívocamente a la entidad en el remoto
	 * </p>
	 * 
	 * @param entityId   Identificador de la entidad a la que se refiere el informe
	 * @param reportType El tipo de informe
	 * @return el informe creado
	 * @throws Exception el tipo de informe no coincide con el del manager
	 */
	public ReportI newReport(String entityId, ReportI.ReportType reportType) throws Exception;

}
