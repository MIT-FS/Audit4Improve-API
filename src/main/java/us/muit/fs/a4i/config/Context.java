/**
 * 
 */
package us.muit.fs.a4i.config;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import us.muit.fs.a4i.model.entities.IndicatorI;

/**
 * <p>
 * Clase para la gestión de los parámetros de contexto
 * </p>
 * <p>
 * El objetivo de Context es el manejo de la configuración
 * </p>
 * <p>
 * En el estado actual Contexto sólo es una aproximación a las posiblidades de
 * configuración. Se presentan posibilidades para:
 * </p>
 * <ul>
 * <li>Localizar el fichero en la carpeta resources, incluida en el jar</li>
 * <li>Localizar el fichero en el home de usuario</li>
 * <li>Localizar el fichero en una ruta introducida de forma "programada"</li>
 * </ul>
 * <p>
 * Único punto para acceso a variables que pueden ser leídas por cualquiera,
 * configuradas sólo por la clase context
 * </p>
 * <p>
 * Sigue el patrón singleton
 * </p>
 * 
 * @author Isabel Román
 *
 */
public class Context {

	private static Logger log = Logger.getLogger(Context.class.getName());
	private static Context contextInstance = null;

	/**
	 * Propiedades de la API Se leerán del fichero de configuración por defecto y
	 * del especificado por la aplicación, si lo hubiera
	 */
	private Properties properties = null;
	/**
	 * Fichero de propiedades de configuración de la API, embebido en el jar
	 */
	private static String confFile = "a4i.conf";

	/**
	 * Fichero de especificación de métricas e indicadores por defecto, embebido en
	 * el jar
	 * 
	 */
	private static String defaultFile = "a4iDefault.json";

	/**
	 * Fichero de propiedades de la API establecido por la aplicación cliente
	 */
	private static String appConFile = null;

	/**
	 * Fichero de especificación de métricas e indicadores establecido por la
	 * aplicación cliente
	 */
	private static String appFile = null;
	/**
	 * Referencia al verificador de métricas e indicadores
	 */
	private Checker checker = null;

	/**
	 * <p>
	 * Constructor privado, sigue el patrón singleton. El único objeto posible se
	 * crea al invocar el método getContext
	 * </p>
	 * 
	 * @throws IOException
	 */
	private Context() throws IOException {
		setProperties();
		log.info("Propiedades del contexto establecidas");
		checker = new Checker();
		log.info("Checker creado");
	}

	/**
	 * <p>
	 * Establece la ruta del fichero de métricas e indicadores indicado por el
	 * cliente/aplicación
	 * </p>
	 * 
	 * @param filename ruta al fichero de configuración de métricas e indicadores de
	 *                 la aplicación cliente
	 */
	public static void setAppRI(String filename) {
		appFile = filename;
	}

	/**
	 * <p>
	 * Consulta la ruta del fichero de configuración de métricas e indicadores del
	 * cliente/aplicación
	 * </p>
	 * 
	 * @return ruta del fichero de configuración de métricas e indicadores de la aplicación cliente
	 */
	public static String getAppRI() {
		return appFile;
	}

	/**
	 * @return la ruta al fichero de configuración de indicadores y métricas por
	 *         defecto
	 */
	public static String getDefaultRI() {
		return defaultFile;
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
	 * Las propiedades no establecidas se cogerán de la configuración por defecto
	 * </p>
	 * 
	 * @param appConPath Ruta completa al fichero de configuración establecido por
	 *                   la propiedad cliente
	 * @throws IOException Problema lectura fichero
	 */
	public static void setAppConf(String appConPath) throws IOException {
		/**
		 * Vuelve a leer las propiedades incluyendo las establecidas por la aplicación
		 */
		appConFile = appConPath;

		// customFile=System.getenv("APP_HOME")+customFile;
		// Otra opción, Usar una variable de entorno para localizar la ruta de
		// instalación y de ahí coger el fichero de configuración
		// También podría localizarse en el home de usuario
		getContext().properties.load(new FileInputStream(appConPath));
		log.info("Las nuevas propiedades son " + getContext().properties);
	}

	/**
	 * @return devuelve el verificador (checker)
	 */
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
		return properties.getProperty("persistence.type");
	}

	/**
	 * <p>
	 * Consulta el tipo de remoto que se quiere manejar
	 * </p>
	 * 
	 * @return El tipo de remoto (NOTA: deuda técnica, podría convenir usar un
	 *         enumerado, para controlar mejor los tipos disponibles)
	 * @throws IOException si hay problemas al consultar las propiedades
	 */
	public String getRemoteType() throws IOException {
		log.info("Se solicita el tipo de remoto");
		return properties.getProperty("remote.type");
	}

	/**
	 * <p>
	 * No Implementado Debería leer las propiedades adecuadas, como color, tamaño,
	 * tipo... y construir un objeto Font Si no se ha establecido un valor por
	 * defecto se crea una fuente simple
	 * </p>
	 * 
	 * @return La fuente por defecto para indicadores y métricas
	 */
	public Font getDefaultFont() {
		// OJO el color no forma parte de la clase font, por loq ue ese atributo debe
		// estar fuera
		// Podría incluir un parámetro font para devolverlo a la salida y que lo que
		// devuelva sea un String con el color
		log.info("Busca la información de configuración de la fuente, por defecto");

		// TO DO
		String color = properties.getProperty("Font.default.color");
		String height = properties.getProperty("Font.default.height");
		String type = properties.getProperty("Font.default.type");
		log.info("Los datos son, color: " + color + " height: " + height + " type: " + type);
		log.info("Intento crear la fuente");

		return new Font(type, Font.ITALIC, Integer.valueOf(height));
	}

	/**
	 * <p>
	 * No Implementado
	 * </p>
	 * <p>
	 * Deberá leer las propiedades adecuadas, como color, tamaño, tipo... y
	 * construir un objeto Font
	 * </p>
	 * <p>
	 * Si no se ha definido una fuente para las métricas se debe devolver la fuente
	 * por defecto
	 * </p>
	 * 
	 * @return la fuente para las métricas
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
	 * Deberá leer las propiedades adecuadas, como color, tamaño, tipo... y
	 * construir un objeto Font
	 * </p>
	 * 
	 * @param state Estado para el que se solicita el color de fuente
	 * @return La fuente para el indicador cuando el estado es el par�metro pasado
	 * @throws IOException problema al leer el fichero
	 */

	public static Font getIndicatorFont(IndicatorI.IndicatorState state) throws IOException {
		Font font = null;

		// TO DO
		return font;
	}

	/**
	 * <p>
	 * Consulta el nombre de todas las propiedades leídas
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
	 * fichero de conf de la API (configuración por defecto)
	 * </p>
	 * 
	 * @throws IOException si hay problemas leyendo el fichero
	 */
	private void setProperties() throws IOException {
		log.info("Lectura del fichero de configuración por defecto");
		FileInputStream file;
		// Establecemos las propiedades por defecto, del fichero de configuración
		// embebido en el jar

		properties = new Properties();
		String filePath = "/" + confFile;
		InputStream is = this.getClass().getResourceAsStream(filePath);
		log.info("InputStream " + is + " para " + filePath);
		properties.load(is);
		log.info("Listado de propiedades " + properties);

	}

}
