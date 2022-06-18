/**
 * 
 */
package us.muit.fs.a4i.test.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.config.Checker;
import us.muit.fs.a4i.config.Context;

/**
 * @author isa
 *
 */
class MetricInfo {

	/**
	 * Test method for {@link us.muit.fs.a4i.config.Context#getChecker()}.
	 */
	@Test
	@Tag("Integracion")
	void testGetChecker() {
		try {
			Checker checker=Context.getContext().getChecker();
			HashMap<String,String> metricInfo=checker.getMetricInfo("issues");
			assertEquals(metricInfo.get("name"),"issues","No se ha leído bien el nombre de la métrica");
			assertEquals(metricInfo.get("type"),"java.lang.Integer","No se ha leído bien el tipo de la métrica");
			assertEquals(metricInfo.get("description"),"Tareas sin finalizar en el repositorio","No se ha leído bien la descripción de la métrica");
			assertEquals(metricInfo.get("unit"),"issues","No se ha leído bien las unidades de la métrica");
		} catch (IOException e) {
			
			e.printStackTrace();
			fail("No debería devolver esta excepción");
		}
		
	}

}

