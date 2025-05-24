package us.muit.fs.a4i.test.control.strategies;

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

import us.muit.fs.a4i.control.strategies.FixTimeStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.model.entities.ReportItemI;

class FixTimeStrategyTest {

	@Test
	public void testCalcIndicator() throws NotAvailableMetricException {
		// Creamos los mocks necesarios
		ReportItemI<Double> mockClasificacion = Mockito.mock(ReportItemI.class);
		ReportItemI<Double> mockCorreccion = Mockito.mock(ReportItemI.class);
		ReportItemI<Double> mockNum_errores = Mockito.mock(ReportItemI.class);

		// Configuramos los mocks para devolver valores predefinidos
		Mockito.when(mockClasificacion.getName()).thenReturn("clasificacion");
		Mockito.when(mockClasificacion.getValue()).thenReturn(10.0);

		Mockito.when(mockCorreccion.getName()).thenReturn("correccion");
		Mockito.when(mockCorreccion.getValue()).thenReturn(5.0);

		Mockito.when(mockNum_errores.getName()).thenReturn("num_errores");
		Mockito.when(mockNum_errores.getValue()).thenReturn(5.0);

		// Creamos una instancia de la estrategia
		FixTimeStrategy strategy = new FixTimeStrategy();

		// Ejecutamos el método que queremos probar con los mocks como argumentos
		List<ReportItemI<Double>> metrics = Arrays.asList(mockClasificacion, mockCorreccion, mockNum_errores);
		ReportItemI<Double> result = strategy.calcIndicator(metrics);

		// Comprobamos que el resultado es el esperado
		Assertions.assertEquals("fixTime", result.getName());
		Assertions.assertEquals(3.0, result.getValue());
		Assertions.assertDoesNotThrow(() -> strategy.calcIndicator(metrics));

	}

	@Test
	public void testCalcIndicatorThrowsNotAvailableMetricException() {
		// Creamos los mocks necesarios
		ReportItemI<Double> mockClasificacion = Mockito.mock(ReportItemI.class);

		// Configuramos los mocks para devolver valores predefinidos
		Mockito.when(mockClasificacion.getName()).thenReturn("clasificacion");
		Mockito.when(mockClasificacion.getValue()).thenReturn(10.0);

		// Creamos una instancia de la estrategia
		FixTimeStrategy strategy = new FixTimeStrategy();

		// Ejecutamos el método que queremos probar con una sola métrica
		List<ReportItemI<Double>> metrics = Arrays.asList(mockClasificacion);
		// Comprobamos que se lanza la excepción adecuada
		NotAvailableMetricException exception = Assertions.assertThrows(NotAvailableMetricException.class,
				() -> strategy.calcIndicator(metrics));

	}

	@Test
	public void testRequiredMetrics() {

		// Creamos una instancia de la estrategia
		FixTimeStrategy strategy = new FixTimeStrategy();

		// Ejecutamos el método que queremos probar
		List<String> requiredMetrics = strategy.requiredMetrics();

		// Comprobamos que el resultado es el esperado
		List<String> expectedMetrics = Arrays.asList("clasificacion", "correccion", "num_errores");
		Assertions.assertEquals(expectedMetrics, requiredMetrics);
	}
}