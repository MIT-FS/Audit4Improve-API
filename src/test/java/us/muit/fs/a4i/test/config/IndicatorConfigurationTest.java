/**
 * 
 */
package us.muit.fs.a4i.test.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import us.muit.fs.a4i.config.Context;
import us.muit.fs.a4i.config.IndicatorConfiguration;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.model.entities.IndicatorI;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItem.ReportItemBuilder;
import us.muit.fs.a4i.model.entities.ReportItemI;

class IndicatorConfigurationTest {
	private static Logger log = Logger.getLogger(IndicatorConfigurationTest.class.getName());
	static IndicatorConfiguration underTest;
	static String appConfPath;
	String appIndicatorsPath = "/test/home";
	
	/**
	 * <p>Acciones a realizar antes de ejecutar los tests definidos en esta clase. En este caso se establece la ruta al fichero de configuración, en la carpeta resources dentro del paquete de test</p>
	 * @throws java.lang.Exception
	 * @see org.junit.jupiter.api.BeforeAll
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		appConfPath = "src" + File.separator + "test" + File.separator + "resources" + File.separator
				+ "appConfTest.json";
	}

	
	/**
	 * <p>Acciones a realizar después de ejecutar todos los tests de esta clase</p>
	 * @throws java.lang.Exception
	 * @see org.junit.jupiter.api.AfterAll
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		log.info("He ejectuado todos los test definidos en esta clase");
	}

	/**
	 * <p>Acciones a realizar antes de cada uno de los tests de esta clase
	 * en este caso se crea el objeto bajo test, un Checker</p>
	 * @throws java.lang.Exception
	 * @see org.junit.jupiter.api.BeforeEach
	 */
	@BeforeEach
	void setUp() throws Exception {
		underTest = new IndicatorConfiguration();
	}

	/**
	 * Acciones a realizar después de cada uno de los tests de esta clase
	 * @throws java.lang.Exception
	 * @see org.junit.jupiter.api.AfterEach
	 */
	@AfterEach
	void tearDown() throws Exception {
		log.info("Acabo de ejecutar un test definido en esta clase"); 
	}

