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
 * @author Isabel Rom�n
 *
 */
class MetricBuilderTest {
	private static Logger log = Logger.getLogger(MetricBuilderTest.class.getName());

	/**
	 * @throws java.lang.Exception Se incluye por defecto al crear autom�ticamente los tests con eclipse
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		//Acciones a realizar antes de ejecutar los tests de esta clase
	}

	/**
	 * @throws java.lang.Exception Se incluye por defecto al crear autom�ticamente los tests con eclipse
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		//Acciones a realizar despu�s de ejecutar todos los tests de esta clase
	}

	/**
	 * @throws java.lang.Exception Se incluye por defecto al crear autom�ticamente los tests con eclipse
	 */
	@BeforeEach
	void setUp() throws Exception {
		//Acciones a realizar antes de cada uno de los tests de esta clase
	}

	/**
	 * @throws java.lang.Exception Se incluye por defecto al crear autom�ticamente los tests con eclipse
	 */
	@AfterEach
	void tearDown() throws Exception {
		//Acciones a realizar despu�s de cada uno de los tests de esta clase
	}

	/**
	 * Test para el constructor Test de MetricBuilder: 
	 * {@link us.muit.fs.a4i.model.entities.Metric.MetricBuilder#MetricBuilder(java.lang.String, java.lang.Object)}.
	 * @see org.junit.jupiter.api.Test
	 */
	@Test
	@Tag("integraci�n")
	@DisplayName("Prueba constructor m�tricas, las clases Context y Checker ya est�n disponibles")
	void testMetricBuilder() {
		
		//Comenzamos probando el caso m�s sencillo, la m�trica existe y el tipo es correcto
		MetricBuilder underTest = null;
		try {
			underTest = new MetricBuilder<Integer>("watchers", 33);
		} catch (MetricException e) {
			fail("No deber�a haber saltado esta excepci�n");
			e.printStackTrace();
		}
		Metric newMetric = underTest.build();
		log.info("M�trica creada "+newMetric.toString());
		assertEquals("watchers", newMetric.getName(), "El nombre establecido no es correcto");
		assertEquals(33, newMetric.getValue(), "El valor establecido no es correcto");
		assertEquals(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)).toString(),
				newMetric.getDate().toString(), "La fecha establecida no es correcta");
		assertEquals(newMetric.getDescription(), "Observadores, en la web aparece com forks","La descripci�n no coincide con la del fichero de configuraci�n");
		assertNull(newMetric.getSource(), "El origen no deber�a estar incluido");
		assertEquals(newMetric.getUnit(),"watchers", "No deber�a incluir las unidades");
		
		// A continuaci�n se prueba que se hace verificaci�n correcta del tipo de m�trica
		// Prueba un tipo que no se corresponde con el definido por la m�trica, tiene que lanzar la excepci�n MetricException
		try {
			underTest = new MetricBuilder<String>("watchers", "hola");
			fail("Deber�a haber lanzado una excepci�n");
		} catch (MetricException e) {
			log.info("Lanza la excepci�n adecuada, MetricException");

		} catch (Exception e) {
			fail("La excepci�n capturada es " + e + " cuando se esperaba de tipo MetricException");
		}
		//Forma ALTERNATIVA de verificar el lanzamiento de una excepci�n, usando la verificaci�n assertThrows
		MetricException thrown = assertThrows(MetricException.class, () -> {
			new MetricBuilder<String>("watchers", "hola");
				}, "Se esperaba la excepci�n MetricException");
		//verifica tambi�n que el mensaje es correcto
		assertEquals("M�trica watchers no definida o tipo java.lang.String incorrecto", thrown.getMessage(),"El mensaje de la excepci�n no es correcto");
		//El constructor de m�tricas no permite que se incluyan m�tricas no definidas
		// Prueba una m�trica que no existe
		try {
			underTest = new MetricBuilder<String>("pepe", "hola");
			fail("Deber�a haber lanzado una excepci�n");
		} catch (MetricException e) {
			log.info("Lanza la excepci�n adecuada, MetricException");

		} catch (Exception e) {
			fail("La excepci�n capturada es " + e + " cuando se esperaba de tipo MetricException");
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
	@Tag("integraci�n")
	@DisplayName("Prueba establecer fuente en constructor, las clases Context y Checker ya est�n disponibles")
	void testSource() {
		//Verificamos que si se establece una fuente en el constructor la m�trica creada especifica esa fuente
				MetricBuilder underTest = null;
				try {
					underTest = new MetricBuilder<Integer>("watchers", 33);
				} catch (MetricException e) {
					fail("No deber�a haber saltado esta excepci�n");
					e.printStackTrace();
				}
				underTest.source("GitHub");
				Metric newMetric = underTest.build();
				log.info("M�trica creada "+newMetric.toString());			
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
