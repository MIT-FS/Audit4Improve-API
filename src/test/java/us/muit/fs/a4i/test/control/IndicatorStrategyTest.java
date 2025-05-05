package us.muit.fs.a4i.test.control;

import static org.junit.jupiter.api.Assertions.*;

import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.control.GDIStrategy;
import us.muit.fs.a4i.control.IndicatorStrategy;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IndicatorStrategyTest {

    @Test
    @DisplayName("Test correcto del cálculo GDI con métricas válidas")
    void testCalcIndicator_OK() throws NotAvailableMetricException, ReportItemException {
        IndicatorStrategy<Double> strategy = new GDIStrategy();

        ReportItemI<Double> total = new ReportItem.ReportItemBuilder<>("issues_total", 10.0).build();
        ReportItemI<Double> etiquetados = new ReportItem.ReportItemBuilder<>("issues_etiquetados", 7.0).build();

        List<ReportItemI<Double>> metrics = Arrays.asList(total, etiquetados);

        ReportItemI<Double> result = strategy.calcIndicator(metrics);
        assertEquals(70.0, result.getValue(), 0.001, "El cálculo del GDI es incorrecto");
        assertEquals("grado_documentacion_issues", result.getName(), "El nombre del resultado no es el esperado");
    }

    @Test
    @DisplayName("Test de excepción cuando faltan métricas")
    void testCalcIndicator_MissingMetrics() throws ReportItemException {
        IndicatorStrategy<Double> strategy = new GDIStrategy();

        ReportItemI<Double> etiquetados = new ReportItem.ReportItemBuilder<>("issues_etiquetados", 7.0).build();

        List<ReportItemI<Double>> metrics = List.of(etiquetados);

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