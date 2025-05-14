package us.muit.fs.a4i.test.control;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import us.muit.fs.a4i.control.IndicatorStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.model.entities.ReportItemI;

/**
 * 
 */

public class IndicatorStrategyTest {

    private static Logger log = Logger.getLogger(IndicatorStrategyTest.class.getName());

    @Test
    public void testCalcIndicator() throws NotAvailableMetricException {
        // Creamos los mocks necesarios
        ReportItemI<Double> mockMRI = Mockito.mock(ReportItemI.class);
        ReportItemI<Double> mockTRPI = Mockito.mock(ReportItemI.class);
        ReportItemI<Double> mockIAPC = Mockito.mock(ReportItemI.class);

        // Configuramos los mocks para devolver valores predefinidos
        Mockito.when(mockMRI.getName()).thenReturn("reopenedIssuesAvg");
        Mockito.when(mockMRI.getValue()).thenReturn(0.5);  // Media Reapertura Issues

        Mockito.when(mockTRPI.getName()).thenReturn("firstTryResolutionRate");
        Mockito.when(mockTRPI.getValue()).thenReturn(80.0); // Tasa Resolución Primer Intento

        Mockito.when(mockIAPC.getName()).thenReturn("postClosureActivityRate");
        Mockito.when(mockIAPC.getValue()).thenReturn(20.0); // Issues con Actividad Posterior al Cierre

        // Creamos una instancia de IndicatorStrategy
        IndicatorStrategy<Double> indicator = new CalidadStrategy();

        // Ejecutamos el método que queremos probar con los mocks como argumentos
        List<ReportItemI<Double>> metrics = Arrays.asList(mockMRI, mockTRPI, mockIAPC);
        ReportItemI<Double> result = indicator.calcIndicator(metrics);
       
        // Calidad esperada para los valores del Mock
        Assertions.assertEquals("calidadResolucion", result.getName());
        Assertions.assertEquals(82.47058823529412, result.getValue(), 0.5);  // Con margen de error para la comparación
        Assertions.assertDoesNotThrow(() -> indicator.calcIndicator(metrics));
    }

    @Test
    public void testCalcIndicatorThrowsNotAvailableMetricException() {
        // Creamos los mocks necesarios
        ReportItemI<Double> mockMRI = Mockito.mock(ReportItemI.class);

        // Configuramos el mock para devolver un valor predefinido
        Mockito.when(mockMRI.getName()).thenReturn("reopenedIssuesAvg");
        Mockito.when(mockMRI.getValue()).thenReturn(0.5);

        // Creamos una instancia de IndicatorStrategy
        IndicatorStrategy<Double> indicator = new CalidadStrategy();

        // Ejecutamos el método que queremos probar con métricas insuficientes
        List<ReportItemI<Double>> metrics = Arrays.asList(mockMRI);

        // Comprobamos que se lanza la excepción adecuada
        NotAvailableMetricException exception = Assertions.assertThrows(NotAvailableMetricException.class,
                () -> indicator.calcIndicator(metrics));
    }

    @Test
    public void testRequiredMetrics() {
        // Creamos una instancia de IndicatorStrategy
    	IndicatorStrategy<Double> indicatorStrategy = new CalidadStrategy();

        // Ejecutamos el método que queremos probar
        List<String> requiredMetrics = indicatorStrategy.requiredMetrics();

        // Comprobamos que el resultado es el esperado
        List<String> expectedMetrics = Arrays.asList("reopenedIssuesAvg", "firstTryResolutionRate", "postClosureActivityRate");
        Assertions.assertEquals(expectedMetrics, requiredMetrics);
    }
    
    @Test
    public void testCalcIndicatorWithExtremeValues() throws NotAvailableMetricException {
        ReportItemI<Double> mri = Mockito.mock(ReportItemI.class);
        ReportItemI<Double> trpi = Mockito.mock(ReportItemI.class);
        ReportItemI<Double> iapc = Mockito.mock(ReportItemI.class);

        Mockito.when(mri.getName()).thenReturn("reopenedIssuesAvg");
        Mockito.when(mri.getValue()).thenReturn(0.0);

        Mockito.when(trpi.getName()).thenReturn("firstTryResolutionRate");
        Mockito.when(trpi.getValue()).thenReturn(100.0);

        Mockito.when(iapc.getName()).thenReturn("postClosureActivityRate");
        Mockito.when(iapc.getValue()).thenReturn(0.0);

        IndicatorStrategy<Double> indicator = new CalidadStrategy();
        List<ReportItemI<Double>> metrics = Arrays.asList(mri, trpi, iapc);
        ReportItemI<Double> result = indicator.calcIndicator(metrics);

        // Aquí el resultado será alto (cerca de 100)
        Assertions.assertTrue(result.getValue() > 90.0);
        
        
    }

}