	@Test
	void testDefinedIndicator() {
		// Creo un par de variables, que me servirán de valores para verificar si comprueba bien el tipo
		// Las métricas del test son de tipo entero, así que creo un entero y un string
		// (el primero no dará problemas el segundo sí)
		Double valOKMock = Double.valueOf(0.3);
		String valKOMock = "KO";
		HashMap<String, String> returnedMap = null;
		// Primero, sin fichero de configuración de aplicación
		try {
	
			// Consulta un indicador no definido, con valor de tipo entero
			// debe devolver null, no está definido
			log.info("Busco el indicador llamado noexiste");
			returnedMap = underTest.definedIndicator("noexiste", valOKMock.getClass().getName());
			assertNull(returnedMap, "Debería ser nulo, el indicador noexiste no está definido");

			/*
			 * Indicador que existe en el fichero de configuración por defecto
			 * {
			"name": "issuesProgress",
			"type": "java.lang.Double",
			"description": "Ratio de issues cerrados frente a totales",
			"unit": "ratio",
			"limits": {
				"ok": 2,
				"warning": 4,
				"critical": 6
			}
		}
			 */
			// Busco el indicador overdued con valor double, no debería dar problemas
			log.info("Busco el indicador issuesProgress");
			returnedMap = underTest.definedIndicator("issuesProgress", valOKMock.getClass().getName());
			assertNotNull(returnedMap, "Debería devolver un hashmap, el indicador issuesProgress está definido");
			assertTrue(returnedMap.containsKey("unit"), "La clave unit tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("description"), "La clave description tiene que estar en el mapa");
			// Se comprueba que los indicadores incluyen los limites definidos
			assertTrue(returnedMap.containsKey("limits.ok"), "La clave correspondiente al limite del estado OK tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("limits.warning"), "La clave correspondiente al limite del estado WARNING tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("limits.critical"), "La clave correspondiente al limite del estado CRITICAL tiene que estar en el mapa");
			// Busco una métrica que existe pero con un tipo incorrecto
			assertNull(underTest.definedIndicator("overdued", valKOMock.getClass().getName()),
					"Debería ser nulo, el indicador overdued está definido para Double");
		} catch (FileNotFoundException e) {
			fail("El fichero está en la carpeta resources");
			e.printStackTrace();
		}
	}
	
	@Test
	void testDefinedIndicatorCustom() {
		// Creo un par de variables, que me servirán de valores para verificar si comprueba bien el tipo
		// Las métricas del test son de tipo entero, así que creo un entero y un string
		// (el primero no dará problemas el segundo sí)
		Double valOKMock = Double.valueOf(0.3);
		String valKOMock = "KO";
		HashMap<String, String> returnedMap = null;
		// Ahora establezco el fichero de configuración de la aplicación, con un
		// nombre de fichero que no existe
		Context.setAppRI("pepe");
		try {
			// Busco un indicador que se que no está en la configuración de la api
			returnedMap = underTest.definedIndicator("pullReqGlory", valOKMock.getClass().getName());
			fail("Debería lanzar una excepción porque intenta buscar en un fichero que no existe");
		} catch (FileNotFoundException e) {
			log.info("Lanza la excepción adecuada, FileNotFoud");
			Context.setAppRI(appConfPath);
		} catch (Exception e) {
			fail("Lanza la excepción equivocada " + e);
		}

		// Ahora establezco un fichero de configuración de la aplicación 
		Context.setAppRI(appConfPath);
		try {
			
			// Busco una métrica que se que no está en la configuración de la api pero
			// sí en la de la aplicación
			log.info("Busco el indicador llamado pullReqGlory");
			returnedMap = underTest.definedIndicator("pullReqGlory", valOKMock.getClass().getName());
			assertNotNull(returnedMap, "Debería devolver un hashmap, el indicador está definido");
			assertTrue(returnedMap.containsKey("unit"), "La clave unit tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("description"), "La clave description tiene que estar en el mapa");
			// Se comprueba que los indicadores incluyen los limites definidos
			assertTrue(returnedMap.containsKey("limits.ok"), "La clave correspondiente al limite del estado OK tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("limits.warning"), "La clave correspondiente al limite del estado WARNING tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("limits.critical"), "La clave correspondiente al limite del estado CRITICAL tiene que estar en el mapa");
		} catch (FileNotFoundException e) {
			fail("No debería devolver esta excepción");
		} catch (Exception e) {
			fail("Lanza una excepción no reconocida " + e);
		}
	}
    /**
     * <p>En este test verifico que si busco el nombre de una métrica el método que verifica el indicador no lo confunde</p>
     */
    @Test
    void testDefinedIndicatorAsMetric() {
		Integer valOKMock = Integer.valueOf(3);
		
		HashMap<String, String> returnedMap = null;

		try {
			// Consulta el nombre de un indicador que en realidad es una métrica
			log.info("Busco el indicador llamado pullReqGlory");
			returnedMap = underTest.definedIndicator("subscribers", valOKMock.getClass().getName());
			assertNull(returnedMap, "Debería ser nulo, subscribers es una métrica y no un indicador no está definido");
		}catch (Exception e) {
			fail("Lanza la excepción " + e);
		}
    }

	@Test
	void testListAllIndicators() {
		fail("Not yet implemented");
	}

	@Test
	void testGetIndicatorState(){ 
		// Prueba 1.
		Context.setAppRI(appConfPath);
	
		ReportItemI indicator = null;
		IndicatorI.IndicatorState estado = IndicatorI.IndicatorState.UNDEFINED;
		
		try {
			indicator = new ReportItemBuilder<Double>("overdued", 2.0).build();
		} catch (ReportItemException e) {
			fail("El archivo de configuración esperado no contiene el indicador necesario para esta prueba.");
		}
		
		estado = underTest.getIndicatorState(indicator);
		
		assertTrue(estado == IndicatorI.IndicatorState.OK, "El estado es INCORRECTO. Debería de ser OK.");
		
		
		
	
	   try {
		indicator = new ReportItemBuilder<Double>("overdued", 9.0).build();
	} catch (ReportItemException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		estado = underTest.getIndicatorState(indicator);
		
		assertTrue(estado == IndicatorI.IndicatorState.WARNING, "El estado es INCORRECTO. Debería de ser WARNING.");


	
	try {
		indicator = new ReportItemBuilder<Double>("overdued", 13.0).build();
	} catch (ReportItemException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		estado = underTest.getIndicatorState(indicator);
		
		assertTrue(estado == IndicatorI.IndicatorState.CRITICAL, "El estado es INCORRECTO. Debería de ser CRITICAL.");


	
	try {
		indicator = new ReportItemBuilder<Double>("issuesRatio", 13.0).build();
	} catch (ReportItemException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		estado = underTest.getIndicatorState(indicator);
		
		assertTrue(estado == IndicatorI.IndicatorState.UNDEFINED, "El estado es INCORRECTO. Debería de ser UNDEFINED.");


	}


}
