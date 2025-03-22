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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.config.IndicatorConfiguration;
import us.muit.fs.a4i.config.MetricConfiguration;
import us.muit.fs.a4i.config.MetricConfigurationI;

/**
 * Verificación de la clase MetricConfiguration si sólo se usa el fichero de
 * ocnfiguración por defecto
 */
class MetricConfigurationTest {

	private static Logger log = Logger.getLogger(MetricConfigurationTest.class.getName());
	private static MetricConfigurationI underTest;
	static String appConfPath;
	private static String defaultFile = "a4iDefault.json";

	/**
	 * @throws java.lang.Exception Crea el objeto bajo test únicamente con el
	 *                             fichero de configuración por defecto
	 */
	@BeforeEach
	void setUp() throws Exception {

		underTest = new MetricConfiguration(defaultFile, null);
	}

	/**
	 * <p>
	 * Test para verificar el método
	 * {@link us.muit.fs.a4i.config.MetricConfiguration#definedMetric(java.lang.String, java.lang.String)}.
	 * Si la métrica no está definida no debería devolver el mapa clave las
	 * etiquetas:
	 * 
	 * Las métricas pueden estar definidas en el fichero de configuración de la api
	 * (a4iDefault.json) o en otro fichero configurado por la aplicación cliente.
	 * Para los test este fichero es appConfTest.json y se guarda junto al código de
	 * test, en la carpeta resources
	 * 
	 * @see org.junit.jupiter.api.Tag
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName *
	 *      </p>
	 */
	@Tag("unidad")
	@DisplayName("Verificación definedMetric si la métrica no existe en el fichero de configuración por defecto")
	@Test
	void testDefinedMetric1() {

		// Las métricas del test son de enteros, así que creo un entero y un string (el
		// primero no dará problemas el segundo sí
		Integer valOKMock = Integer.valueOf(3);
		String valKOMock = "KO";
		HashMap<String, String> returnedMap = null;
		// Primero, sin fichero de configuraci�n de aplicaci�n
		try {
			// Busco una métrica que se que no está en la configuración de la api
			log.info("Busco la métrica llamada downloads");
			returnedMap = underTest.definedMetric("downloads", valOKMock.getClass().getName());
			assertNull(returnedMap,
					"Debería ser nulo, la métrica downloads no está definida en el fichero de configuración por defecto");
		} catch (FileNotFoundException e) {
			fail("El fichero NO está en la carpeta resources");
			e.printStackTrace();
		}

	}

	/**
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
	 * Las métricas pueden estar definidas en el fichero de configuración de la api
	 * (a4iDefault.json) o en otro fichero configurado por la aplicación cliente.
	 * Para los test este fichero es appConfTest.json y se guarda junto al código de
	 * test, en la carpeta resources
	 * 
	 * @see org.junit.jupiter.api.Tag
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName
	 *      </p>
	 */
	@Tag("unidad")
	@DisplayName("Verificación definedMetric si la métrica existe en el fichero de configuración por defecto y el tipo es correcto")
	@Test
	void testDefinedMetric2() {
		// Creo valores Mock para verificar si comprueba bien el tipo
		// Las m�tricas del test son de enteros, as� que creo un entero y un string (el
		// primero no dar� problemas el segundo sí)
		Integer valOKMock = Integer.valueOf(3);

		HashMap<String, String> returnedMap = null;
		// Primero, sin fichero de configuraci�n de aplicaci�n
		try {

			/*
			 * En el fichero por defecto la métrica issues está definida del siguiente modo
			 * { "name": "issues", "type": "java.lang.Integer", "description":
			 * "Tareas totales", "unit": "issues" }
			 */
			// Primero se busca la métrica con el tipo definido
			HashMap<String, String> metricInfo = underTest.definedMetric("issues", valOKMock.getClass().getName());
			assertEquals("issues", metricInfo.get("name"), "No se ha leído bien el nombre de la métrica");
			assertEquals("java.lang.Integer", metricInfo.get("type"), "No se ha leído bien el tipo de la métrica");
			assertEquals("Tareas totales", metricInfo.get("description"),
					"No se ha leído bien la descripción de la métrica");
			assertEquals(metricInfo.get("unit"), "issues", "No se ha leído bien las unidades de la métrica");

		} catch (FileNotFoundException e) {
			fail("El fichero NO está en la carpeta resources");
			e.printStackTrace();
		}

	}

