/**
 * 
 */
package us.muit.fs.a4i.control.strategies;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import us.muit.fs.a4i.control.IndicatorStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;

/**
 * % de pull requests cerrados, sobre el total
 * 
 * @author Sergio García López (equipo 4 del curso 23/24) RECUERDA: los
 *         indicadores tienen que estar incluidos en el fichero de configuración
 *         a4iDefault.json
 *
 */
public class PullRequestIndicatorStrategy implements IndicatorStrategy<Double> {
	private static Logger log = Logger.getLogger(PullRequestIndicatorStrategy.class.getName());

	// Métricas necesarias para calcular el indicador
	private static final List<String> REQUIRED_METRICS = Arrays.asList("totalPullReq", "closedPullReq");

	@Override
	public ReportItemI<Double> calcIndicator(List<ReportItemI<Double>> metrics) throws NotAvailableMetricException {

		// Indicador a devolver
		ReportItemI<Double> indicatorReport = null;

		// Estado del indicador
		IndicatorState estado = IndicatorState.UNDEFINED;

		Optional<ReportItemI<Double>> totalPullReq = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(0).equals(m.getName())).findAny();
		Optional<ReportItemI<Double>> closedPullReq = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(1).equals(m.getName())).findAny();

		if (totalPullReq.isPresent() && closedPullReq.isPresent()) {

			// Calculamos el indicador
			Double pullRequestCompletion = 0.0;

			if (totalPullReq.get().getValue() > 0) {
				pullRequestCompletion = 100 * closedPullReq.get().getValue() / totalPullReq.get().getValue();

				// Criterios de calidad (porcentuales)
				// estos límites deben estar configurados en el fichero a4iDefault.json, no aquí
				if (pullRequestCompletion > 75) {
					estado = IndicatorState.OK;
				} else if (pullRequestCompletion > 50) {
					estado = IndicatorState.WARNING;
				} else {
					estado = IndicatorState.CRITICAL;
				}
			}

			try {

				indicatorReport = new ReportItem.ReportItemBuilder<Double>("pullRequestCompletion",
						pullRequestCompletion).metrics(Arrays.asList(totalPullReq.get(), closedPullReq.get()))
						.indicator(estado).build();

			} catch (ReportItemException e) {
				log.info("Error en ReportItemBuilder: " + e);
			}

		} else {
			log.info("Falta alguna de las métricas");
			throw new NotAvailableMetricException(REQUIRED_METRICS.toString());
		}

		return indicatorReport;

	}

	@Override
	public List<String> requiredMetrics() {
		return REQUIRED_METRICS;
	}
}
