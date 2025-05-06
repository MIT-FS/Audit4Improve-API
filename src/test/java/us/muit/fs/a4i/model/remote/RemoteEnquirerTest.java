/**
 * 
 */
package us.muit.fs.a4i.model.remote;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.model.remote.RemoteEnquirer;

class RemoteEnquirerTest {

    private RemoteEnquirer enquirer;

    private ReportI mockReport;
    private ReportItemI mockMetric;

    @BeforeEach
    void setUp() throws MetricException {
        // Creamos el mock de la interfaz
        enquirer = mock(RemoteEnquirer.class);

        // Creamos mocks para los valores devueltos
        mockReport = mock(ReportI.class);
        mockMetric = mock(ReportItemI.class);

        // Definimos comportamiento del mock
        when(enquirer.buildReport("test-entity")).thenReturn(mockReport);
        when(enquirer.getMetric("reopenedIssuesAvg", "test-entity")).thenReturn(mockMetric);
        when(enquirer.getAvailableMetrics()).thenReturn(
            Arrays.asList("reopenedIssuesAvg", "firstTryResolutionRate", "postClosureActivityRate")
        );
        when(enquirer.getRemoteType()).thenReturn(RemoteEnquirer.RemoteType.GITHUB);
    }

    @Test
    void testBuildReport() {
        ReportI report = enquirer.buildReport("test-entity");
        assertNotNull(report);
    }

    @Test
    void testGetMetric() throws MetricException {
        ReportItemI metric = enquirer.getMetric("reopenedIssuesAvg", "test-entity");
        assertNotNull(metric);
    }

    @Test
    void testGetAvailableMetrics() {
        List<String> metrics = enquirer.getAvailableMetrics();
        assertEquals(3, metrics.size());
        assertTrue(metrics.contains("reopenedIssuesAvg"));
    }

    @Test
    void testGetRemoteType() {
        assertEquals(RemoteEnquirer.RemoteType.GITHUB, enquirer.getRemoteType());
    }
}
