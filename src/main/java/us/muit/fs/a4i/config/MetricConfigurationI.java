package us.muit.fs.a4i.config;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Isabel Román
 *
 */

public interface MetricConfigurationI {
	/**
	 * <p>
	 * Comprueba si la métrica está definida en el fichero por defecto o en el de la
	 * aplicación cliente
	 * </p>
	 * <p>
	 * También verifica que el tipo es el adecuado
	 * </p>
	 * 
	 * @param metricName nombre de la métrica que se quiere comprobar
	 * @param metricType tipo de la métrica
	 * @return metricDefinition Si la métrica está definida y el tipo es correcto se
	 *         devuelve un mapa con las unidades y la descripción
	 * @throws FileNotFoundException Si hay algún problema con el fichero de
	 *                               configuración
	 */
	public HashMap<String, String> definedMetric(String metricName, String metricType) throws FileNotFoundException;

	/**
	 * @param metricName nombre de la métrica que se consulta
	 * @return la información de la métrica en un mapa
	 * @throws FileNotFoundException Si hay algún problema con el fichero de
	 *                               configuración
	 */
	public HashMap<String, String> getMetricInfo(String metricName) throws FileNotFoundException;

	/**
	 * @return listado con los nombres de todas las métricas disponibles
	 * @throws FileNotFoundException Si hay algún problema con el fichero de
	 *                               configuración
	 */
	List<String> listAllMetrics() throws FileNotFoundException;
}
