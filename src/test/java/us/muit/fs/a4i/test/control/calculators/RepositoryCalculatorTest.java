package us.muit.fs.a4i.test.control.calculators;

/***
 * @author celllarod, curso 22/23
 * Pruebas añadidas por alumnos del curso 22/23 para probar la clase RepositoryCalculator
 * REF: https://javadoc.io/doc/org.mockito/mockito-core/4.3.1/org/mockito/Mockito.html
 */

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.control.IndicatorStrategy;
import us.muit.fs.a4i.control.ReportManagerI;
import us.muit.fs.a4i.control.calculators.RepositoryCalculator;
import us.muit.fs.a4i.control.strategies.IssuesRatioIndicatorStrategy;

import us.muit.fs.a4i.exceptions.IndicatorException;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.IndicatorI;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItem.ReportItemBuilder;

import us.muit.fs.a4i.model.entities.ReportItemI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class RepositoryCalculatorTest {

	private static Logger log = Logger.getLogger(RepositoryCalculatorTest.class.getName());

	/**
	 * Test para el metodo calcIndicator de RepositoryCalculator cuando las métricas
	 * que se pasan no son las necesarias RepositoryCalculator debe antes obtenerlas
	 * {@link us.muit.fs.a4i.control.RepositoryCalculator.calcIndicator(String,
	 * ReportManagerI)}.
	 * 
	 * @throws NotAvailableMetricException
	 * @throws ReportItemException
	 */
	@Test
	@Tag("unidad")
	@DisplayName("Prueba calcIndicator de RepositoryCalculator metricas incorrectas")
	void testCalIndicatorNotRequiredMetrics() throws NotAvailableMetricException, ReportItemException {
		// Creamos mocks necesarios, de menor a mayor
		// primero las métricas

		ReportItemI metrica1 = Mockito.mock(ReportItemI.class);
		ReportItemI metrica2 = Mockito.mock(ReportItemI.class);
		// Tengo que hacer que metrica1 y metrica2 devuelvan lo que devolvería el de
		// verdad
		Mockito.when(metrica1.getName()).thenReturn("issues");
		Mockito.when(metrica1.getValue()).thenReturn(2);

		Mockito.when(metrica2.getName()).thenReturn("closedIssues");
		Mockito.when(metrica2.getValue()).thenReturn(1.0);
		List<ReportItemI> metricsMock = new ArrayList<>();
		metricsMock.add(metrica1);
		metricsMock.add(metrica2);

		// Ahora la estrategia de cálculo
		// Empezando por Las dependencias que va indicar la estrategia de cálculo
		List<String> required = new ArrayList<String>();
		required.add("otradiferente");
		required.add("closedIssues");

		// El indicador que va a calcular
		ReportItemI indicator = Mockito.mock(ReportItemI.class);
		Mockito.when(indicator.getName()).thenReturn("indicador");

		IndicatorStrategy indicatorStrategyMock = Mockito.mock(IndicatorStrategy.class);
		Mockito.when(indicatorStrategyMock.requiredMetrics()).thenReturn(required);
		Mockito.when(indicatorStrategyMock.calcIndicator(metricsMock)).thenReturn(indicator);

		// Ahora gestor de informe
		// Empezamos creando el informe
		ReportI report = Mockito.mock(ReportI.class);
		Mockito.when(report.getAllMetrics()).thenReturn(metricsMock);
		Mockito.when(report.getMetricByName("issues")).thenReturn(metrica1);
		Mockito.when(report.getMetricByName("closedIssues")).thenReturn(metrica2);

		ReportManagerI reportManagerMock = Mockito.mock(ReportManagerI.class);
		Mockito.when(reportManagerMock.getMetric("issues")).thenReturn(metrica1);
		Mockito.when(reportManagerMock.getMetric("closedIssues")).thenReturn(metrica2);
		Mockito.when(reportManagerMock.getReport()).thenReturn(report);
		
		
		// Creamos la clase a probar
		RepositoryCalculator repositoryCalculator = new RepositoryCalculator();
		repositoryCalculator.setIndicator("indicator", indicatorStrategyMock);
		try {
			repositoryCalculator.calcIndicator("indicator", reportManagerMock);


		} catch (IndicatorException e) {
			fail("No debería lanzar la excepción");
			e.printStackTrace();
		}

		// Verificamos que se ha invocado el método para añadir la métrica que falta, pero las otras no
		Mockito.verify(reportManagerMock, times(1)).addMetric("otradiferente");
		Mockito.verify(reportManagerMock, times(0)).addMetric("issues");
		Mockito.verify(reportManagerMock, times(0)).addMetric("clossedIssues");
		
		
		// Verificamos que se han comprobado las métricas, invocado el método de cálculo
		// y que el indicador se ha añadido al informe
		Mockito.verify(indicatorStrategyMock, times(1)).requiredMetrics();
		Mockito.verify(indicatorStrategyMock, times(1)).calcIndicator(metricsMock);
		Mockito.verify(report, times(1)).addIndicator(indicator);
		//Al menos una vez
		Mockito.verify(reportManagerMock,Mockito.atLeastOnce()).getReport();
		
		Mockito.verify(report,Mockito.atLeastOnce()).getAllMetrics();
	}

	/**
	 * Test para el metodo calcIndicator de RepositoryCalculator cuando las métricas
	 * disponibles son suficientes
	 * {@link us.muit.fs.a4i.control.RepositoryCalculator.calcIndicator(String,
	 * ReportManagerI)}.
	 * 
	 * @throws NotAvailableMetricException
	 * @throws ReportItemException
	 */
	@Test
	@Tag("unidad")
	@DisplayName("Prueba calcIndicator de  RepositoryCalculator metricas correctas")
	void testCalIndicatorWithRequiredMetrics() throws NotAvailableMetricException, ReportItemException {
		// Si no queremos depender de los objetos reales habría que eliminar las
		// dependencias, pero en este caso se ha etiquetado como prueba
		// de integración

		// Creamos la clase a probar
		RepositoryCalculator repositoryCalculator = new RepositoryCalculator();

		// Creamos mocks necesarios

		ReportItemI metrica1 = Mockito.mock(ReportItemI.class);
		ReportItemI metrica2 = Mockito.mock(ReportItemI.class);
		// Tengo que hacer que metrica1 y metrica2 devuelvan lo que devolvería el de
		// verdad
		Mockito.when(metrica1.getName()).thenReturn("issues");
		Mockito.when(metrica1.getValue()).thenReturn(2);

		Mockito.when(metrica2.getName()).thenReturn("closedIssues");
		Mockito.when(metrica2.getValue()).thenReturn(1.0);
		List<ReportItemI> metricsMock = new ArrayList<>();
		metricsMock.add(metrica1);
		metricsMock.add(metrica2);
		List<String> required = new ArrayList<String>();
		required.add("issues");
		required.add("closedIssues");

		ReportItemI indicator = Mockito.mock(ReportItemI.class);
		Mockito.when(indicator.getName()).thenReturn("indicador");

		ReportI report = Mockito.mock(ReportI.class);
		Mockito.when(report.getAllMetrics()).thenReturn(metricsMock);

		ReportManagerI reportManagerMock = Mockito.mock(ReportManagerI.class);
		Mockito.when(reportManagerMock.getMetric("issues")).thenReturn(metrica1);
		Mockito.when(reportManagerMock.getMetric("closedIssues")).thenReturn(metrica2);
		Mockito.when(reportManagerMock.getReport()).thenReturn(report);

		IndicatorStrategy indicatorStrategyMock = Mockito.mock(IndicatorStrategy.class);
		Mockito.when(indicatorStrategyMock.calcIndicator(metricsMock)).thenReturn(indicator);
		Mockito.when(indicatorStrategyMock.requiredMetrics()).thenReturn(required);

		repositoryCalculator.setIndicator("indicator", indicatorStrategyMock);
		try {
			repositoryCalculator.calcIndicator("indicator", reportManagerMock);
		} catch (IndicatorException e) {
			fail("No debería lanzar la excepción");
			e.printStackTrace();
		}
		// Verificamos que NO se ha invocado el método para añadir ninguna de las
		// métricas
		Mockito.verify(reportManagerMock, times(0)).addMetric("issues");
		Mockito.verify(reportManagerMock, times(0)).addMetric("clossedIssues");

		// Verificamos que se han comprobado las métricas, invocado el método de cálculo
		// y que el indicador se ha añadido al informe
		Mockito.verify(indicatorStrategyMock, times(1)).requiredMetrics();
		Mockito.verify(indicatorStrategyMock, times(1)).calcIndicator(metricsMock);
		Mockito.verify(report, times(1)).addIndicator(indicator);
		
		//Al menos una vez
		Mockito.verify(reportManagerMock,Mockito.atLeastOnce()).getReport();			
		Mockito.verify(report,Mockito.atLeastOnce()).getAllMetrics();
	}

}
