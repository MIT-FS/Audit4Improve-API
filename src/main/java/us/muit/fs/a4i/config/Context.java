/**
 * 
 */
package us.muit.fs.a4i.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import java.awt.Color;
import java.awt.Font;

import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.model.entities.Metric;

/**
 * <p>
 * Clase para la gesti�n de los par�metros de contexto
 * </p>
 * <p>
 * El objetivo de Context es el manejo de la configuraci�n
 * </p>
 * <p>
 * En el estado actual Contexto s�lo es una aproximaci�n a las posiblidades de
 * configuraci�n. Se presentan posibilidades para:
 * </p>
 * <ul>
 * <li>Localizar el fichero en la carpeta resources, incluida en
 * el jar</li>
 * <li>Localizar el fichero en el home de usuario</li>
 * <li>Localizar el fichero en una ruta introducida de forma
 * "programada"</li>
 * </ul>
 * <p>
 * �nico punto para acceso a variables que pueden ser le�das por cualquiera,
 * configuradas s�lo por la clase context
 * </p>
 * <p>
 * Sigue el patr�n singleton
 * </p>
 * 
 * @author Isabel Rom�n
 *
 */
public class Context {

	private static Logger log = Logger.getLogger(Context.class.getName());
	private static Context contextInstance = null;

	private Properties properties = null;
	// Fichero de propiedades de la API, embebido en el jar
	private static String confFile = "a4i.conf";
	// Fichero de propiedades de la API establecido por la aplicaci�n cliente
	private static String appConFile = null;
	// Fichero de configuraci�n de m�tricas e indicadores por defecto, embebido en
	// el jar
	private static String defaultFile = "a4iDefault.json";
	// Fichero de configuraci�n de m�tricas e indicadores establecido por la
	// aplicaci�n cliente
	private static String appFile = null;
	private Checker checker = null;

	private Context() throws IOException {
		setProperties();
		checker = new Checker();
	}

	/**
	 * <p>
	 * Devuelve la instancia �nica de Context. Si no estaba creada la crea, leyendo
	 * la configuraci�n por defecto
	 * </p>
	 * 
	 * @return La instancia �nica de Context
	 * @throws IOException Si hay problemas con la lectura del fichero de
	 *                     configuraci�n
	 */
	public static Context getContext() throws IOException {
		/**
		 * Si no est� creada crea la instancia �nica con las propiedades por defecto
		 */
		if (contextInstance == null) {
			contextInstance = new Context();
		}
		return contextInstance;
	}

	/**
	 * <p>
	 * Establece el fichero de configuraci�n espec�fico de la aplicaci�n cliente.
	 * Las propiedades no establecidas se coger�n de la configuraci�n por defecto
	 * </p>
	 * 
	 * @param appConPath Ruta completa al fichero de configuraci�n establecido por la
	 *               propiedad cliente
	 * @throws IOException Problema lectura fichero
	 */
	public static void setAppConf(String appConPath) throws IOException {
		/**
		 * Vuelve a leer las propiedades incluyendo las establecidas por la aplicaci�n
		 */
		appConFile = appConPath;

		// customFile=System.getenv("APP_HOME")+customFile;
		// Otra opci�n, Usar una variable de entorno para la localizar la ruta de
		// instalaci�n y de ah� coger el fichero de configuraci�n
		// Tambi�n podr�a localizarse en el home de usuario
		getContext().properties.load(new FileInputStream(appConPath));
	}

	public Checker getChecker() {
		return checker;
	}

	/**
	 * <p>
	 * Consulta el tipo de persistencia que se quiere utilizar
	 * </p>
	 * 
	 * @return El tipo de persistencia usado (NOTA: deuda t�cnica, podr�a convenir
	 *         usar un enumerado, para controlar mejor los tipos disponibles)
	 * @throws IOException si hay problemas al consultar las propiedades
	 */
	public String getPersistenceType() throws IOException {
		return properties.getProperty("persistence.type");
	}

	/**
	 * <p>
	 * Consulta el tipo de remoto que se quiere manejar
	 * </p>
	 * 
	 * @return El tipo de remoto (NOTA: deuda t�cnica, podr�a convenir usar un
	 *         enumerado, para controlar mejor los tipos disponibles)
	 * @throws IOException si hay problemas al consultar las propiedades
	 */
	public String getRemoteType() throws IOException {
		return properties.getProperty("remote.type");
	}

	/**
	 * <p>
	 * No Implementado	
	 * Deber� leer las propiedades adecuadas, como color, tama�o, tipo... y
	 * construir un objeto Font
	 * Si no se ha establecido un valor por defecto se crea una fuente simple
	 * </p>
	 * 
	 * @return La fuente por defecto para indicadores y m�tricas
	 */
	public MyFont getDefaultFont() {
		String color = properties.getProperty("Font.default.color");
		String height = properties.getProperty("Font.default.height");
		String type = properties.getProperty("Font.default.type");
		Font font = new Font(type, Font.PLAIN, Integer.parseInt(height));
		MyFont mfont = new MyFont(font, Color.getColor(color));
		return mfont;
	}

	/**
	 * <p>
	 * No Implementado
	 * </p>
	 * <p>
	 * Deber� leer las propiedades adecuadas, como color, tama�o, tipo... y
	 * construir un objeto Font
	 * </p>
	 * <p>
	 * Si no se ha definido una fuente para las m�tricas se debe devolver la fuente
	 * por defecto
	 * </p>
	 * 
	 * @return la fuente para las m�tricas
	 */
	public static Font getMetricFont() {
		Font font = null;
		// TO DO
		return font;
	}

	/**
	 * <p>
	 * No Implementado
	 * </p>
	 * <p>
	 * Deber� leer las propiedades adecuadas, como color, tama�o, tipo... y
	 * construir un objeto Font
	 * </p>
	 * 
	 * @param state Estado para el que se solicita el color de fuente
	 * @return La fuente para el indicador cuando el estado es el par�metro pasado
	 * @throws IOException problema al leer el fichero
	 */

	public static MyFont getIndicatorFont(Indicator.State state) throws IOException {
		Color color = null;
		switch (state) {
			case OK: color = Color.BLACK;
			case WARNING: color = Color.ORANGE;
			case CRITICAL: color = Color.RED;
		}
		return new MyFont(new Font("Times", Font.PLAIN, 12), color);
	}

	/**
	 * <p>
	 * Consulta el nombre de todas las propiedades le�das
	 * </p>
	 * 
	 * @return Conjunto con todos los nombres de las propiedades de configuraci�n
	 *         le�das
	 * @throws IOException si hay problemas al leer las propiedades
	 */
	public Set<String> getPropertiesNames() throws IOException {

		return properties.stringPropertyNames();
	}

	/**
	 * <p>
	 * Crea las propiedades, incluye las propiedades por defecto, leyendo del
	 * fichero de conf de la API (configuraci�n por defecto)
	 * </p>
	 * 
	 * @throws IOException si hay problemas leyendo el fichero
	 */
	private void setProperties() throws IOException {
		log.info("Lectura del fichero de configuraci�n por defecto");
		FileInputStream file;
		// Establecemos las propiedades por defecto, del fichero de configuraci�n
		// embebido en el jar
	
		properties = new Properties();
		String filePath="/"+confFile;
		InputStream is=this.getClass().getResourceAsStream(filePath);
		log.info("InputStream "+is+" para "+filePath);			
		properties.load(is);		
		log.fine("Listado de propiedades "+properties);

	}

}
