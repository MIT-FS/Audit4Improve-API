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

import us.muit.fs.a4i.model.entities.ReportItem.ReportItemBuilder;

import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.model.entities.ReportItem;

/**
 * <p>
 * Test para probar el constructor de objetos de tipo ReportItem
 * </p>
 * 
 * @author Isabel Rom·n
 *
 */
class ReportItemBuilderTest {
	private static Logger log = Logger.getLogger(ReportItemBuilderTest.class.getName());

	/**
	 * @throws java.lang.Exception Se incluye por defecto al crear autom√°ticamente los tests con eclipse
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		//Acciones a realizar antes de ejecutar los tests de esta clase
	}

	/**
	 * @throws java.lang.Exception Se incluye por defecto al crear autom√°ticamente los tests con eclipse
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		//Acciones a realizar despu√©s de ejecutar todos los tests de esta clase
	}

	/**
	 * @throws java.lang.Exception Se incluye por defecto al crear autom√°ticamente los tests con eclipse
	 */
	@BeforeEach
	void setUp() throws Exception {
		//Acciones a realizar antes de cada uno de los tests de esta clase
	}

	/**
	 * @throws java.lang.Exception Se incluye por defecto al crear autom√°ticamente los tests con eclipse
	 */
	@AfterEach
	void tearDown() throws Exception {
		//Acciones a realizar despu√©s de cada uno de los tests de esta clase
	}

	/**
	 * Test para el constructor Test de ReportItemBuilder: 
	 * {@link us.muit.fs.a4i.model.entities.ReportItem.ReportItemBuilder#ReportItemBuilder(java.lang.String, java.lang.Object)}.
	 * @see org.junit.jupiter.api.Test
	 */
	@Test
	@Tag("integraciÛn")
	@DisplayName("Prueba constructor reportItem, las clases Context y Checker ya est·n disponibles")
	void testReportItemBuilder() {
		
		//Comenzamos probando el caso m√°s sencillo, la m√©trica existe y el tipo es correcto
		ReportItemBuilder underTest = null;
		try {
			underTest = new ReportItemBuilder<Integer>("watchers", 33);
		} catch (MetricException e) {
			fail("No deberÌa haber saltado esta excepciÛn");
			e.printStackTrace();
		}
		ReportItem newMetric = underTest.build();
		log.info("MÈtrica creada "+newMetric.toString());
		assertEquals("watchers", newMetric.getName(), "El nombre establecido no es correcto");
		assertEquals(33, newMetric.getValue(), "El valor establecido no es correcto");
		assertEquals(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)).toString(),
				newMetric.getDate().toString(), "La fecha establecida no es correcta");
		assertEquals(newMetric.getDescription(), "Observadores, en la web aparece com forks","La descripciÛn no coincide con la del fichero de configuraciÛn");
		assertNull(newMetric.getSource(), "El origen no deberÌa estar incluido");
		assertEquals(newMetric.getUnit(),"watchers", "No deberÌa incluir las unidades");
		
		// A continuaci√≥n se prueba que se hace verificaci√≥n correcta del tipo de m√©trica
		// Prueba un tipo que no se corresponde con el definido por la m√©trica, tiene que lanzar la excepci√≥n MetricException
		try {
			underTest = new ReportItemBuilder<String>("watchers", "hola");
			fail("DeberÌa haber lanzado una excepci√≥n");
		} catch (MetricException e) {
			log.info("Lanza la excepciÛn adecuada, MetricException");

		} catch (Exception e) {
			fail("La excepciÛn capturada es " + e + " cuando se esperaba de tipo MetricException");
		}
		//Forma ALTERNATIVA de verificar el lanzamiento de una excepci√≥n, usando la verificaci√≥n assertThrows
		MetricException thrown = assertThrows(MetricException.class, () -> {
			new ReportItemBuilder<String>("watchers", "hola");
				}, "Se esperaba la excepciÛn MetricException");
		//verifica tambi√©n que el mensaje es correcto
		assertEquals("MÈtrica watchers no definida o tipo java.lang.String incorrecto", thrown.getMessage(),"El mensaje de la excepciÛn no es correcto");
		//El constructor de m√©tricas no permite que se incluyan m√©tricas no definidas
		// Prueba una m√©trica que no existe
		try {
			underTest = new ReportItemBuilder<String>("pepe", "hola");
			fail("DeberÌa haber lanzado una excepciÛn");
		} catch (MetricException e) {
			log.info("Lanza la excepciÛn adecuada, MetricException");

		} catch (Exception e) {
			fail("La excepciÛn capturada es " + e + " cuando se esperaba de tipo MetricException");
		}

	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.model.entities.ReportItem.ReportItemBuilder#description(java.lang.String)}.
	 */
	@Test
	void testDescription() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.model.entities.ReportItem.ReportItemBuilder#source(java.lang.String)}.
	 */
	@Test
	@Tag("integraciÛn")
	@DisplayName("Prueba establecer fuente en constructor, las clases Context y Checker ya est·n disponibles")
	void testSource() {
		//Verificamos que si se establece una fuente en el constructor la m√©trica creada especifica esa fuente
				ReportItemBuilder underTest = null;
				try {
					underTest = new ReportItemBuilder<Integer>("watchers", 33);
				} catch (MetricException e) {
					fail("No deberÌa haber saltado esta excepci√≥n");
					e.printStackTrace();
				}
				underTest.source("GitHub");
				ReportItem newMetric = underTest.build();
				log.info("MÈtrica creada "+newMetric.toString());			
				assertEquals("GitHub",newMetric.getSource(),"Source no tiene el valor esperado");
			
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.model.entities.ReportItem.ReportItemBuilder#unit(java.lang.String)}.
	 */
	@Test
	void testUnit() {
		fail("Not yet implemented"); // TODO
	}
	

}