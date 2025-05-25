/**
 * 
 */
package us.muit.fs.a4i.test.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.config.IndicatorConfiguration;
import us.muit.fs.a4i.config.MetricConfiguration;
import us.muit.fs.a4i.config.MetricConfigurationI;

/**
 * En esta clase se verifica la clase MetricConfiguration pero usando un fichero
 * de configuración de métricas de la aplicación cliente
 */
class MetricConfigurationTest2 {

	private static Logger log = Logger.getLogger(MetricConfigurationTest2.class.getName());
	private static MetricConfigurationI underTest;
	static String appConfPath;
	private static String defaultFile = "a4iDefault.json";
	String appIndicatorsPath = "/test/home";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		appConfPath = "src" + File.separator + "test" + File.separator + "resources" + File.separator
				+ "appConfTest.json";

	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.config.MetricConfiguration#definedMetric(java.lang.String, java.lang.String)}.
	 * En este caso se usa un único métdo para verificar todos los casos
	 * <p>
	 * Test para verificar el método
	 * {@link us.muit.fs.a4i.config.MetricConfiguration#definedMetric(java.lang.String, java.lang.String)}.
	 * Si la métrica está definida y el tipo de valor que se quiere establecer es el
	 * adecuado debe devolver un hashmap con los datos de la métrica, usando como
	 * clave las etiquetas:
	 * <ul>
	 * <li>description</li>
	 * <li>unit</li>
	 * </ul>
	 * Pero si no está definida no debe crear el mapa Las métricas pueden estar
	 * definidas en el fichero de configuración de la api (a4iDefault.json) o en
	 * otro fichero configurado por la aplicación cliente. Para los test este
	 * fichero es appConfTest.json y se guarda junto al código de test, en la
	 * carpeta resources
	 * 
	 * @see org.junit.jupiter.api.Tag
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 *      </p>
	 */
	@Tag("unidad")
	@DisplayName("Verificación del método definedMetric")
	@Test
	void testDefinedMetric() {
		underTest = new MetricConfiguration(defaultFile, appConfPath);
		// Creo valores Mock para verificar si comprueba bien el tipo
		// Las m�tricas del test son de enteros, as� que creo un entero y un string (el
		// primero no dar� problemas el segundo sí)
		Integer valOKMock = Integer.valueOf(3);
		String valKOMock = "KO";
		HashMap<String, String> returnedMap = null;
		// Primero, sin fichero de configuraci�n de aplicaci�n
		try {
			// Consulta una m�trica no definida, con valor de tipo entero
			// debe devolver null, no est� definida
			log.info("Busco la métrica llamada NoExiste");
			returnedMap = underTest.definedMetric("NoExiste", valOKMock.getClass().getName());
			assertNull(returnedMap, "Debería ser nulo, la métrica NoExiste no está definida");
			/*
			 * En el fichero por defecto la métrica issues está definida del siguiente modo
			 * { "name": "issues", "type": "java.lang.Integer", "description":
			 * "Tareas totales", "unit": "issues" }
			 */
			// Primero se busca la métrica con el tipo definido
			HashMap<String, String> metricInfo = underTest.definedMetric("issues", "java.lang.Integer");
			assertEquals("issues", metricInfo.get("name"), "No se ha leído bien el nombre de la métrica");
			assertEquals("java.lang.Integer", metricInfo.get("type"), "No se ha leído bien el tipo de la métrica");
			assertEquals("Tareas totales", metricInfo.get("description"),
					"No se ha leído bien la descripción de la métrica");
			assertEquals(metricInfo.get("unit"), "issues", "No se ha leído bien las unidades de la métrica");

			// Busco la métrica watchers con valor entero
			log.info("Busco la métrica watchers");
			returnedMap = underTest.definedMetric("watchers", valOKMock.getClass().getName());
			assertNotNull(returnedMap, "Debería devolver un hashmap, la métrica está definida");
			assertTrue(returnedMap.containsKey("unit"), "La clave unit tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("description"), "La clave description tiene que estar en el mapa");

			// Busco la métrica watchers con un tipo incorrecto
			assertNull(underTest.definedMetric("watchers", valKOMock.getClass().getName()),
					"Debería ser nulo, la métrica está definida para Integer");
			// Busco una métrica que se que no está en la configuración de la api pero sí en
			// la de la aplicación
			log.info("Busco la métrica llamada downloads");
			returnedMap = underTest.definedMetric("downloads", valOKMock.getClass().getName());
			assertNotNull(returnedMap, "Debería devolver un hashmap, la métrica está definida");
			assertTrue(returnedMap.containsKey("unit"), "La clave unit tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("description"), "La clave description tiene que estar en el mapa");
		} catch (FileNotFoundException e) {
			fail("El fichero NO está en la carpeta resources");
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.config.MetricConfiguration#definedMetric(java.lang.String, java.lang.String)}.
	 * /**
	 * <p>
	 * Test para verificar el método
	 * {@link us.muit.fs.a4i.config.MetricConfiguration#metricInfo(java.lang.String)}.
	 * Si la métrica está definida en el fichero de configuración del cliente debe
	 * devolver un hashmap con los datos de la métrica, usando como clave las
	 * etiquetas:
	 * <ul>
	 * <li>description</li>
	 * <li>unit</li>
	 * </ul>
	 * Pero si no está definida no debe crear el mapa Las métricas pueden estar
	 * definidas en el fichero de configuración de la api (a4iDefault.json) o en
	 * otro fichero configurado por la aplicación cliente. Para los test este
	 * fichero es appConfTest.json y se guarda junto al código de test, en la
	 * carpeta resources
	 * 
	 * @see org.junit.jupiter.api.Tag
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 *      </p>
	 */
	@Tag("unidad")
	@DisplayName("Verificación de lectura métrica disponible en configuración de la aplicación")
	@Test
	void testGetMetricInfo1() {
		underTest = new MetricConfiguration(defaultFile, appConfPath);
		HashMap<String, String> returnedMap;
		try {
			// Busco una métrica que se que no está en la configuración de la api pero sí en
			// la de la aplicación
			log.info("Busco la métrica llamada downloads");
			returnedMap = underTest.getMetricInfo("downloads");
			assertNotNull(returnedMap, "Debería devolver un hashmap, la métrica está definida");
			assertTrue(returnedMap.containsKey("unit"), "La clave unit tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("description"), "La clave description tiene que estar en el mapa");

		} catch (FileNotFoundException e) {
			fail("no debería devolver esta excepción");
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.config.MetricConfiguration#listAllMetrics()}. Debe
	 * devolver una lista con todas las métricas, en ambos ficheros
	 */
	@Tag("unidad")
	@DisplayName("Verificación de consulta de métricas con los dos ficheros de configuración")
	@Test
	void testListAllMetrics() {
		underTest = new MetricConfiguration(defaultFile, appConfPath);
		List<String> metricsList;
		try {
			metricsList = underTest.listAllMetrics();
			/**
			 * En el momento de codificar este test 21/3/25 el número de métricas totales es
			 * 41 (39 de configuración + 2 del cliente)
			 */
			assertEquals(41, metricsList.size(), "No lee correctamente las métricas");
		} catch (FileNotFoundException e) {
			fail("No debería lanzar esta excepción");
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.config.MetricConfiguration#definedMetric(java.lang.String, java.lang.String)}.
	 * /**
	 * <p>
	 * Test para verificar el método
	 * {@link us.muit.fs.a4i.config.MetricConfiguration#metricInfo(java.lang.String)}.
	 * Si la métrica está definida en el fichero por defecto debe devolver un
	 * hashmap con los datos de la métrica, usando como clave las etiquetas:
	 * <ul>
	 * <li>description</li>
	 * <li>unit</li>
	 * </ul>
	 * Pero si no está definida no debe crear el mapa Las métricas pueden estar
	 * definidas en el fichero de configuración de la api (a4iDefault.json) o en
	 * otro fichero configurado por la aplicación cliente. Para los test este
	 * fichero es appConfTest.json y se guarda junto al código de test, en la
	 * carpeta resources
	 * 
	 * @see org.junit.jupiter.api.Tag
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 *      </p>
	 */
	@Tag("unidad")
	@DisplayName("Verificación de lectura métrica disponible en configuración por defecto")
	@Test
	void testGetMetricInfo2() {
		underTest = new MetricConfiguration(defaultFile, appConfPath);
		try {
			/*
			 * En el fichero por defecto la métrica issues está definida del siguiente modo
			 * { "name": "issues", "type": "java.lang.Integer", "description":
			 * "Tareas totales", "unit": "issues" }
			 */
			// Primero se busca una métrica que existe
			HashMap<String, String> metricInfo = underTest.getMetricInfo("issues");
			assertEquals("issues", metricInfo.get("name"), "No se ha leído bien el nombre de la métrica");
			assertEquals("java.lang.Integer", metricInfo.get("type"), "No se ha leído bien el tipo de la métrica");
			assertEquals("Tareas totales", metricInfo.get("description"),
					"No se ha leído bien la descripción de la métrica");
			assertEquals("issues", metricInfo.get("unit"), "No se ha leído bien las unidades de la métrica");
		} catch (IOException e) {

			e.printStackTrace();
			fail("No debería devolver esta excepción");
		}
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.config.MetricConfiguration#definedMetric(java.lang.String, java.lang.String)}.
	 * /**
	 * <p>
	 * Test para verificar el método
	 * {@link us.muit.fs.a4i.config.MetricConfiguration#definedMetric(java.lang.String, java.lang.String)}.
	 * Si la métrica no está definida no debe crear el mapa Las métricas pueden
	 * estar definidas en el fichero de configuración de la api (a4iDefault.json) o
	 * en otro fichero configurado por la aplicación cliente. Para los test este
	 * fichero es appConfTest.json y se guarda junto al código de test, en la
	 * carpeta resources
	 * 
	 * @see org.junit.jupiter.api.Tag
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 *      </p>
	 */
	@DisplayName("Verificación de lectura métrica no existente")
	@Test
	void testGetMetricInfo3() {
		underTest = new MetricConfiguration(defaultFile, appConfPath);
		try {
			/*
			 * En el fichero por defecto la métrica noexiste no existe
			 */

			HashMap<String, String> metricInfo = underTest.getMetricInfo("noexiste");
			assertNull(metricInfo, "El mapa no debe haberse creado");
		} catch (IOException e) {
			fail("Lanza excepcion indebida, no localiza el fichero");
			e.printStackTrace();

		}
	}

	/**
	 * <p>
	 * Test para verificar que se lanza adecuadamente la excepción de fichero no
	 * localizado en todos los métodos
	 * 
	 * @see org.junit.jupiter.api.Tag
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 *      </p>
	 */
	@DisplayName("Verificación de excepción FileNotFound cuando el fichero de configuración del cliente no está bien especificado")
	@Test
	void testExceptionFile() {

		underTest = new MetricConfiguration(defaultFile, "clienteKO");
		/*
		 * el fichero de la api cliente no existe, pruebo los tres métodos
		 */
		FileNotFoundException thrown = assertThrows(FileNotFoundException.class,

				() -> underTest.definedMetric("downloads", "java.lang.Integer"),
				"Debería haber lanzado la excepción de fichero no encontrado, pero no lo ha hecho");

		assertTrue(thrown.getMessage().contains("clienteKO"), "La excepción debería indicar el fichero no localizado");

		thrown = assertThrows(FileNotFoundException.class,

				() -> underTest.listAllMetrics(),
				"Debería haber lanzado la excepción de fichero no encontrado, pero no lo ha hecho");

		assertTrue(thrown.getMessage().contains("clienteKO"), "La excepción debería indicar el fichero no localizado");

		/*
		 * el fichero de la api cliente no existe
		 */
		thrown = assertThrows(FileNotFoundException.class,

				() -> underTest.getMetricInfo("downloads"),
				"Debería haber lanzado la excepción de fichero no encontrado, pero no lo ha hecho");

		assertTrue(thrown.getMessage().contains("clienteKO"), "La excepción debería indicar el fichero no localizado");
	}

}
