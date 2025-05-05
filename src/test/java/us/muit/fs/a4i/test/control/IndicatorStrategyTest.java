package us.muit.fs.a4i.test.control;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.control.GDIStrategy;
import us.muit.fs.a4i.control.IndicatorStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;

class IndicatorStrategyTest {

    @Test
    @DisplayName("Test correcto del cálculo GDI con métricas válidas")
    void testCalcIndicator_OK() throws NotAvailableMetricException {
        IndicatorStrategy<Double> strategy = new GDIStrategy();

        List<ReportItemI<Double>> metrics = Arrays.asList(
            new ReportItem<>("issues_total", 10.0),
            new ReportItem<>("issues_etiquetados", 7.0)
        );

        ReportItemI<Double> result = strategy.calcIndicator(metrics);
        assertEquals(70.0, result.getValue(), 0.001, "El cálculo del GDI es incorrecto");
        assertEquals("grado_documentacion_issues", result.getName(), "El nombre del resultado no es el esperado");
    }

    @Test
    @DisplayName("Test de excepción cuando faltan métricas")
    void testCalcIndicator_MissingMetrics() {
        IndicatorStrategy<Double> strategy = new GDIStrategy();

        List<ReportItemI<Double>> metrics = List.of(
            new ReportItem<>("issues_etiquetados", 7.0)
        );

        assertThrows(NotAvailableMetricException.class, () -> {
            strategy.calcIndicator(metrics);
        }, "Se esperaba una excepción por falta de métricas");
    }

    @Test
    @DisplayName("Test de requiredMetrics() devuelve métricas necesarias")
    void testRequiredMetrics() {
        IndicatorStrategy<Double> strategy = new GDIStrategy();
        List<String> required = strategy.requiredMetrics();

        assertTrue(required.contains("issues_total"));
        assertTrue(required.contains("issues_etiquetados"));
        assertEquals(2, required.size(), "Se esperaban exactamente dos métricas");
    }
}
