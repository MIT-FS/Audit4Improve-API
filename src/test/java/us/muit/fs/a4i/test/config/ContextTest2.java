/**
 * Separo los tests de Context en dos, para evitar el problema con el singleton:
 * Si se modifican los ficheros de configuración los resultados son distintos, porque el orden de ejecución de los tests es aleatorio
 */
package us.muit.fs.a4i.test.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import us.muit.fs.a4i.model.entities.Font; // Clase que sustituye a java.awt.Font

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.config.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import us.muit.fs.a4i.model.entities.IndicatorI;
import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Isabel Román Verificación de la clase context cuando hay ficheros de
 *         configuración personalizados
 */
class ContextTest2 {
	private static Logger log = Logger.getLogger(ContextTest2.class.getName());
	/**
	 * Ruta al fichero de configuración de indicadores y métricas establecidos por
	 * la aplicación
	 */
	static String appConfPath;
	/**
	 * Ruta al fichero de configuración de propiedades de funcionamiento
	 * establecidos por la aplicación
	 */
	static String appPath;

	/**
	 * @throws java.lang.Exception Se establecen los ficheros de configuración de la
	 *                             api y de métricas e indicadores propietarios
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		log.info("Estableciendo las rutas de los ficheros de configuración del cliente");
		// Fichero de métricas e indicadores establecido por el cliente
		appPath = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "appConfTest.json";
		// Fichero de configuración de la api establecido por el cliente
		appConfPath = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "appTest.conf";
		Context.setAppConf(appConfPath);
		Context.setAppRI(appPath);
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
		// Ejecutar tras cada test
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#getContext()}.
	 */
	@Test
	void testGetContext() {
		try {
			assertNotNull(Context.getContext(), "Devuelve null");
			assertTrue(Context.getContext() instanceof us.muit.fs.a4i.config.Context, "No es del tipo apropiado");
			assertSame(Context.getContext(), Context.getContext(), "No se devuelve el mismo contexto siempre");

		} catch (IOException e) {
			fail("No debería lanzar esta excepción");
			e.printStackTrace();
		}

	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.config.Context#setAppConf(java.lang.String)}.
	 */
	@Test
	@Tag("Integracion")
	void testSetAppConf() {

		try {

			Context.setAppConf(appPath);
			assertTrue(appPath.equals(Context.getAppConf()),
					"No coincide la ruta del fichero de métricas con la configurada");

		} catch (IOException e) {
			fail("No se encuentra el fichero de especificación de métricas e indicadores");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#getChecker()}.
	 */
	@Test
	void testGetChecker() {
		try {
			assertNotNull(Context.getContext().getChecker(), "No devuelve el checker");
			assertTrue(Context.getContext().getChecker() instanceof us.muit.fs.a4i.config.Checker,
					"No es del tipo apropiado");

		} catch (IOException e) {
			fail("No debería devolver esta excepción");
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#getPersistenceType()}.
	 */
	@Test
	void testGetPersistenceType() {
		try {
			assertEquals("WORD", Context.getContext().getPersistenceType(),
					"En el fichero de configuración personalizado está definido el tipo word");
		} catch (IOException e) {
			fail("El fichero no se localiza");
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#getRemoteType()}.
	 */
	@Test
	void testGetRemoteType() {
		try {
			assertEquals("GITLAB", Context.getContext().getRemoteType(),
					"En el fichero de configuración personalizado está el tipo gitlab");
		} catch (IOException e) {
			fail("El fichero no se localiza");
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Este test permite verificar que se lee bien la fuente. Este método es
	 * idéntico al de la clase ContextTest porque el fichero personalizado no
	 * modifica los parámetros por defecto
	 * </p>
	 * Test method for {@link us.muit.fs.a4i.config.Context#getDefaultFont()}.
	 *
	 */
	@Test
	void testGetDefaultFont() {
		try {
			Font font = null;
			String color;
			// Uso esto para ver los tipos de fuentes de los que dispongo
			// String[] fontNames =
			// GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			// log.info("listado de fuentes " + Arrays.toString(fontNames));
			font = Context.getContext().getDefaultFont();
			assertNotNull(font, "No se ha inicializado bien la fuente");
			assertEquals(Color.BLACK.toString(), font.getColor().toString(),
					"No es el color de fuente especificado en el fichero de propiedades");
			assertEquals(10, font.getFont().getSize(),
					"No es el tamaño de fuente especificado en el fichero de propiedades");
			assertEquals("Arial", font.getFont().getFamily(),
					"No es el tipo de fuente especificado en el fichero de propiedades");

		} catch (IOException e) {
			fail("No debería devolver esta excepción");
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Este test permite verificar si se sobreescribe la configuración por defecto
	 * </p>
	 * Test method for {@link us.muit.fs.a4i.config.Context#getMetricFont()}.
	 * 
	 * @throws IOException
	 */
	@Test

	void testGetMetricFont() {
		try {

			Font font = null;
			// Uso esto para ver los tipos de fuentes de los que dispongo
			// String[] fontNames =
			// GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			// log.info("listado de fuentes " + Arrays.toString(fontNames));
			font = Context.getContext().getMetricFont();
			assertNotNull(font, "No se ha inicializado bien la fuente");
			assertEquals(Color.RED.toString(), font.getColor().toString(),
					"No es el color de fuente especificado en el fichero de propiedades");
			assertTrue(15 == font.getFont().getSize(),
					"No es el tamaño de fuente especificado en el fichero de propiedades");
			assertEquals("Arial", font.getFont().getFamily(),
					"No es el tipo de fuente especificado en el fichero de propiedades");

		} catch (IOException e) {
			fail("No debería devolver esta excepción");
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.config.Context#getIndicatorFont(us.muit.fs.a4i.model.entities.Indicator.State)}.
	 */
	@Test
	void testGetIndicatorFont() {
		try {
			Font font = null;

			// Se le solicita la fuente del estado indefinido, que tendrá los valores por
			// defecto al no estar
			// definidas sus propiedades en el fichero de configuración utilizados en los
			// tests.

			font = Context.getContext().getIndicatorFont(IndicatorState.UNDEFINED);
			assertNotNull(font, "No se ha inicializado bien la fuente");
			// El nombre o tipo de la fuente podrá ser Arial o Times según el momento en el
			// que se realicen los tests.
			assertEquals("Arial", font.getFont().getFamily(),
					"No es el tipo de fuente especificado en el fichero de propiedades");

			// Se le solicita al contexto la fuente del estao "CRITICAL"
			font = Context.getContext().getIndicatorFont(IndicatorState.CRITICAL);
			assertNotNull(font, "No se ha inicializado bien la fuente");
			assertEquals("Courier New", font.getFont().getFamily(),
					"No es el tipo de fuente especificado en el fichero de propiedades");
			assertEquals(Color.RED.toString(), font.getColor().toString(),
					"No es el color de fuente especificado en el fichero de propiedades");
			assertEquals(20, font.getFont().getSize(),
					"No es el tamaño de fuente especificado en el fichero de propiedades");

		} catch (IOException e) {
			fail("No debería devolver esta excepción");
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#getPropertiesNames()}.
	 * 
	 * @throws IOException Este método de verificación está incompleto, deberá ser
	 *                     completado commo ejercicio Verificar que los nombres son
	 *                     correctos
	 */
	@Test
	void testGetPropertiesNames() throws IOException {
		log.info(Context.getContext().getPropertiesNames().toString());
	}

}
