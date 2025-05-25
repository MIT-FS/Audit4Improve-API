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
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

/**
 * Clase para verificar IndicatorConfiguration cuando se configura un fichero de
 * cliente que no existe
 */
class IndicatorConfigurationTest2 {
	private static Logger log = Logger.getLogger(IndicatorConfigurationTest2.class.getName());
	static IndicatorConfiguration underTest;
	static String appConfPath;
	private static String defaultFile = "a4iDefault.json";
	String appIndicatorsPath = "/test/home";

	/**
	 * <p>
	 * Acciones a realizar antes de ejecutar los tests definidos en esta clase. Se
	 * va a crear un objeto IndicatorConfiguration
	 * </p>
	 * 
	 * @throws java.lang.Exception
	 * @see org.junit.jupiter.api.BeforeAll
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		appConfPath = "src" + File.separator + "test" + File.separator + "resources" + File.separator
				+ "appConfTestKO.json";
		underTest = new IndicatorConfiguration(defaultFile, appConfPath);
	}

	/**
	 * Verifico que si el fichero no existe lanza la excepción adecuada
	 */
	@DisplayName("Intento de acceso a fichero que no existe")
	@Test
	void testDefinedIndicatorCustom() {

		List<String> indicadores = null;
		try {

			// Busco una métrica que se que no está en la configuración de la api pero
			// sí en la de la aplicación
			log.info("Consulto los indicadores");
			indicadores = underTest.listAllIndicators();
			fail("Debería haber devuelto una excepción");

		} catch (FileNotFoundException e) {
			assertNotNull(e, "Debería haber recibido una excepción");
		} catch (Exception e) {
			fail("Lanza una excepción no reconocida " + e);
		}
	}

}
