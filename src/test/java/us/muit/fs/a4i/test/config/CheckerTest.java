/**
 * 
 */
package us.muit.fs.a4i.test.config;

import static org.junit.jupiter.api.Assertions.*;

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

/**
 * Test de la clase Checker que verifica las métricas e indicadores
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
	 * @throws java.lang.Exception
	 * @see org.junit.jupiter.api.BeforeAll
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		//Acciones a realizar antes de ejecutar los tests de esta clase
		appConfPath="src"+File.separator+"test"+File.separator+"resources"+File.separator+"appConfTest.json";
	}

	/**
	 * @throws java.lang.Exception
	 * @see org.junit.jupiter.api.AfterAll
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		//Acciones a realizar después de ejecutar todos los tests de esta clase
	}

	/**
	 * @throws java.lang.Exception
	 * @see org.junit.jupiter.api.BeforeEach
	 */
	@BeforeEach
	void setUp() throws Exception {
		//Acciones a realizar antes de cada uno de los tests de esta clase
		//Creo el objeto bajo test, un Checker
		underTest = new Checker();
	}

	/**
	 * @throws java.lang.Exception
	 * @see org.junit.jupiter.api.AfterEach
	 */
	@AfterEach
	void tearDown() throws Exception {
		//Acciones a realizar después de cada uno de los tests de esta clase
	}

	/**
	 * Test para el método que establece el fichero de configuración de la aplicación
	 * {@link us.muit.fs.a4i.config.Checker#setAppMetrics(java.lang.String)}.
	 */
	@Test
	void testSetAppMetrics() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test para verificar el método
	 * {@link us.muit.fs.a4i.config.Checker#definedMetric(java.lang.String, java.lang.String)}.
	 * Si la métrica está definida y el tipo de valor que se quiere establecer es el adecuado 
	 * debe devolver un hashmap con los datos de la métrica, usando como clave las etiquetas:
	 * <ul>
	 * <li>description</li>
	 * <li>unit</li>
	 * </ul>
	 * Las métricas pueden estar definidas en el fichero de configuración de la api (a4iDefault.json) o en otro fichero
	 * configurado por la aplicación cliente. Para los test este fichero es appConfTest.json y se guarda
	 * junto al código de test, en la carpeta resources
	 * @see org.junit.jupiter.api.Tag
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 * 
	 * Test para verificar el método
	 * {@link us.muit.fs.a4i.config.Checker#definedMetric(java.lang.String, java.lang.String)}.
	 */
	@Test
	@Tag("unidad")
	@DisplayName("Prueba para el método definedMetric, que verifica si la métrica está definida con un tipo determinado y devuelve su configuración")
	void testDefinedMetric() {
		
		//Creo valores Mock para verificar si comprueba bien el tipo
		//Las métricas del test son de enteros, así que creo un entero y un string (el primero no dará problemas el segundo sí)
		Integer valOKMock = Integer.valueOf(3);
		String valKOMock = "KO";
		HashMap<String,String> returnedMap=null;
		//Primero, sin fichero de configuración de aplicación
		try {
			//Consulta una métrica no definida, con valor de tipo entero
			//debe devolver null, no está definida
			log.info("Busco la métrica llamada downloads");
			returnedMap=underTest.definedMetric("downloads", valOKMock.getClass().getName());
			assertNull(returnedMap, "Debería ser nulo, la métrica noexiste no está definida");
			
			//Busco la métrica watchers con valor entero, no debería dar problemas
			log.info("Busco la métrica watchers");
			returnedMap=underTest.definedMetric("watchers", valOKMock.getClass().getName());
			assertNotNull(returnedMap,"Debería devolver un hashmap, la métrica está definida");
			assertTrue(returnedMap.containsKey("unit"),"La clave unit tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("description"),"La clave description tiene que estar en el mapa");
	        
			//Busco una métrica que existe pero con un tipo incorrecto en el valor
			assertNull(underTest.definedMetric("watchers", valKOMock.getClass().getName()),
					"Debería ser nulo, la métrica está definida para Integer");
		} catch (FileNotFoundException e) {
			fail("El fichero está en la carpeta resources");
			e.printStackTrace();
		}
		
		//Ahora establezco el fichero de configuración de la aplicación, con un nombre de fichero que no existe
		underTest.setAppMetrics("pepe");
		try {
			//Busco una métrica que se que no está en la configuración de la api
			returnedMap=underTest.definedMetric("downloads", valOKMock.getClass().getName());
			fail("Debería lanzar una excepción porque intenta buscar en un fichero que no existe");
		} catch (FileNotFoundException e) {
			log.info("Lanza la excepción adecuada, FileNotFoud");
		} catch (Exception e) {
			fail("Lanza la excepción equivocada " + e);
		}
			
		//Ahora establezco un fichero de configuración de la aplicación que sí existe
		underTest.setAppMetrics(appConfPath);
		try {
			//Busco una métrica que se que no está en la configuración de la api pero sí en la de la aplicación
			log.info("Busco la métrica llamada downloads");
			returnedMap=underTest.definedMetric("downloads", valOKMock.getClass().getName());
			assertNotNull(returnedMap,"Debería devolver un hashmap, la métrica está definida");
			assertTrue(returnedMap.containsKey("unit"),"La clave unit tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("description"),"La clave description tiene que estar en el mapa");
		} catch (FileNotFoundException e) {
			fail("No debería devolver esta excepción");
		} catch (Exception e) {
			fail("Lanza una excepción no reconocida " + e);
		}

	}
	


	/**
	 * @see org.junit.jupiter.api.Tag
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 * 
	 * Test para el método
	 * {@link us.muit.fs.a4i.config.Checker#definedIndicator(java.lang.String, java.lang.String)}.
	 */
	@Test
	@Tag("unidad")
	@DisplayName("Prueba para el método definedIndicator, que verifica si el indicador está definido con un tipo determinado y devuelve su configuración")
	void testDefinedIndicator() {
		
		//Creo valores Mock para verificar si comprueba bien el tipo
		//Las métricas del test son de enteros, así que creo un entero y un string (el primero no dará problemas el segundo sí)
		Double valOKMock = Double.valueOf(0.3);
		String valKOMock = "KO";
		HashMap<String,String> returnedMap=null;
		//Primero, sin fichero de configuración de aplicación
		try {
			//Consulta un indicador no definido, con valor de tipo entero
			//debe devolver null, no está definido
			log.info("Busco el indicador llamado pullReqGlory");
			returnedMap=underTest.definedIndicator("pullReqGlory", valOKMock.getClass().getName());
			assertNull(returnedMap, "Debería ser nulo, el indicador pullReqGlory no está definido");
			
			//Busco el indicador overdued con valor double, no debería dar problemas
			log.info("Busco el indicador overdued");
			returnedMap=underTest.definedIndicator("overdued", valOKMock.getClass().getName());
			assertNotNull(returnedMap,"Debería devolver un hashmap, el indicador overdued está definido");
			assertTrue(returnedMap.containsKey("unit"),"La clave unit tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("description"),"La clave description tiene que estar en el mapa");
	        
			//Busco una métrica que existe pero con un tipo incorrecto en el valor
			assertNull(underTest.definedIndicator("overdued", valKOMock.getClass().getName()),
					"Debería ser nulo, el indicador overdued está definido para Double");
		} catch (FileNotFoundException e) {
			fail("El fichero está en la carpeta resources");
			e.printStackTrace();
		}
		
		//Ahora establezco el fichero de configuración de la aplicación, con un nombre de fichero que no existe
		underTest.setAppMetrics("pepe");
		try {
			//Busco un indicador que se que no está en la configuración de la api
			returnedMap=underTest.definedIndicator("pullReqGlory", valOKMock.getClass().getName());
			fail("Debería lanzar una excepción porque intenta buscar en un fichero que no existe");
		} catch (FileNotFoundException e) {
			log.info("Lanza la excepción adecuada, FileNotFoud");
		} catch (Exception e) {
			fail("Lanza la excepción equivocada " + e);
		}
		
		//Ahora establezco un fichero de configuración de la aplicación que sí existe
		underTest.setAppMetrics(appConfPath);
		try {
			//Busco una métrica que se que no está en la configuración de la api pero sí en la de la aplicación
			log.info("Busco el indicador llamado pullReqGlory");
			returnedMap=underTest.definedIndicator("pullReqGlory", valOKMock.getClass().getName());
			assertNotNull(returnedMap,"Debería devolver un hashmap, el indicador está definido");
			assertTrue(returnedMap.containsKey("unit"),"La clave unit tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("description"),"La clave description tiene que estar en el mapa");
		} catch (FileNotFoundException e) {
			fail("No debería devolver esta excepción");
		} catch (Exception e) {
			fail("Lanza una excepción no reconocida " + e);
		}

	}

}
