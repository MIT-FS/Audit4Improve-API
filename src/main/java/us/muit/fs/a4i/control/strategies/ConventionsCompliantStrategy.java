/**
 * 
 */
package us.muit.fs.a4i.control.strategies;

/**
 * 
 */
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import java.util.Optional;

import us.muit.fs.a4i.control.IndicatorStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;

public class ConventionsCompliantStrategy implements IndicatorStrategy<Double> {
	// MARK: - Attributes
	private static Logger log = Logger.getLogger(Indicator.class.getName());
	// The metrics needed to calculate the indicator output
	private static final List<String> REQUIRED_METRICS = Arrays.asList("conventionalCommits", "commitsWithDescription",
			"issuesWithLabels", "gitFlowBranches", "conventionalPullRequests");
	public static final String ID = "conventionsCompliant";

	// Weights for each metric
	private double WEIGHT_CONVENTIONAL_COMMITS = 0.2;
	private double WEIGHT_COMMITS_WITH_DESCRIPTION = 0.2;
	private double WEIGHT_ISSUES_WITH_LABELS = 0.2;
	private double WEIGHT_GIT_FLOW_BRANCHES = 0.2;
	private double WEIGHT_CONVENTIONAL_PULL_REQUESTS = 0.2;

	// MARK: - Constructor
	/**
	 * Constructor. Default weights are equal for all metrics (0.2).
	 */
	public ConventionsCompliantStrategy() {
		super();
	}

	/**
	 * Constructor. Set the weights for each metric. The sum of the weights must be
	 * equal to 1.
	 * 
	 * @param weightConventionalCommits      the weight for the conventional commits
	 *                                       metric
	 * @param weightCommitsWithDescription   the weight for the commits with
	 *                                       description metric
	 * @param weightIssuesWithLabels         the weight for the issues with labels
	 *                                       metric
	 * @param weightGitFlowBranches          the weight for the git flow branches
	 *                                       metric
	 * @param weightConventionalPullRequests the weight for the conventional pull
	 *                                       requests metric
	 */
	public ConventionsCompliantStrategy(Double weightConventionalCommits, Double weightCommitsWithDescription,
			Double weightIssuesWithLabels, Double weightGitFlowBranches, Double weightConventionalPullRequests)
			throws IllegalArgumentException {
		// Control that the sum of the weights is equal to 1
		if (weightConventionalCommits + weightCommitsWithDescription + weightIssuesWithLabels + weightGitFlowBranches
				+ weightConventionalPullRequests != 1) {
			throw new IllegalArgumentException("The sum of the weights must be equal to 1");
		}

		// Set the weights
		this.WEIGHT_CONVENTIONAL_COMMITS = weightConventionalCommits;
		this.WEIGHT_COMMITS_WITH_DESCRIPTION = weightCommitsWithDescription;
		this.WEIGHT_ISSUES_WITH_LABELS = weightIssuesWithLabels;
		this.WEIGHT_GIT_FLOW_BRANCHES = weightGitFlowBranches;
		this.WEIGHT_CONVENTIONAL_PULL_REQUESTS = weightConventionalPullRequests;
	}

	// MARK: - Implemented methods
	// Calculate the indicator output based on the provided metrics
	@Override
	public ReportItemI<Double> calcIndicator(List<ReportItemI<Double>> metrics) throws NotAvailableMetricException {
		// Attributes
		Optional<ReportItemI<Double>> conventionalCommits = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(0).equals(m.getName())).findAny();
		Optional<ReportItemI<Double>> commitsWithDescription = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(1).equals(m.getName())).findAny();
		Optional<ReportItemI<Double>> issuesWithLabels = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(2).equals(m.getName())).findAny();
		Optional<ReportItemI<Double>> gitFlowBranches = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(3).equals(m.getName())).findAny();
		Optional<ReportItemI<Double>> conventionalPullRequests = metrics.stream()
				.filter(m -> REQUIRED_METRICS.get(4).equals(m.getName())).findAny();
		ReportItemI<Double> indicatorReport = null;

		// Check if the required metrics are present
		if (conventionalCommits.isPresent() && commitsWithDescription.isPresent() && issuesWithLabels.isPresent()
				&& gitFlowBranches.isPresent() && conventionalPullRequests.isPresent()) {
			// Calculate the indicator
			Double metric1, metric2, metric3, metric4, metric5;
			// Initialize the metrics
			if (conventionalCommits.get().getValue() >= 0.8499)
				metric1 = conventionalCommits.get().getValue();
			else
				metric1 = 0.0;
			if (commitsWithDescription.get().getValue() >= 0.8499)
				metric2 = commitsWithDescription.get().getValue();
			else
				metric2 = 0.0;
			if (issuesWithLabels.get().getValue() >= 0.8499)
				metric3 = issuesWithLabels.get().getValue();
			else
				metric3 = 0.0;
			if (gitFlowBranches.get().getValue() >= 0.999)
				metric4 = 1.0;
			else
				metric4 = 0.0;
			if (conventionalPullRequests.get().getValue() >= 0.8499)
				metric5 = conventionalPullRequests.get().getValue();
			else
				metric5 = 0.0;

			Double indicatorValue = WEIGHT_CONVENTIONAL_COMMITS * metric1 + WEIGHT_COMMITS_WITH_DESCRIPTION * metric2
					+ WEIGHT_ISSUES_WITH_LABELS * metric3 + WEIGHT_GIT_FLOW_BRANCHES * metric4
					+ WEIGHT_CONVENTIONAL_PULL_REQUESTS * metric5;

			try {
				// Create the indicator
				indicatorReport = new ReportItem.ReportItemBuilder<Double>(ID, indicatorValue)
						.metrics(Arrays.asList(conventionalCommits.get(), commitsWithDescription.get(),
								issuesWithLabels.get(), gitFlowBranches.get(), conventionalPullRequests.get()))
						.indicator(IndicatorState.UNDEFINED).build();
			} catch (ReportItemException e) {
				log.info("Error en ReportItemBuilder.");
				e.printStackTrace();
			}

		} else {
			log.info("No se han proporcionado las métricas necesarias");
			throw new NotAvailableMetricException(REQUIRED_METRICS.toString());
		}

		// Temporarily throw
		return indicatorReport;
	}

	// Return the metrics required to calculate the indicator output
	@Override
	public List<String> requiredMetrics() {
		return REQUIRED_METRICS;
	}

}
