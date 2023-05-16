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

	private static Logger log = Logger.getLogger(Checker.class.getName());

	private HashMap<String, String> isDefinedMetric(String metricName, String metricType, InputStreamReader isr)
			throws FileNotFoundException {

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
					metricDefinition.put("description", metrics.get(i).asJsonObject().getString("description"));
					metricDefinition.put("unit", metrics.get(i).asJsonObject().getString("unit"));
				}

			}
		}

		return metricDefinition;
	}

	private HashMap<String, String> getMetric(String metricName, InputStreamReader isr) throws FileNotFoundException {

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

		String filePath = "/" + Context.getDefaultRI();
		log.info("Buscando el archivo " + filePath);
		InputStream is = this.getClass().getResourceAsStream(filePath);
		log.info("InputStream " + is + " para " + filePath);
		InputStreamReader isr = new InputStreamReader(is);

		/**
		 * Busca primero en el fichero de configuración de métricas por defecto
		 */
		metricDefinition = isDefinedMetric(name, type, isr);
		/**
		 * En caso de que no estuviera ahí la métrica busco en el fichero de
		 * configuración de la aplicación
		 */
		if ((metricDefinition == null) && Context.getAppRI() != null) {
			is = new FileInputStream(Context.getAppRI());
			isr = new InputStreamReader(is);
			metricDefinition = isDefinedMetric(name, type, isr);
		}

		return metricDefinition;
	}

	@Override
	public HashMap<String, String> getMetricInfo(String name) throws FileNotFoundException {
		log.info("Consulta información de la métrica " + name);
		HashMap<String, String> metricDefinition = null;

		String filePath = "/" + Context.getDefaultRI();
		log.info("Buscando el archivo " + filePath);
		InputStream is = this.getClass().getResourceAsStream(filePath);
		log.info("InputStream " + is + " para " + filePath);
		InputStreamReader isr = new InputStreamReader(is);

		/**
		 * Busca primero en el fichero de configuración de métricas por defecto
		 */
		metricDefinition = getMetric(name, isr);
		/**
		 * En caso de que no estuviera ahí la métrica busco en el fichero de
		 * configuración de la aplicación
		 */
		if ((metricDefinition == null) && Context.getAppRI() != null) {
			is = new FileInputStream(Context.getAppRI());
			isr = new InputStreamReader(is);
			metricDefinition = getMetric(name, isr);
		}

		return metricDefinition;
	}

	@Override
	public List<String> listAllMetrics() throws FileNotFoundException {
		log.info("Consulta todas las métricas");

		List<String> allmetrics = new ArrayList<String>();

		String filePath = "/" + Context.getDefaultRI();
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
		JsonArray metrics = confObject.getJsonArray("metrics");
		log.info("El número de métricas es " + metrics.size());
		for (int i = 0; i < metrics.size(); i++) {
			log.info("Añado nombre: " + metrics.get(i).asJsonObject().getString("name"));
			allmetrics.add(metrics.get(i).asJsonObject().getString("name"));
		}
		if (Context.getAppRI() != null) {
			is = new FileInputStream(Context.getAppRI());
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
}