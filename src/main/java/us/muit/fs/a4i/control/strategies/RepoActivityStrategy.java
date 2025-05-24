/**
 * 
 */
package us.muit.fs.a4i.control.strategies;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import us.muit.fs.a4i.control.IndicatorStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;

/**
 * Clase para calcular el indicador compuesto de issues, pull requests y
 * contribuyentes.
 * 
 * @param <Double> Tipo de dato del indicador.
 */
public class RepoActivityStrategy implements IndicatorStrategy<Double> {
	private static Logger log = Logger.getLogger(RepoActivityStrategy.class.getName());

	// Métricas necesarias para calcular el indicador
	private static final List<String> REQUIRED_METRICS = Arrays.asList("closedIssues", "issues", "closedPullReq",
			"totalPullReq", "activeContributor", "totalContributor");

	@Override
	public ReportItemI<Double> calcIndicator(List<ReportItemI<Double>> metricas) throws NotAvailableMetricException {

		// Variables para almacenar las métricas
		ReportItemI<Double> issuesCerrados = null;
		ReportItemI<Double> issuesTotales = null;
		ReportItemI<Double> pullRequestsCerrados = null;
		ReportItemI<Double> pullRequestsTotales = null;
		ReportItemI<Double> contribuyentesActivos = null;
		ReportItemI<Double> contribuyentesTotales = null;

		// Indicador a devolver
		ReportItemI<Double> indicatorReport = null;

		// Estado del indicador
		IndicatorState estado = IndicatorState.UNDEFINED;

		// Buscamos las métricas en la lista
		for (ReportItemI<Double> metrica : metricas) {
			switch (metrica.getName()) {
			case "closedIssues":
				issuesCerrados = metrica;
				break;
			case "issues":
				issuesTotales = metrica;
				break;
			case "closedPullReq":
				pullRequestsCerrados = metrica;
				break;
			case "totalPullReq":
				pullRequestsTotales = metrica;
				break;
			case "activeContributor":
				contribuyentesActivos = metrica;
				break;
			case "totalContributor":
				contribuyentesTotales = metrica;
				break;
			}
		}

		if (issuesCerrados != null && issuesTotales != null && pullRequestsCerrados != null
				&& pullRequestsTotales != null && contribuyentesActivos != null && contribuyentesTotales != null) {

			// Verificamos que los valores totales no sean cero para evitar divisiones por
			// cero
			if (issuesTotales.getValue() == 0 || pullRequestsTotales.getValue() == 0
					|| contribuyentesTotales.getValue() == 0) {
				throw new NotAvailableMetricException("Los valores totales no pueden ser cero.");
			}

			// Calculamos el indicador
			double issuesRatio = (issuesCerrados.getValue() / issuesTotales.getValue()) * 40;
			double pullRequestsRatio = (pullRequestsCerrados.getValue() / pullRequestsTotales.getValue()) * 40;
			double contribuyentesRatio = (contribuyentesActivos.getValue() / contribuyentesTotales.getValue()) * 20;

			double indicador = issuesRatio + pullRequestsRatio + contribuyentesRatio;

			// Criterios de calidad
			if (indicador >= 90) {
				estado = IndicatorState.OK;
			} else if (indicador >= 80) {
				estado = IndicatorState.WARNING;
			} else if (indicador >= 70) {
				estado = IndicatorState.WARNING;
			} else if (indicador >= 50) {
				estado = IndicatorState.CRITICAL;
			} else if (indicador >= 26) {
				estado = IndicatorState.CRITICAL;
			} else {
				estado = IndicatorState.UNDEFINED;
			}

			try {
				indicatorReport = new ReportItem.ReportItemBuilder<Double>("repoActivity", indicador)
						.metrics(Arrays.asList(issuesCerrados, issuesTotales, pullRequestsCerrados, pullRequestsTotales,
								contribuyentesActivos, contribuyentesTotales))
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
