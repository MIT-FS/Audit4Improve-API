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

public class MetricConfiguration implements MetricConfigurationI {

	/**
	 * El creador de MetricConfiguration tiene que indicar los nombres de los
	 * ficheros de métricas por defecto y del cliente
	 * 
	 * @param defaultRI nombre del fichero de métricas por defecto
	 * @param appRI     nombre del fichero de métricas definido por el cliente
	 */
	public MetricConfiguration(String defaultRI, String appRI) {
		super();
		this.defaultRI = defaultRI;
		this.appRI = appRI;
	}

	private static Logger log = Logger.getLogger(Checker.class.getName());
	private String defaultRI;
	private String appRI;

	/**
	 * Método privado que verifica si una métrica está definida en un fichero de
	 * configuración determinado y si efectivamente tiene el tipo esperado
	 * 
	 * @param metricName String con el nombre de la métrica a buscar
	 * @param metricType String con el tipo de la métrica
	 * @param isr        InputStreamReader del fichero de configuración
	 * @return Mapa de parámetros de la métrica, siempre que exista y sea del tipo
	 *         especificado
	 * 
	 */
	private HashMap<String, String> isDefinedMetric(String metricName, String metricType, InputStreamReader isr) {

		HashMap<String, String> metricDefinition = null;

		JsonReader reader = Json.createReader(isr);
		log.info("Creo el JsonReader");

		JsonObject confObject = reader.readObject();
		log.info("Leo el objeto");
		reader.close();

		log.info("Muestro la configuración leída " + confObject);
		JsonArray metrics = confObject.getJsonArray("metrics");
		log.info("El número de métricas es " + metrics.size());
		for (int i = 0; i < metrics.size(); i++) {
			log.info("nombre: " + metrics.get(i).asJsonObject().getString("name"));
			if (metrics.get(i).asJsonObject().getString("name").equals(metricName)) {
				log.info("Localizada la métrica");
				log.info("tipo: " + metrics.get(i).asJsonObject().getString("type"));
				if (metrics.get(i).asJsonObject().getString("type").equals(metricType)) {
					metricDefinition = new HashMap<String, String>();
					metricDefinition.put("name", metricName);
					metricDefinition.put("type", metricType);
					metricDefinition.put("description", metrics.get(i).asJsonObject().getString("description"));
					metricDefinition.put("unit", metrics.get(i).asJsonObject().getString("unit"));
				}

			}
		}

		return metricDefinition;
	}

	/**
	 * Obtiene los datos de una métrica en un fichero de configuración
	 * 
	 * @param metricName String con el nombre de la métrica buscada
	 * @param isr        InputStreamReader del fichero de configuración
	 * @return Mapa con las características de la métrica
	 * 
	 */
	private HashMap<String, String> getMetric(String metricName, InputStreamReader isr) {

		HashMap<String, String> metricDefinition = null;

		JsonReader reader = Json.createReader(isr);
		log.info("Creo el JsonReader");

		JsonObject confObject = reader.readObject();
		log.info("Leo el objeto");
		reader.close();

		log.info("Muestro la configuración leída " + confObject);
		JsonArray metrics = confObject.getJsonArray("metrics");
		log.info("El número de métricas es " + metrics.size());
		for (int i = 0; i < metrics.size(); i++) {
			log.info("nombre: " + metrics.get(i).asJsonObject().getString("name"));
			if (metrics.get(i).asJsonObject().getString("name").equals(metricName)) {
				log.info("Localizada la métrica");
				metricDefinition = new HashMap<String, String>();
				metricDefinition.put("name", metricName);
				metricDefinition.put("type", metrics.get(i).asJsonObject().getString("type"));
				metricDefinition.put("description", metrics.get(i).asJsonObject().getString("description"));
				metricDefinition.put("unit", metrics.get(i).asJsonObject().getString("unit"));
			}
		}

		return metricDefinition;
	}

