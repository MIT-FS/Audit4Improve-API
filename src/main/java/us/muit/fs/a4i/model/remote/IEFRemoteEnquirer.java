package us.muit.fs.a4i.model.remote;

import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.Report;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;
import org.kohsuke.github.*;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * RemoteEnquirer para extraer las métricas Kanban–Scrum de un repositorio GitHub.
 *
 * Métricas:
 * 1) cycleTime  – Tiempo medio (h) desde que una tarea está en "Sprint Backlog/Pendiente"
 *                  (punto de partida) hasta su cierre.
 * 2) waitTime   – Tiempo medio (h) que una tarea permanece en estados previos a "En progreso".
 * 3) throughput – Número de tareas cerradas en la última semana (tareas/semana).
 * 4) WIP        – Promedio de tareas abiertas en las cuatro etapas activas del tablero.
 */
public class IEFRemoteEnquirer implements RemoteEnquirer {

    private final GitHub github;

    private ReportItemI safeBuildReportItem(String metricName, Object value) {
        try {
            // Creamos el builder con el tipo correcto
            ReportItem.ReportItemBuilder<?> builder = new ReportItem.ReportItemBuilder<>(metricName, value);
            return builder.build();
        } catch (ReportItemException e) {
            throw new RuntimeException("Error creando ReportItem", e);
        }
    }    
    public IEFRemoteEnquirer(GitHub github) {
        this.github = github;
    }

    @Override
    public ReportI buildReport(String entityId) {
        // Construye un informe añadiendo cada métrica soportada
        ReportI report = new Report();
        for (String metric : getAvailableMetrics()) {
            try {
                report.addMetric(getMetric(metric, entityId));
            } catch (MetricException e) {
                System.err.println("Error al añadir la métrica '" + metric + "': " + e.getMessage());
            }
        }
        return report;
    }

    @Override
    public ReportItemI getMetric(String metricName, String entityId) throws MetricException {
        try {
            GHRepository repo = github.getRepository(entityId);
            Instant now = Instant.now();
            Instant oneWeekAgo = now.minus(Duration.ofDays(7));

            switch (metricName) {
                case "throughput":
                case "waitTime":
                case "cycleTime": {
                    // Listado de todas las issues cerradas
                    PagedIterable<GHIssue> closedIssues = repo.listIssues(GHIssueState.CLOSED);
                    int count = 0;
                    double totalWait = 0, totalCycle = 0;

                    for (GHIssue issue : closedIssues.toList()) {
                        Instant closedAt = issue.getClosedAt().toInstant();
                        // Sólo consideramos las cerradas durante la última semana para throughput
                        if (closedAt.isBefore(oneWeekAgo)) continue;
                        count++;

                        // Fecha de creación: corresponde a entrada en Sprint Backlog/Pendiente
                        Instant createdAt = issue.getCreatedAt().toInstant();

                        // Calcular waitTime: desde creación hasta etiquetado "En progreso"
                        Instant enterProgress = createdAt;
                        for (GHIssueEvent ev : issue.listEvents().toList()) {
                            if ("labeled".equals(ev.getEvent())
                                && "En progreso".equals(ev.getLabel().getName())) {
                                enterProgress = ev.getCreatedAt().toInstant();
                                break;
                            }
                        }
                        totalWait += Duration.between(createdAt, enterProgress).toHours();

                        // Calcular cycleTime: ahora es desde creación (Sprint Backlog) hasta cierre
                        totalCycle += Duration.between(createdAt, closedAt).toHours();
                    }
double value;
                    if ("throughput".equals(metricName)) {
                        // throughput = número de issues cerradas en la última semana
                        value = count;
                    } else if ("waitTime".equals(metricName)) {
                        // waitTime = tiempo medio en espera antes de entrar en progreso
                        value = count > 0 ? totalWait / count : 0;
                    } else {
                        // cycleTime = tiempo medio desde backlog (creación) hasta cierre
                        value = count > 0 ? totalCycle / count : 0;
                    }
                    return safeBuildReportItem(metricName, value);

                }

                case "WIP": {
                    // WIP = promedio de tareas abiertas en las cuatro etapas activas
                    PagedIterable<GHIssue> openIssues = repo.listIssues(GHIssueState.OPEN);
                    int backlog=0, inProgress=0, reviewPend=0, inReview=0;

                    for (GHIssue issue : openIssues.toList()) {
                        for (GHLabel lbl : issue.getLabels()) {
                            switch (lbl.getName()) {
                                case "Sprint Backlog/Pendiente": backlog++;     break;
                                case "En progreso":             inProgress++; break;
                                case "Pendiente de Revisión":   reviewPend++; break;
                                case "En revisión":             inReview++;   break;
                            }
                        }
                    }
                    double wipValue = (backlog + inProgress + reviewPend + inReview) / 4.0;
                    return safeBuildReportItem("WIP", wipValue);
                }

                default:
                    throw new MetricException("Métrica no soportada: " + metricName);
            }

        } catch (IOException e) {
        	throw new MetricException("Error consultando GitHub: " + e.getMessage());

        }
    }

    @Override
    public List<String> getAvailableMetrics() {
        // Definimos las métricas que este RemoteEnquirer puede consultar
        return List.of("cycleTime", "waitTime", "throughput", "WIP");
    }

    @Override
    public RemoteType getRemoteType() {
        return RemoteType.GITHUB;
    }
}