	/**
	 * <p>
	 * Test para verificar el método
	 * {@link us.muit.fs.a4i.config.MetricConfiguration#definedMetric(java.lang.String, java.lang.String)}.
	 * Si la métrica está definida pero el tipo de valor no es el adecuado no debe
	 * devolver el mapa Las métricas pueden estar definidas en el fichero de
	 * configuración de la api (a4iDefault.json) o en otro fichero configurado por
	 * la aplicación cliente. Para los test este fichero es appConfTest.json y se
	 * guarda junto al código de test, en la carpeta resources
	 * 
	 * @see org.junit.jupiter.api.Tag
	 * @see org.junit.jupiter.api.Test
	 * @see org.junit.jupiter.api.DisplayName *
	 *      </p>
	 */
	@Tag("unidad")
	@DisplayName("Verificación definedMetric si la métrica existe en el fichero de configuración por defecto pero el tipo es incorrecto")
	@Test
	void testDefinedMetric3() {
		// Creo valores Mock para verificar si comprueba bien el tipo
		// Las m�tricas del test son de enteros, as� que creo un entero y un string (el
		// primero no dar� problemas el segundo sí)
		Integer valOKMock = Integer.valueOf(3);
		String valKOMock = "KO";
		HashMap<String, String> returnedMap = null;
		// Primero, sin fichero de configuraci�n de aplicaci�n
		try {

			/*
			 * En el fichero por defecto la métrica issues está definida del siguiente modo
			 * { "name": "issues", "type": "java.lang.Integer", "description":
			 * "Tareas totales", "unit": "issues" }
			 */
			// Busco la métrica con tipo incorrecto
			HashMap<String, String> metricInfo = underTest.definedMetric("issues", valKOMock.getClass().getName());
			assertNull(metricInfo, "La métrica no tiene el tipo correcto, no debería devolver el mapa");

		} catch (FileNotFoundException e) {
			fail("El fichero NO está en la carpeta resources");
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.config.MetricConfiguration#getMetricInfo(java.lang.String)}.
	 * Si la métrica no está configurada no debe devolver el mapa
	 */
	@Tag("unidad")
	@DisplayName("Verificación de getMetricInfo si la métrica no existe en el fichero de configuración")
	@Test
	void testGetMetricInfo1() {
		HashMap<String, String> returnedMap = null;

		// Las métricas del test son de enteros, así que creo un entero y un string (el
		// primero no dará problemas el segundo sí
		Integer valOKMock = Integer.valueOf(3);
		String valKOMock = "KO";
		// Primero, sin fichero de configuraci�n de aplicaci�n
		try {
			// Busco una métrica que se que no está en la configuración de la api
			log.info("Busco la métrica llamada downloads");
			returnedMap = underTest.getMetricInfo("downloads");
			assertNull(returnedMap,
					"Debería ser nulo, la métrica downloads no está definida en el fichero de configuración por defecto");
		} catch (FileNotFoundException e) {
			fail("El fichero NO está en la carpeta resources");
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.config.MetricConfiguration#definedMetric(java.lang.String, java.lang.String)}.
	 * Si la métrica existe debe devolver el mapa con los valores
	 */
	@Tag("unidad")
	@DisplayName("Verificación getMetricInfo si la métrica existe en el fichero de configuración por defecto")
	@Test
	void testGetMetricInfo2() {
		HashMap<String, String> returnedMap = null;

		try {

			/*
			 * En el fichero por defecto la métrica issues está definida del siguiente modo
			 * { "name": "issues", "type": "java.lang.Integer", "description":
			 * "Tareas totales", "unit": "issues" }
			 */
			// Primero se busca la métrica con el tipo definido
			HashMap<String, String> metricInfo = underTest.getMetricInfo("issues");
			assertEquals("issues", metricInfo.get("name"), "No se ha leído bien el nombre de la métrica");
			assertEquals("java.lang.Integer", metricInfo.get("type"), "No se ha leído bien el tipo de la métrica");
			assertEquals("Tareas totales", metricInfo.get("description"),
					"No se ha leído bien la descripción de la métrica");
			assertEquals(metricInfo.get("unit"), "issues", "No se ha leído bien las unidades de la métrica");

		} catch (FileNotFoundException e) {
			fail("El fichero NO está en la carpeta resources");
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.config.MetricConfiguration#listAllMetrics()}. El número
	 * de métricas debe coincidir con las que haya en el fichero de configuración
	 */
	@Tag("unidad")
	@DisplayName("Verificación de consulta de métricas para el fichero de configuración por defecto")
	@Test
	void testListAllMetrics() {
		List<String> metricsList;
		try {
			metricsList = underTest.listAllMetrics();
			/**
			 * En el momento de codificar este test 21/3/25 el número de métricas en el
			 * fichero de configuración por defecto es 39
			 */
			assertEquals(39, metricsList.size(), "No lee correctamente las métricas");
		} catch (FileNotFoundException e) {
			fail("No debería lanzar esta excepción");
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
	@DisplayName("Verificación de excepción FileNotFound cuando el fichero por defecto no está bien indicado")
	@Test
	void testExceptionFile() {
		underTest = new MetricConfiguration("defaultKO", appConfPath);
		/*
		 * el fichero por defecto no existe, pruebo los tres métodos
		 */
		FileNotFoundException thrown = assertThrows(FileNotFoundException.class,

				() -> underTest.getMetricInfo("downloads"),
				"Debería haber lanzado la excepción de fichero no encontrado, pero no lo ha hecho");

		assertTrue(thrown.getMessage().contains("defaultKO"), "La excepción debería indicar el fichero no localizado");

		thrown = assertThrows(FileNotFoundException.class,

				() -> underTest.definedMetric("downloads", "java.lang.Integer"),
				"Debería haber lanzado la excepción de fichero no encontrado, pero no lo ha hecho");

		assertTrue(thrown.getMessage().contains("defaultKO"), "La excepción debería indicar el fichero no localizado");

		thrown = assertThrows(FileNotFoundException.class,

				() -> underTest.listAllMetrics(),
				"Debería haber lanzado la excepción de fichero no encontrado, pero no lo ha hecho");

		assertTrue(thrown.getMessage().contains("defaultKO"), "La excepción debería indicar el fichero no localizado");

	}

}
