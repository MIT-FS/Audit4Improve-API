package us.muit.fs.a4i.model.remote;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.model.entities.ReportItem.ReportItemBuilder;
import us.muit.fs.a4i.model.entities.Report;
import us.muit.fs.a4i.model.entities.ReportI;


public class ExtraccionMetricas implements RemoteEnquirer {
	
    private static final List<String> SUPPORTED_METRICS = Arrays.asList("totalIssues", "labeledIssues");

	@Override
	public ReportI buildReport(String entityId) {
        try {
            Report report = new Report(ReportI.ReportType.REPOSITORY, entityId);

            for (String metric : getAvailableMetrics()) {
                ReportItemI<?> item = getMetric(metric, entityId);
                report.addMetric(item);
            }

            return report;

        } catch (MetricException e) {
            throw new RuntimeException("Error al construir el informe: " + e.getMessage(), e);
        }
	}

	@Override
    public ReportItemI getMetric(String metricName, String entityId) throws MetricException {
        if (!SUPPORTED_METRICS.contains(metricName)) {
            throw new MetricException("Métrica no soportada: " + metricName);
        }

        if (!entityId.contains("/")) {
            entityId = entityId + "/Audit4Improve-API";
        }
        
        try {
        	GitHub github;
        	String token = System.getenv("GITHUB_PACKAGES");

        	if (token != null && !token.isEmpty()) {
        	    github = new GitHubBuilder().withOAuthToken(token).build();
        	} else {
        	    github = GitHub.connectAnonymously();
        	} 
            GHRepository repo = github.getRepository(entityId);

            int total = 0;
            int etiquetados = 0;

            for (GHIssue issue : repo.getIssues(org.kohsuke.github.GHIssueState.OPEN)) {
                if (!issue.isPullRequest()) {
                    total++;
                    if (!issue.getLabels().isEmpty()) {
                        etiquetados++;
                    }
                }
            }

            if (metricName.equals("totalIssues")) {
                return new ReportItemBuilder<Double>("totalIssues", (double) total)
                        .source("GitHub")
                        .build();
            } else if (metricName.equals("labeledIssues")) {
                return new ReportItemBuilder<Double>("labeledIssues", (double) etiquetados)
                        .source("GitHub")
                        .build();
            } else {
                throw new MetricException("Métrica desconocida: " + metricName);
            }

        } catch (IOException e) {
        	System.err.println("IOException: " + e.getMessage());
            throw new MetricException("Error al conectar con GitHub: " + e.getMessage());
        } catch (Exception e) {
        	System.err.println("Exception: " + e.getMessage());
            throw new MetricException("Error al construir ReportItem: " + e.getMessage());
        }
    }

	@Override
	public List<String> getAvailableMetrics() {
		return SUPPORTED_METRICS;
	}

	@Override
	public RemoteType getRemoteType() {
		return RemoteType.GITHUB;
	}

}
