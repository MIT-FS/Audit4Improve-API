/**
 * 
 */
package us.muit.fs.a4i.model.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;

import us.muit.fs.a4i.control.IndicatorsCalculator;

/**
 * <p>
 * Aspectos generales de todos los informes
 * </p>
 * <p>
 * Todos incluyen un conjunto de métricas de tipo numérico y otro de tipo Date
 * </p>
 * 
 * @author Isabel Román
 *
 */

public class Report implements ReportI {
	private static Logger log = Logger.getLogger(Report.class.getName());
	/**
	 * <p>
	 * Identificador unívoco de la entidad a la que se refire el informe en el
	 * servidor remoto que se va a utilizar
	 * </p>
	 */
	private String entityId;

	private ReportI.ReportType type = null;
	/**
	 * Mapa de Métricas
	 * 
	 */

	private HashMap<String, ReportItemI> metrics;

	/**
	 * Mapa de indicadores
	 */

	private HashMap<String, ReportItemI> indicators;

	public Report() {
		createMaps();

	}

	public Report(String entityId) {
		createMaps();
		this.entityId = entityId;
	}

	public Report(ReportType type) {
		createMaps();
		this.type = type;
	}

	public Report(ReportType type, String entityId) {
		createMaps();
		this.type = type;
		this.entityId = entityId;
	}

	private void createMaps() {
		metrics = new HashMap<String, ReportItemI>();
		indicators = new HashMap<String, ReportItemI>();
	}

	/**
	 * <p>
	 * Busca la métrica solicita en el informe y la devuelve
	 * </p>
	 * <p>
	 * Si no existe devuelve null
	 * </p>
	 * 
	 * @param name Nombre de la métrica buscada
	 * @return la métrica localizada
	 */
	@Override
	public ReportItemI getMetricByName(String name) {
		log.info("solicitada m�trica de nombre " + name);
		ReportItemI metric = null;

		if (metrics.containsKey(name)) {
			log.info("La m�trica est� en el informe");
			metric = metrics.get(name);
		}
		return metric;
	}

	/**
	 * <p>
	 * Añade una métrica al informe
	 * </p>
	 */

	@Override
	public void addMetric(ReportItemI met) {
		metrics.put(met.getName(), met);
		log.info("A�adida m�trica " + met + " Con nombre " + met.getName());
	}

	/**
	 * <p>
	 * Busca el indicador solicitado en el informe y lo devuelve
	 * </p>
	 * <p>
	 * Si no existe devuelve null
	 * </p>
	 * 
	 * @param name Nombre del indicador buscado
	 * @return el indicador localizado
	 */
	@Override
	public ReportItemI getIndicatorByName(String name) {
		log.info("solicitado indicador de nombre " + name);
		ReportItemI indicator = null;

		if (indicators.containsKey(name)) {
			indicator = indicators.get(name);
		}
		return indicator;
	}

	/**
	 * <p>
	 * A�ade un indicador al informe
	 * </p>
	 * 
	 */
	@Override
	public void addIndicator(ReportItemI ind) {

		indicators.put(ind.getName(), ind);
		log.info("Añadido indicador " + ind);

	}

	/**
	 * <p>
	 * Calcula el indicador solicitado y lo incluye en el informe, si se necesita
	 * alguna métrica que no exista la calculadora la busca y la incluye
	 * </p>
	 */
	@Override
	public String getEntityId() {
		return entityId;
	}

	@Override
	public String toString() {
		String repoinfo;
		repoinfo = "Información del Informe:\n - Métricas: ";
		for (String clave : metrics.keySet()) {
			repoinfo += "\n Clave: " + clave + metrics.get(clave);
		}
		repoinfo += "\n - Indicadores: ";
		for (String clave : indicators.keySet()) {
			repoinfo += "\n Clave: " + clave + indicators.get(clave);
		}
		return repoinfo;
	}

	@Override
	public Collection<ReportItemI> getAllMetrics() {

		return metrics.values();
	}

	@Override
	public Collection<ReportItemI> getAllIndicators() {

		return indicators.values();
	}

	@Override
	public ReportType getType() {
		return type;
	}
}
