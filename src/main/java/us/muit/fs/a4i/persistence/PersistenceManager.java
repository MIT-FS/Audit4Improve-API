package us.muit.fs.a4i.persistence;

import us.muit.fs.a4i.exceptions.ReportNotDefinedException;
import us.muit.fs.a4i.model.entities.ReportI;

/**
 * <p>
 * Interfaz de los gestores de persistencia
 * </p>
 * 
 * @author Isabel Román
 *
 */

public interface PersistenceManager {

	public static enum PersistenceType {
		EXCEL, DDBB
	}

	/**
	 * <p>
	 * Establece el elemento que establece el formato
	 * </p>
	 * 
	 * @param formater Elemento que maneja las características de formato
	 */
	void setFormater(ReportFormaterI formater);

	/**
	 * <p>
	 * Persiste el informe
	 * </p>
	 * 
	 * @param report informe a persistir
	 * @throws ReportNotDefinedException si el informe es nulo lanzará excepción
	 */
	void saveReport(ReportI report) throws ReportNotDefinedException;

	/**
	 * <p>
	 * Borra el informe
	 * </p>
	 * 
	 * @param report informe a borrar
	 * @throws ReportNotDefinedException si el informe es nulo dará error
	 */
	void deleteReport(ReportI report) throws ReportNotDefinedException;

}