/**
 * 
 */
package us.muit.fs.a4i.test.config;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.config.Checker;
import us.muit.fs.a4i.config.Context;

/**
 * Test de la clase Checker que permite verificar y configurar las métricas e indicadores
 * 
 * @author Isabel Román
 * @see org.junit.jupiter.api.Tag
 *
 */

@Tag("unidad")
class CheckerTest {
	private static Logger log = Logger.getLogger(CheckerTest.class.getName());
	static Checker underTest;
	static String appConfPath;

	/**
	 * <p>Acciones a realizar antes de ejecutar los tests definidos en esta clase</p>
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
		
	}

	/**
	 * <p> Acciones a realizar antes de cada uno de los tests de esta clase</p>
	 * @throws java.lang.Exception
	 * @see org.junit.jupiter.api.BeforeEach
	 */
	@BeforeEach
	void setUp() throws Exception {
		// Creo el objeto bajo test, un Checker
		underTest = Context.getContext().getChecker();
	}

	/**
	 * <p>Acciones a realizar después de cada uno de los tests de esta clase</p>
	 * @throws java.lang.Exception
	 * @see org.junit.jupiter.api.AfterEach
	 */
	@AfterEach
	void tearDown() throws Exception {
		
	}

	/**
	 * <p>Test para el método que establece el fichero de configuración de la
	 * aplicación</p>
	 * {@link us.muit.fs.a4i.config.Checker#setAppMetrics(java.lang.String)}.
	 */
	@Test
	void testSetAppMetrics() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * <p>Test para verificar el método
	 * {@link us.muit.fs.a4i.config.Checker#definedMetric(java.lang.String, java.lang.String)}.
	 * Si la métrica está definida y el tipo de valor que se quiere establecer es el
	 * adecuado debe devolver un hashmap con los datos de la métrica, usando como
	 * clave las etiquetas:
	 * <ul>
	 * <li>description</li>
	 * <li>unit</li>
	 * </ul>
	 * Las métricas pueden estar definidas en el fichero de configuración de la api
	 * (a4iDefault.json) o en otro fichero configurado por la aplicación cliente.
	 * Para los test este fichero es appConfTest.json y se guarda junto al código de
	 * test, en la carpeta resources
	 * 
	 * @see org.junit.jupiter.api.Tag
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName	 * 
	 *    </p>
	 */
	@Test
	@Tag("unidad")
	@DisplayName("Prueba para el m�todo definedMetric, que verifica si la m�trica est� definida con un tipo determinado y devuelve su configuraci�n")
	void testDefinedMetric() {

		// Creo valores Mock para verificar si comprueba bien el tipo
		// Las m�tricas del test son de enteros, as� que creo un entero y un string (el
		// primero no dar� problemas el segundo s�)
		Integer valOKMock = Integer.valueOf(3);
		String valKOMock = "KO";
		HashMap<String, String> returnedMap = null;
		// Primero, sin fichero de configuraci�n de aplicaci�n
		try {
			// Consulta una m�trica no definida, con valor de tipo entero
			// debe devolver null, no est� definida
			log.info("Busco la m�trica llamada downloads");
			returnedMap = underTest.getMetricConfiguration().definedMetric("downloads", valOKMock.getClass().getName());
			assertNull(returnedMap, "Deber�a ser nulo, la m�trica noexiste no est� definida");

			// Busco la m�trica watchers con valor entero, no deber�a dar problemas
			log.info("Busco la m�trica watchers");
			returnedMap = underTest.getMetricConfiguration().definedMetric("watchers", valOKMock.getClass().getName());
			assertNotNull(returnedMap, "Deber�a devolver un hashmap, la m�trica est� definida");
			assertTrue(returnedMap.containsKey("unit"), "La clave unit tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("description"), "La clave description tiene que estar en el mapa");

			// Busco una m�trica que existe pero con un tipo incorrecto en el valor
			assertNull(underTest.getMetricConfiguration().definedMetric("watchers", valKOMock.getClass().getName()),
					"Deber�a ser nulo, la m�trica est� definida para Integer");
		} catch (FileNotFoundException e) {
			fail("El fichero est� en la carpeta resources");
			e.printStackTrace();
		}

		// Ahora establezco el fichero de configuraci�n de la aplicaci�n, con un nombre
		// de fichero que no existe
		Context.setAppRI("pepe");
		try {
			// Busco una m�trica que se que no est� en la configuraci�n de la api
			returnedMap = underTest.getMetricConfiguration().definedMetric("downloads", valOKMock.getClass().getName());
			fail("Deber�a lanzar una excepci�n porque intenta buscar en un fichero que no existe");
		} catch (FileNotFoundException e) {
			log.info("Lanza la excepci�n adecuada, FileNotFoud");
		} catch (Exception e) {
			fail("Lanza la excepci�n equivocada " + e);
		}

		// Ahora establezco un fichero de configuraci�n de la aplicaci�n que s� existe
		Context.setAppRI(appConfPath);
		try {
			// Busco una m�trica que se que no est� en la configuraci�n de la api pero s� en
			// la de la aplicaci�n
			log.info("Busco la m�trica llamada downloads");
			returnedMap = underTest.getMetricConfiguration().definedMetric("downloads", valOKMock.getClass().getName());
			assertNotNull(returnedMap, "Deber�a devolver un hashmap, la m�trica est� definida");
			assertTrue(returnedMap.containsKey("unit"), "La clave unit tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("description"), "La clave description tiene que estar en el mapa");
		} catch (FileNotFoundException e) {
			fail("No deber�a devolver esta excepci�n");
		} catch (Exception e) {
			fail("Lanza una excepci�n no reconocida " + e);
		}

	}

	/**
	 * @see org.junit.jupiter.api.Tag
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 * 
	 *      Test para el m�todo
	 *      {@link us.muit.fs.a4i.config.Checker#definedIndicator(java.lang.String, java.lang.String)}.
	 */
	@Test
	@Tag("unidad")
	@DisplayName("Prueba para el m�todo definedIndicator, que verifica si el indicador est� definido con un tipo determinado y devuelve su configuraci�n")
	void testDefinedIndicator() {

		// Creo valores Mock para verificar si comprueba bien el tipo
		// Las m�tricas del test son de enteros, as� que creo un entero y un string (el
		// primero no dar� problemas el segundo s�)
		Double valOKMock = Double.valueOf(0.3);
		String valKOMock = "KO";
		HashMap<String, String> returnedMap = null;
		// Primero, sin fichero de configuraci�n de aplicaci�n
		try {
			// Consulta un indicador no definido, con valor de tipo entero
			// debe devolver null, no est� definido
			log.info("Busco el indicador llamado pullReqGlory");
			returnedMap = underTest.getIndicatorConfiguration().definedIndicator("pullReqGlory",
					valOKMock.getClass().getName());
			assertNull(returnedMap, "Deber�a ser nulo, el indicador pullReqGlory no est� definido");

			// Busco el indicador overdued con valor double, no deber�a dar problemas
			log.info("Busco el indicador overdued");
			returnedMap = underTest.getIndicatorConfiguration().definedIndicator("overdued",
					valOKMock.getClass().getName());
			assertNotNull(returnedMap, "Deber�a devolver un hashmap, el indicador overdued est� definido");
			assertTrue(returnedMap.containsKey("unit"), "La clave unit tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("description"), "La clave description tiene que estar en el mapa");

			// Busco una m�trica que existe pero con un tipo incorrecto en el valor
			assertNull(
					underTest.getIndicatorConfiguration().definedIndicator("overdued", valKOMock.getClass().getName()),
					"Deber�a ser nulo, el indicador overdued est� definido para Double");
		} catch (FileNotFoundException e) {
			fail("El fichero est� en la carpeta resources");
			e.printStackTrace();
		}

		// Ahora establezco el fichero de configuraci�n de la aplicaci�n, con un nombre
		// de fichero que no existe
		Context.setAppRI("pepe");
		try {
			// Busco un indicador que se que no est� en la configuraci�n de la api
			returnedMap = underTest.getIndicatorConfiguration().definedIndicator("pullReqGlory",
					valOKMock.getClass().getName());
			fail("Deber�a lanzar una excepci�n porque intenta buscar en un fichero que no existe");
		} catch (FileNotFoundException e) {
			log.info("Lanza la excepci�n adecuada, FileNotFoud");
		} catch (Exception e) {
			fail("Lanza la excepci�n equivocada " + e);
		}

		// Ahora establezco un fichero de configuraci�n de la aplicaci�n que s� existe
		Context.setAppRI(appConfPath);
		try {
			// Busco una m�trica que se que no est� en la configuraci�n de la api pero s� en
			// la de la aplicaci�n
			log.info("Busco el indicador llamado pullReqGlory");
			returnedMap = underTest.getIndicatorConfiguration().definedIndicator("pullReqGlory",
					valOKMock.getClass().getName());
			assertNotNull(returnedMap, "Deber�a devolver un hashmap, el indicador est� definido");
			assertTrue(returnedMap.containsKey("unit"), "La clave unit tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("description"), "La clave description tiene que estar en el mapa");
		} catch (FileNotFoundException e) {
			fail("No deber�a devolver esta excepci�n");
		} catch (Exception e) {
			fail("Lanza una excepci�n no reconocida " + e);
		}

	}

}
