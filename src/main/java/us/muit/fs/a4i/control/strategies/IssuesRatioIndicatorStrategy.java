package us.muit.fs.a4i.control.strategies;

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

/**
 * @author alumnos del curso 23/24, grupo 3 RECUERDA: los indicadores tienen que
 *         estar incluidos en el fichero de configuración a4iDefault.json
 */
public class IssuesRatioIndicatorStrategy implements IndicatorStrategy<Double> {

	private static Logger log = Logger.getLogger(Indicator.class.getName());

	// M�tricas necesarias para calcular el indicador
	private static final List<String> REQUIRED_METRICS = Arrays.asList("openIssues", "closedIssues");

	@Override
	public ReportItemI<Double> calcIndicator(List<ReportItemI<Double>> metrics) throws NotAvailableMetricException {
		// Se obtienen y se comprueba que se pasan las m�tricas necesarias para calcular
		// el indicador.
		Optional<ReportItemI<Double>> openIssues = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(0).equals(m.getName())).findAny();
		Optional<ReportItemI<Double>> closedIssues = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(1).equals(m.getName())).findAny();
		ReportItemI<Double> indicatorReport = null;

		if (openIssues.isPresent() && closedIssues.isPresent()) {
			Double issuesRatio;

			// Se realiza el c�lculo del indicador
			if (closedIssues.get().getValue() != 0)
				issuesRatio = openIssues.get().getValue() / closedIssues.get().getValue();
			else
				issuesRatio = openIssues.get().getValue();

			try {
				// Se crea el indicador
				indicatorReport = new ReportItem.ReportItemBuilder<Double>("issuesRatio", issuesRatio)
						.metrics(Arrays.asList(openIssues.get(), closedIssues.get()))
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
		// Para calcular el indicador "IssuesRatio", ser�n necesarias las m�tricas
		// "openIssues" y "closedIssues".
		return REQUIRED_METRICS;
	}
}