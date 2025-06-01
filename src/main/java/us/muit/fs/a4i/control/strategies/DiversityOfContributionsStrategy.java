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
import java.util.HashMap;
import java.util.ArrayList;


import us.muit.fs.a4i.control.IndicatorStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;

/**
 * REMEMBER: metrics and indicators must be included in a4iDefault.json
 */

public class DiversityOfContributionsStrategy implements IndicatorStrategy<Object> {

	private static Logger log = Logger.getLogger(Indicator.class.getName());

	// Métricas necesarias para calcular el indicador
	private static final List<String> REQUIRED_METRICS = Arrays.asList("TotalCommitsLastMonth", "TotalCommitsPerUserLastMonth", 
																		"TotalLinesLastMonth", "TotalLinesPerUserLastMonth");

	@Override
	public ReportItemI<Object> calcIndicator(List<ReportItemI<Object>> metrics) throws NotAvailableMetricException {
	    List<ReportItemI<?>> mixedMetrics = (List<ReportItemI<?>>) (List<?>) metrics;

		Optional<ReportItemI<Integer>> totalCommitsLastMonth = mixedMetrics.stream()
				.filter(m -> REQUIRED_METRICS.get(0).equals(m.getName()) && m.getValue() instanceof Integer)
				.map(m -> (ReportItemI<Integer>) m).findAny();

	    Optional<ReportItemI<HashMap<String, Integer>>> totalCommitsPerUserLastMonth = mixedMetrics.stream()
	        .filter(m -> REQUIRED_METRICS.get(1).equals(m.getName()))
	        .map(m -> (ReportItemI<HashMap<String, Integer>>) m)
	        .findAny();

		Optional<ReportItemI<Double>> totalLinesLastMonth = mixedMetrics.stream()
				.filter(m -> REQUIRED_METRICS.get(2).equals(m.getName()) && m.getValue() instanceof Double)
				.map(m -> (ReportItemI<Double>) m).findAny();

	    Optional<ReportItemI<HashMap<String, Integer>>> totalLinesPerUserLastMonth = mixedMetrics.stream()
	        .filter(m -> REQUIRED_METRICS.get(3).equals(m.getName()))
	        .map(m -> (ReportItemI<HashMap<String, Integer>>) m)
	        .findAny();
        
		ReportItemI<?> indicatorReport = null;
		
		Double probContributionUserN = 0.0;
		List<Double> probContributionUsers = new ArrayList<Double>();
		Double probContribution = 0.0;
		Double entropyValue = 0.0;

		if (totalCommitsLastMonth.isPresent() && totalCommitsPerUserLastMonth.isPresent() && 
			totalLinesLastMonth.isPresent() && totalLinesPerUserLastMonth.isPresent()) {
		
			// Se realiza el calculo del indicador

			if (totalCommitsLastMonth.get().getValue() >= 0 && !totalCommitsPerUserLastMonth.get().getValue().isEmpty()
					&& totalLinesLastMonth.get().getValue() >= 0 && !totalLinesPerUserLastMonth.get().getValue().isEmpty()) {
				
				// Calculamos el número de lineas de código modificadas por usuario partido del número de lineas modificadas totales
				// y el número de commits por usuario partido del número de commits totales, recorriendo los hash maps de la misma longitud
				for (int i = 0; i < totalCommitsPerUserLastMonth.get().getValue().size(); i++) {
					String user = (String) totalCommitsPerUserLastMonth.get().getValue().keySet().toArray()[i];
					Integer commits = totalCommitsPerUserLastMonth.get().getValue().get(user);
					Integer lines = totalLinesPerUserLastMonth.get().getValue().get(user);

					// Evitamos la división por cero
					if (commits != 0 && lines != 0) {
						probContributionUserN = (double) commits / totalCommitsLastMonth.get().getValue() + 
								(double) lines / totalLinesLastMonth.get().getValue();
						probContributionUsers.add(probContributionUserN);
					}
				}
	            
				// Calculamos el indicador DiversityOfContributions
			    for (Double probUser : probContributionUsers) {
                    probContribution -= probUser * Math.log(probUser);
                }
			    
			    entropyValue = probContribution / Math.log(probContributionUsers.size());
			}
			
			else
				entropyValue = 0.0;

			try {
				// Se crea el indicador
				indicatorReport = new ReportItem.ReportItemBuilder<Double>("diversityOfContributions", 
						entropyValue)
						.metrics(Arrays.asList(totalCommitsLastMonth.get(), 
								totalCommitsPerUserLastMonth.get(), 
								totalLinesLastMonth.get(),
								totalLinesPerUserLastMonth.get()))
						.indicator(IndicatorState.UNDEFINED).build();
			} catch (ReportItemException e) {
				log.info("Error en ReportItemBuilder.");
				e.printStackTrace();
			}

		}
		else {
			log.info("No se han proporcionado las metricas necesarias");
			throw new NotAvailableMetricException(REQUIRED_METRICS.toString());
		}

		return (ReportItem<Object>) indicatorReport;
	}

	@Override
	public List<String> requiredMetrics() {
		// Para calcular el indicador DiversityOfContributionsStrategy se requieren las siguientes métricas:
		// - TotalCommitsLastMonth
		// - TotalCommitsPerUserLastMonth
		// - TotalLinesLastMonth
		// - TotalLinesPerUserLastMonth
		log.info("Métricas requeridas: " + REQUIRED_METRICS);
		return REQUIRED_METRICS;
	}
}