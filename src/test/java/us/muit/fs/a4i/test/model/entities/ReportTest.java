package us.muit.fs.a4i.test.model.entities;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.logging.Logger;


import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mockitoSession;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;


import us.muit.fs.a4i.control.IndicatorsCalculator;
import us.muit.fs.a4i.exceptions.IndicatorException;
import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.model.entities.Metric;
import us.muit.fs.a4i.model.entities.Report;
import us.muit.fs.a4i.model.entities.ReportI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;


/**
 * <p>Test para probar la clase Report</p>
 * @author Isabel RomÃ¡n
 *
 */
@ExtendWith(MockitoExtension.class)
class ReportTest {
	private static Logger log=Logger.getLogger(ReportTest.class.getName());
	/**
	 * <p>Objetos tipo Mock, sustitutos de las clases de las que depende Report</p>
	 * 
	 */
	@Mock(serializable =true)
	private static IndicatorsCalculator indCalcMock= Mockito.mock(IndicatorsCalculator.class);
		
	
	//ServirÃ¡n para conocer el argumento con el que se ha invocado algÃºn mÃ©todo de alguno de los mocks (sustitutos o representantes)
    //ArgumentCaptor es un genÃ©rico, indico al declararlo el tipo del argumento que quiero capturar
	@Captor
	private ArgumentCaptor<Integer> intCaptor;
	@Captor
	private ArgumentCaptor<String> strCaptor;
	@Captor
	private ArgumentCaptor<Metric> metricCaptor;
	@Captor
	private ArgumentCaptor<Indicator> indicatorCaptor;
	@Captor
	private ArgumentCaptor<Report> reportCaptor;
	
