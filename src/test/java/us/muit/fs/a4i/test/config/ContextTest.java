/**
 * 
 */
package us.muit.fs.a4i.test.config;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import us.muit.fs.a4i.config.Checker;
import us.muit.fs.a4i.config.Context;
import us.muit.fs.a4i.config.MyFont;
import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.model.entities.Indicator.IndicatorBuilder;
import us.muit.fs.a4i.model.entities.Indicator.State;

/**
 * @author Isabel Rom�n
 *
 */
class ContextTest {
	private static Logger log = Logger.getLogger(CheckerTest.class.getName());
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
	 * Test method for {@link us.muit.fs.a4i.config.Context#getContext()}.
	 */
	@Test
	void testGetContext() {
		Context contextut = null;
		try {
			contextut = Context.getContext();
			
		}catch (Exception e) {
			fail("Fallo al abrir el fichero de configuración por defecto");
			e.printStackTrace();
		} 
		log.info("Contexto creado");
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#setAppConf(java.lang.String)}.
	 */
	@Test
	void testSetAppConf() {
		try {
			Context.setAppConf(System.getenv("APP_HOME"));
		}catch (Exception e){
			fail("Fallo al abrir el fichero de configuración cliente");
			e.printStackTrace();
		}
		log.info("Configuración de App actualizada con éxito");
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#getChecker()}.
	 */
	@Test
	void testGetChecker() {
		Checker checker = null;
		try {
			Context contextut = null;
			contextut = Context.getContext();
			checker = contextut.getChecker();
		}catch (Exception e){
			fail("Fallo al abrir el fichero de configuración cliente");
			e.printStackTrace();
		}
		log.info("Checker creado");		
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#getPersistenceType()}.
	 */
	@Test
	void testGetPersistenceType() {
		try {					
			assertEquals("excel",Context.getContext().getPersistenceType(),"Deber�a estar definido el tipo excel por defecto en a4i.conf");
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
			assertEquals("github",Context.getContext().getRemoteType(),"Deber�a estar definido el tipo github por defecto en a4i.conf");
		} catch (IOException e) {
			fail("El fichero no se localiza");
			e.printStackTrace();
		}		
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#getDefaultFont()}.
	 */
	@Test
	void testGetDefaultFont() {
		Context contextut = null;
		try {
			contextut = Context.getContext();
		}catch (Exception e){
			fail("Fallo al abrir el fichero de configuración cliente");
			e.printStackTrace();
		}
		log.info("Contexto creado correctamente en testGetDefaultFont");
		MyFont myFontTest = contextut.getDefaultFont();
		assertEquals(myFontTest.getColor(),Color.BLACK, "El color debería de ser negro, ya que se acaba de crear la fuente y es el color por defecto");
		assertEquals(myFontTest.getSize(),12, "El tamaño debería de ser 12, ya que se acaba de crear la fuente y es el tamaño por defecto");
		assertEquals(myFontTest.getFontName(),"Times", "El tipo debería de ser Times, ya que se acaba de crear la fuente y es el tipo por defecto");
	
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#getMetricFont()}.
	 */
	@Test
	void testGetMetricFont() {
		MyFont myFontTest = null;
		try {
			myFontTest = Context.getContext().getMetricFont();
		}catch (Exception e){
			fail("Fallo al abrir el fichero de configuración cliente");
			e.printStackTrace();
		}
		log.info("Fuente de métrica obtenida correctamente en testGetMetricFont");
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#getIndicatorFont(us.muit.fs.a4i.model.entities.Indicator.State)}.
	 */
	@Test
	void testGetIndicatorFont() {
		MyFont myFontTest = null;
		try {
			myFontTest = Context.getContext().getIndicatorFont(State.OK);
		}catch (Exception e){
			fail("Fallo al abrir el fichero de configuración cliente");
			e.printStackTrace();
		}
		log.info("Fuente de estado OK creada en testGetIndicatorFont");
		assertEquals(myFontTest.getColor(),Color.BLACK, "El color debería de ser negro, ya que el estado del indicador es OK");
		try {
			myFontTest = Context.getContext().getIndicatorFont(State.WARNING);
		}catch (Exception e){
			fail("Fallo al abrir el fichero de configuración cliente");
			e.printStackTrace();
		}
		log.info("Fuente de estado WARNING creada en testGetIndicatorFont");
		assertEquals(myFontTest.getColor(),Color.YELLOW, "El color debería de ser negro, ya que el estado del indicador es WARNING");
		try {
			myFontTest = Context.getContext().getIndicatorFont(State.CRITICAL);
		}catch (Exception e){
			fail("Fallo al abrir el fichero de configuración cliente");
			e.printStackTrace();
		}
		log.info("Fuente de estado CRITICAL creada en testGetIndicatorFont");
		assertEquals(myFontTest.getColor(),Color.RED, "El color debería de ser negro, ya que el estado del indicador es CRITICAL");
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#getPropertiesNames()}.
	 */
	@Test
	void testGetPropertiesNames() {		
		Context contextut = null;
		try {
			contextut = Context.getContext();
		}catch (Exception e){
			fail("Fallo al abrir el fichero de configuración cliente");
			e.printStackTrace();
		}
		log.info("Contexto creado correctamente en testGetPropertiesNames");
		try {
			assertNotNull(contextut.getPropertiesNames(), "Las propiedades del contexto no pueden estar vacías. Al menos debe existir el fichero de configuración a41.conf");
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
