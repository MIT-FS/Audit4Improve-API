
package us.muit.fs.a4i.test.control.strategies;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.control.strategies.IEFStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;
import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.model.entities.ReportItem;

public class IEFStrategyTest {
    private static Logger log = Logger.getLogger(IEFStrategyTest.class.getName());
    private IEFStrategy strat;

    @BeforeEach
    void setUp() {
        strat = new IEFStrategy();
    }
    @Test
    public void testIEF_Excelente() throws NotAvailableMetricException, ReportItemException {
        // Creamos mocks para cada métrica
        ReportItemI<Double> mC = mock(ReportItemI.class);
        ReportItemI<Double> mW = mock(ReportItemI.class);
        ReportItemI<Double> mT = mock(ReportItemI.class);
        ReportItemI<Double> mP = mock(ReportItemI.class);

        when(mC.getName()).thenReturn("cycleTime");
        when(mC.getValue()).thenReturn(0.0);
        when(mW.getName()).thenReturn("waitTime");
        when(mW.getValue()).thenReturn(0.0);
        when(mT.getName()).thenReturn("throughput");
        when(mT.getValue()).thenReturn(50.0);
        when(mP.getName()).thenReturn("WIP");
        when(mP.getValue()).thenReturn(0.0);

        List<ReportItemI<Double>> metrics = List.of(mC, mW, mT, mP);
        log.info("Métricas de entrada: " + metrics);

        ReportItemI<Double> result = strat.calcIndicator(metrics);
        log.info("Resultado IEF: " + result);

        assertEquals("IEF", result.getName());
        assertEquals(1.0, result.getValue(), 1e-6);
        assertNotNull(result.getIndicator());
        assertEquals(IndicatorState.OK, result.getIndicator().getState());
    }

    @Test
    public void testIEF_Bueno() throws NotAvailableMetricException, ReportItemException {
        ReportItemI<Double> mC = mock(ReportItemI.class);
        ReportItemI<Double> mW = mock(ReportItemI.class);
        ReportItemI<Double> mT = mock(ReportItemI.class);
        ReportItemI<Double> mP = mock(ReportItemI.class);

        when(mC.getName()).thenReturn("cycleTime");
        when(mC.getValue()).thenReturn(40.0);  // 0.25
        when(mW.getName()).thenReturn("waitTime");
        when(mW.getValue()).thenReturn(20.0);  // 0.25
        when(mT.getName()).thenReturn("throughput");
        when(mT.getValue()).thenReturn(35.0);  // 0.7
        when(mP.getName()).thenReturn("WIP");
        when(mP.getValue()).thenReturn(5.0);   // 0.25

        List<ReportItemI<Double>> metrics = List.of(mC, mW, mT, mP);
        ReportItemI<Double> result = strat.calcIndicator(metrics);

        assertEquals("IEF", result.getName());
        assertEquals(0.8125, result.getValue(), 1e-4);  // cae en "Excelente"
        assertNotNull(result.getIndicator());
        assertEquals(IndicatorState.OK, result.getIndicator().getState());  // este valor da "Excelente"
    }

    @Test
    public void testIEF_Aceptable() throws NotAvailableMetricException, ReportItemException {
        // Creamos mocks para cada métrica
        ReportItemI<Double> mC = mock(ReportItemI.class);
        ReportItemI<Double> mW = mock(ReportItemI.class);
        ReportItemI<Double> mT = mock(ReportItemI.class);
        ReportItemI<Double> mP = mock(ReportItemI.class);

        when(mC.getName()).thenReturn("cycleTime");
        when(mC.getValue()).thenReturn(80.0);
        when(mW.getName()).thenReturn("waitTime");
        when(mW.getValue()).thenReturn(40.0);
        when(mT.getName()).thenReturn("throughput");
        when(mT.getValue()).thenReturn(25.0);
        when(mP.getName()).thenReturn("WIP");
        when(mP.getValue()).thenReturn(10.0);

        List<ReportItemI<Double>> metrics = List.of(mC, mW, mT, mP);
        log.info("Métricas de entrada: " + metrics);

        ReportItemI<Double> result = strat.calcIndicator(metrics);
        log.info("Resultado IEF: " + result);

        assertEquals("IEF", result.getName());
        assertEquals(0.5, result.getValue(), 1e-6);
        assertNotNull(result.getIndicator());
        assertEquals(IndicatorState.WARNING, result.getIndicator().getState());
    }

    @Test
    public void testIEF_Bajo() throws NotAvailableMetricException, ReportItemException {
        // Creamos mocks para cada métrica
        ReportItemI<Double> mC = mock(ReportItemI.class);
        ReportItemI<Double> mW = mock(ReportItemI.class);
        ReportItemI<Double> mT = mock(ReportItemI.class);
        ReportItemI<Double> mP = mock(ReportItemI.class);

        when(mC.getName()).thenReturn("cycleTime");
        when(mC.getValue()).thenReturn(200.0);
        when(mW.getName()).thenReturn("waitTime");
        when(mW.getValue()).thenReturn(100.0);
        when(mT.getName()).thenReturn("throughput");
        when(mT.getValue()).thenReturn(0.0);
        when(mP.getName()).thenReturn("WIP");
        when(mP.getValue()).thenReturn(30.0);

        List<ReportItemI<Double>> metrics = List.of(mC, mW, mT, mP);
        log.info("Métricas de entrada: " + metrics);

        ReportItemI<Double> result = strat.calcIndicator(metrics);
        log.info("Resultado IEF: " + result);

        assertEquals("IEF", result.getName());
        assertEquals(0.0, result.getValue(), 1e-6);
        assertNotNull(result.getIndicator());
        assertEquals(IndicatorState.CRITICAL, result.getIndicator().getState());
    }

    @Test
    public void testIEF_FaltanMetricas() throws ReportItemException {
        ReportItemI<Double> mC = new ReportItem.ReportItemBuilder<>("cycleTime", 10.0).build();
        List<ReportItemI<Double>> metrics = List.of(mC);

        assertThrows(NotAvailableMetricException.class, () -> strat.calcIndicator(metrics));
    }
}