	@Override
	public HashMap<String, String> definedMetric(String name, String type) throws FileNotFoundException {
		log.info("Checker solicitud de búsqueda métrica " + name);

		HashMap<String, String> metricDefinition = null;
		InputStream is = null;
		InputStreamReader isr = null;
		String filePath = "/" + defaultRI;
		try {

			log.info("Buscando el archivo " + filePath);
			is = this.getClass().getResourceAsStream(filePath);
			log.info("InputStream " + is + " para " + filePath);
			isr = new InputStreamReader(is);

			/**
			 * Busca primero en el fichero de configuración de métricas por defecto
			 */
			metricDefinition = isDefinedMetric(name, type, isr);
		} catch (NullPointerException e) {
			throw new FileNotFoundException(
					"No se localiza el fichero de la configuración por defecto de la API " + filePath);
		}
		try {
			/**
			 * En caso de que no estuviera ahí la métrica busco en el fichero de
			 * configuración de la aplicación
			 */
			if ((metricDefinition == null) && appRI != null) {
				is = new FileInputStream(appRI);
				isr = new InputStreamReader(is);
				metricDefinition = isDefinedMetric(name, type, isr);
			}
		} catch (NullPointerException e) {
			throw new FileNotFoundException("No se localiza el fichero de la aplicación cliente  " + appRI);
		}

		return metricDefinition;
	}

	@Override
	public HashMap<String, String> getMetricInfo(String name) throws FileNotFoundException {
		log.info("Consulta información de la métrica " + name);
		HashMap<String, String> metricDefinition = null;
		String filePath = "/" + defaultRI;
		InputStream is = null;
		InputStreamReader isr = null;
		try {

			log.info("Buscando el archivo " + filePath);

			is = this.getClass().getResourceAsStream(filePath);
			log.info("InputStream " + is + " para " + filePath);
			isr = new InputStreamReader(is);

			/**
			 * Busca primero en el fichero de configuración de métricas por defecto
			 */
			metricDefinition = getMetric(name, isr);
		} catch (NullPointerException e) {
			throw new FileNotFoundException(
					"No se localiza el fichero de la configuración por defecto de la API " + filePath);
		}
		try {
			/**
			 * En caso de que no estuviera ahí la métrica busco en el fichero de
			 * configuración de la aplicación
			 */
			if ((metricDefinition == null) && appRI != null) {
				is = new FileInputStream(appRI);
				isr = new InputStreamReader(is);
				metricDefinition = getMetric(name, isr);
			}
		} catch (NullPointerException e) {
			throw new FileNotFoundException("No se localiza el fichero de la aplicación cliente  " + appRI);
		}

		return metricDefinition;
	}

	@Override
	public List<String> listAllMetrics() throws FileNotFoundException {
		log.info("Consulta todas las métricas");

		List<String> allmetrics = new ArrayList<String>();
		InputStream is = null;
		InputStreamReader isr = null;
		JsonReader reader = null;
		JsonObject confObject = null;
		JsonArray metrics = null;

		String filePath = "/" + defaultRI;
		try {
			log.info("Buscando el archivo " + filePath);
			is = this.getClass().getResourceAsStream(filePath);
			log.info("InputStream " + is + " para " + filePath);
			isr = new InputStreamReader(is);

			reader = Json.createReader(isr);
			log.info("Creo el JsonReader");

			confObject = reader.readObject();
			log.info("Leo el objeto");
			reader.close();

			log.info("Muestro la configuración leída " + confObject);
			metrics = confObject.getJsonArray("metrics");
			log.info("El número de métricas es " + metrics.size());
			for (int i = 0; i < metrics.size(); i++) {
				log.info("Añado nombre: " + metrics.get(i).asJsonObject().getString("name"));
				allmetrics.add(metrics.get(i).asJsonObject().getString("name"));
			}
		} catch (NullPointerException e) {
			throw new FileNotFoundException(
					"No se localiza el fichero de la configuración por defecto de la API " + filePath);
		}

		try {
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
		} catch (NullPointerException e) {
			throw new FileNotFoundException("No se localiza el fichero de la aplicación cliente " + appRI);
		}

		return allmetrics;
	}
}