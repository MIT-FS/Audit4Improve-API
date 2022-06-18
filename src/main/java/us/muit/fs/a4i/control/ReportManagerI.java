/**
 * 
 */
package us.muit.fs.a4i.control;

import us.muit.fs.a4i.persistence.PersistenceManager;
import us.muit.fs.a4i.exceptions.ReportNotDefinedException;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.remote.RemoteEnquirer;
import us.muit.fs.a4i.persistence.ReportFormaterI;

/**
 * <p>Interfaz con los métodos disponibles para manejar informes</p>
 * <p>No depende del sistema de persistencia utilizado</p>
 * <p>No depende del tipo de remoto del que se obtienen las métricas</p>
 * <p>No depende del modo de calcular los indicadores</p> 
 * <p>En las primeras versiones sólo se leen desde remotos y se guardarán los informes localmente</p>
 * <p>Versiones posteriores permitirán leer y modificar informes</p>
 * @author Isabel Román
 *
 */
public interface ReportManagerI {
    
    /**
     * <p>Recupera el informe que se están manejando</p>
     * @return Devuelve el informe manejado
     */
	public ReportI getReport();
	/**
	 * <p>Establece el objeto que se usará para consultar al servidor remoto y obtener las métricas</p>
	 * @param remote Objeto RemoteEnquirer que consultará al servidor remoto
	 */
	
	public void setRemoteEnquirer(RemoteEnquirer remote);
	/**
	 * <p>Establece el objeto PersistenceManager que se encargará de guardar el informe localmente</p>
	 * @param persistence Objeto PersistenceManager concreto
	 */
	public void setPersistenceManager(PersistenceManager persistence);
	/**
	 * <p>Establece el formateador a usar</p>
	 * @param formater El gestor de formato a utilizar
	 */
	public void setFormater(ReportFormaterI formater);
	public void setIndicatorCalc(IndicatorsCalculator calc);
	
	/**
	 * <p>Persiste el informe que recibe como parámetro, según las reglas del gestor de persistencia y formateador establecidos</p>
	 * @param report <p>El informe a persistir</p>
	 */
	public void saveReport(ReportI report);
	/**
	 * <p>Establecer el informe que se quiere crear</p>
	 * @throws ReportNotDefinedException Si no se había establecido un informe
	 */
	public void save() throws ReportNotDefinedException;
	
	/**
	 * <p>Crea un informe para la entidad indicada como parámetro, según las reglas del RemoteBuilder Establecido</p>
	 * <p>El id debe identificar unívocamente a la entidad en el remoto</p>
	 * @param id Identificador de la entidad a la que se refiere el informe
	 * @return el informe creado
	 */
	public ReportI createReport(String id);
	/**
	 * <p>Borra el informe pasado como parámetro, según las reglas establecidas por el gestor de persistencia</p>
	 * @param report El informe que se quiere borrar
	 */
	public void deleteReport(ReportI report);
	/**
	 * <p>Borra el informe que se está manejando actualmente, si la referencia no era nula, según las reglas establecidas por el gestor de persistencia</p>
	 */
	public void deleteReport();
}
