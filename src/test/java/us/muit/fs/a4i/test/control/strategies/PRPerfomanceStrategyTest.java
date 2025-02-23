package us.muit.fs.a4i.test.control.strategies;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import us.muit.fs.a4i.control.strategies.PRPerformanceStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.model.entities.ReportItemI;

class PRPerfomanceStrategyTest {

	public void testCalcIndicator() throws NotAvailableMetricException {
		// Creamos los mocks necesarios
		ReportItemI<Double> mockAcceptedLastYear = Mockito.mock(ReportItemI.class);
		ReportItemI<Double> mockAcceptedLastMonth = Mockito.mock(ReportItemI.class);
		ReportItemI<Double> mockRejectedLastYear = Mockito.mock(ReportItemI.class);
		ReportItemI<Double> mockRejectedLastMonth = Mockito.mock(ReportItemI.class);

		// Configuramos los mocks para devolver valores predefinidos
		Mockito.when(mockAcceptedLastYear.getName()).thenReturn("PRAcceptedLastYear");
		Mockito.when(mockAcceptedLastYear.getValue()).thenReturn((double) 100);

		Mockito.when(mockAcceptedLastMonth.getName()).thenReturn("PRAcceptedLastMonth");
		Mockito.when(mockAcceptedLastMonth.getValue()).thenReturn((double) 10);

		Mockito.when(mockRejectedLastYear.getName()).thenReturn("PRRejectedLastYear");
		Mockito.when(mockRejectedLastYear.getValue()).thenReturn((double) 20);

		Mockito.when(mockRejectedLastMonth.getName()).thenReturn("PRRejectedLastMonth");
		Mockito.when(mockRejectedLastMonth.getValue()).thenReturn((double) 2);

		// Creamos una instancia de PRPerformanceStrategy
		PRPerformanceStrategy indicator = new PRPerformanceStrategy();

		// Ejecutamos el método que queremos probar con los mocks como argumentos
		List<ReportItemI<Double>> metrics = Arrays.asList(mockAcceptedLastYear, mockAcceptedLastMonth,
				mockRejectedLastYear, mockRejectedLastMonth);
		ReportItemI<Double> result = indicator.calcIndicator(metrics);

		// Comprobamos que el resultado es el esperado
		Assertions.assertEquals("PRPerformance", result.getName());
		Assertions.assertEquals((double) 100 / 20 - (double) 10 / 2, result.getValue());
		Assertions.assertDoesNotThrow(() -> indicator.calcIndicator(metrics));
	}

	@Test
	public void testCalcIndicatorThrowsNotAvailableMetricException() {
		// Creamos los mocks necesarios
		ReportItemI<Double> mockAcceptedLastYear = Mockito.mock(ReportItemI.class);

		// Configuramos los mocks para devolver valores predefinidos
		Mockito.when(mockAcceptedLastYear.getName()).thenReturn("PRAcceptedLastYear");
		Mockito.when(mockAcceptedLastYear.getValue()).thenReturn((double) 100);

		// Creamos una instancia de PullRequestsAcceptanceIndicatorStrategy
		PRPerformanceStrategy indicator = new PRPerformanceStrategy();

		// Ejecutamos el método que queremos probar con una sola métrica
		List<ReportItemI<Double>> metrics = Arrays.asList(mockAcceptedLastYear);
		// Comprobamos que se lanza la excepción adecuada
		NotAvailableMetricException exception = Assertions.assertThrows(NotAvailableMetricException.class,
				() -> indicator.calcIndicator(metrics));
	}

	@Test
	public void testRequiredMetrics() {
		// Creamos una instancia de PRPerformanceStrategy
		PRPerformanceStrategy indicatorStrategy = new PRPerformanceStrategy();

		// Ejecutamos el método que queremos probar
		List<String> requiredMetrics = indicatorStrategy.requiredMetrics();

		// Comprobamos que el resultado es el esperado
		List<String> expectedMetrics = Arrays.asList("PRAcceptedLastYear", "PRAcceptedLastMonth", "PRRejectedLastMonth",
				"PRRejectedLastYear");
		Assertions.assertEquals(expectedMetrics, requiredMetrics);
	}
}
