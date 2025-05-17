package us.muit.fs.a4i.test.model.remote;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kohsuke.github.GitHub;

import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.model.remote.IEFRemoteEnquirer;

/**
 * Test de integración para IEFRemoteEnquirer,
 * consulta las métricas Kanban–Scrum directamente en GitHub.
 */
class IEFRemoteEnquirerTest {
        private static final Logger log = Logger.getLogger(IEFRemoteEnquirerTest.class.getName());

    private static final String REPO = "MIT-FS/Audit4Improve-API-G10";

    private GitHub github;
    private IEFRemoteEnquirer ghEnquirer;

    @BeforeEach
    void setUp() throws IOException {
        // Asume que tienes la variable de entorno GITHUB_TOKEN con el token de acceso
        String token = System.getenv("GITHUB_TOKEN");
        github = GitHub.connectUsingOAuth(token);
        ghEnquirer = new IEFRemoteEnquirer(github);
    }

    @Test
    @DisplayName("cycleTime: media horas desde backlog hasta cierre")
    void testCycleTime() throws MetricException {
        ReportItem<Double> metric = (ReportItem<Double>) ghEnquirer.getMetric("cycleTime", REPO);
        assertEquals("cycleTime", metric.getName(), "El nombre debe ser cycleTime");
        double value = metric.getValue();
        log.info("cycleTime = " + value + "h");
        // Debe ser al menos 0
        assertTrue(value >= 0, "cycleTime debe ser >= 0");
        assertNotNull(metric.getDescription(), "Debe tener descripción");
    }

    @Test
    @DisplayName("waitTime: media horas hasta entrar en progreso")
    void testWaitTime() throws MetricException {
        ReportItem<Double> metric = (ReportItem<Double>) ghEnquirer.getMetric("waitTime", REPO);
        assertEquals("waitTime", metric.getName(), "El nombre debe ser waitTime");
        double value = metric.getValue();
        log.info("waitTime = " + value + "h");
        assertTrue(value >= 0, "waitTime debe ser >= 0");
        assertNotNull(metric.getDescription(), "Debe tener descripción");
    }

    @Test
    @DisplayName("throughput: tareas cerradas última semana")
    void testThroughput() throws MetricException {
        ReportItem<Double> metric = (ReportItem<Double>) ghEnquirer.getMetric("throughput", REPO);
        assertEquals("throughput", metric.getName(), "El nombre debe ser throughput");
        double value = metric.getValue();
        log.info("throughput = " + value + " tareas/semana");
        assertTrue(value >= 0, "throughput debe ser >= 0");
        assertNotNull(metric.getDescription(), "Debe tener descripción");
    }

    @Test
    @DisplayName("WIP: promedio tareas en curso en cuatro etapas")
    void testWIP() throws MetricException {
        ReportItem<Double> metric = (ReportItem<Double>) ghEnquirer.getMetric("WIP", REPO);
        assertEquals("WIP", metric.getName(), "El nombre debe ser WIP");
        double value = metric.getValue();
        log.info("WIP = " + value + " tareas");
        assertTrue(value >= 0, "WIP debe ser >= 0");
        assertNotNull(metric.getDescription(), "Debe tener descripción");
    }

    @Test
    @DisplayName("getAvailableMetrics() debe listar las cuatro métricas")
    void testGetAvailableMetrics() {
        List<String> list = ghEnquirer.getAvailableMetrics();
        log.info("Available metrics: " + list);
        assertEquals(4, list.size(), "Deben ser cuatro métricas");
        assertTrue(list.containsAll(List.of("cycleTime","waitTime","throughput","WIP")));
    }

    @Test
    @DisplayName("buildReport() debe devolver ReportI con 4 ítems")
    void testBuildReport() {
        ReportI report = ghEnquirer.buildReport(REPO);
        assertNotNull(report, "El reporte no debe ser nulo");
        List<ReportItemI> items = new ArrayList<>(report.getAllMetrics());
        log.info("Informe generado con ítems: " + items);
        assertEquals(4, items.size(), "Informe debe contener 4 métricas");
        // Verificamos rápidamente los nombres
        assertTrue(items.stream().anyMatch(i -> "cycleTime".equals(i.getName())));
        assertTrue(items.stream().anyMatch(i -> "waitTime".equals(i.getName())));
        assertTrue(items.stream().anyMatch(i -> "throughput".equals(i.getName())));
        assertTrue(items.stream().anyMatch(i -> "WIP".equals(i.getName())));
    }
}
