/**
 * Tests para ContextTest sin modificar los ficheros de ocnfiguraicón pro defecto en ningún momento
 */
package us.muit.fs.a4i.test.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.config.Context;

import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;
import us.muit.fs.a4i.model.entities.Font; // Clase que sustituye a java.awt.Font

/**
 * @author Isabel Román Verificación de la clase Context cuando sólo se utiliza
 *         el fichero de configuración por defecto (a4i.conf)
 */
class ContextTest {
	private static Logger log = Logger.getLogger(ContextTest.class.getName());
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
	 * @throws java.lang.Exception
	 * 
	 * @BeforeAll static void setUpBeforeClass() throws Exception { appConfPath =
	 *            "src" + File.separator + "test" + File.separator + "resources" +
	 *            File.separator + "appConfTest.json"; appPath = "src" +
	 *            File.separator + "test" + File.separator + "resources" +
	 *            File.separator + "appTest.conf"; }
	 */

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
			assertEquals("EXCEL", Context.getContext().getPersistenceType(),
					"En el fichero de configuración por defecto, a4i.conf, está definido el tipo excel");
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
			assertEquals("GITHUB", Context.getContext().getRemoteType(),
					"En el fichero de configuración por defecto, a4i.conf, está el tipo github");
		} catch (IOException e) {
			fail("El fichero no se localiza");
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Este test permite verificar que se lee bien la fuente, además es
	 * independiente del orden de ejecución del resto de test. La complejidad de la
	 * verifiación está impuesta por estar probando un singleton
	 * </p>
	 * Test method for {@link us.muit.fs.a4i.config.Context#getDefaultFont()}.
	 */
	@Test
	void testGetDefaultFont() {
		try {
			Font font = null;
			String color; // No entiendo cómo usarlo, ¿para darle valor luego?//
			// Uso esto para ver los tipos de fuentes de los que dispongo
			// String[] fontNames =
			// GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			// log.info("listado de fuentes " + Arrays.toString(fontNames));
			font = Context.getContext().getDefaultFont();
			assertNotNull(font, "No se ha inicializado bien la fuente");
			// Al ser Context un singleton una vez creada la instancia no se puede eliminar
			// "desde fuera"
			// De manera que el valor de la fuente depende del orden en el que se ejecuten
			// los test, y para que el test sea independiente de eso la verificación
			// comprueba los dos posibles valores
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
	 * Test method for {@link us.muit.fs.a4i.config.Context#getMetricFont()}.
	 * 
	 * @throws IOException
	 */
	@DisplayName("Verificación lectura de configuración de fuente de métricas")
	@Test
	void testGetMetricFont() {
		try {

			Font font = null;

			font = Context.getContext().getMetricFont();
			assertNotNull(font, "No se ha inicializado bien la fuente");
			assertEquals(Color.GREEN.toString(), font.getColor().toString(),
					"No es el color de fuente especificado en el fichero de propiedades");
			assertTrue(10 == font.getFont().getSize(),
					"No es el tamaño de fuente especificado en el fichero de propiedades");
			assertEquals(java.awt.Font.SERIF, font.getFont().getFamily(),
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
	@DisplayName("Verificación lectura de configuración de fuente de indicadores")
	@Test
	void testGetIndicatorFont() {
		try {
			Font font = null;

			// Se le solicita la fuente del estado indefinido, que tendrá los valores por
			// defecto Font.Default.xxx al no estar
			// definidas sus propiedades en el fichero de configuración utilizados en los
			// tests.
			font = Context.getContext().getIndicatorFont(IndicatorState.OK);
			assertNotNull(font, "No se ha inicializado bien la fuente");
			// El nombre o tipo de la fuente podrá ser Arial o Times según el momento en el
			// que se realicen los tests.
			log.info("familia " + font.getFont().getFamily());
			assertEquals("Serif", font.getFont().getFamily(),
					"No es el tipo de fuente especificado en el fichero de propiedades");
			assertEquals(Color.BLACK, font.getColor(),
					"No es el color de fuente especificado en el fichero de propiedades");
			assertEquals(10, font.getFont().getSize(),
					"No es el tamaño de fuente especificado en el fichero de propiedades");

			// Se le solicita al contexto la fuente del estao "CRITICAL", cuyas propiedades
			// están definidas en el
			// fichero de configuración por defecto.
			font = Context.getContext().getIndicatorFont(IndicatorState.CRITICAL);

			assertNotNull(font, "No se ha inicializado bien la fuente");

			log.info("Datos fuente estado CRITICAL familia " + font.getFont().getFamily() + " tamano "
					+ font.getFont().getSize() + " color " + font.getColor());

			assertTrue(font.getFont().getFamily().equals("Arial"),
					"No es el tipo de fuente especificado en el fichero de propiedades");
			assertEquals(Color.RED, font.getColor(),
					"No es el color de fuente especificado en el fichero de propiedades");
			assertEquals(12, font.getFont().getSize(),
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
	@DisplayName("Verificación obtención nombre de propiedades configuradas")
	@Test
	void testGetPropertiesNames() throws IOException {
		log.info(Context.getContext().getPropertiesNames().toString());
	}

}
