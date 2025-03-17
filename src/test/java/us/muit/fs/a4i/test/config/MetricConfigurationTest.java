package us.muit.fs.a4i.test.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.config.Checker;
import us.muit.fs.a4i.config.Context;
import us.muit.fs.a4i.config.MetricConfiguration;
import us.muit.fs.a4i.config.MetricConfigurationI;

class MetricConfigurationTest {
	private static Logger log = Logger.getLogger(MetricConfigurationTest.class.getName());
	static MetricConfiguration underTest;

	/**
	 * <p>
	 * Acciones a realizar antes de ejecutar los tests definidos en esta clase.
	 * </p>
	 * 
	 * @throws java.lang.Exception
	 * @see org.junit.jupiter.api.BeforeAll
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		underTest = new MetricConfiguration();
	}

	@Test
	void testDefinedMetric() {
		try {
			/*
			 * En el fichero por defecto la métrica issues está definida del siguiente modo
			 * { "name": "issues", "type": "java.lang.Integer", "description":
			 * "Tareas totales", "unit": "issues" }
			 */
			// Primero se busca la métrica con el tipo definido
			HashMap<String, String> metricInfo = underTest.definedMetric("issues", "java.lang.Integer");
			assertEquals("issues", metricInfo.get("name"), "No se ha leído bien el nombre de la métrica");
			assertEquals("java.lang.Integer", metricInfo.get("type"), "No se ha leído bien el tipo de la métrica");
			assertEquals("Tareas totales",metricInfo.get("description"),
					"No se ha leído bien la descripción de la métrica");
			assertEquals(metricInfo.get("unit"), "issues", "No se ha leído bien las unidades de la métrica");

			// ahora busco con un tipo incorrecto
			// Primero se busca la métrica con el tipo definido
			metricInfo = underTest.definedMetric("issues", "java.lang.String");
			assertNull(metricInfo, "No debería haber localizado la métrica");
		} catch (IOException e) {

			e.printStackTrace();
			fail("No debería devolver esta excepción");
		}
	}

	/**
	 * Verifica la localización de una métrica que existe en el fichero de
	 * configuración por defecto
	 */
	@DisplayName("Verificación de lectura métrica disponible en configuración por defecto")
	@Test
	void testGetMetricInfoOK() {
		try {
			/*
			 * En el fichero por defecto la métrica issues está definida del siguiente modo
			 * { "name": "issues", "type": "java.lang.Integer", "description":
			 * "Tareas totales", "unit": "issues" }
			 */
			// Primero se busca una métrica que existe
			HashMap<String, String> metricInfo = underTest.getMetricInfo("issues");
			assertEquals( "issues", metricInfo.get("name"),"No se ha leído bien el nombre de la métrica");
			assertEquals("java.lang.Integer", metricInfo.get("type"), "No se ha leído bien el tipo de la métrica");
			assertEquals("Tareas totales",metricInfo.get("description"),
					"No se ha leído bien la descripción de la métrica");
			assertEquals("issues", metricInfo.get("unit"),"No se ha leído bien las unidades de la métrica");
		} catch (IOException e) {

			e.printStackTrace();
			fail("No debería devolver esta excepción");
		}
	}

	/**
	 * Verifica la localización de una métrica que NO existe en el fichero de
	 * configuración por defecto
	 */
	@DisplayName("Verificación de lectura métrica no existente")
	@Test
	void testGetMetricInfoKO() {
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

	@Test
	void testListAllMetrics() {
		/*
		 * Actualmente hay 39 métricas en el fichero de configuración por defecto
		 * (17/3/25)
		 */
		try {
			assertEquals(39, underTest.listAllMetrics().size(), "El número de métricas leídas no es correcto");
		} catch (FileNotFoundException e) {
			fail("Lanza excepción indebida, no localiza el fichero");
			e.printStackTrace();
		}
	}

}
