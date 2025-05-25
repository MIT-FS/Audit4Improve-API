/**
 * 
 */
package us.muit.fs.a4i.control.strategies;

/**
 * Strategy for the calculation of the indicator PRPerformance
 * REMEMBER: metrics must be included in a4iDefault.json
 */

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import us.muit.fs.a4i.control.IndicatorStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;

public class PRPerformanceStrategy implements IndicatorStrategy<Double> {

	private static Logger log = Logger.getLogger(Indicator.class.getName());

	// M�tricas necesarias para calcular el indicador
	private static final List<String> REQUIRED_METRICS = Arrays.asList("pullRequestsAcceptedLastYear",
			"pullRequestsAcceptedLastMonth", "pullRequestsRejectedLastMonth", "pullRequestsRejectedLastYear");

	@Override
	public ReportItemI<Double> calcIndicator(List<ReportItemI<Double>> metrics) throws NotAvailableMetricException {

		// Obtener las métricas necesarias
		Optional<ReportItemI<Double>> pullRequestsAcceptedLastYear = metrics.stream()
				.filter(m -> "pullRequestsAcceptedLastYear".equals(m.getName())).findAny();
		Optional<ReportItemI<Double>> pullRequestsAcceptedLastMonth = metrics.stream()
				.filter(m -> "pullRequestsAcceptedLastMonth".equals(m.getName())).findAny();
		Optional<ReportItemI<Double>> pullRequestsRejectedLastYear = metrics.stream()
				.filter(m -> "pullRequestsRejectedLastYear".equals(m.getName())).findAny();
		Optional<ReportItemI<Double>> pullRequestsRejectedLastMonth = metrics.stream()
				.filter(m -> "pullRequestsRejectedLastMonth".equals(m.getName())).findAny();

		ReportItemI<Double> indicatorReport = null;

		// Comprobar que todas las métricas necesarias están presentes
		if (pullRequestsAcceptedLastYear.isPresent() && pullRequestsAcceptedLastMonth.isPresent()
				&& pullRequestsRejectedLastYear.isPresent() && pullRequestsRejectedLastMonth.isPresent()) {

			// Obtener los valores de las métricas
			Double acceptedLastYear = pullRequestsAcceptedLastYear.get().getValue();
			Double acceptedLastMonth = pullRequestsAcceptedLastMonth.get().getValue();
			Double rejectedLastYear = pullRequestsRejectedLastYear.get().getValue();
			Double rejectedLastMonth = pullRequestsRejectedLastMonth.get().getValue();

			// Calcular el ratio de issues
			Double pullRequestsAcceptance = 0.0;
			if (acceptedLastYear != 0 && acceptedLastMonth != 0) {
				pullRequestsAcceptance = rejectedLastYear / acceptedLastYear - rejectedLastMonth / acceptedLastMonth;
			} else {
				// Si una compañía productora de software no ha hecho un PR en el último mes,
				// mal va.
				pullRequestsAcceptance = 1.0;
			}

			try {
				// Crear el indicador
				indicatorReport = new ReportItem.ReportItemBuilder<Double>("PRPerformance", pullRequestsAcceptance)
						.metrics(Arrays.asList(pullRequestsAcceptedLastYear.get(), pullRequestsAcceptedLastMonth.get(),
								pullRequestsRejectedLastYear.get(), pullRequestsRejectedLastMonth.get()))
						.indicator(IndicatorState.OK).build();
			} catch (ReportItemException e) {
				log.info("Error en ReportItemBuilder.");
				e.printStackTrace();
			}

		} else {
			log.info("No se han proporcionado las métricas necesarias");
			throw new NotAvailableMetricException(REQUIRED_METRICS.toString());
		}

		return indicatorReport;
	}

	@Override
	public List<String> requiredMetrics() {
		// Para calcular el indicador "IssuesRatio", ser�n necesarias las m�tricas
		// "openIssues" y "closedIssues".
		return REQUIRED_METRICS;
	}
}
