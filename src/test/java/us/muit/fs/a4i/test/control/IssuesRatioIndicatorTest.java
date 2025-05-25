package us.muit.fs.a4i.test.control;

/***
 * @author celllarod, curso 22/23
 * Pruebas añadidas por alumnos del curso 22/23 para probar la clase IssuesRatioIndicator
 */

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
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import us.muit.fs.a4i.control.strategies.IssuesRatioIndicatorStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.model.entities.ReportItemI;

public class IssuesRatioIndicatorTest {

	private static Logger log = Logger.getLogger(IssuesRatioIndicatorTest.class.getName());

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	public void testCalcIndicator() throws NotAvailableMetricException {
		// Creamos los mocks necesarios
		ReportItemI<Double> mockOpenIssues = Mockito.mock(ReportItemI.class);
		ReportItemI<Double> mockClosedIssues = Mockito.mock(ReportItemI.class);

		// Configuramos los mocks para devolver valores predefinidos
		Mockito.when(mockOpenIssues.getName()).thenReturn("openIssues");
		Mockito.when(mockOpenIssues.getValue()).thenReturn(10.0);

		Mockito.when(mockClosedIssues.getName()).thenReturn("closedIssues");
		Mockito.when(mockClosedIssues.getValue()).thenReturn(5.0);

		// Creamos una instancia de IssuesRatioIndicator
		IssuesRatioIndicatorStrategy indicator = new IssuesRatioIndicatorStrategy();

		// Ejecutamos el método que queremos probar con los mocks como argumentos
		List<ReportItemI<Double>> metrics = Arrays.asList(mockOpenIssues, mockClosedIssues);
		ReportItemI<Double> result = indicator.calcIndicator(metrics);

		// Comprobamos que el resultado es el esperado
		Assertions.assertEquals("issuesRatio", result.getName());
		Assertions.assertEquals(2.0, result.getValue());
		Assertions.assertDoesNotThrow(() -> indicator.calcIndicator(metrics));
	}

	@Test
	public void testCalcIndicatorThrowsNotAvailableMetricException() {
		// Creamos los mocks necesarios
		ReportItemI<Double> mockOpenIssues = Mockito.mock(ReportItemI.class);

		// Configuramos los mocks para devolver valores predefinidos
		Mockito.when(mockOpenIssues.getName()).thenReturn("openIssues");
		Mockito.when(mockOpenIssues.getValue()).thenReturn(10.0);

		// Creamos una instancia de IssuesRatioIndicator
		IssuesRatioIndicatorStrategy indicator = new IssuesRatioIndicatorStrategy();

		// Ejecutamos el método que queremos probar con una sola métrica
		List<ReportItemI<Double>> metrics = Arrays.asList(mockOpenIssues);
		// Comprobamos que se lanza la excepción adecuada
		NotAvailableMetricException exception = Assertions.assertThrows(NotAvailableMetricException.class,
				() -> indicator.calcIndicator(metrics));

	}

	@Test
	public void testRequiredMetrics() {
		// Creamos una instancia de IssuesRatioIndicator
		IssuesRatioIndicatorStrategy indicatorStrategy = new IssuesRatioIndicatorStrategy();

		// Ejecutamos el método que queremos probar
		List<String> requiredMetrics = indicatorStrategy.requiredMetrics();

		// Comprobamos que el resultado es el esperado
		List<String> expectedMetrics = Arrays.asList("openIssues", "closedIssues");
		Assertions.assertEquals(expectedMetrics, requiredMetrics);
	}
}
