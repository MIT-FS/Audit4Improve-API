/**
 * 
 */
package us.muit.fs.a4i.test.control.managers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import us.muit.fs.a4i.control.ReportManagerI;
import us.muit.fs.a4i.control.managers.ReportManager;
import us.muit.fs.a4i.model.entities.ReportI;

/**
 * @author Isabel Román Martínez
 *
 */
public class ReportManagerTest {
	private static Logger log = Logger.getLogger(ReportManagerTest.class.getName());

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

}
