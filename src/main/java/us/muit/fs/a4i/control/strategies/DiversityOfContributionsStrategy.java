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
public class DiversityOfContributionsStrategy implements IndicatorStrategy<HashMap<String, Double>> {

	private static Logger log = Logger.getLogger(Indicator.class.getName());

	// Métricas necesarias para calcular el indicador
	private static final List<String> REQUIRED_METRICS = Arrays.asList("TotalCommitsPerUserLastMonth", 
																	   "TotalLinesPerUserLastMonth");

	@Override
	public ReportItemI<HashMap<String, Double>> calcIndicator(List<ReportItemI<HashMap<String, Double>>> metrics) throws NotAvailableMetricException {

	    Optional<ReportItemI<HashMap<String, Double>>> totalCommitsPerUserLastMonth = metrics.stream()
	        .filter(m -> REQUIRED_METRICS.get(0).equals(m.getName()))
	        .map(m -> (ReportItemI<HashMap<String, Double>>) m)
	        .findAny();

	    Optional<ReportItemI<HashMap<String, Double>>> totalLinesPerUserLastMonth = metrics.stream()
	        .filter(m -> REQUIRED_METRICS.get(1).equals(m.getName()))
	        .map(m -> (ReportItemI<HashMap<String, Double>>) m)
	        .findAny();
        
		ReportItemI<HashMap<String, Double>> indicatorReport = null;
		
		Double probContributionUserN = 0.0;
		List<Double> probContributionUsers = new ArrayList<Double>();
		Double probContribution = 0.0;
		Double entropyValue = 0.0;
		Integer totalCommitsLastMonth = 0;
		Integer totalLinesLastMonth = 0;

		if (totalCommitsPerUserLastMonth.isPresent() && 
			totalLinesPerUserLastMonth.isPresent()) {
		
			// Se realiza el calculo del indicador

			if (!totalCommitsPerUserLastMonth.get().getValue().isEmpty() && 
				!totalLinesPerUserLastMonth.get().getValue().isEmpty()) {
				
				// Calculamos el número de lineas de código modificadas por usuario partido del número de lineas modificadas totales
				// y el número de commits por usuario partido del número de commits totales, recorriendo los hash maps de la misma longitud
				// Antes de bucle, se sacan todos los commits y líneas totales del mes pasado
				
				totalCommitsLastMonth = totalCommitsPerUserLastMonth.get().getValue().values().stream().mapToInt(Double::intValue).sum();
				totalLinesLastMonth = totalLinesPerUserLastMonth.get().getValue().values().stream().mapToInt(Double::intValue).sum();
				
				for (int i = 0; i < totalCommitsPerUserLastMonth.get().getValue().size(); i++) {
					String user = (String) totalCommitsPerUserLastMonth.get().getValue().keySet().toArray()[i];
					Double commits = totalCommitsPerUserLastMonth.get().getValue().get(user);
					Double lines = totalLinesPerUserLastMonth.get().getValue().get(user);

					// Evitamos la división por cero
					if (commits != 0 && lines != 0) {
						probContributionUserN = (double) commits / totalCommitsLastMonth + 
								(double) lines / totalLinesLastMonth;
						probContributionUsers.add(probContributionUserN);
					}
				}
	            
				// Calculamos el indicador DiversityOfContributions
			    for (Double probUser : probContributionUsers) {
                    probContribution -= probUser * Math.log(probUser);
                }
			    
			    entropyValue = probContribution / Math.log(probContributionUsers.size());
			    HashMap<String, Double> result = new HashMap<>();
			    result.put("entropyValue", entropyValue.doubleValue());
			}
			
			else
				entropyValue = 0.0;
				HashMap<String, Double> result = new HashMap<>();
				result.put("entropyValue", entropyValue.doubleValue());

			try {
				// Se crea el indicador
				indicatorReport = new ReportItem.ReportItemBuilder<HashMap<String, Double>>("diversityOfContributions", result)
						.metrics(Arrays.asList(totalCommitsPerUserLastMonth.get(), totalLinesPerUserLastMonth.get()))
						.indicator(IndicatorState.UNDEFINED)
						.build();
				
			} catch (ReportItemException e) {
				log.info("Error en ReportItemBuilder.");
				e.printStackTrace();
			}

		}
		else {
			log.info("No se han proporcionado las metricas necesarias");
			throw new NotAvailableMetricException(REQUIRED_METRICS.toString());
		}

		return indicatorReport;
	}

	@Override
	public List<String> requiredMetrics() {
		// Para calcular el indicador DiversityOfContributionsStrategy se requieren las siguientes métricas:
		// - TotalCommitsPerUserLastMonth
		// - TotalLinesPerUserLastMonth
		log.info("Métricas requeridas: " + REQUIRED_METRICS);
		return REQUIRED_METRICS;
	}
}