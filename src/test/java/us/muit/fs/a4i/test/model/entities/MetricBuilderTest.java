/**
 * 
 */
package us.muit.fs.a4i.test.model.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.model.entities.Metric.MetricBuilder;

import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.model.entities.Metric;

/**
 * <p>
 * Test para probar el constructor de objetos de tipo Metric
 * </p>
 * 
 * @author Isabel Román
 *
 */
class MetricBuilderTest {
	private static Logger log = Logger.getLogger(MetricBuilderTest.class.getName());

	/**
	 * @throws java.lang.Exception Se incluye por defecto al crear automáticamente los tests con eclipse
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		//Acciones a realizar antes de ejecutar los tests de esta clase
	}

	/**
	 * @throws java.lang.Exception Se incluye por defecto al crear automáticamente los tests con eclipse
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		//Acciones a realizar después de ejecutar todos los tests de esta clase
	}

	/**
	 * @throws java.lang.Exception Se incluye por defecto al crear automáticamente los tests con eclipse
	 */
	@BeforeEach
	void setUp() throws Exception {
		//Acciones a realizar antes de cada uno de los tests de esta clase
	}

	/**
	 * @throws java.lang.Exception Se incluye por defecto al crear automáticamente los tests con eclipse
	 */
	@AfterEach
	void tearDown() throws Exception {
		//Acciones a realizar después de cada uno de los tests de esta clase
	}

	/**
	 * Test para el constructor Test de MetricBuilder: 
	 * {@link us.muit.fs.a4i.model.entities.Metric.MetricBuilder#MetricBuilder(java.lang.String, java.lang.Object)}.
	 * @see org.junit.jupiter.api.Test
	 */
	@Test
	@Tag("integración")
	@DisplayName("Prueba constructor métricas, las clases Context y Checker ya están disponibles")
	void testMetricBuilder() {
		
		//Comenzamos probando el caso más sencillo, la métrica existe y el tipo es correcto
		MetricBuilder underTest = null;
//		assertEquals("watchers", "gadsf", "El nombre establecido no es correcto");
		try {
			underTest = new MetricBuilder<Integer>("watchers", 33);
		} catch (MetricException e) {
			e.printStackTrace();
			fail("No debería haber saltado esta excepción");
			
		}
		Metric newMetric = underTest.build();
		log.info("Métrica creada "+newMetric.toString());
		assertEquals("watchers", newMetric.getName(), "El nombre establecido no es correcto");
		assertEquals(33, newMetric.getValue(), "El valor establecido no es correcto");
		assertEquals(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)).toString(),
				newMetric.getDate().toString(), "La fecha establecida no es correcta");
		assertEquals(newMetric.getDescription(), "Observadores, en la web aparece com forks","La descripción no coincide con la del fichero de configuración");
		assertNull(newMetric.getSource(), "El origen no debería estar incluido");
		assertEquals(newMetric.getUnit(),"watchers", "No debería incluir las unidades");
		
		// A continuación se prueba que se hace verificación correcta del tipo de métrica
		// Prueba un tipo que no se corresponde con el definido por la métrica, tiene que lanzar la excepción MetricException
		try {
			underTest = new MetricBuilder<String>("watchers", "hola");
			fail("Debería haber lanzado una excepción");
		} catch (MetricException e) {
			log.info("Lanza la excepción adecuada, MetricException");

		} catch (Exception e) {
			fail("La excepción capturada es " + e + " cuando se esperaba de tipo MetricException");
		}
		//Forma ALTERNATIVA de verificar el lanzamiento de una excepción, usando la verificación assertThrows
		MetricException thrown = assertThrows(MetricException.class, () -> {
			new MetricBuilder<String>("watchers", "hola");
				}, "Se esperaba la excepción MetricException");
		//verifica también que el mensaje es correcto
		assertEquals("Métrica watchers no definida o tipo java.lang.String incorrecto", thrown.getMessage(),"El mensaje de la excepción no es correcto");
		//El constructor de métricas no permite que se incluyan métricas no definidas
		// Prueba una métrica que no existe
		try {
			underTest = new MetricBuilder<String>("pepe", "hola");
			fail("Debería haber lanzado una excepción");
		} catch (MetricException e) {
			log.info("Lanza la excepción adecuada, MetricException");

		} catch (Exception e) {
			fail("La excepción capturada es " + e + " cuando se esperaba de tipo MetricException");
		}

	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.model.entities.Metric.MetricBuilder#description(java.lang.String)}.
	 */
	@Test
	void testDescription() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.model.entities.Metric.MetricBuilder#source(java.lang.String)}.
	 */
	@Test
	@Tag("integración")
	@DisplayName("Prueba establecer fuente en constructor, las clases Context y Checker ya están disponibles")
	void testSource() {
		//Verificamos que si se establece una fuente en el constructor la métrica creada especifica esa fuente
				MetricBuilder underTest = null;
				try {
					underTest = new MetricBuilder<Integer>("watchers", 33);
				} catch (MetricException e) {
					fail("No debería haber saltado esta excepción");
					e.printStackTrace();
				}
				underTest.source("GitHub");
				Metric newMetric = underTest.build();
				log.info("Métrica creada "+newMetric.toString());			
				assertEquals("GitHub",newMetric.getSource(),"Source no tiene el valor esperado");		
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.model.entities.Metric.MetricBuilder#unit(java.lang.String)}.
	 */
	@Test
	void testUnit() {
		fail("Not yet implemented"); // TODO
	}
	

}
