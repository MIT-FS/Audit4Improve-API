package us.muit.fs.a4i.model.remote;

import us.muit.fs.a4i.model.remote.RemoteEnquirer;
import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.Report;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItem.ReportItemBuilder;
import us.muit.fs.a4i.model.entities.ReportItemI;


import org.kohsuke.github.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitHubRemoteEnquirer implements RemoteEnquirer {

    private static final List<String> AVAILABLE_METRICS = List.of(
            "reopenedIssuesAvg",
            "firstTryResolutionRate",
            "postClosureActivityRate"
    );

    private final GitHub github;
    
    public GitHubRemoteEnquirer() throws IOException {
    	String token = System.getenv("GITHUB_OAUTH");
    	if (token == null || token.isEmpty()) {
    	    token = System.getenv("GITHUB_TOKEN");
    	}
    	if (token == null || token.isEmpty()) {
    	    throw new IllegalStateException("No GitHub token provided.");
    	}
    	this.github = new GitHubBuilder().withOAuthToken(token).build();

    }

    @Override
    public ReportI buildReport(String repoFullName) {
        Report report = new Report(repoFullName);
        for (String metric : AVAILABLE_METRICS) {
            try {
                report.addMetric(getMetric(metric, repoFullName));
            } catch (MetricException e) {
                System.err.println("Error al obtener la métrica: " + metric);
            }
        }
        return report;
    }

    @Override
    public ReportItemI getMetric(String metricName, String repoFullName) throws MetricException {
        try {
            GHRepository repo = github.getRepository(repoFullName);
            List<GHIssue> issues = repo.getIssues(GHIssueState.CLOSED);
            int reopenedCount = 0;
            int firstTrySuccess = 0;
            int postClosureActivity = 0;

            for (GHIssue issue : issues) {
                boolean reopened = false;
                for (GHIssueEvent event : issue.listEvents()) {
                    if (event.getEvent().equals("reopened")) {
                        reopened = true;
                        reopenedCount++;
                        break;
                    }
                }

                if (!reopened) {
                    firstTrySuccess++;
                }

                if (issue.getUpdatedAt().after(issue.getClosedAt())) {
                    postClosureActivity++;
                }
            }

            int totalIssues = issues.size();
            if (totalIssues == 0) throw new MetricException("No hay issues cerrados para analizar.");

            double avgReopened = (double) reopenedCount / totalIssues;
            double trpi = ((double) firstTrySuccess / totalIssues) * 100;
            double pcap = ((double) postClosureActivity / totalIssues) * 100;
         
            try {
                switch (metricName) {
                    case "reopenedIssuesAvg":
                        return new ReportItemBuilder<Double>(metricName, avgReopened)
                                .source("GitHub")
                                .build();

                    case "firstTryResolutionRate":
                        return new ReportItemBuilder<Double>(metricName, trpi)
                                .source("GitHub")
                                .build();

                    case "postClosureActivityRate":
                        return new ReportItemBuilder<Double>(metricName, pcap)
                                .source("GitHub")
                                .build();

                    default:
                        throw new MetricException("Métrica no definida: " + metricName);
                }

            } catch (ReportItemException e) {
                throw new MetricException("Error al construir el ReportItem para " + metricName + ": " + e.getMessage());
            }

        } catch (IOException e) {
            throw new MetricException("Error al procesar la métrica: " + e.getMessage());
        }
    }

    @Override
    public List<String> getAvailableMetrics() {
        return new ArrayList<>(AVAILABLE_METRICS);
    }

    @Override
    public RemoteType getRemoteType() {
        return RemoteType.GITHUB;
    }
}
