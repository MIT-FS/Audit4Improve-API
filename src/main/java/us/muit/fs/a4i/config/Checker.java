/**
 * 
 */
package us.muit.fs.a4i.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * <p>
 * Clase para verificar las m�tricas e indicadores
 * </p>
 * <p>
 * La API incluye sus métricas e indicadores por defecto en la carpeta
 * resources, en el fichero a4iDefault.json
 * </p>
 * <p>
 * Si la aplicación cliente crea nuevas métricas o indicadores las guarda en un
 * fichero json de configuraci�n, que tendrá que indicársele al Checker
 * </p>
 * <p>
 * Deuda técnica: analizar la posibilidad de leer sólo una vez y mantener en
 * memoria
 * </p>
 * 
 * @author Isabel Rom�n
 *
 */
public class Checker {
	private static Logger log = Logger.getLogger(Checker.class.getName());
	private String a4iMetrics = "a4iDefault.json";
	/*
	 * Definir la ruta completa del fichero de configuraci�n
	 */
	private String appMetrics = null;

	public void setAppMetrics(String appMetricsPath) {
		appMetrics = appMetricsPath;
	}

	/**
	 * <p>
	 * Comprueba si la métrica está definida en el fichero por defecto o en el de la
	 * aplicación cliente
	 * </p>
	 * <p>
	 * Tambi�n verifica que el tipo es el adecuado
	 * </p>
	 * 
	 * @param metricName nombre de la métrica que se quiere comprobar
	 * @param metricType tipo de la métrica
	 * @return metricDefinition Si la métrica está definida y el tipo es correcto se devuelve un mapa con las unidades y la descripci�n
	 * @throws FileNotFoundException Si no se localiza el fichero de configuración
	 */
	public HashMap<String,String> definedMetric(String metricName, String metricType) throws FileNotFoundException {
		log.info("Checker solicitud de b�squeda m�trica " + metricName);
		
		HashMap<String,String> metricDefinition=null;
		
		String filePath="/"+a4iMetrics;
		log.info("Buscando el archivo " + filePath);
		InputStream is=this.getClass().getResourceAsStream(filePath);
		log.info("InputStream "+is+" para "+filePath);
		InputStreamReader isr = new InputStreamReader(is);
		
	
		metricDefinition = isDefinedMetric(metricName, metricType, isr);
		if ((metricDefinition==null) && appMetrics != null) {
			is=new FileInputStream(appMetrics);
			isr=new InputStreamReader(is);			
			metricDefinition = isDefinedMetric(metricName, metricType, isr);
		}

		return metricDefinition;
	}

	private HashMap<String,String> isDefinedMetric(String metricName, String metricType, InputStreamReader isr)
			throws FileNotFoundException {
		
		HashMap<String,String> metricDefinition=null;

	
		JsonReader reader = Json.createReader(isr);
		log.info("Creo el JsonReader");

		JsonObject confObject = reader.readObject();
		log.info("Leo el objeto");
		reader.close();

		log.info("Muestro la configuración leída " + confObject);
		JsonArray metrics = confObject.getJsonArray("metrics");
		log.info("El número de m�tricas es " + metrics.size());
		for (int i = 0; i < metrics.size(); i++) {
			log.info("nombre: " + metrics.get(i).asJsonObject().getString("name"));
			if (metrics.get(i).asJsonObject().getString("name").equals(metricName)) {
				log.info("Localizada la métrica");
				log.info("tipo: " + metrics.get(i).asJsonObject().getString("type"));
				if (metrics.get(i).asJsonObject().getString("type").equals(metricType)) {
					metricDefinition=new HashMap<String,String>();
					metricDefinition.put("description", metrics.get(i).asJsonObject().getString("description"));
					metricDefinition.put("unit", metrics.get(i).asJsonObject().getString("unit"));
				}

			}
		}

		return metricDefinition;
	}