	@Mock(serializable = true)
	private static Metric<Integer> metricIntMock= Mockito.mock(Metric.class);
	@Mock(serializable = true)
	private static Metric<Date> metricDatMock= Mockito.mock(Metric.class);
	@Mock(serializable = true)
	private static Indicator<Integer> indicatorIntMock= Mockito.mock(Indicator.class);
	@Mock(serializable = true)
	private static Indicator<Date> indicatorDatMock= Mockito.mock(Indicator.class);

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
	 * Test del constructor simple
	 * Test method for {@link us.muit.fs.a4i.model.entities.Report#Report()}.
	 */
	@Test
	@Tag("noacabado")
	void testReport() {
		//fail("Not yet implemented"); // TODO
		//El constructor estÃ¡ formado por el string id y por el tipo type.
		reportTested=new Report("Id");
		//reportTested=new Report("Id"); falta el type
		reportTested=new Report();
		ReportI.Type type=reportTested.getType();
		assertEquals(type,reportTested.getType(),"No se establece correctamente el tipo del informe");
		assertEquals("Id",reportTested.getId(),"No se establece correctamente el identificador del informe");
	}
	
	
	
	
	
	

	/**
	 * Test del constructor pasÃ¡ndole el id
	 * Test method for {@link us.muit.fs.a4i.model.entities.Report#Report(java.lang.String)}.
	 */
	@Test
	@Tag("noacabado")
	void testReportString() {
		reportTested=new Report("Id");
		assertEquals("Id",reportTested.getId(),"No se establece correctamente el identificador del informe");
			
	}
	
	
	
	
	
	
	
	
	
	
	
	
	@Test
	@Tag("noacabado")
	void testReportType() {
		reportTested=new Report();
		ReportI.Type type=reportTested.getType();
		assertEquals(type,reportTested.getType(),"No se establece correctamente el tipo del informe");	
	}

	
	
	/**
	 * Test method for {@link us.muit.fs.a4i.model.entities.Report#getMetricByName(java.lang.String)}.
	 */
	@Test
	@Tag("noacabado")
	void testGetMetricByName() {
		// TODO
		String name="nombre";
		//Metric metric=new Metric();
		Metric metric= null;
		reportTested=new Report();
		reportTested.getMetricByName(name);
		assertEquals(null,reportTested.getMetricByName(name),"No se establece correctamente el buscador del nombre de la mÃ©trica");
		
	}
	
	
	
	/**
	 * Test method for {@link us.muit.fs.a4i.model.entities.Report#addMetric(us.muit.fs.a4i.model.entities.Metric)}.
	 */
	@Test
	void testAddMetric() {
		reportTested=new Report();	
		setMetricsMocks();		
		//Primero se prueba a aÃ±adir una mÃ©trica de tipo Integer
		reportTested.addMetric(metricIntMock);
		//Verifico que se ha consultado el nombre una vez al invocar este mÃ©todo, se usa como clave para meterlo en un mapa, hay que consultarlo
		//Â¿Por quÃ© falla? Â¿Con quÃ© no habÃ­a contado? Â¿Hay problemas en el test o en el cÃ³digo?
		//Prueba a sustituir por la lÃ­nea comentada
		Mockito.verify(metricIntMock).getName();
		//Mockito.verify(metricIntMock, atLeast(1)).getName();
		Metric metric=reportTested.getMetricByName("issues");
		assertEquals(metric.getValue(),3,"DeberÃ­a tener el valor especificado en el mock");
		assertEquals(metric.getDescription(),"Tareas sin finalizar en el repositorio","DeberÃ­a tener el valor especificado en el mock");
		
		//Ahora se prueba una mÃ©trica de tipo Date
		reportTested.addMetric(metricDatMock);
		metric=reportTested.getMetricByName("lastPush");
		assertEquals(metric.getValue(),metricDatMock.getValue(),"DeberÃ­a tener el valor especificado en el mock");
		assertEquals(metric.getDescription(),"Ãšltimo push realizado en el repositorio","DeberÃ­a tener el valor especificado en el mock");
		
		//Ahora se prueba a aÃ±adir otra vez la misma mÃ©trica pero con otro valor		
		reportTested.addMetric(metricIntMock);
		Mockito.when(metricIntMock.getValue()).thenReturn(55);	
		metric=reportTested.getMetricByName("issues");
		assertEquals(metric.getValue(),55,"DeberÃ­a tener el valor especificado en el mock");
		assertEquals(metric.getDescription(),"Tareas sin finalizar en el repositorio","DeberÃ­a tener el valor especificado en el mock");
	}
	
	
	/**
	 * Test method for {@link us.muit.fs.a4i.model.entities.Report#getIndicatorByName(java.lang.String)}.
	 */
	@Test
	@Tag("noacabado")
	void testGetIndicatorByName() {
		// TODO
		String name="nombre";
		//Metric metric=new Metric();
		Indicator indicator= null;
		reportTested=new Report();
		//reportTested.getIndicatorByName(name);
		assertEquals(null,reportTested.getIndicatorByName(name),"No se establece correctamente el buscador del nombre del indicador");
		
	}
	
	
	
	

	/**
	 * Test method for {@link us.muit.fs.a4i.model.entities.Report#addIndicator(us.muit.fs.a4i.model.entities.Indicator)}.
	 */
	@Test
	@Tag("noacabado")
	void testAddIndicator() {
		//fail("Not yet implemented"); // TODO, HECHO

			reportTested=new Report();	
			setIndicatorsMocks();		
			//Primero se prueba a aÃ±adir una mÃ©trica de tipo Integer
			reportTested.addIndicator(indicatorIntMock);
			//Verifico que se ha consultado el nombre una vez al invocar este mÃ©todo, se usa como clave para meterlo en un mapa, hay que consultarlo
			//Â¿Por quÃ© falla? Â¿Con quÃ© no habÃ­a contado? Â¿Hay problemas en el test o en el cÃ³digo?
			//Prueba a sustituir por la lÃ­nea comentada
			Mockito.verify(indicatorIntMock).getName();
			//Mockito.verify(metricIntMock, atLeast(1)).getName();
			Indicator indicator=reportTested.getIndicatorByName("issues");
			assertEquals(indicator.getValue(),3,"DeberÃ­a tener el valor especificado en el mock");
			assertEquals(indicator.getDescription(),"Tareas sin finalizar en el repositorio","DeberÃ­a tener el valor especificado en el mock");
			
			//Ahora se prueba una mÃ©trica de tipo Date
			reportTested.addIndicator(indicatorDatMock);
			indicator=reportTested.getIndicatorByName("lastPush");
			assertEquals(indicator.getValue(),metricDatMock.getValue(),"DeberÃ­a tener el valor especificado en el mock");
			assertEquals(indicator.getDescription(),"Ãšltimo push realizado en el repositorio","DeberÃ­a tener el valor especificado en el mock");
			
			//Ahora se prueba a aÃ±adir otra vez la misma mÃ©trica pero con otro valor		
			reportTested.addIndicator(indicatorIntMock);
			Mockito.when(indicatorIntMock.getValue()).thenReturn(55);	
			indicator=reportTested.getIndicatorByName("issues");
			assertEquals(indicator.getValue(),55,"DeberÃ­a tener el valor especificado en el mock");
			assertEquals(indicator.getDescription(),"Tareas sin finalizar en el repositorio","DeberÃ­a tener el valor especificado en el mock");
		
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.model.entities.Report#calcIndicator(java.lang.String)}.
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
//			fail("No deberÃ­a lanzar la excepciÃ³n");
//		}
//		//Se solicita el cÃ¡lculo de un indicador determinado
//		reportTested.calcIndicator("issues");
//		//Se observa con quÃ© parÃ¡metros se invoca a la calculadora de indicadores
//		try {
//			Mockito.verify(indCalcMock).calcIndicator(strCaptor.capture(), reportCaptor.capture());
//			//Elimine el comentario que aparece a continuaciÃ³n, ejecute el test y explique por quÃ© falla
//			//Mockito.verify(indCalcMock).calcAllIndicators(reportTested);
//		} catch (IndicatorException e) {
//			fail("No deberÃ­a lanzar la excepciÃ³n");
//		}
//		
//		//Se verfica que se usa el nombre correcto y se pasa la referencia al informe correcto
//		assertEquals("issues",strCaptor.getValue(),"Se solicita el cÃ¡lculo de la mÃ©trica adecuada");
//		assertEquals(reportTested,reportCaptor.getValue(),"Se pasa la referencia correcta del informe");
//		//Hago un test que asegure que el propio informe captura y gestiona la excepciÃ³n de que el indicador no existe
//		try {
//			Mockito.doThrow(new IndicatorException("El indicador no existe")).when(indCalcMock).calcIndicator("indKO", reportTested);
//			reportTested.calcIndicator("indKO");			
//		
//		} catch (IndicatorException e) {
//			fail("La excepciÃ³n la debe gestionar el propio informe");
//		}    
//		
//	}

	/**
	 * Test method for {@link us.muit.fs.a4i.model.entities.Report#setId(java.lang.String)}.
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
	 * Test method for {@link us.muit.fs.a4i.model.entities.Report#setIndicatorsCalculator(us.muit.fs.a4i.control.IndicatorsCalculator)}.
	 */
//	@Test
//	void testSetIndicatorsCalculator() {
//		//Definimos calculadora de tipo repositorio
//		Mockito.when(indCalcMock.getReportType()).thenReturn(ReportI.Type.REPOSITORY);
//		ReportI orgReport=new Report(ReportI.Type.ORGANIZATION);
//		ReportI repoReport=new Report(ReportI.Type.REPOSITORY);
//		ReportI report=new Report();
//		//Vamos a probar establecer la calculadora en un informe que no tiene el tipo aÃºn establecido (DeberÃ­a tener el tipo de la calculadora al final)
//		//Para ello usamos report
//		try {
//			report.setIndicatorsCalculator(indCalcMock);
//			//Se ha tenido que consultar el tipo de calculadora
//			Mockito.verify(indCalcMock).getReportType();
//			assertEquals(indCalcMock.getReportType(),report.getType());
//		} catch (IndicatorException e) {
//			fail("No deberÃ­a lanzar excepciÃ³n");
//		}
//		
//		//Vamos a probar a establecer la calculadora si el tipo de ambos coincide, uso repoReport
//		try {
//			repoReport.setIndicatorsCalculator(indCalcMock);
//			//Se ha tenido que consultar el tipo de calculadora
//			//Mockito.verify(indCalcMock, times(2)).getReportType();
//			assertEquals(indCalcMock.getReportType(),repoReport.getType());
//		} catch (IndicatorException e) {
//			fail("No deberÃ­a lanzar excepciÃ³n");
//		}
//		
//		//Vamos a probar a establecer la calculadora si el tipo de la calculadora discrepa con el tipo del informe, uso orgReport
//	
//		try {
//			orgReport.setIndicatorsCalculator(indCalcMock);
//			//Se ha tenido que consultar el tipo de calculadora
//			fail("Debe saltar una excepciÃ³n antes, no deberÃ­a llegar aquÃ­");
//		} catch (IndicatorException e) {
//			
//			log.info("Ha saltado la excepciÃ³n de indicador");
//			//Suponga que los requisitos cambian, le piden que el mensaje debe ser "El tipo de la calculadora discrepa del tipo del informe"
//			//Cambie el test para que lo verifique y ejecute Â¿QuÃ© ocurre?
//			assertEquals(e.getMessage(),"La calculadora no concuerda con el tipo de informe","El mensaje es correcto");
//		} catch(Exception e) {
//			fail("La excepciÃ³n no es del tipo IndicatorException como se esperaba");
//		}
//		//Esta verificaciÃ³n es para mostrar que se puede analizar tambiÃ©n el comportamiento interno de la clase
//		//en esta ocasiÃ³n el nÃºmero de veces que invoca a la calculadora durante el test
//		//Probar a cambiar 5 por otro nÃºmero y ejecutar
//		Mockito.verify(indCalcMock,times(5)).getReportType();
//	
//	}
	
	//----------------------------------------------------------------------------------------------------------
	/**
	 * Test method for {@link us.muit.fs.a4i.model.entities.Report#toString()}.
	 * toString devuelve un tipo string de repoinfo formado por: 
	 * "Informaciï¿½n del Informe:\n - Mï¿½tricas: "
	 * "\n Clave: " + clave + metrics.get(clave)
	 * "\n Clave: " + clave + indicators.get(clave)
	 */
	@Test
	@Tag("noacabado")
	void testToString() {
		reportTested=new Report();	
		String refString="referencia";
		try {
			assertEquals(reportTested.toString().getClass(),refString.getClass(),"ComparaciÃ³n de tipo String");
		} catch (IndicatorException e) {
			fail("ToString no devuelve un tipo String");
		}
	}
	//----------------------------------------------------------------------------------------------------------
	

	/**
	 * Test method for {@link us.muit.fs.a4i.model.entities.Report#getAllMetrics()}.
	 */
	@Test
	@Tag("noacabado")
	void testGetAllMetrics() {
		Metric metric= null;
		reportTested=new Report();

		try {
			assertNull(reportTested.getAllMetrics(),"All metric Null");
		} catch (IndicatorException e) {
			fail(“All Metrics Null” );
		}
	}
	}
	void setMetricsMocks() {
		Date date=Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
		Mockito.when(metricIntMock.getName()).thenReturn("issues");
		Mockito.when(metricIntMock.getDescription()).thenReturn("Tareas sin finalizar en el repositorio");	
		Mockito.when(metricIntMock.getValue()).thenReturn(3);	
	
		Mockito.when(metricDatMock.getName()).thenReturn("lastPush");
		Mockito.when(metricDatMock.getDescription()).thenReturn("Ãšltimo push realizado en el repositorio");	
		Mockito.when(metricDatMock.getValue()).thenReturn(date);
	}
	
	
	/**
	 * Test method for {@link us.muit.fs.a4i.model.entities.Report#getAllIndicators()}.
	 */
	@Test
	@Tag("noacabado")
	void testGetAllIndicators() {
		Metric metric= null;
				reportTested=new Report();

				try {
					assertNull(reportTested.getAllIndicator(name),"All indicator Null");
				} catch (IndicatorException e) {
					fail(“All Indicator Null” );
				}
			}
			}
	void setIndicatorsMocks() {
		Date date=Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
		Mockito.when(indicatorIntMock.getName()).thenReturn("issues");
		Mockito.when(indicatorIntMock.getDescription()).thenReturn("Tareas sin finalizar en el repositorio");	
		Mockito.when(indicatorIntMock.getValue()).thenReturn(3);	
	
		Mockito.when(indicatorDatMock.getName()).thenReturn("lastPush");
		Mockito.when(indicatorDatMock.getDescription()).thenReturn("Ãšltimo push realizado en el repositorio");	
		Mockito.when(indicatorDatMock.getValue()).thenReturn(date);
	}
	
}
