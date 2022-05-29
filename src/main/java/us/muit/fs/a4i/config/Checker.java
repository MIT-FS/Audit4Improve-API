package us.muit.fs.a4i.config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Logger;
import us.muit.fs.a4i.config.MetricConfiguration;
import us.muit.fs.a4i.config.IndicatorConfiguration;

import javax.json.*;

public class Checker(IndicatorsConfiguration indConf, MetricsConfiguration metricConf){
	private String a4iMetrics = "a4iDefault.json";
	private String appMetrics = null;
	private static Logger log = Logger.getLogger(Checker.class.getName());
	
	public HashMap<String,String> getMetricInfo(String metricName) throws FileNotFoundException {
		log.info("Checker solicitud de b�squeda detalles de la m�trica " + metricName);
		
		HashMap<String,String> metricDefinition=null;	
		String filePath="/"+a4iMetrics;
		log.info("Buscando el archivo " + filePath);
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

		log.info("Muestro la configuraci�n le�da " + confObject);
		JsonArray metrics = confObject.getJsonArray("metrics");
		log.info("El n�mero de m�tricas es " + metrics.size());
		for (int i = 0; i < metrics.size(); i++) {
			log.info("nombre: " + metrics.get(i).asJsonObject().getString("name"));
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
