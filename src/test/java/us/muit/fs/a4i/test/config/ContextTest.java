/**
 * 
 */
package us.muit.fs.a4i.test.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.config.Context;

/**
 * @author Isabel Román
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
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#setAppConf(java.lang.String)}.
	 */
	@Test
	void testSetAppConf() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#getChecker()}.
	 */
	@Test
	void testGetChecker() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#getPersistenceType()}.
	 */
	@Test
	void testGetPersistenceType() {
		try {					
			assertEquals("excel",Context.getContext().getPersistenceType(),"Debería estar definido el tipo excel por defecto en a4i.conf");
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
			assertEquals("github",Context.getContext().getRemoteType(),"Debería estar definido el tipo github por defecto en a4i.conf");
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
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#getMetricFont()}.
	 */
	@Test
	void testGetMetricFont() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#getIndicatorFont(us.muit.fs.a4i.model.entities.Indicator.State)}.
	 */
	@Test
	void testGetIndicatorFont() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#getPropertiesNames()}.
	 */
	@Test
	void testGetPropertiesNames() {
		fail("Not yet implemented");
	}

}
