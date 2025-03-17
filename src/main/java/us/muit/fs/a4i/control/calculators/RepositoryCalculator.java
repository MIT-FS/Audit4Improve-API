/**
 * 
 */
package us.muit.fs.a4i.control.calculators;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import us.muit.fs.a4i.control.IndicatorStrategy;
import us.muit.fs.a4i.control.IndicatorsCalculator;
import us.muit.fs.a4i.control.ReportManagerI;
import us.muit.fs.a4i.exceptions.IndicatorException;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItemI;
import java.util.stream.Collectors;

/**
 * <p>
 * Implementa los métodos para calcular indicadores referidos a un repositorio
 * </p>
 * <p>
 * Puede hacerse uno a uno o todos a la vez
 * </p>
 * RECUERDA: los indicadores tienen que estar incluidos en el fichero de
 * configuración a4iDefault.json
 * 
 * @author Isabel Román
 *
 */
public class RepositoryCalculator implements IndicatorsCalculator {
	private static Logger log = Logger.getLogger(RepositoryCalculator.class.getName());
	private static ReportI.ReportType reportType = ReportI.ReportType.REPOSITORY;
	private static HashMap<String, IndicatorStrategy> strategies = new HashMap<>();

	@Override
	public void calcIndicator(String indicatorName, ReportManagerI reportManager) throws IndicatorException {
		log.info("Calcula el indicador de nombre " + indicatorName);
		/**
		 * Tiene que mirar si están ya las métricas que necesita Si están lo calcula Si
		 * no están busca las métricas, las añade al informe y lo calcula
		 * 
		 */
		IndicatorStrategy indicatorStrategy = strategies.get(indicatorName);
		List<String> requiredMetrics = indicatorStrategy.requiredMetrics();
		log.fine("Las métricas necesarias son: " + requiredMetrics.toString());
		List<ReportItemI> metrics = reportManager.getReport().getAllMetrics().stream().collect(Collectors.toList());
		List<String> metricsName = metrics.stream().map(ReportItemI::getName).collect(Collectors.toList());
		for (String metric : requiredMetrics) {
			if (!metricsName.contains(metric)) {
				log.fine("se añade la métrica " + metric + " que no estaba disponible aún en el informe");
				reportManager.addMetric(metric);
			}
		}

		try {
			// añadir el indicador al informe
			reportManager.getReport().addIndicator(indicatorStrategy.calcIndicator(reportManager.getReport().getAllMetrics().stream().collect(Collectors.toList())));
			log.info("Añadido al informe indicador");
		} catch (NotAvailableMetricException e) {
			log.info("No se han proporcionado todas las métricas necesarias");
			e.printStackTrace();
		}

	}

	/**
	 * Calcula todos los indicadores definidos para un repositorio Recupera todas
	 * las métricas que necesite y que no estén en el informe y las añade al mismo
	 * 
	 */
	@Override
	public void calcAllIndicators(ReportManagerI reportManager) throws IndicatorException {
		log.info("Calcula todos los indicadores del repositorio y los incluye en el informe");
	}

	private Indicator commitsPerUser(ReportI report) {
		Indicator indicator = null;

		return indicator;
	}

	@Override
	public ReportI.ReportType getReportType() {
		return reportType;
	}

	@Override
	public void setIndicator(String indicatorName, IndicatorStrategy strategy) {
		strategies.put(indicatorName, strategy);

	}

}