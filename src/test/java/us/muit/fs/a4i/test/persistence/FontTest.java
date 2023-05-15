package us.muit.fs.a4i.test.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
//import java.awt.Font;
import java.util.logging.Logger;

import us.muit.fs.a4i.model.entities.Font;

/**
 * Test de la clase Font
 * 
 * @author Denis Ortega Luis
 * @see org.junit.jupiter.api.Tag
 *
 */

class FontTest {
	private static Logger log = Logger.getLogger(FontTest.class.getName());
	
	static Font underTest;
	
	/*Test que comprueba que existe un color por defecto y que su formato es válido*/
	@Test 
	void testColorPorDefecto(){
		underTest = new Font();
		String colorDevuelto;
		
		try {
			//Compruebo que tiene un valor por defecto
			assertNotNull(underTest.getColor(), "Debe tener un color por defecto");
		
			colorDevuelto = undetTest.getColor();
			
		}catch(Exception e) {
			fail("El tipo de dato color no es de tipo String o el formato no es válido");
			e.printStackTrace();
		}
		
		//Compruebo el formato del color por defecto
		try {
			Color.decode(colorDevuelto);
			
		}catch(NumberFormatException e) {
			fail("El formato del color no es un formato válido");
			e.printStackTrace();
		}
	}
	
	
	@Test
	void testFuente() {
		underTest = new Font();
		
		//Compruebo que se devuelve el mismo tipo
		assertEquals(underTest.getFont().getClass(), java.awt.Font.class, "El objeto devuelto debe ser la misma fuente")
	}
	
	@Test
	void testSize() {
		underTest = new Font();
		
		//Compruebo que se tenga un valor por defecto
		assertNotNull(underTest.getSize(), "Debe tener un valor por defecto");
		
		//Compruebo que sea del tipo int
		assertEquals(underTest.getSize().getClass(), Integer.class, "Deberia ser del tipo Integer.");
	}
	
	@Test
	void testNombre() {
		underTest = new Font();
		
		//Compruebo que se tenga un valor por defecto
		assertNotNull(underTest.getName(), "Debe tener un valor por defecto");
		
		//Compruebo que sea del tipo int
		assertEquals(underTest.getName().getClass(), String.class, "Deberia ser del tipo Integer.");
	}
}
