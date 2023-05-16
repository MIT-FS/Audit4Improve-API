package us.muit.fs.a4i.exceptions;

/**
 * @author Isabel Román
 *
 */
public class MetricException extends Exception {
	/**
	 * Excepción al manejar métrica
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
	public MetricException(String info) {
		message = info;
	}

	@Override
	public String getMessage() {
		return message;
	}
}