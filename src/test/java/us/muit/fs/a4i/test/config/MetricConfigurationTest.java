package us.muit.fs.a4i.test.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.logging.Logger;

import us.muit.fs.a4i.config.MetricConfigurationI;

public class MetricConfigurationTest {
    private static Logger log = Logger.getLogger(MetricConfiguration.class.getName());
    static MetricConfiguration underTest;
    static String appConfPath;

    void testDefinedMetric() {
		
		//Creo valores Mock para verificar si comprueba bien el tipo
		//Las m�tricas del test son de enteros, as� que creo un entero y un string (el primero no dar� problemas el segundo s�)
		Integer valOKMock = Integer.valueOf(3);
		String valKOMock = "KO";
		HashMap<String,String> returnedMap=null;
		//Primero, sin fichero de configuraci�n de aplicaci�n
		try {
			//Consulta una m�trica no definida, con valor de tipo entero
			//debe devolver null, no est� definida
			log.info("Busco la m�trica llamada downloads");
			returnedMap=underTest.definedMetric("downloads", valOKMock.getClass().getName());
			assertNull(returnedMap, "Deber�a ser nulo, la m�trica noexiste no est� definida");
			
			//Busco la m�trica watchers con valor entero, no deber�a dar problemas
			log.info("Busco la m�trica watchers");
			returnedMap=underTest.definedMetric("watchers", valOKMock.getClass().getName());
			assertNotNull(returnedMap,"Deber�a devolver un hashmap, la m�trica est� definida");
			assertTrue(returnedMap.containsKey("unit"),"La clave unit tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("description"),"La clave description tiene que estar en el mapa");
	        
			//Busco una m�trica que existe pero con un tipo incorrecto en el valor
			assertNull(underTest.definedMetric("watchers", valKOMock.getClass().getName()),
					"Deber�a ser nulo, la m�trica est� definida para Integer");
		} catch (FileNotFoundException e) {
			fail("El fichero est� en la carpeta resources");
			e.printStackTrace();
		}
		
		//Ahora establezco el fichero de configuraci�n de la aplicaci�n, con un nombre de fichero que no existe
		underTest.setAppMetrics("pepe");
		try {
			//Busco una m�trica que se que no est� en la configuraci�n de la api
			returnedMap=underTest.definedMetric("downloads", valOKMock.getClass().getName());
			fail("Deber�a lanzar una excepci�n porque intenta buscar en un fichero que no existe");
		} catch (FileNotFoundException e) {
			log.info("Lanza la excepci�n adecuada, FileNotFoud");
		} catch (Exception e) {
			fail("Lanza la excepci�n equivocada " + e);
		}
			
		//Ahora establezco un fichero de configuraci�n de la aplicaci�n que s� existe
		underTest.setAppMetrics(appConfPath);
		try {
			//Busco una m�trica que se que no est� en la configuraci�n de la api pero s� en la de la aplicaci�n
			log.info("Busco la m�trica llamada downloads");
			returnedMap=underTest.definedMetric("downloads", valOKMock.getClass().getName());
			assertNotNull(returnedMap,"Deber�a devolver un hashmap, la m�trica est� definida");
			assertTrue(returnedMap.containsKey("unit"),"La clave unit tiene que estar en el mapa");
			assertTrue(returnedMap.containsKey("description"),"La clave description tiene que estar en el mapa");
		} catch (FileNotFoundException e) {
			fail("No deber�a devolver esta excepci�n");
		} catch (Exception e) {
			fail("Lanza una excepci�n no reconocida " + e);
		}
	}
}
