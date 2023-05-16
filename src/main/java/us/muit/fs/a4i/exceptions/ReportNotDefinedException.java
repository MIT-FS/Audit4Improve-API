/**
 * 
 */
package us.muit.fs.a4i.exceptions;

/**
 * @author isa
 *
 */
public class ReportNotDefinedException extends Exception {
	/**
	 * Excepción que indica que se está intentando recuperar una entidad sin haber
	 * establecido su id
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "No se ha establecido aún un informe";
	}
}
