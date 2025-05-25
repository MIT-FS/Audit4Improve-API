/**
 * 
 */
package us.muit.fs.a4i.control.strategies;

import us.muit.fs.a4i.control.IndicatorStrategy;
/**
 * 
 */
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;

import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.lang.Math;
import java.lang.Double;

public class TeamsBalanceStrategy implements IndicatorStrategy<Double> {

	private static Logger log = Logger.getLogger(Indicator.class.getName());

	// M�tricas necesarias para calcular el indicador
	private static final List<String> REQUIRED_METRICS = Arrays.asList("teamsBalance", "repositories");

	@Override
	public ReportItemI<Double> calcIndicator(List<ReportItemI<Double>> metrics) throws NotAvailableMetricException {
		// Se obtienen y se comprueba que se pasan las m�tricas necesarias para calcular
		// el indicador.
		Optional<ReportItemI<Double>> teamsBalance = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(0).equals(m.getName())).findAny();
		Optional<ReportItemI<Double>> repositories = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(1).equals(m.getName())).findAny();
		ReportItemI<Double> indicatorReport = null;
		log.info("Teamsbalance: " + teamsBalance.toString());
		log.info("Repositories: " + repositories.toString());
		if (teamsBalance.isPresent() && repositories.isPresent()) {
			Double teamsBalanceResult;

			// Se realiza el c�lculo del indicador
			teamsBalanceResult = (Double) teamsBalance.get().getValue() * 100 / repositories.get().getValue();

			log.info("teamsBalanceResult: " + teamsBalanceResult.toString());
			try {
				// Se crea el indicador
				// No se entiende por qué el indicador tiene el mismo nombere que una de las
				// métricas
				indicatorReport = new ReportItem.ReportItemBuilder<Double>("teamsBalance", teamsBalanceResult)
						.metrics(Arrays.asList(teamsBalance.get(), repositories.get()))
						.indicator(IndicatorState.UNDEFINED).build();

				log.info("IndicatorReport: " + indicatorReport.toString());
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
