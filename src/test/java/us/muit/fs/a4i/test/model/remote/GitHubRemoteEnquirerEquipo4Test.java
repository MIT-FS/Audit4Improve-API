package us.muit.fs.a4i.test.model.remote;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.model.remote.GitHubRemoteEnquirerEquipo4;

public class GitHubRemoteEnquirerEquipo4Test {

    private static final String TEST_REPO = "MIT-FS/Audit4Improve-API";

    @Test
    public void testConstructorLoadsConfig() {
        assertDoesNotThrow(() -> {
            GitHubRemoteEnquirerEquipo4 enquirer = new GitHubRemoteEnquirerEquipo4();
            assertNotNull(enquirer);
        });
    }

    @Test
    public void testGetAvailableMetrics() throws Exception {
        GitHubRemoteEnquirerEquipo4 enquirer = new GitHubRemoteEnquirerEquipo4();
        var metrics = enquirer.getAvailableMetrics();
        assertNotNull(metrics);
        assertTrue(metrics.contains("issue_response_time"));
        assertTrue(metrics.contains("issues_without_response_ratio"));
        assertTrue(metrics.contains("resolved_under_48h_ratio"));
        // Ya no debe contener "issue_management_speed"
        assertFalse(metrics.contains("issue_management_speed"));
    }

    @Test
    public void testGetMetricValid() throws Exception {
        GitHubRemoteEnquirerEquipo4 enquirer = new GitHubRemoteEnquirerEquipo4();

        var metric = enquirer.getMetric("issue_response_time", TEST_REPO);
        assertNotNull(metric);
        assertEquals("issue_response_time", metric.getName());
        assertTrue(metric.getValue() instanceof Number);
    }

    @Test
    public void testGetMetricInvalid() throws Exception {
        GitHubRemoteEnquirerEquipo4 enquirer = new GitHubRemoteEnquirerEquipo4();

        MetricException thrown = assertThrows(MetricException.class, () -> {
            enquirer.getMetric("metric_no_existente", TEST_REPO);
        });

        assertTrue(thrown.getMessage().contains("Métrica no soportada"));
    }

    @Test
    public void testBuildReport() throws Exception {
        GitHubRemoteEnquirerEquipo4 enquirer = new GitHubRemoteEnquirerEquipo4();
        var report = enquirer.buildReport(TEST_REPO);
        assertNotNull(report);
        assertEquals(TEST_REPO, report.getEntityId());
        // Que solo contenga las 3 métricas que quedan
        assertEquals(3, report.getAllMetrics().size());
    }
}