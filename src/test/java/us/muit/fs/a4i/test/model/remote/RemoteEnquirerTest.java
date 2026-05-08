/**
 * Código revisado por Sergio Ramírez.
 */

package us.muit.fs.a4i.test.model.remote;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.model.remote.ExtraccionMetricas;
import us.muit.fs.a4i.model.remote.GitHubRepositoryEnquirer;
import us.muit.fs.a4i.model.remote.RemoteEnquirer;

class RemoteEnquirerTest {

    private static Logger log = Logger.getLogger(RemoteEnquirerTest.class.getName());

    private RemoteEnquirer enquirer = new ExtraccionMetricas();  // Asegúrate que esta clase existe y está implementada
   

    // Test que certifica que se puede obtener la métrica "totalIssues" (el resultado no es nulo,
    // el nombre de la métrica coincide al completo y el valor es de tipo numérico).
    
    @Test
    void testGetTotalIssuesMetric() throws MetricException {
        String repoId = "MIT-FS/Audit4Improve-API"; // Reemplazar con un repositorio válido de prueba

        ReportItemI metric = enquirer.getMetric("totalIssues", repoId);
        assertNotNull(metric, "La métrica 'totalIssues' no debe ser null");
        assertEquals("totalIssues", metric.getName());
        assertTrue(metric.getValue() instanceof Number, "El valor debe ser numérico");

        log.info("Total Issues: " + metric.getValue());
    }

    // Test que certifica que se puede obtener la métrica "labeledIssues" (el resultado no es nulo,
    // el nombre de la métrica coincide al completo y el valor es de tipo numérico).
    
    @Test
    void testGetLabeledIssuesMetric() throws MetricException {
        String repoId = "MIT-FS/Audit4Improve-API";

        ReportItemI metric = enquirer.getMetric("labeledIssues", repoId);
        assertNotNull(metric, "La métrica 'labeledIssues' no debe ser null");
        assertEquals("labeledIssues", metric.getName());
        assertTrue(metric.getValue() instanceof Number, "El valor debe ser numérico");

        log.info("Issues con etiquetas: " + metric.getValue());
    }

    
    // Test que verifica que el objeto informa correctamente de las métricas que soporta (que
    // la lista no es nula y que dentro de las posibilidades están "totalIssues" y "labeledIssues")
    
    @Test
    void testGetAvailableMetrics() {
        List<String> metrics = enquirer.getAvailableMetrics();
        assertNotNull(metrics);
        assertTrue(metrics.contains("totalIssues"), "Debe contener la métrica 'totalIssues'");
        assertTrue(metrics.contains("labeledIssues"), "Debe contener la métrica 'labeledIssues'");
    }

    // Test que intenta pedir una métrica que no existe, para comprobar que la excepción salta como se espera
    
    
    @Test
    void testInvalidMetricThrowsException() {
        String repoId = "MIT-FS/Audit4Improve-API";

        assertThrows(MetricException.class, () -> {
            enquirer.getMetric("nonExistentMetric", repoId);
        });
    }

    
    // Test que verifica que el tipo de la clase está correctamente identificado como GITHUB
    
    @Test
    void testGetRemoteType() {
        assertEquals(RemoteEnquirer.RemoteType.GITHUB, enquirer.getRemoteType());
    }
    
    // Test sencillo que comprueba que la lista de métricas disponibles no está vacía.
    // Este test no depende de conexión a GitHub, por lo que es estable y rápido.
    
    @Test
    void testAvailableMetricsIsNotEmpty() {
        List<String> metrics = enquirer.getAvailableMetrics();

        assertNotNull(metrics, "La lista de métricas disponibles no debe ser null");
        assertFalse(metrics.isEmpty(), "La lista de métricas disponibles no debe estar vacía");
    }
    
    // Test que comprueba que una métrica con nombre null se rechaza correctamente.
    // No se realiza conexión a GitHub porque la validación de la métrica se hace antes.
    
    @Test
    void testNullMetricNameThrowsException() {
        String repoId = "MIT-FS/Audit4Improve-API";

        assertThrows(MetricException.class, () -> {
            enquirer.getMetric(null, repoId);
        }, "Solicitar una métrica null debe lanzar MetricException");
    }
}
