/**
 * 
 */
package us.muit.fs.a4i.model.entities;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import us.muit.fs.a4i.control.IndicatorsCalculator;
import us.muit.fs.a4i.exceptions.IndicatorException;

/**
 * <p>Aspectos generales de todos los informes</p>
 * <p>Todos incluir�n un conjunto de m�tricas de tipo num�rico y otro de tipo Date</p>
 * @author Isabel Rom�n
 *
 */

public class Report implements ReportI {
	private static Logger log=Logger.getLogger(Report.class.getName());
	/**
	 * <p>Identificador un�voco de la entidad a la que se refire el informe en el servidor remoto que se va a utilizar</p>
	 */
	private String entityId;
	/**
	 * <p>Los objetos que implementen esta interfaz recurren a calcuadoras con los algoritmos para el c�lculo de indicadores<p>
	 * <p>Los algoritmos de c�lculo de indicadores ser�n espec�ficos para un tipo de informe<p>
	 */
	private IndicatorsCalculator calc;
	
	
	
	
	private ReportI.Type type=null;
	/**
	 * Mapa de M�tricas
	 * 
	 * */
	 
	private HashMap<String,Metric> metrics;
	
	/**
	 * Mapa de indicadores
	 */
		
	private HashMap<String,Indicator> indicators;
	
	public Report(){
		createMaps();
		
	}
	public Report(String entityId){
		createMaps();
		this.entityId=entityId;		
	}
	public Report(Type type){
		createMaps();
		this.type=type;		
	}
	public Report(Type type,String entityId){
		createMaps();
		this.type=type;	
		this.entityId=entityId;		
	}	
	private void createMaps() {
		metrics=new HashMap<String,Metric>();
		indicators=new HashMap<String,Indicator>();
	}
	/**
	 * <p>Busca la m�trica solicita en el informe y la devuelve</p>
	 * <p>Si no existe devuelve null</p>
	 * @param name Nombre de la m�trica buscada
	 * @return la m�trica localizada
	 */
	@Override
	public Metric getMetricByName(String name) {
		log.info("solicitada m�trica de nombre "+name);
		Metric metric=null;
		
		if (metrics.containsKey(name)){
			log.info("La m�trica est� en el informe");
			metric=metrics.get(name);
		}
		return metric;
	}
	/**
	 * <p>A�ade una m�trica al informe</p>
	 */

	@Override
	public void addMetric(Metric met) {		
		metrics.put(met.getName(), met);
		log.info("A�adida m�trica "+met+" Con nombre "+met.getName());
	}
	/**
	 * <p>Busca el indicador solicitado en el informe y lo devuelve</p>
	 * <p>Si no existe devuelve null</p>
	 * @param name Nombre del indicador buscado
	 * @return el indicador localizado
	 */
	@Override
	public Indicator getIndicatorByName(String name) {
		log.info("solicitado indicador de nombre "+name);
		Indicator indicator=null;
		
		if (indicators.containsKey(name)){
			indicator=indicators.get(name);
		}
		return indicator;
	}
/**
 * <p>A�ade un indicador al informe</p>	
 * 
 */ 
	@Override
	public void addIndicator(Indicator ind) {
		
		indicators.put(ind.getName(), ind);
		log.info("A�adido indicador "+ind);

	}
	/**
	 * <p>Calcula el indicador solicitado y lo incluye en el informe, si se necesita alguna m�trica que no exista la calculadora la busca y la incluye</p>
	 */


    @Override
	public String getEntityId() {
    	return entityId;
    }

	
	@Override
	public String toString() {
		String repoinfo;
		repoinfo="Informaci�n del Informe:\n - M�tricas: ";
		for (String clave:metrics.keySet()) {
		     repoinfo+="\n Clave: " + clave + metrics.get(clave);
		}
		repoinfo+="\n - Indicadores: ";
		for (String clave:indicators.keySet()) {
		     repoinfo+="\n Clave: " + clave + indicators.get(clave);
		}
		return repoinfo;
	}
	@Override
	public Collection<Metric> getAllMetrics() {
		// TODO Auto-generated method stub
		return metrics.values();
	}
	
	@Override
	public Collection<Indicator> getAllIndicators() {
		// TODO Auto-generated method stub
		return indicators.values();
	}
	@Override
	public ReportI.Type getType() {
		return type;
	}



}
