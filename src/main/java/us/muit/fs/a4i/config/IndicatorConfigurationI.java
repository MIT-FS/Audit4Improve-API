/**
 * 
 */
package us.muit.fs.a4i.config;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import us.muit.fs.a4i.model.entities.IndicatorI;
import us.muit.fs.a4i.model.entities.ReportItemI;

/**
 * @author Isabel Román
 *
 */
public interface IndicatorConfigurationI {
	/**
	 * <p>
	 * Verifica si el indicador existe y el tipo es correcto
	 * </p>
	 * 
	 * @param name nombre del indicador
	 * @param type tipo del valor del indicador
	 * @return mapa con los detalles del indicador
	 * @throws FileNotFoundException si hay algún problema con el fichero de
	 *                               configuración
	 */
	public HashMap<String, String> definedIndicator(String name, String type) throws FileNotFoundException;

	/**
	 * @return lista con los nombres de todos los indicadores disponibles
	 * @throws FileNotFoundException en caso de problemas con el fichero de
	 *                               configuración
	 */
	public List<String> listAllIndicators() throws FileNotFoundException;

	/**
	 * <p>
	 * Infiere el estado del indicador a partir del valor del ReportItem y de la
	 * configuración de estados en el fichero
	 * </p>
	 * 
	 * @param indicator ReportItem para el que se quiere calcular el estado
	 * @return El estado del indicador
	 */
	public IndicatorI.IndicatorState getIndicatorState(ReportItemI indicator);
}