	/**
	 * <p>
	 * Comprueba si el indicador está definido en el fichero por defecto o en el de
	 * la aplicación cliente
	 * </p>
	 * <p>
	 * Tambi�n verifica que el tipo es el adecuado
	 * </p>
	 * 
	 * @param indicatorName nombre del indicador que se quiere comprobar
	 * @param indicatorType tipo del indicador
	 * @return indicatorDefinition Si el indicador está definido y el tipo es correcto se devuelve un mapa con las unidades y la descripci�n
	 * @throws FileNotFoundException Si no se localiza el fichero de configuración
	 */
	public HashMap<String,String> definedIndicator(String indicatorName, String indicatorType) throws FileNotFoundException {
		HashMap<String,String> indicatorDefinition=null;
		log.info("Checker solicitud de b�squeda indicador " + indicatorName);
		boolean defined = false;
		
		
		String filePath="/"+a4iMetrics;
		log.info("Buscando el archivo " + filePath);
		InputStream is=this.getClass().getResourceAsStream(filePath);
		log.info("InputStream "+is+" para "+filePath);
		InputStreamReader isr = new InputStreamReader(is);
		
	
				
		indicatorDefinition = isDefinedIndicator(indicatorName, indicatorType, isr);
		if ((indicatorDefinition==null) && appMetrics != null) {
			is=new FileInputStream(appMetrics);
			isr=new InputStreamReader(is);			
			indicatorDefinition = isDefinedIndicator(indicatorName, indicatorType, isr);
		}

		return indicatorDefinition;
	}

	private  HashMap<String,String> isDefinedIndicator(String indicatorName, String indicatorType, InputStreamReader isr)
			throws FileNotFoundException {
		HashMap<String,String> indicatorDefinition=null;
	
		JsonReader reader = Json.createReader(isr);
		log.info("Creo el JsonReader");

		JsonObject confObject = reader.readObject();
		log.info("Leo el objeto");
		reader.close();

		log.log(Level.INFO, "Muestro la configuraci�n leída {}", confObject);
		JsonArray indicators = confObject.getJsonArray("indicators");
		log.log(Level.INFO, "El número de indicadores es {}", indicators.size());
		for (int i = 0; i < indicators.size(); i++) {
			log.log(Level.INFO, "nombre: {}", indicators.get(i).asJsonObject().getString("name"));
			if (indicators.get(i).asJsonObject().getString("name").equals(indicatorName)) {
				log.log(Level.INFO, "Localizado el indicador");
				log.log(Level.INFO, "tipo: {}", indicators.get(i).asJsonObject().getString("type"));
				if (indicators.get(i).asJsonObject().getString("type").equals(indicatorType)) {
					indicatorDefinition=new HashMap<String,String>();
					indicatorDefinition.put("description", indicators.get(i).asJsonObject().getString("description"));
					indicatorDefinition.put("unit", indicators.get(i).asJsonObject().getString("unit"));
				}

			}
		}

		return indicatorDefinition;
	}
	
	public HashMap<String,String> getMetricInfo(String metricName) throws FileNotFoundException {
		log.log(Level.INFO, "Checker solicitud de b�squeda detalles de la métrica {}", metricName);
		
		HashMap<String,String> metricDefinition=null;	
		String filePath="/"+a4iMetrics;
		log.log(Level.INFO, "Buscando el archivo {}", filePath);
		InputStream is=this.getClass().getResourceAsStream(filePath);
		log.info("InputStream "+is+" para "+filePath);
		InputStreamReader isr = new InputStreamReader(is);

		log.info("Creo el inputStream");
		metricDefinition = getMetricInfo(metricName, isr);
		if ((metricDefinition==null) && appMetrics != null) {
			is=new FileInputStream(appMetrics);
			isr=new InputStreamReader(is);
			metricDefinition = getMetricInfo(metricName, isr);
		}

		return metricDefinition;
	}
	
	
	private HashMap<String,String> getMetricInfo(String metricName, InputStreamReader isr)
			throws FileNotFoundException {
		
		HashMap<String,String> metricDefinition=null;
		
		JsonReader reader = Json.createReader(isr);
		log.info("Creo el JsonReader");

		JsonObject confObject = reader.readObject();
		log.info("Leo el objeto");
		reader.close();

		log.log(Level.INFO, "Muestro la configuraci�n leída {}", confObject);
		JsonArray metrics = confObject.getJsonArray("metrics");
		log.log(Level.INFO, "El número de m�tricas es {}", metrics.size());
		for (int i = 0; i < metrics.size(); i++) {
			log.log(Level.INFO, "nombre: {}", metrics.get(i).asJsonObject().getString("name"));
			if (metrics.get(i).asJsonObject().getString("name").equals(metricName)) {
				log.info("Localizada la m�trica");
				log.info("tipo: " + metrics.get(i).asJsonObject().getString("type"));
				metricDefinition=new HashMap<String,String>();
				metricDefinition.put("name", metrics.get(i).asJsonObject().getString("name"));
				metricDefinition.put("description", metrics.get(i).asJsonObject().getString("description"));
				metricDefinition.put("unit", metrics.get(i).asJsonObject().getString("unit"));
				metricDefinition.put("type", metrics.get(i).asJsonObject().getString("type"));	

			}
		}

		return metricDefinition;
	}

}
