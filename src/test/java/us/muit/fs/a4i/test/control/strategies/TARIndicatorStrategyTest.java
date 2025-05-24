package us.muit.fs.a4i.test.control.strategies;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.control.strategies.TARIndicatorStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;
import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.model.entities.ReportItem;

public class TARIndicatorStrategyTest {
    private static Logger log = Logger.getLogger(TARIndicatorStrategyTest.class.getName());
    private TARIndicatorStrategy strat;

    @BeforeEach
    void setUp() {
        strat = new TARIndicatorStrategy();
    }

    @Test
    public void testTAR_Alto() throws NotAvailableMetricException, ReportItemException {
        ReportItemI<Double> visitors = mock(ReportItemI.class);
        ReportItemI<Double> clones = mock(ReportItemI.class);

        when(visitors.getName()).thenReturn("uniqueVisitors");
        when(visitors.getValue()).thenReturn(60.0);
        when(clones.getName()).thenReturn("uniqueClones");
        when(clones.getValue()).thenReturn(30.0);

        List<ReportItemI<Double>> metrics = List.of(visitors, clones);
        ReportItemI<Double> result = strat.calcIndicator(metrics);

        assertEquals("GithubTraffic", result.getName());
        assertEquals(0.5, result.getValue(), 1e-6);
        assertEquals(IndicatorState.OK, result.getIndicator().getState());
    }

    @Test
    public void testTAR_Medio() throws NotAvailableMetricException, ReportItemException {
        ReportItemI<Double> visitors = mock(ReportItemI.class);
        ReportItemI<Double> clones = mock(ReportItemI.class);

        when(visitors.getName()).thenReturn("uniqueVisitors");
        when(visitors.getValue()).thenReturn(30.0);
        when(clones.getName()).thenReturn("uniqueClones");
        when(clones.getValue()).thenReturn(9.0);

        List<ReportItemI<Double>> metrics = List.of(visitors, clones);
        ReportItemI<Double> result = strat.calcIndicator(metrics);

        assertEquals("GithubTraffic", result.getName());
        assertEquals(0.3, result.getValue(), 1e-6);
        assertEquals(IndicatorState.WARNING, result.getIndicator().getState());
    }

    @Test
    public void testTAR_Bajo() throws NotAvailableMetricException, ReportItemException {
        ReportItemI<Double> visitors = mock(ReportItemI.class);
        ReportItemI<Double> clones = mock(ReportItemI.class);

        when(visitors.getName()).thenReturn("uniqueVisitors");
        when(visitors.getValue()).thenReturn(5.0);
        when(clones.getName()).thenReturn("uniqueClones");
        when(clones.getValue()).thenReturn(1.0);

        List<ReportItemI<Double>> metrics = List.of(visitors, clones);
        ReportItemI<Double> result = strat.calcIndicator(metrics);

        assertEquals("GithubTraffic", result.getName());
        assertEquals(0.2, result.getValue(), 1e-6);
        assertEquals(IndicatorState.CRITICAL, result.getIndicator().getState());
    }

    @Test
    public void testTAR_VisitantesCero() {
        ReportItemI<Double> visitors = mock(ReportItemI.class);
        ReportItemI<Double> clones = mock(ReportItemI.class);

        when(visitors.getName()).thenReturn("uniqueVisitors");
        when(visitors.getValue()).thenReturn(0.0);
        when(clones.getName()).thenReturn("uniqueClones");
        when(clones.getValue()).thenReturn(5.0);

        List<ReportItemI<Double>> metrics = List.of(visitors, clones);

        assertThrows(NotAvailableMetricException.class, () -> strat.calcIndicator(metrics));
    }

    @Test
    public void testTAR_FaltanMetricas() throws ReportItemException {
        ReportItemI<Double> onlyVisitors = new ReportItem.ReportItemBuilder<>("uniqueVisitors", 25.0).build();
        List<ReportItemI<Double>> metrics = List.of(onlyVisitors);

        assertThrows(NotAvailableMetricException.class, () -> strat.calcIndicator(metrics));
    }
}
