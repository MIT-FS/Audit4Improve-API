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

public class IndicatorPullRequest implements IndicatorStrategy<Double> {

    private static final Logger log = Logger.getLogger(IndicatorPullRequest.class.getName());

    private static final List<String> REQUIRED_METRICS =
            Arrays.asList("totalPullReq", "closedPullReq");

    @Override
    public ReportItem<Double> calcIndicator(List<ReportItemI<Double>> metrics)
            throws NotAvailableMetricException {

        Optional<ReportItemI<Double>> totalPullReq = metrics.stream()
                .filter(m -> REQUIRED_METRICS.get(0).equals(m.getName()))
                .findAny();

        Optional<ReportItemI<Double>> closedPullReq = metrics.stream()
                .filter(m -> REQUIRED_METRICS.get(1).equals(m.getName()))
                .findAny();

        if (totalPullReq.isEmpty() || closedPullReq.isEmpty()) {
            log.info("Some required metrics are missing");
            throw new NotAvailableMetricException(REQUIRED_METRICS.toString());
        }

        double total = totalPullReq.get().getValue();
        double closed = closedPullReq.get().getValue();

        double pullRequestIndicator = calculateEfficiency(closed, total);

        IndicatorState estado;

        if (pullRequestIndicator > 75) {
            estado = IndicatorState.OK;
        } else if (pullRequestIndicator > 50) {
            estado = IndicatorState.WARNING;
        } else {
            estado = IndicatorState.CRITICAL;
        }

        try {
            return new ReportItem.ReportItemBuilder<Double>(
                    "pullRequestIndicator",
                    pullRequestIndicator
            )
                    .metrics(Arrays.asList(totalPullReq.get(), closedPullReq.get()))
                    .indicator(estado)
                    .build();

        } catch (ReportItemException e) {
            throw new NotAvailableMetricException(e.getMessage());
        }
    }

    public double calculateEfficiency(double closedPullRequests, double totalPullRequests) {

        if (closedPullRequests < 0 || totalPullRequests < 0) {
            throw new IllegalArgumentException("Pull request values cannot be negative");
        }

        if (closedPullRequests > totalPullRequests) {
            throw new IllegalArgumentException("Closed PRs cannot exceed total PRs");
        }

        if (totalPullRequests == 0) {
            return 0.0;
        }

        return 100.0 * closedPullRequests / totalPullRequests;
    }

    public String evaluateQualityLevel(double efficiency) {

        if (efficiency > 75) {
            return "Correcto";
        } else if (efficiency > 50) {
            return "Precaución";
        } else {
            return "Crítico";
        }
    }

    @Override
    public List<String> requiredMetrics() {
        return REQUIRED_METRICS;
    }
}