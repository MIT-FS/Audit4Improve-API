/**
 * 
 */
package us.muit.fs.a4i.control.strategies;

import us.muit.fs.a4i.control.IndicatorStrategy;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;

/**
 * Strategy for the calculation of a developer performance comparing him with
 * the rest of developers
 * 
 * @author fracrusan (year 23/24)
 * @author Isabel Román RECUERDA: los indicadores tienen que estar incluidos en
 *         el fichero de configuración a4iDefault.json
 */
public class DeveloperPerformanceStrategy implements IndicatorStrategy<Double> {

	private static Logger log = Logger.getLogger(Indicator.class.getName());
	// M�tricas necesarias para calcular el indicador
	private static final List<String> REQUIRED_METRICS = Arrays.asList("issuesLastMonth", "closedIssuesLastMonth",
			"issues4DevLastMonth", "meanClosedIssuesLastMonth");

	@Override
	public ReportItemI calcIndicator(List metrics) throws NotAvailableMetricException {
		// Se obtienen y se comprueba que se pasan las m�tricas necesarias para calcular
		// el indicador.
		Optional<ReportItemI<Integer>> issuesLastMonth = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(0).equals(((ReportItemI) m).getName())).findAny();
		Optional<ReportItemI<Integer>> closedIssuesLastMonth = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(1).equals(((ReportItemI) m).getName())).findAny();
		Optional<ReportItemI<Double>> issues4DevLastMonth = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(2).equals(((ReportItemI) m).getName())).findAny();
		Optional<ReportItemI<Double>> meanClosedIssuesLastMonth = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(3).equals(((ReportItemI) m).getName())).findAny();
		ReportItemI<Double> indicatorReport = null;

		if (issuesLastMonth.isPresent() && closedIssuesLastMonth.isPresent() && issues4DevLastMonth.isPresent()
				&& meanClosedIssuesLastMonth.isPresent()) {
			Double rendimientoMiembro;

			// calculating indicator
			if (issuesLastMonth.get().getValue() != 0 && issues4DevLastMonth.get().getValue() != 0
					&& meanClosedIssuesLastMonth.get().getValue() != 0)
				rendimientoMiembro = (closedIssuesLastMonth.get().getValue() / issuesLastMonth.get().getValue())
						/ (meanClosedIssuesLastMonth.get().getValue() / issues4DevLastMonth.get().getValue());
			else if (meanClosedIssuesLastMonth.get().getValue() != 0)
				rendimientoMiembro = 1.0;
			else
				rendimientoMiembro = 0.0;

			try {
				// Se crea el indicador
				indicatorReport = new ReportItem.ReportItemBuilder<Double>("developerPerformance", rendimientoMiembro)
						.metrics(Arrays.asList(issuesLastMonth.get(), closedIssuesLastMonth.get(),
								issues4DevLastMonth.get(), meanClosedIssuesLastMonth.get()))
						.indicator(IndicatorState.UNDEFINED).build();
			} catch (ReportItemException e) {
				log.info("Error en ReportItemBuilder.");
				e.printStackTrace();
			}

		} else {
			log.info("No se han proporcionado las m�tricas necesarias");
			throw new NotAvailableMetricException(REQUIRED_METRICS.toString());
		}

		return indicatorReport;
	}

	@Override
	public List<String> requiredMetrics() {
		// Para calcular el indicador "rendimientoMiembro", ser�n necesarias las
		// m�tricas
		// "openIssues" y "closedIssues".
		return REQUIRED_METRICS;
	}

}
