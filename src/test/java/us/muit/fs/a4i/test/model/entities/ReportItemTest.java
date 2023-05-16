/**
 * 
 */
package us.muit.fs.a4i.test.model.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

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

import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItem.ReportItemBuilder;

/**
 * <p>
 * Test para probar el constructor de objetos de tipo ReportItem
 * </p>
 * 
 * @author Isabel Rom�n
 *
 */
class ReportItemTest {
	private static Logger log = Logger.getLogger(ReportItemTest.class.getName());

	/**
	 * @throws java.lang.Exception Se incluye por defecto al crear automáticamente
	 *                             los tests con eclipse
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// Acciones a realizar antes de ejecutar los tests de esta clase
	}

	/**
	 * @throws java.lang.Exception Se incluye por defecto al crear automáticamente
	 *                             los tests con eclipse
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		// Acciones a realizar después de ejecutar todos los tests de esta clase
	}

	/**
	 * @throws java.lang.Exception Se incluye por defecto al crear automáticamente
	 *                             los tests con eclipse
	 */
	@BeforeEach
	void setUp() throws Exception {
		// Acciones a realizar antes de cada uno de los tests de esta clase
	}

	/**
	 * @throws java.lang.Exception Se incluye por defecto al crear automáticamente
	 *                             los tests con eclipse
	 */
	@AfterEach
	void tearDown() throws Exception {
		// Acciones a realizar después de cada uno de los tests de esta clase
	}

	/**
	 * Test para el constructor Test de ReportItemBuilder:
	 * {@link us.muit.fs.a4i.model.entities.ReportItem.ReportItemBuilder#ReportItemBuilder(java.lang.String, java.lang.Object)}.
	 * 
	 * @see org.junit.jupiter.api.Test
	 */
	@Test
	@Tag("unidad")
	@DisplayName("Prueba constructor reportItem, las clases Context y Checker ya est�n disponibles")
	void testReportItemBuilder() {

		// Comenzamos probando el caso más sencillo, la métrica existe y el tipo es
		// correcto
		ReportItemBuilder underTest = null;
		try {
			underTest = new ReportItemBuilder<Integer>("watchers", 33);
		} catch (ReportItemException e) {
			fail("Watchers existe y no deber�a haber saltado esta excepci�n");
			e.printStackTrace();
		}
		ReportItem newMetric = underTest.build();
		log.info("M�trica creada " + newMetric.toString());
		assertEquals("watchers", newMetric.getName(), "El nombre establecido no es correcto");
		assertEquals(33, newMetric.getValue(), "El valor establecido no es correcto");
		assertEquals(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)).toString(),
				newMetric.getDate().toString(), "La fecha establecida no es correcta");
		assertEquals(newMetric.getDescription(), "Observadores, en la web aparece com forks",
				"La descripci�n no coincide con la del fichero de configuraci�n");
		assertNull(newMetric.getSource(), "El origen no deber�a estar incluido");
		assertEquals(newMetric.getUnit(), "watchers", "No deber�a incluir las unidades");

		// A continuación se prueba que se hace verificación correcta del tipo de
		// métrica
		// Prueba un tipo que no se corresponde con el definido por la métrica, tiene
		// que lanzar la excepción MetricException
		try {
			underTest = new ReportItemBuilder<String>("watchers", "hola");
			fail("Deber�a haber lanzado una excepción");
		} catch (ReportItemException e) {
			log.info("Lanza la excepci�n adecuada, ReportItemException");

		} catch (Exception e) {
			fail("La excepci�n capturada es " + e + " cuando se esperaba de tipo ReportItemException");
		}
		// Forma ALTERNATIVA de verificar el lanzamiento de una excepción, usando la
		// verificación assertThrows
		ReportItemException thrown = assertThrows(ReportItemException.class, () -> {
			new ReportItemBuilder<String>("watchers", "hola");
		}, "Se esperaba la excepci�n ReportItemException");
		// verifica también que el mensaje es correcto
		assertEquals("ReportItem watchers no definido o tipo java.lang.String incorrecto", thrown.getMessage(),
				"El mensaje de la excepci�n no es correcto");
		// El constructor de métricas no permite que se incluyan métricas no definidas
		// Prueba una métrica que no existe
		try {
			underTest = new ReportItemBuilder<String>("pepe", "hola");
			fail("Deber�a haber lanzado una excepci�n");
		} catch (ReportItemException e) {
			log.info("Lanza la excepci�n adecuada, ReportItemException");

		} catch (Exception e) {
			fail("La excepci�n capturada es " + e + " cuando se esperaba de tipo ReportItemException");
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
	@Tag("integraci�n")
	@DisplayName("Prueba establecer fuente en constructor, las clases Context y Checker ya est�n disponibles")
	void testSource() {
		// Verificamos que si se establece una fuente en el constructor la métrica
		// creada especifica esa fuente
		ReportItemBuilder underTest = null;
		try {
			underTest = new ReportItemBuilder<Integer>("watchers", 33);
		} catch (ReportItemException e) {
			fail("El elemento watchers existe, no deber�a haber saltado esta excepción");
			e.printStackTrace();
		}
		underTest.source("GitHub");
		ReportItem newMetric = underTest.build();
		log.info("M�trica creada: " + newMetric.toString());
		assertEquals("GitHub", newMetric.getSource(), "Source no tiene el valor esperado");

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