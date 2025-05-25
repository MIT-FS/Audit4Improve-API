package us.muit.fs.a4i.exceptions;

/**
 * @author Isabel Román
 *
 */
public class IndicatorException extends Exception {
	/**
	 * Excepción al manejar Indicador
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Información sobre el error
	 */
	private String message;

	/**
	 * <p>
	 * Constructor
	 * </p>
	 * 
	 * @param info Mensaje definiendo el error
	 */
	public IndicatorException(String info) {
		message = info;
	}

	@Override
	public String getMessage() {
		return message;
	}
}