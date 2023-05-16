package us.muit.fs.a4i.test.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.atLeast;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import us.muit.fs.a4i.control.IndicatorsCalculator;
import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.model.entities.Report;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;

/**
 * <p>
 * Test para probar la clase Report
 * </p>
 * 
 * @author Isabel Román
 *
 */
@ExtendWith(MockitoExtension.class)
class ReportTest {
	private static Logger log = Logger.getLogger(ReportTest.class.getName());
	/**
	 * <p>
	 * Objetos tipo Mock, sustitutos de las clases de las que depende Report
	 * </p>
	 * 
	 */
	@Mock(serializable = true)
	private static IndicatorsCalculator indCalcMock = Mockito.mock(IndicatorsCalculator.class);

	// Servirán para conocer el argumento con el que se ha invocado algún método
	// de alguno de los mocks (sustitutos o representantes)
	// ArgumentCaptor es un genérico, indico al declararlo el tipo del argumento
	// que quiero capturar
	@Captor
	private ArgumentCaptor<Integer> intCaptor;
	@Captor
	private ArgumentCaptor<String> strCaptor;
	@Captor
	private ArgumentCaptor<ReportItem> reportItemCaptor;
	@Captor
	private ArgumentCaptor<Indicator> indicatorCaptor;
	@Captor
	private ArgumentCaptor<Report> reportCaptor;

