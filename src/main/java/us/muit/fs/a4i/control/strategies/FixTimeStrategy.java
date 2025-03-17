/**
 * 
 */
package us.muit.fs.a4i.control.strategies;

/**
 * 
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

/**
 * Estrategia de indicador equipo 6 del curso 23/24, no coincide con lo que
 * decían ni las métricas están incluidas en ningún Enquirer Aparece una clase
 * Equirer MetricasG6.java pero es una copia incompleta del que se les da y no
 * añade los indicadores que aquí aparecen 
 * REMEMBER: metrics and indicators must be included in a4iDefault.json
 */

public class FixTimeStrategy implements IndicatorStrategy<Double> {

	private static Logger log = Logger.getLogger(Indicator.class.getName());

	// M�tricas necesarias para calcular el indicador
	private static final List<String> REQUIRED_METRICS = Arrays.asList("clasificacion", "correccion", "num_errores");

	@Override
	public ReportItemI<Double> calcIndicator(List<ReportItemI<Double>> metrics) throws NotAvailableMetricException {
		// Se obtienen y se comprueba que se pasan las m�tricas necesarias para calcular
		// el indicador.
		Optional<ReportItemI<Double>> clasificacion = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(0).equals(m.getName())).findAny();
		Optional<ReportItemI<Double>> correccion = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(1).equals(m.getName())).findAny();
		Optional<ReportItemI<Double>> num_errores = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(2).equals(m.getName())).findAny();
		ReportItemI<Double> indicatorReport = null;

		if (clasificacion.isPresent() && correccion.isPresent() && num_errores.isPresent()) {
			Double procesoDeIssues;

			// Se realiza el c�lculo del indicador
			if (clasificacion.get().getValue() != 0 && correccion.get().getValue() != 0
					&& num_errores.get().getValue() != 0)
				procesoDeIssues = (clasificacion.get().getValue() + correccion.get().getValue())
						/ num_errores.get().getValue();
			else
				procesoDeIssues = 0.0;

			try {
				// Se crea el indicador
				indicatorReport = new ReportItem.ReportItemBuilder<Double>("fixTime", procesoDeIssues)
						.metrics(Arrays.asList(clasificacion.get(), correccion.get(), num_errores.get()))
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