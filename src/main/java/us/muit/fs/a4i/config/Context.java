/**
 * 
 */
package us.muit.fs.a4i.config;

import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import us.muit.fs.a4i.model.entities.Indicator;

/**
 * <p>
 * Clase para la gestión de los parámetros de contexto
 * </p>
 * <p>
 * El objetivo de Context es el manejo de la configuración
 * </p>
 * <p>
 * En el estado actual Contexto sólo es una aproximaci�n a las posiblidades de
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
 * configuradas sólo por la clase context
 * </p>
 * <p>
 * Sigue el patr�n singleton
 * </p>
 * 
 * @author Isabel Román
 *
 */
public class Context {

	private static Logger log = Logger.getLogger(Context.class.getName());
	private static Context contextInstance = null;

	private Properties properties = null;
	// Fichero de propiedades de la API, embebido en el jar
	private static String confFile = "a4i.conf";
	// Fichero de propiedades de la API establecido por la aplicación cliente
	private static String appConFile = null;
	// Fichero de configuración de métricas e indicadores por defecto, embebido en
	// el jar
	private static String defaultFile = "a4iDefault.json";
	// Fichero de configuración de métricas e indicadores establecido por la
	// aplicación cliente
	private static String appFile = null;
	private Checker checker = null;

	private Context() throws IOException {
		setProperties();
		checker = new Checker();
	}

	/**
	 * <p>
	 * Devuelve la instancia única de Context. Si no estaba creada la crea, leyendo
	 * la configuración por defecto
	 * </p>
	 * 
	 * @return La instancia única de Context
	 * @throws IOException Si hay problemas con la lectura del fichero de
	 *                     configuración
	 */
	public static Context getContext() throws IOException {
		/**
		 * Si no está creada crea la instancia única con las propiedades por defecto
		 */
		if (contextInstance == null) {
			contextInstance = new Context();
		}
		return contextInstance;
	}

	/**
	 * <p>
	 * Establece el fichero de configuración específico de la aplicación cliente.
	 * Las propiedades no establecidas se coger�n de la configuraci�n por defecto
	 * </p>
	 * 
	 * @param appConPath Ruta completa al fichero de configuración establecido por la
	 *               propiedad cliente
	 * @throws IOException Problema lectura fichero
	 */
	public static void setAppConf(String appConPath) throws IOException {
		/**
		 * Vuelve a leer las propiedades incluyendo las establecidas por la aplicaci�n
		 */
		Context.appConFile = appConPath;

		// customFile=System.getenv("APP_HOME")+customFile;
		// Otra opción, Usar una variable de entorno para la localizar la ruta de
		// instalación y de ahí coger el fichero de configuración
		// También podría localizarse en el home de usuario
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
	 * @return El tipo de persistencia usado (NOTA: deuda técnica, podría convenir
	 *         usar un enumerado, para controlar mejor los tipos disponibles)
	 * @throws IOException si hay problemas al consultar las propiedades
	 */
	public String getPersistenceType() throws IOException {
		return getProperty("persistence.type");
	}

	/**
	 * <p>
	 * Consulta el tipo de remoto que se quiere manejar
	 * </p>
	 * 
	 * @return El tipo de remoto (NOTA: deuda t�cnica, podría convenir usar un
	 *         enumerado, para controlar mejor los tipos disponibles)
	 * @throws IOException si hay problemas al consultar las propiedades
	 */
	public String getRemoteType() throws IOException {
		return getProperty("remote.type");
	}

	/**
	 * <p>
	 * No Implementado	
	 * Deber� leer las propiedades adecuadas, como color, tamaño, tipo... y
	 * construir un objeto Font
	 * Si no se ha establecido un valor por defecto se crea una fuente simple
	 * </p>
	 * 
	 * @return La fuente por defecto para indicadores y métricas
	 */
	public MyFont getDefaultFont() {
		// check for client settings first, if null, use default configuration
		String color = getProperty("Font.color");
		if (color == null)
			color = getProperty("Font.default.color");
		String height = getProperty("Font.height");
		if (height == null)
			height = getProperty("Font.default.height");
		String type = getProperty("Font.type") != null ? getProperty("Font.type") : getProperty("Font.default.type");
		Font font = new Font(type, Font.PLAIN, Integer.parseInt(height));
		return new MyFont(font, Color.getColor(color));
	}

	/**
	 * <p>
	 * No Implementado
	 * </p>
	 * <p>
	 * Deber� leer las propiedades adecuadas, como color, tamaño, tipo... y
	 * construir un objeto Font
	 * </p>
	 * <p>
	 * Si no se ha definido una fuente para las métricas se debe devolver la fuente
	 * por defecto
	 * </p>
	 * 
	 * @return la fuente para las métricas
	 */
	public static MyFont getMetricFont() {
		try{
			return getContext().getDefaultFont();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * <p>
	 * No Implementado
	 * </p>
	 * <p>
	 * Deber� leer las propiedades adecuadas, como color, tamaño, tipo... y
	 * construir un objeto Font
	 * </p>
	 * 
	 * @param state Estado para el que se solicita el color de fuente
	 * @return La fuente para el indicador cuando el estado es el parámetro pasado
	 * @throws IOException problema al leer el fichero
	 */

	public static MyFont getIndicatorFont(Indicator.State state) throws IOException {
		MyFont mfont = getContext().getDefaultFont();
		Color color = Color.BLACK;
		switch (state) {
			case OK:
				color = getPropertyS("Font.color.ok") != null ? Color.getColor(getPropertyS("Font.color.ok")) : color;
				break;
			case WARNING: 
				color = getPropertyS("Font.color.warning") != null ? Color.getColor(getPropertyS("Font.color.warning")) : Color.YELLOW;
				break;
			case CRITICAL: 
				color = getPropertyS("Font.color.critical") != null ? Color.getColor(getPropertyS("Font.color.critical")) : Color.RED;
		}
		mfont.setColor(color);
		return mfont;
	}
	
	private static String getPropertyS(String str) throws IOException {
		return getContext().properties.getProperty(str);
	}
	
	private String getProperty(String str) {
		return properties.getProperty(str);

	}

	/**
	 * <p>
	 * Consulta el nombre de todas las propiedades le�das
	 * </p>
	 * 
	 * @return Conjunto con todos los nombres de las propiedades de configuración
	 *         leídas
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
		log.info("Lectura del fichero de configuración por defecto");
		// Establecemos las propiedades por defecto, del fichero de configuración
		// embebido en el jar
	
		properties = new Properties();
		String filePath="/"+confFile;
		InputStream is=this.getClass().getResourceAsStream(filePath);
		log.info("InputStream "+is+" para "+filePath);			
		properties.load(is);		
		log.fine("Listado de propiedades "+properties);

	}

}