	@Mock(serializable = true)
	private static ReportItem metricIntMock = Mockito.mock(ReportItem.class);
	@Mock(serializable = true)
	private static ReportItem metricDatMock = Mockito.mock(ReportItem.class);
	@Mock(serializable = true)
	private static ReportItem<Integer> indicatorIntMock = Mockito.mock(ReportItem.class);
	@Mock(serializable = true)
	private static ReportItem<Date> indicatorDatMock = Mockito.mock(ReportItem.class);
	private static Report reportTested;

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
	 * Test del constructor simple Test method for
	 * {@link us.muit.fs.a4i.model.entities.Report#Report()}.
	 */
	@Test
	@Tag("noacabado")
	void testReport() {
		// fail("Not yet implemented"); // TODO
		// El constructor está formado por el string id y por el tipo type.
		reportTested = new Report("entityId");
		// reportTested=new Report("entityId"); falta el type
		reportTested = new Report();
		ReportI.ReportType type = reportTested.getType();
		assertEquals(type, reportTested.getType(), "No se establece correctamente el tipo del informe");
		assertEquals("entityId", reportTested.getEntityId(),
				"No se establece correctamente el identificador del informe");
	}

	/**
	 * Test del constructor pasándole el id Test method for
	 * {@link us.muit.fs.a4i.model.entities.Report#Report(java.lang.String)}.
	 */
	@Test
	@Tag("noacabado")
	void testReportString() {
		reportTested = new Report("entityId");
		assertEquals("entityId", reportTested.getEntityId(),
				"No se establece correctamente el identificador del informe");

	}

	@Test
	@Tag("noacabado")
	void testReportType() {
		reportTested = new Report();
		ReportI.ReportType type = reportTested.getType();
		assertEquals(type, reportTested.getType(), "No se establece correctamente el tipo del informe");
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.model.entities.Report#getMetricByName(java.lang.String)}.
	 */
	@Test
	@Tag("noacabado")
	void testGetMetricByName() {
		// TODO
		String name = "nombre";
		// Metric metric=new Metric();
		ReportItem metric = null;
		reportTested = new Report();
		reportTested.getMetricByName(name);
		assertEquals(null, reportTested.getMetricByName(name),
				"No se establece correctamente el buscador del nombre de la métrica");

	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.model.entities.Report#addMetric(us.muit.fs.a4i.model.entities.Metric)}.
	 */
	@Test
	void testAddMetric() {
		reportTested = new Report();
		setMetricsMocks();
		// Primero se prueba a añadir una métrica de tipo Integer
		reportTested.addMetric(metricIntMock);
		/*
		 * //Verifico que se ha consultado el nombre una vez al invocar este m�todo, se
		 * usa como clave para meterlo en un mapa, hay que consultarlo //�Por qu� falla?
		 * �Con qu� no hab�a contado? �Hay problemas en el test o en el c�digo? //Prueba
		 * a sustituir por la l�nea comentada //Mockito.verify(metricIntMock).getName();
		 * Mockito.verify(metricIntMock, atLeast(1)).getName(); ReportItem
		 * metric=reportTested.getMetricByName("issues");
		 * assertEquals(metric.getValue(),
		 * 3,"Deber�a tener el valor especificado en el mock");
		 * assertEquals(metric.getDescription(),"Tareas sin finalizar en el repositorio"
		 * ,"Deber�a tener el valor especificado en el mock");
		 */

		// Verifico que se ha consultado el nombre una vez al invocar este m�todo, se
		// usa como clave para meterlo en un mapa, hay que consultarlo
		// �Por qu� falla? �Con qu� no hab�a contado? �Hay problemas en el test o en el
		// c�digo?
		// Prueba a sustituir por la l�nea comentada
		Mockito.verify(metricIntMock, atLeast(1)).getName();
		// Mockito.verify(metricIntMock, atLeast(1)).getName();
		ReportItemI metric = reportTested.getMetricByName("issues");
		assertEquals(metric.getValue(), 3, "Deber�aa tener el valor especificado en el mock");
		assertEquals(metric.getDescription(), "Tareas sin finalizar en el repositorio",
				"Deber�a tener el valor especificado en el mock");

		// Ahora se prueba una métrica de tipo Date
		reportTested.addMetric(metricDatMock);
		metric = reportTested.getMetricByName("lastPush");
		assertEquals(metric.getValue(), metricDatMock.getValue(), "Debería tener el valor especificado en el mock");
		assertEquals(metric.getDescription(), "Último push realizado en el repositorio",
				"Debería tener el valor especificado en el mock");

		// Ahora se prueba a añadir otra vez la misma métrica pero con otro valor
		reportTested.addMetric(metricIntMock);
		Mockito.when(metricIntMock.getValue()).thenReturn(55);
		metric = reportTested.getMetricByName("issues");
		assertEquals(metric.getValue(), 55, "Debería tener el valor especificado en el mock");
		assertEquals(metric.getDescription(), "Tareas sin finalizar en el repositorio",
				"Debería tener el valor especificado en el mock");
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.model.entities.Report#getIndicatorByName(java.lang.String)}.
	 */
	@Test
	@Tag("noacabado")
	void testGetIndicatorByName() {
		// TODO
		String name = "nombre";
		// Metric metric=new Metric();
		ReportItem indicator = null;
		reportTested = new Report();
		// reportTested.getIndicatorByName(name);
		assertEquals(null, reportTested.getIndicatorByName(name),
				"No se establece correctamente el buscador del nombre del indicador");
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.model.entities.Report#addIndicator(us.muit.fs.a4i.model.entities.Indicator)}.
	 */
	@Test
	@Tag("noacabado")
	void testAddIndicator() {
		// fail("Not yet implemented"); // TODO, HECHO

		reportTested = new Report();
		setIndicatorsMocks();
		// Primero se prueba a añadir una métrica de tipo Integer
		reportTested.addIndicator(indicatorIntMock);
		// Verifico que se ha consultado el nombre una vez al invocar este método, se
		// usa como clave para meterlo en un mapa, hay que consultarlo
		// ¿Por qué falla? ¿Con qué no había contado? ¿Hay problemas en el test o
		// en el código?
		// Prueba a sustituir por la línea comentada
		Mockito.verify(indicatorIntMock).getName();
		// Mockito.verify(metricIntMock, atLeast(1)).getName();
		ReportItemI indicator = reportTested.getIndicatorByName("issues");
		assertEquals(indicator.getValue(), 3, "Debería tener el valor especificado en el mock");
		assertEquals(indicator.getDescription(), "Tareas sin finalizar en el repositorio",
				"Debería tener el valor especificado en el mock");

		// Ahora se prueba una métrica de tipo Date
		reportTested.addIndicator(indicatorDatMock);
		indicator = reportTested.getIndicatorByName("lastPush");
		assertEquals(indicator.getValue(), metricDatMock.getValue(), "Debería tener el valor especificado en el mock");
		assertEquals(indicator.getDescription(), "Último push realizado en el repositorio",
				"Debería tener el valor especificado en el mock");

		// Ahora se prueba a añadir otra vez la misma métrica pero con otro valor
		reportTested.addIndicator(indicatorIntMock);
		Mockito.when(indicatorIntMock.getValue()).thenReturn(55);
		indicator = reportTested.getIndicatorByName("issues");
		assertEquals(indicator.getValue(), 55, "Debería tener el valor especificado en el mock");
		assertEquals(indicator.getDescription(), "Tareas sin finalizar en el repositorio",
				"Debería tener el valor especificado en el mock");

	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.model.entities.Report#calcIndicator(java.lang.String)}.
	 */
//	@Test
//	void testCalcIndicator() {
//		//Definimos calculadora de tipo repositorio
//		Mockito.when(indCalcMock.getReportType()).thenReturn(Report.Type.REPOSITORY);
//		
//		
//		//Se configura la calculadora de indicadores del informe
//		try {
//			reportTested.setIndicatorsCalculator(indCalcMock);
//			Mockito.verify(indCalcMock).getReportType();
//		} catch (IndicatorException e1) {
//			fail("No debería lanzar la excepción");
//		}
//		//Se solicita el cálculo de un indicador determinado
//		reportTested.calcIndicator("issues");
//		//Se observa con qué parámetros se invoca a la calculadora de indicadores
//		try {
//			Mockito.verify(indCalcMock).calcIndicator(strCaptor.capture(), reportCaptor.capture());
//			//Elimine el comentario que aparece a continuación, ejecute el test y explique por qué falla
//			//Mockito.verify(indCalcMock).calcAllIndicators(reportTested);
//		} catch (IndicatorException e) {
//			fail("No debería lanzar la excepción");
//		}
//		
//		//Se verfica que se usa el nombre correcto y se pasa la referencia al informe correcto
//		assertEquals("issues",strCaptor.getValue(),"Se solicita el cálculo de la métrica adecuada");
//		assertEquals(reportTested,reportCaptor.getValue(),"Se pasa la referencia correcta del informe");
//		//Hago un test que asegure que el propio informe captura y gestiona la excepción de que el indicador no existe
//		try {
//			Mockito.doThrow(new IndicatorException("El indicador no existe")).when(indCalcMock).calcIndicator("indKO", reportTested);
//			reportTested.calcIndicator("indKO");			
//		
//		} catch (IndicatorException e) {
//			fail("La excepción la debe gestionar el propio informe");
//		}    
//		
//	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.model.entities.Report#setId(java.lang.String)}.
	 */
//	@Test
//	@Tag("noacabado")
//	void testSetId() {
//		fail("Not yet implemented"); // TODO
//	}

	/**
	 * Test method for {@link us.muit.fs.a4i.model.entities.Report#getId()}.
	 */
	@Test
	@Tag("noacabado")
	void testGetEntityId() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.model.entities.Report#setIndicatorsCalculator(us.muit.fs.a4i.control.IndicatorsCalculator)}.
	 */
//	@Test
//	void testSetIndicatorsCalculator() {
//		//Definimos calculadora de tipo repositorio
//		Mockito.when(indCalcMock.getReportType()).thenReturn(ReportI.Type.REPOSITORY);
//		ReportI orgReport=new Report(ReportI.Type.ORGANIZATION);
//		ReportI repoReport=new Report(ReportI.Type.REPOSITORY);
//		ReportI report=new Report();
//		//Vamos a probar establecer la calculadora en un informe que no tiene el tipo aún establecido (Debería tener el tipo de la calculadora al final)
//		//Para ello usamos report
//		try {
//			report.setIndicatorsCalculator(indCalcMock);
//			//Se ha tenido que consultar el tipo de calculadora
//			Mockito.verify(indCalcMock).getReportType();
//			assertEquals(indCalcMock.getReportType(),report.getType());
//		} catch (IndicatorException e) {
//			fail("No debería lanzar excepción");
//		}
//		
//		//Vamos a probar a establecer la calculadora si el tipo de ambos coincide, uso repoReport
//		try {
//			repoReport.setIndicatorsCalculator(indCalcMock);
//			//Se ha tenido que consultar el tipo de calculadora
//			//Mockito.verify(indCalcMock, times(2)).getReportType();
//			assertEquals(indCalcMock.getReportType(),repoReport.getType());
//		} catch (IndicatorException e) {
//			fail("No debería lanzar excepción");
//		}
//		
//		//Vamos a probar a establecer la calculadora si el tipo de la calculadora discrepa con el tipo del informe, uso orgReport
//	
//		try {
//			orgReport.setIndicatorsCalculator(indCalcMock);
//			//Se ha tenido que consultar el tipo de calculadora
//			fail("Debe saltar una excepción antes, no debería llegar aquí");
//		} catch (IndicatorException e) {
//			
//			log.info("Ha saltado la excepción de indicador");
//			//Suponga que los requisitos cambian, le piden que el mensaje debe ser "El tipo de la calculadora discrepa del tipo del informe"
//			//Cambie el test para que lo verifique y ejecute ¿Qué ocurre?
//			assertEquals(e.getMessage(),"La calculadora no concuerda con el tipo de informe","El mensaje es correcto");
//		} catch(Exception e) {
//			fail("La excepción no es del tipo IndicatorException como se esperaba");
//		}
//		//Esta verificación es para mostrar que se puede analizar también el comportamiento interno de la clase
//		//en esta ocasión el número de veces que invoca a la calculadora durante el test
//		//Probar a cambiar 5 por otro número y ejecutar
//		Mockito.verify(indCalcMock,times(5)).getReportType();
//	
//	}

	// ----------------------------------------------------------------------------------------------------------
	/**
	 * Test method for {@link us.muit.fs.a4i.model.entities.Report#toString()}.
	 * toString devuelve un tipo string de repoinfo formado por: "Informaci�n del
	 * Informe:\n - M�tricas: " "\n Clave: " + clave + metrics.get(clave) "\n Clave:
	 * " + clave + indicators.get(clave)
	 */
	@Test
	@Tag("noacabado")
	void testToString() {
		reportTested = new Report();
		String refString = "referencia";
		try {
			assertEquals(reportTested.toString().getClass(), refString.getClass(), "Comparación de tipo String");
		} catch (Exception e) {
			fail("ToString no devuelve un tipo String");
		}
	}
	// ----------------------------------------------------------------------------------------------------------

	/**
	 * Test method for {@link us.muit.fs.a4i.model.entities.Report#getAllMetrics()}.
	 */
	@Test
	@Tag("noacabado")
	void testGetAllMetrics() {
		ReportItemI metric = null;
		reportTested = new Report();

		try {
			assertNull(reportTested.getAllMetrics(), "All metric Null");
		} catch (Exception e) {
			fail("All metric null");
		}
	}

	void setMetricsMocks() {
		Date date = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
		Mockito.when(metricIntMock.getName()).thenReturn("issues");
		Mockito.when(metricIntMock.getDescription()).thenReturn("Tareas sin finalizar en el repositorio");
		Mockito.when(metricIntMock.getValue()).thenReturn(3);

		Mockito.when(metricDatMock.getName()).thenReturn("lastPush");
		Mockito.when(metricDatMock.getDescription()).thenReturn("Último push realizado en el repositorio");
		Mockito.when(metricDatMock.getValue()).thenReturn(date);
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.model.entities.Report#getAllIndicators()}.
	 */
	@Test
	@Tag("noacabado")
	void testGetAllIndicators() {
		ReportItemI indicator = null;
		reportTested = new Report();

		try {
			assertNull(reportTested.getAllIndicators(), "All indicator Null");
		} catch (Exception e) {
			fail("Al indicators null");
		}
	}

	void setIndicatorsMocks() {
		Date date = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
		Mockito.when(indicatorIntMock.getName()).thenReturn("issues");
		Mockito.when(indicatorIntMock.getDescription()).thenReturn("Tareas sin finalizar en el repositorio");
		Mockito.when(indicatorIntMock.getValue()).thenReturn(3);

		Mockito.when(indicatorDatMock.getName()).thenReturn("lastPush");
		Mockito.when(indicatorDatMock.getDescription()).thenReturn("Último push realizado en el repositorio");
		Mockito.when(indicatorDatMock.getValue()).thenReturn(date);
	}

}
