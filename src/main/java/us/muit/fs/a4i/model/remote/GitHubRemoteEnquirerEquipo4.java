package us.muit.fs.a4i.model.remote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.InputStream;
import java.util.Properties;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.model.entities.Report;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItemI;

import us.muit.fs.a4i.model.entities.impl.*;

public class GitHubRemoteEnquirerEquipo4 implements RemoteEnquirer {

    // Nombres de métricas
    private static final String M1_NAME = "issue_response_time";
    private static final String M2_NAME = "issues_without_response_ratio";
    private static final String M3_NAME = "resolved_under_48h_ratio";

    private GitHub github;
    private static Properties properties = new Properties();

    static {
        try (InputStream input = GitHubRemoteEnquirerEquipo4.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("No se encontró el archivo config.properties en el classpath");
            } else {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Error cargando config.properties: " + e.getMessage());
        }
    }

    public GitHubRemoteEnquirerEquipo4() throws IOException {
        String token = properties.getProperty("github.token");
        String apiUrl = properties.getProperty("github.api.url", "https://api.github.com");

        if (token == null || token.isEmpty()) {
            throw new IOException("Token de GitHub no configurado en config.properties");
        }
        github = new GitHubBuilder().withEndpoint(apiUrl).withOAuthToken(token).build();
    }

    @Override
    public ReportI buildReport(String entityId) {
        Report report = new Report(ReportI.ReportType.REPOSITORY, entityId);
        try {
            report.addMetric(getMetric(M1_NAME, entityId));
            report.addMetric(getMetric(M2_NAME, entityId));
            report.addMetric(getMetric(M3_NAME, entityId));
        } catch (MetricException e) {
            System.err.println("Error al obtener las métricas: " + e.getMessage());
        }
        return report;
    }

    @Override
    public ReportItemI getMetric(String metricName, String entityId) throws MetricException {
        try {
            switch (metricName) {
                case M1_NAME:
                    return new SimpleReportItem(M1_NAME, calcularM1(entityId));
                case M2_NAME:
                    return new SimpleReportItem(M2_NAME, calcularM2(entityId));
                case M3_NAME:
                    return new SimpleReportItem(M3_NAME, calcularM3(entityId));
                default:
                    throw new MetricException("Métrica no soportada: " + metricName);
            }
        } catch (IOException e) {
            throw new MetricException("Error al conectar con GitHub: " + e.getMessage());
        }
    }

    private double calcularM1(String repoFullName) throws IOException {
        // Tiempo medio de respuesta en horas, máximo 72
        GHRepository repo = github.getRepository(repoFullName);
        int totalIssues = 0;
        double totalResponseTimeHours = 0;

        // Recorremos issues abiertas y cerradas
        for (GHIssue issue : repo.getIssues(GHIssueState.ALL)) {
            totalIssues++;

            List<GHIssueComment> comments = issue.getComments();
            if (!comments.isEmpty()) {
                // Tiempo desde apertura hasta primer comentario
                long respuestaMillis = comments.get(0).getCreatedAt().getTime() - issue.getCreatedAt().getTime();
                double respuestaHoras = respuestaMillis / (1000.0 * 60 * 60);
                totalResponseTimeHours += respuestaHoras;
            } else {
                // Sin respuesta, considerar 72 horas para cálculo
                totalResponseTimeHours += 72;
            }
        }

        if (totalIssues == 0) return 1.0; // Sin issues = máximo índice

        double promedioHoras = totalResponseTimeHours / totalIssues;

        return promedioHoras <= 72 ? 1 - (promedioHoras / 72) : 0.0;
    }

    private double calcularM2(String repoFullName) throws IOException {
        GHRepository repo = github.getRepository(repoFullName);
        int sinRespuesta = 0;
        int totalIssues = 0;

        for (GHIssue issue : repo.getIssues(GHIssueState.OPEN)) {
            totalIssues++;
            if (issue.getComments().isEmpty()) {
                sinRespuesta++;
            }
        }
        if (totalIssues == 0) return 1.0; // Si no hay issues abiertos, ratio máximo
        return 1.0 - ((double) sinRespuesta / totalIssues);
    }

    private double calcularM3(String repoFullName) throws IOException {
        GHRepository repo = github.getRepository(repoFullName);
        int cerrados = 0;
        int resueltos48h = 0;

        for (GHIssue issue : repo.getIssues(GHIssueState.CLOSED)) {
            cerrados++;
            long diffMillis = issue.getClosedAt().getTime() - issue.getCreatedAt().getTime();
            double horas = diffMillis / (1000.0 * 60 * 60);
            if (horas <= 48) {
                resueltos48h++;
            }
        }

        if (cerrados == 0) return 1.0; // Si no hay cerrados, ratio máximo
        return (double) resueltos48h / cerrados;
    }

    @Override
    public List<String> getAvailableMetrics() {
        List<String> metrics = new ArrayList<>();
        metrics.add(M1_NAME);
        metrics.add(M2_NAME);
        metrics.add(M3_NAME);
        return metrics;
    }

    @Override
    public RemoteType getRemoteType() {
        return RemoteType.GITHUB;
    }
}