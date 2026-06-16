package us.muit.fs.a4i.control.strategies;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

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

	private static final List<String> REQUIRED_METRICS = Arrays.asList(
			"totalCommitsPerUserLastYear",
			"totalLinesPerUserLastYear"
	);

	@Override
	public ReportItemI<HashMap<String, Double>> calcIndicator(List<ReportItemI<HashMap<String, Double>>> metrics)
			throws NotAvailableMetricException {

		Optional<ReportItemI<HashMap<String, Double>>> totalCommitsPerUserLastYear = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(0).equals(m.getName()))
				.findAny();

		Optional<ReportItemI<HashMap<String, Double>>> totalLinesPerUserLastYear = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(1).equals(m.getName()))
				.findAny();

		if (!totalCommitsPerUserLastYear.isPresent() || !totalLinesPerUserLastYear.isPresent()) {
			log.info("No se han proporcionado las metricas necesarias");
			throw new NotAvailableMetricException(REQUIRED_METRICS.toString());
		}

		HashMap<String, Double> commitsPerUser = totalCommitsPerUserLastYear.get().getValue();
		HashMap<String, Double> linesPerUser = totalLinesPerUserLastYear.get().getValue();

		double entropyValue = calculateDiversityOfContributions(commitsPerUser, linesPerUser);

		HashMap<String, Double> result = new HashMap<>();
		result.put("entropyValue", entropyValue);

		ReportItemI<HashMap<String, Double>> indicatorReport = null;

		try {
			indicatorReport = new ReportItem.ReportItemBuilder<HashMap<String, Double>>(
					"diversityOfContributions",
					result
			)
					.metrics(Arrays.asList(totalCommitsPerUserLastYear.get(), totalLinesPerUserLastYear.get()))
					.indicator(IndicatorState.UNDEFINED)
					.build();

		} catch (ReportItemException e) {
			log.info("Error en ReportItemBuilder.");
			e.printStackTrace();
		}

		return indicatorReport;
	}

	private double calculateDiversityOfContributions(HashMap<String, Double> commitsPerUser,
			HashMap<String, Double> linesPerUser) {

		if (commitsPerUser == null || linesPerUser == null) {
			return 0.0;
		}

		Set<String> users = new HashSet<>();
		users.addAll(commitsPerUser.keySet());
		users.addAll(linesPerUser.keySet());

		if (users.size() <= 1) {
			return 0.0;
		}

		double totalCommits = commitsPerUser.values().stream()
				.mapToDouble(Double::doubleValue)
				.sum();

		double totalLines = linesPerUser.values().stream()
				.mapToDouble(Double::doubleValue)
				.sum();

		if (totalCommits <= 0.0 || totalLines <= 0.0) {
			return 0.0;
		}

		double entropy = 0.0;
		int activeUsers = 0;

		for (String user : users) {
			double commits = commitsPerUser.getOrDefault(user, 0.0);
			double lines = linesPerUser.getOrDefault(user, 0.0);

			double commitContribution = commits / totalCommits;
			double lineContribution = lines / totalLines;
			double userContribution = (commitContribution + lineContribution) / 2.0;

			if (userContribution > 0.0) {
				entropy -= userContribution * Math.log(userContribution);
				activeUsers++;
			}
		}

		if (activeUsers <= 1) {
			return 0.0;
		}

		double normalizedEntropy = entropy / Math.log(activeUsers);

		if (Double.isNaN(normalizedEntropy) || Double.isInfinite(normalizedEntropy)) {
			return 0.0;
		}

		return normalizedEntropy;
	}

	@Override
	public List<String> requiredMetrics() {
		log.info("Métricas requeridas: " + REQUIRED_METRICS);
		return REQUIRED_METRICS;
	}
}