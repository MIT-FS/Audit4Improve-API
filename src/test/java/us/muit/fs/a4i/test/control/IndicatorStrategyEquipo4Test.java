package us.muit.fs.a4i.test.control;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import us.muit.fs.a4i.control.IndicatorStrategyEquipo4;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.model.entities.impl.SimpleReportItem;

public class IndicatorStrategyEquipo4Test {

    private final IndicatorStrategyEquipo4 strategy = new IndicatorStrategyEquipo4();

    @Test
    public void testCalcIndicatorCorrectValues() throws NotAvailableMetricException {
        List<ReportItemI<Double>> metrics = Arrays.asList(
                new SimpleReportItem("issue_response_time", 0.9),
                new SimpleReportItem("issues_without_response_ratio", 0.8),
                new SimpleReportItem("resolved_under_48h_ratio", 1.0)
        );

        ReportItemI<Double> result = strategy.calcIndicator(metrics);

        assertEquals("issue_management_speed", result.getName());
        assertEquals((0.9 + 0.8 + 1.0) / 3, result.getValue(), 0.0001);
    }

    @Test
    public void testCalcIndicatorWithZeros() throws NotAvailableMetricException {
        List<ReportItemI<Double>> metrics = Arrays.asList(
                new SimpleReportItem("issue_response_time", 0.0),
                new SimpleReportItem("issues_without_response_ratio", 0.0),
                new SimpleReportItem("resolved_under_48h_ratio", 0.0)
        );

        ReportItemI<Double> result = strategy.calcIndicator(metrics);

        assertEquals("issue_management_speed", result.getName());
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testCalcIndicatorWithMaxValues() throws NotAvailableMetricException {
        List<ReportItemI<Double>> metrics = Arrays.asList(
                new SimpleReportItem("issue_response_time", 1.0),
                new SimpleReportItem("issues_without_response_ratio", 1.0),
                new SimpleReportItem("resolved_under_48h_ratio", 1.0)
        );

        ReportItemI<Double> result = strategy.calcIndicator(metrics);

        assertEquals("issue_management_speed", result.getName());
        assertEquals(1.0, result.getValue(), 0.0001);
    }

    @Test
    public void testMissingMetricThrowsException() {
        List<ReportItemI<Double>> metrics = Arrays.asList(
                new SimpleReportItem("issue_response_time", 0.9),
                new SimpleReportItem("issues_without_response_ratio", 0.8)
                // Falta "resolved_under_48h_ratio"
        );

        assertThrows(NotAvailableMetricException.class, () -> {
            strategy.calcIndicator(metrics);
        });
    }
}