package us.muit.fs.a4i.exceptions;

/**
 * @author Isabel Rom�n
 *
 */
public class MetricException extends Exception {
	 /**
	 * Excepci�n que indica que se est� intentando recuperar una entidad sin haber establecido su id
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Informaci�n sobre el error
	 */
	private String message;
	/**
	 * <p>Constructor</p>
	 * @param info Mensaje definiendo el error
	 */
	public MetricException(String info){
		message=info;
	}

	@Override
	    public String getMessage(){
		 return message;
    }
}