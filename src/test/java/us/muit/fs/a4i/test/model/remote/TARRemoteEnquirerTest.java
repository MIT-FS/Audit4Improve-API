package us.muit.fs.a4i.test.model.remote;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.model.remote.TARRemoteEnquirer;

/**
 * Test de integración para TARRemoteEnquirer.
 */
class TARRemoteEnquirerTest {

    private static final Logger log = Logger.getLogger(TARRemoteEnquirerTest.class.getName());
    private static final String REPO = "MIT-FS/Audit4Improve-API-G10";

    private TARRemoteEnquirer ghEnquirer;

    @BeforeEach
    void setUp() {
        String token = System.getProperty("github.token");
        assertNotNull(token, "Debe definir github.token como propiedad del sistema con -Dgithub.token=...");
        ghEnquirer = new TARRemoteEnquirer();
    }

    @Test
    @DisplayName("uniqueVisitorsLastDay: total visitantes únicos")
    void testUniqueVisitors() throws MetricException {
        ReportItem<?> item = (ReportItem<?>) ghEnquirer.getMetric("uniqueVisitorsLastDay", REPO);

        assertTrue(item.getValue() instanceof Integer, "El valor debe ser Integer");

        Integer value = (Integer) item.getValue();
        ReportItemI<Integer> metric = (ReportItemI<Integer>) item;

        assertEquals("uniqueVisitorsLastDay", metric.getName());
        log.info("uniqueVisitorsLastDay = " + value);
        assertTrue(value >= 0);
    }

    @Test
    @DisplayName("uniqueClonesLastDay: total clones únicos")
    void testUniqueClones() throws MetricException {
        ReportItem<?> item = (ReportItem<?>) ghEnquirer.getMetric("uniqueClonesLastDay", REPO);

        assertTrue(item.getValue() instanceof Integer, "El valor debe ser Integer");

        Integer value = (Integer) item.getValue();
        ReportItemI<Integer> metric = (ReportItemI<Integer>) item;

        assertEquals("uniqueClonesLastDay", metric.getName());
        log.info("uniqueClonesLastDay = " + value);
        assertTrue(value >= 0);
    }

    @Test
    @DisplayName("cloneConversionRate: conversión (%) de visitas a clones")
    void testCloneConversionRate() throws MetricException {
        ReportItem<?> item = (ReportItem<?>) ghEnquirer.getMetric("cloneConversionRate", REPO);

        assertTrue(item.getValue() instanceof Double, "El valor debe ser Double");

        Double value = (Double) item.getValue();
        ReportItemI<Double> metric = (ReportItemI<Double>) item;

        assertEquals("cloneConversionRate", metric.getName());
        log.info("cloneConversionRate = " + value + "%");
        assertTrue(value >= 0.0);
    }

    @Test
    @DisplayName("getAvailableMetrics() debe listar las 3 métricas")
    void testGetAvailableMetrics() {
        List<String> list = ghEnquirer.getAvailableMetrics();
        log.info("Available metrics: " + list);
        assertEquals(3, list.size());
        assertTrue(list.contains("uniqueVisitorsLastDay"));
        assertTrue(list.contains("uniqueClonesLastDay"));
        assertTrue(list.contains("cloneConversionRate"));
    }

    @Test
    @DisplayName("buildReport() debe devolver ReportI con 3 ítems")
    void testBuildReport() {
        ReportI report = ghEnquirer.buildReport(REPO);
        assertNotNull(report, "El reporte no debe ser nulo");

        List<ReportItemI<?>> items = new ArrayList<ReportItemI<?>>();
        for (ReportItemI<?> metric : report.getAllMetrics()) {
            items.add(metric);
        }

        log.info("Informe generado con ítems: " + items);
        assertEquals(3, items.size(), "Informe debe contener 3 métricas");

        assertTrue(items.stream().anyMatch(i -> "uniqueVisitorsLastDay".equals(i.getName())));
        assertTrue(items.stream().anyMatch(i -> "uniqueClonesLastDay".equals(i.getName())));
        assertTrue(items.stream().anyMatch(i -> "cloneConversionRate".equals(i.getName())));
    }
}
