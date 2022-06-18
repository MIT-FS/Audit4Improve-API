package us.muit.fs.a4i.persistence;

import us.muit.fs.a4i.control.ReportManagerI;
import us.muit.fs.a4i.exceptions.ReportNotDefinedException;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.remote.RemoteEnquirer;

/**
 * <p>Interfaz de los gestores de persistencia</p>
 * @author isa
 *
 */
public interface PersistenceManager {

	/**
	 *<p>Establece el informe que se va a manejar</p> 
	 * @param report El informe a manejar
	 */
	void setReport(ReportI report);
	
	/**
	 * <p>Establece el elemento que establece el formato</p>
	 * @param formater Elemento que maneja las caracter�sticas de formato
	 */
	void setFormater(ReportFormaterI formater);
	
	/**
	 * <p>Persiste el informe</p>
	 * @throws ReportNotDefinedException Si no se estableci� el informe lanzar� error
	 */
	void saveReport() throws ReportNotDefinedException;
	/**
	 * <p>Borra el informe</p>
	 * 
	 * @throws ReportNotDefinedException Si no se estableci� el informe dar� error
	 */
	void deleteReport()throws ReportNotDefinedException;
	
}