/**
 * 
 */
package us.muit.fs.a4i.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import us.muit.fs.a4i.model.entities.IndicatorI;
import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;
import us.muit.fs.a4i.model.entities.ReportItemI;

/**
 * @author Isabel Román
 *
 */
public class IndicatorConfiguration implements IndicatorConfigurationI {
	 /**
     * El creador de IndicatorConfiguration tiene que indicar los nombres de los ficheros de métricas por defecto y del cliente
     * @param defaultRI nombre del fichero de métricas por defecto
     * @param appRI nombre del fichero de métricas definido por el cliente
     */
	public IndicatorConfiguration(String defaultRI, String appRI) {
		super();
		this.defaultRI = defaultRI;
		this.appRI = appRI;
	}

	private static Logger log = Logger.getLogger(Checker.class.getName());

	private String CRITICAL_LIMIT = "limits.critical";
	private String WARNING_LIMIT = "limits.warning";
	private String OK_LIMIT = "limits.ok";
	private String defaultRI;
	private String appRI;

	@Override
	/**
	 * <p>
	 * Comprueba si el indicador está definido en el fichero por defecto o en el de
	 * la aplicación cliente
	 * </p>
	 * <p>
	 * También verifica que el tipo es el adecuado
	 * </p>
	 * 
	 * @param indicatorName nombre del indicador que se quiere comprobar
	 * @param indicatorType tipo del indicador
	 * @return indicatorDefinition Si el indicador está definido y el tipo es
	 *         correcto se devuelve un mapa con las unidades y la descripción
	 * @throws FileNotFoundException Si no se localiza el fichero de configuración
	 */
	public HashMap<String, String> definedIndicator(String name, String type) throws FileNotFoundException {
		HashMap<String, String> indicatorDefinition = null;
		log.info("Checker solicitud de búsqueda indicador " + name);

		String filePath = "/" + defaultRI;
		log.info("Buscando el archivo " + filePath);
		InputStream is = this.getClass().getResourceAsStream(filePath);
		log.info("InputStream " + is + " para " + filePath);
		InputStreamReader isr = new InputStreamReader(is);

		indicatorDefinition = isDefinedIndicator(name, type, isr);
		if ((indicatorDefinition == null) && appRI != null) {
			is = new FileInputStream(appRI);
			isr = new InputStreamReader(is);
			indicatorDefinition = isDefinedIndicator(name, type, isr);
		}

		return indicatorDefinition;
	}

	/**
	 * Busca el indicador con un nombre y un tipo determinado y devuelve sus
	 * características en un mapa
	 * 
	 * @param indicatorName
	 * @param indicatorType
	 * @param isr
	 * @return
	 * @throws FileNotFoundException
	 */
	private HashMap<String, String> isDefinedIndicator(String indicatorName, String indicatorType,
			InputStreamReader isr) throws FileNotFoundException {
		HashMap<String, String> indicatorDefinition = null;

		JsonReader reader = Json.createReader(isr);
		log.info("Creo el JsonReader");

		JsonObject confObject = reader.readObject();
		log.info("Leo el objeto");
		reader.close();

		log.info("Muestro la configuración leída " + confObject);
		JsonArray indicators = confObject.getJsonArray("indicators");
		log.info("El número de indicadores es " + indicators.size());
		for (int i = 0; i < indicators.size(); i++) {
			log.info("nombre: " + indicators.get(i).asJsonObject().getString("name"));
			if (indicators.get(i).asJsonObject().getString("name").equals(indicatorName)) {
				log.info("Localizado el indicador");
				log.info("tipo: " + indicators.get(i).asJsonObject().getString("type"));
				if (indicators.get(i).asJsonObject().getString("type").equals(indicatorType)) {
					indicatorDefinition = new HashMap<String, String>();
					indicatorDefinition.put("name", indicatorName);
					indicatorDefinition.put("type", indicatorType);
					indicatorDefinition.put("description", indicators.get(i).asJsonObject().getString("description"));
					indicatorDefinition.put("unit", indicators.get(i).asJsonObject().getString("unit"));

					JsonObject limits = indicators.get(i).asJsonObject().getJsonObject("limits");
					int okLimit = 0;
					int warningLimit = 0;
					int criticalLimit = 0;

					if (limits != null) {
						okLimit = limits.getInt("ok");
						warningLimit = limits.getInt("warning");
						criticalLimit = limits.getInt("critical");
						indicatorDefinition.put(OK_LIMIT, Integer.toString(okLimit));
						indicatorDefinition.put(WARNING_LIMIT, Integer.toString(warningLimit));
						indicatorDefinition.put(CRITICAL_LIMIT, Integer.toString(criticalLimit));
					} else {
						log.info("El fichero de configuración no especifica límites para este indicador");
					}

				}

			}
		}

		return indicatorDefinition;
	}

