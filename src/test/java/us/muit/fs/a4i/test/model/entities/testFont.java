/**
 * 
 */
package us.muit.fs.a4i.test.model.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.model.entities.Font;

/**
 * 
 */
class testFont {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.model.entities.Font#Font(java.lang.String, int, java.lang.String)}.
	 */
	@Test
	void testFontStringIntString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link us.muit.fs.a4i.model.entities.Font#getColor()}.
	 */
	@Test
	void testConstructor() {
		Font underTest=new Font(java.awt.Font.SANS_SERIF,java.awt.Font.BOLD,12,"azul");
		assertEquals(Color.BLUE.toString(),underTest.getColor().toString(),"No se ha establecido bien el color");
		assertEquals(java.awt.Font.SANS_SERIF,underTest.getFont().getFamily(),"No se ha puesto bien el tipo de fuente");
		assertEquals(java.awt.Font.BOLD, underTest.getFont().getStyle(),"El estilo no se ha puesto bien");
		underTest=new Font("Verdana",12,"azul");
		assertEquals(Color.BLUE.toString(),underTest.getColor().toString(),"No se ha establecido bien el color");
		assertEquals("Verdana",underTest.getFont().getFamily(),"No se ha puesto bien el tipo de fuente");
		assertEquals(java.awt.Font.PLAIN, underTest.getFont().getStyle(),"El estilo no se ha puesto bien");
		underTest=new Font("azul");
		assertEquals(Color.BLUE.toString(),underTest.getColor().toString(),"No se ha establecido bien el color");
		assertEquals(java.awt.Font.SERIF,underTest.getFont().getFamily(),"No se ha puesto bien el tipo de fuente");
		assertEquals(java.awt.Font.PLAIN, underTest.getFont().getStyle(),"El estilo no se ha puesto bien");
		underTest=new Font();
		assertEquals(Color.BLACK.toString(),underTest.getColor().toString(),"No se ha establecido bien el color");
		assertEquals(java.awt.Font.SERIF,underTest.getFont().getFamily(),"No se ha puesto bien el tipo de fuente");
		assertEquals(java.awt.Font.PLAIN, underTest.getFont().getStyle(),"El estilo no se ha puesto bien");
	}

}
