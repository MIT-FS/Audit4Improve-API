package us.muit.fs.a4i.test.control;

/***
 * @author celllarod, curso 22/23
 * Pruebas añadidas por alumnos del curso 22/23 para probar la clase RepositoryCalculator
 */

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

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
import us.muit.fs.a4i.control.RepositoryCalculator;
import us.muit.fs.a4i.control.strategies.IssuesRatioIndicatorStrategy;
import us.muit.fs.a4i.exceptions.IndicatorException;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.IndicatorI;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItem.ReportItemBuilder;
import us.muit.fs.a4i.model.entities.ReportItemI;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RepositoryCalculatorTest {

	private static Logger log = Logger.getLogger(ReportManagerTest.class.getName());

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

	/**
	 * Test para el metodo calcIndicator de RepositoryCalculator
	 * {@link us.muit.fs.a4i.control.RepositoryCalculator.calcIndicator(String,
	 * ReportManagerI)}.
	 */
	@Test
	@DisplayName("Prueba calcIndicator de  RepositoryCalculator")
	void testCalIndicator() {

		// Creamos la clase a probar
		RepositoryCalculator repositoryCalculator = new RepositoryCalculator();

		// Creamos mocks necesarios
		ReportManagerI reportManagerMock = Mockito.mock(ReportManagerI.class);
		ReportI report = Mockito.mock(ReportI.class);

		List<ReportItemI> metricsMock = new ArrayList<>();
		ReportItemI metric1 = Mockito.mock(ReportItemI.class);
		// Faltaba: configurar el método getName de las métricas
		Mockito.when(metric1.getName()).thenReturn("metric1");
		ReportItemI metric2 = Mockito.mock(ReportItemI.class);
		Mockito.when(metric2.getName()).thenReturn("metric2");

		metricsMock.add(metric1);
		metricsMock.add(metric2);

		IndicatorStrategy indicatorStrategyMock = Mockito.mock(IndicatorStrategy.class);

		Mockito.when(reportManagerMock.getReport()).thenReturn(report);

		// Metrics
		Mockito.when(report.getAllMetrics()).thenReturn(metricsMock);

		// Required metrics para el indicador forzando a que coincidan con las m�tricas
		// creadas
		List<String> requiredMetrics = new ArrayList<>();
		requiredMetrics.add(metric1.getName());
		requiredMetrics.add(metric2.getName());
		ReportItemI nuevoIndicador = Mockito.mock(ReportItemI.class);
		Mockito.when(nuevoIndicador.getName()).thenReturn("nuevoIndicador");
		Mockito.when(indicatorStrategyMock.requiredMetrics()).thenReturn(requiredMetrics);
		try {
			Mockito.when(indicatorStrategyMock.calcIndicator(metricsMock)).thenReturn(nuevoIndicador);
		} catch (NotAvailableMetricException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Seteamos alguna estrategia
		repositoryCalculator.setIndicator("indicator1", indicatorStrategyMock);

		// Llamamos a calIndicator
		try {
			repositoryCalculator.calcIndicator("indicator1", reportManagerMock);
		} catch (IndicatorException e) {
			e.printStackTrace();
		}

		// Verificamos que cada uno de los m�todos hayan sido llamados

		Mockito.verify(report).getAllMetrics();
		Mockito.verify(indicatorStrategyMock).requiredMetrics();
		try {
			Mockito.verify(indicatorStrategyMock).calcIndicator(metricsMock);
		} catch (NotAvailableMetricException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test para el metodo calcIndicator de RepositoryCalculator Verifica que no
	 * llama a calIndiicator si no se le pasa la m�trica adecuada
	 * {@link us.muit.fs.a4i.control.RepositoryCalculator.calcIndicator(String,
	 * ReportManagerI)}.
	 * 
	 * @throws NotAvailableMetricException
	 * @throws ReportItemException
	 */
	@Test
	@Tag("unit")
	@DisplayName("Prueba calcIndicator de RepositoryCalculator con metricas incorrectas y usando mocks")
	void unitTestCalIndicatorNotRequiredMetrics() throws NotAvailableMetricException, ReportItemException {
		// prueba la calculadora usando mocks, para el caso de que las métricas necesarias no estén disponibles

		// Creamos la clase a probar
		RepositoryCalculator repositoryCalculator = new RepositoryCalculator();

		// Creamos mocks necesarios
		ReportManagerI reportManagerMock = Mockito.mock(ReportManagerI.class);
		ReportI report = Mockito.mock(ReportI.class);

		List<ReportItemI> metricsMock = new ArrayList<>();
		ReportItemI metric1 = Mockito.mock(ReportItemI.class);
		// Faltaba: configurar el método getName de las métricas
		Mockito.when(metric1.getName()).thenReturn("metric1");
		ReportItemI metric2 = Mockito.mock(ReportItemI.class);
		Mockito.when(metric2.getName()).thenReturn("metric2");

		metricsMock.add(metric1);
		metricsMock.add(metric2);

		IndicatorStrategy indicatorStrategyMock = Mockito.mock(IndicatorStrategy.class);

		Mockito.when(reportManagerMock.getReport()).thenReturn(report);

		// Metrics
		Mockito.when(report.getAllMetrics()).thenReturn(metricsMock);

		// Required metrics para el indicador forzando a que coincidan con las m�tricas
		// creadas
		List<String> requiredMetrics = new ArrayList<>();
		requiredMetrics.add("otra1");
		requiredMetrics.add("otra2");
		ReportItemI nuevoIndicador = Mockito.mock(ReportItemI.class);
		Mockito.when(nuevoIndicador.getName()).thenReturn("nuevoIndicador");
		Mockito.when(indicatorStrategyMock.requiredMetrics()).thenReturn(requiredMetrics);

		// Seteamos alguna estrategia
		repositoryCalculator.setIndicator("indicator1", indicatorStrategyMock);

		// Llamamos a calIndicator
		try {
			repositoryCalculator.calcIndicator("indicator1", reportManagerMock);
		} catch (IndicatorException e) {
			e.printStackTrace();
		}

		// Verificamos que no se ha llamado al m�todo
		Mockito.verify(indicatorStrategyMock, times(0)).calcIndicator(metricsMock);
	}

	/**
	 * Test para el metodo calcIndicator de RepositoryCalculator Verifica que no
	 * llama a calIndiicator si no se le pasa la m�trica adecuada
	 * {@link us.muit.fs.a4i.control.RepositoryCalculator.calcIndicator(String,
	 * ReportManagerI)}.
	 * 
	 * @throws NotAvailableMetricException
	 * @throws ReportItemException
	 */
	@Test
	@Tag("integration")
	@DisplayName("Prueba calcIndicator de  RepositoryCalculator metricas incorrectas")
	void testCalIndicatorNotRequiredMetrics() throws NotAvailableMetricException, ReportItemException {
		// Si no queremos depender de los objetos reales habría que eliminar las
		// dependencias, pero en este caso se ha etiquetado como prueba
		// de integración

		// Creamos la clase a probar
		RepositoryCalculator repositoryCalculator = new RepositoryCalculator();

		// Creamos mocks necesarios
		ReportManagerI reportManagerMock = Mockito.mock(ReportManagerI.class);
		ReportI report = Mockito.mock(ReportI.class);

		List<ReportItemI> metricsMock = new ArrayList<>();

		ReportItemBuilder<Integer> mb1 = new ReportItem.ReportItemBuilder<Integer>("issues", 2);
		ReportItemBuilder<Double> mb2 = new ReportItem.ReportItemBuilder<Double>("closedIssues", 1.0);

		metricsMock.add(mb1.build());
		metricsMock.add(mb2.build());

		IndicatorStrategy indicatorStrategyMock = Mockito.mock(IndicatorStrategy.class);

		Mockito.when(reportManagerMock.getReport()).thenReturn(report);

		// Metrics
		Mockito.when(report.getAllMetrics()).thenReturn(metricsMock);

		// Seteamos alguna estrategia
		repositoryCalculator.setIndicator("issuesRatio", new IssuesRatioIndicatorStrategy());

		// Verificamos que no se ha llamado al m�todo
		Mockito.verify(indicatorStrategyMock, times(0)).calcIndicator(metricsMock);
	}

}