	@Override
	public List<String> listAllIndicators() throws FileNotFoundException {
		log.info("Consulta todas las métricas");

		List<String> allmetrics = new ArrayList<String>();

		String filePath = "/" + defaultRI;
		log.info("Buscando el archivo " + filePath);
		InputStream is = this.getClass().getResourceAsStream(filePath);
		log.info("InputStream " + is + " para " + filePath);
		InputStreamReader isr = new InputStreamReader(is);

		JsonReader reader = Json.createReader(isr);
		log.info("Creo el JsonReader");

		JsonObject confObject = reader.readObject();
		log.info("Leo el objeto");
		reader.close();

		log.info("Muestro la configuración leída " + confObject);
		JsonArray metrics = confObject.getJsonArray("indicators");
		log.info("El número de indicadores es " + metrics.size());
		for (int i = 0; i < metrics.size(); i++) {
			log.info("Añado nombre: " + metrics.get(i).asJsonObject().getString("name"));
			allmetrics.add(metrics.get(i).asJsonObject().getString("name"));
		}
		if (appRI != null) {
			is = new FileInputStream(appRI);
			isr = new InputStreamReader(is);
			reader = Json.createReader(isr);
			confObject = reader.readObject();
			reader.close();

			log.info("Muestro la configuración leída " + confObject);
			metrics = confObject.getJsonArray("metrics");
			log.info("El número de métricas es " + metrics.size());
			for (int i = 0; i < metrics.size(); i++) {
				log.info("Añado nombre: " + metrics.get(i).asJsonObject().getString("name"));
				allmetrics.add(metrics.get(i).asJsonObject().getString("name"));
			}
		}

		return allmetrics;
	}

	@Override
	public IndicatorState getIndicatorState(ReportItemI indicator) {
		// TODO: change indicator definitions key name to a constant.

		// Necesita optimización del cálculo de estado

		String indicatorType = indicator.getValue().getClass().getName();

		IndicatorState finalState = IndicatorState.UNDEFINED;
		try {
			HashMap<String, String> indicatorDefinition = definedIndicator(indicator.getName(), indicatorType);

			String criticalLimit = indicatorDefinition.get(CRITICAL_LIMIT);
			String warningLimit = indicatorDefinition.get(WARNING_LIMIT);
			String okLimit = indicatorDefinition.get(OK_LIMIT);

			// Si no se han encontrado límites definidos para ese indicador el estado es
			// UNDEFINED.
			if (criticalLimit != null && warningLimit != null && okLimit != null) {
				// Se tienen en cuenta los posibles tipos de indicadores para compararlos.
				if (indicatorType.equals(Integer.class.getName())) {
					Integer value = (Integer) indicator.getValue();

					if (value >= Integer.parseInt(criticalLimit))
						finalState = IndicatorState.CRITICAL;
					else if (value <= Integer.parseInt(okLimit))
						finalState = IndicatorState.OK;
					else
						finalState = IndicatorState.WARNING;

				} else if (indicatorType.equals(Double.class.getName())) {
					Double value = (Double) indicator.getValue();

					if (value >= Integer.parseInt(criticalLimit))
						finalState = IndicatorState.CRITICAL;
					else if (value <= Integer.parseInt(okLimit))
						finalState = IndicatorState.OK;
					else
						finalState = IndicatorState.WARNING;

				}
			} else {
				log.warning("No se han encontrado límites definidos para el indicador: " + indicator.getName());
				finalState = IndicatorState.UNDEFINED;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		log.info("El estado del Indicador " + indicator.getName() + " es " + finalState.toString());
		return finalState;
	}

}
