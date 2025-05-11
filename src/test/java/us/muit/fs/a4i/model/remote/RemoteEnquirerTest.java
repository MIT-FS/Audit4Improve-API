/**
 * 
 */
package us.muit.fs.a4i.model.remote;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.model.remote.RemoteEnquirer;

class RemoteEnquirerTest {

    private GitHubRemoteEnquirer enquirer;

    @BeforeEach
    void setUp() throws MetricException, IOException {
        enquirer = new GitHubRemoteEnquirer();
    }

    @Test
    void testBuildReport() {
        ReportI report = enquirer.buildReport("MIT-FS/Audit4Improve-API");
        assertNotNull(report);
    }

    @Test
    void testGetMetric() throws MetricException {
        ReportItemI metric = enquirer.getMetric("reopenedIssuesAvg", "MIT-FS/Audit4Improve-API");
        assertNotNull(metric);
    }


    @Test
    void testGetAvailableMetrics() {
        List<String> metrics = enquirer.getAvailableMetrics();
        assertEquals(3, metrics.size());
        assertTrue(metrics.contains("reopenedIssuesAvg"));
        assertTrue(metrics.contains("firstTryResolutionRate"));
        assertTrue(metrics.contains("postClosureActivityRate"));        
    }

    @Test
    void testGetRemoteType() {
        assertEquals(RemoteEnquirer.RemoteType.GITHUB, enquirer.getRemoteType());
    }
}
