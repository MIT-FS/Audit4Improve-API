
package us.muit.fs.a4i.control;

import java.util.HashMap;
import java.io.FileNotFoundException;
import java.util.logging.Logger;
import javax.json.JsonReader;
import java.util.logging.Logger;
import javax.json.JsonObject;
import javax.json.JsonArray;
import java.io.InputStreamReader;

import us.muit.fs.a4i.exceptions.IndicatorException;
import us.muit.fs.a4i.model.entities.ReportI;

public class MetricConfiguration {

    private String a4iMetrics = "a4iDefault.json";
	private String appMetrics = null;
	private static Logger log = Logger.getLogger(Checker.class.getName());

    private HashMap<String,String> isDefinedMetric(String metricName, String metricType, InputStreamReader isr)
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
				if (metrics.get(i).asJsonObject().getString("type").equals(metricType)) {
					metricDefinition=new HashMap<String,String>();
					metricDefinition.put("description", metrics.get(i).asJsonObject().getString("description"));
					metricDefinition.put("unit", metrics.get(i).asJsonObject().getString("unit"));
				}

			}
		}

		return metricDefinition;
	}

    public interface MetricConfigurationI {

        public void setAppMetrics(String appMetricsPath) {
		    appMetrics = appMetricsPath;
	    }

        public HashMap<String,String> definedMetric(String metricName, String metricType) throws FileNotFoundException {
		log.info("Checker solicitud de búsqueda métrica " + metricName);
		
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
	} 
}