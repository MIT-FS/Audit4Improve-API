/**
 * 
 */
package us.muit.fs.a4i.test.control;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.control.ReportManager;
import us.muit.fs.a4i.control.ReportManagerI;
import us.muit.fs.a4i.model.entities.ReportI;

/**
 * @author isabo
 *
 */
class ReportManagerTest {
	private static Logger log = Logger.getLogger(ReportManagerTest.class.getName());

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
	 * Test method for
	 * {@link us.muit.fs.a4i.control.ReportManager#ReportManager(us.muit.fs.a4i.model.entities.ReportI.ReportType)}.
	 */
	@Test
	void testReportManager() {
		ReportManagerI manager = null;
		ReportI report = null;
		try {
			manager = new ReportManager(ReportI.ReportType.REPOSITORY);
			assertNotNull(manager, "No se ha creado el gestor");
			log.info("Creado el gestor");
			report = manager.newReport("MIT-FS/Audit4Improve-API", ReportI.ReportType.REPOSITORY);
			log.info("Creado el informe");
		} catch (Exception e) {
			fail("Se lanza alguna excepción al crear el gestor o el informe");
			e.printStackTrace();
		}
		assertNotNull(report, "No se ha construido el informe");
		System.out.println(report);
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.control.ReportManager#deleteReport(us.muit.fs.a4i.model.entities.ReportI)}.
	 */
	@Test
	void testDeleteReportReportI() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.control.ReportManager#setRemoteEnquirer(us.muit.fs.a4i.model.remote.RemoteEnquirer)}.
	 */
	@Test
	void testSetRemoteEnquirer() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.control.ReportManager#setPersistenceManager(us.muit.fs.a4i.persistence.PersistenceManager)}.
	 */
	@Test
	void testSetPersistenceManager() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.control.ReportManager#setFormater(us.muit.fs.a4i.persistence.ReportFormaterI)}.
	 */
	@Test
	void testSetFormater() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.control.ReportManager#setIndicatorCalc(us.muit.fs.a4i.control.IndicatorsCalculator)}.
	 */
	@Test
	void testSetIndicatorCalc() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.control.ReportManager#saveReport(us.muit.fs.a4i.model.entities.ReportI)}.
	 */
	@Test
	void testSaveReportReportI() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.control.ReportManager#saveReport()}.
	 */
	@Test
	void testSaveReport() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.control.ReportManager#newReport(java.lang.String, us.muit.fs.a4i.model.entities.ReportI.ReportType)}.
	 */
	@Test
	void testNewReport() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.control.ReportManager#deleteReport()}.
	 */
	@Test
	void testDeleteReport() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.control.ReportManager#getReport()}.
	 */
	@Test
	void testGetReport() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.control.ReportManager#addMetric(java.lang.String)}.
	 */
	@Test
	void testAddMetric() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.control.ReportManager#getMetric(java.lang.String)}.
	 */
	@Test
	void testGetMetric() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.control.ReportManager#addIndicator(java.lang.String)}.
	 */
	@Test
	void testAddIndicator() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.control.ReportManager#getIndicator(java.lang.String)}.
	 */
	@Test
	void testGetIndicator() {
		fail("Not yet implemented");
	}

}
