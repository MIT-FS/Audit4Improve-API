/**
 * 
 */
package us.muit.fs.a4i.config;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import us.muit.fs.a4i.model.entities.IndicatorI;
import us.muit.fs.a4i.model.entities.Font;

/**
 * <p>
 * Clase para la gestión de los parámetros de contexto
 * </p>
 * <p>
 * El objetivo de Context es el manejo de la configuración de la api. La
 * configuración por defecto se separa en dos ficheros principales:
 * <ol>
 * <li>a4i.conf: contiene la configuración por defecto de la api. Permite
 * seleccionar el tipo de remoto con el que se quiere interaccionar, el tipo de
 * persistencia de los informes y características de presentación de estos
 * informes.</li>
 * <li>a4iDefault.json: contiene la configuración por defecto de métricas e
 * indicadores. Esta configuración se maneja en la clase checker</li>
 * </ol>
 * Hay una configuración embebida en el jar, es decir, una configuración por
 * defecto. Pero esta puede ser modificada si la aplicación cliente define
 * ficheros de configuración personalizados.
 * </p>
 * <p>
 * En el estado actual Context es una aproximación a las posiblidades de
 * configuración. Se presentan posibilidades para:
 * </p>
 * <ul>
 * <li>Localizar el fichero a4i.conf en la carpeta resources, incluida en el
 * jar</li>
 * <li>Localizar el fichero *.conf (configuración personalizada) en el home de
 * usuario</li>
 * <li>Localizar el fichero *.conf (configuración personalizada) en una ruta
 * introducida de forma "programada"</li>
 * </ul>
 * <p>
 * Único punto para acceso a variables que pueden ser leídas por cualquiera,
 * configuradas sólo por la clase context
 * </p>
 * <p>
 * Sigue el patrón singleton. Context tiene la responsabilidad de crear Checker, es decir, debe seleccionar el IndicatorConfiguration y el MetricConfiguration adecuados al contexto.
 * Se crean la primera vez que se solicita Context, por lo que los ficheros de configuración no se pueden cambiar "en caliente", sólo al inicio de la ejecución
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
	private static String appConfFile = null;

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
	private Context(Checker checker) throws IOException {
		setProperties();	
		this.checker = checker;
		log.info("Propiedades del contexto establecidas");
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
		log.fine("Fichero configuración de métricas establecido "+filename);
		appFile = filename;
	}

	/**
	 * <p>
	 * Consulta la ruta del fichero de configuración de métricas e indicadores del
	 * cliente/aplicación
	 * </p>
	 * 
	 * @return ruta del fichero de configuración de métricas e indicadores de la
	 *         aplicación cliente
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
			Checker checker=createChecker();
			contextInstance = new Context(checker);
		}
		return contextInstance;
	}
	/**
	 * Método responsable de seleccionar el IndicatorConfiguration y el MetricConfiguration adecuado al contexto
	 * DEUDA TÉCNICA:
	 * En esta versión al crear el checker no hay ninguna comprobación de contexto, pero en el futuro deberá primero examinarse las propiedas de configuración para decidir que configuradores hay que crear. 
	 * @return
	 */
	static private Checker createChecker(){
		IndicatorConfigurationI indConf=new IndicatorConfiguration(Context.getDefaultRI(),Context.getAppRI());
		MetricConfigurationI metConf=new MetricConfiguration(Context.getDefaultRI(),Context.getAppRI());
		log.info("Creados los configuradores de métricas e indicadores, se crea checker");
		return new Checker(metConf,indConf);
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
		appConfFile = appConPath;
		log.fine("Fichero configuración de la API establecido "+appConPath);
		// customFile=System.getenv("APP_HOME")+customFile;
		// Otra opción, Usar una variable de entorno para localizar la ruta de
		// instalación y de ahí coger el fichero de configuración
		// También podría localizarse en el home de usuario
		getContext().properties.load(new FileInputStream(appConPath));
		log.info("Las nuevas propiedades son " + getContext().properties);
	}

	public static String getAppConf() throws IOException {
		return appConfFile;
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
	 * Lee las propiedades adecuadas, como color, tamaño, tipo... y construir un
	 * objeto Font Si no se ha establecido un valor por defecto se crea una fuente
	 * simple
	 * </p>
	 * 
	 * @return La fuente por defecto para indicadores y métricas
	 */
	public Font getDefaultFont() {
		// OJO el color no forma parte de la clase font, por lo que ese atributo debe
		// estar fuera
		// Podría incluir un parámetro font para devolverlo a la salida y que lo que
		// devuelva sea un String con el color
		log.info("Busca la información de configuración de la fuente, por defecto");

		String color = getDefaultParam("color");
		String height = getDefaultParam("height");
		String type = getDefaultParam("type");

		log.info("Los datos son, color: " + color + " height: " + height + " type: " + type);
		log.info("Intento crear la fuente");

		return new Font(type, Integer.valueOf(height), color);

	}

	private String getDefaultParam(String param) {
		String property = "Font.default." + param;
		String value = properties.getProperty(property);
		/**
		 * Se asegura de que se da un valor por defecto, aunque no esté configurado en
		 * el fichero
		 */
		if (value == null) {
			switch (param) {
			case "type":
				value = "Arial";
				break;
			case "color":
				value = "black";
				break;
			case "height":
				value = "12";
				break;
			}
		}
		return value;
	}

	/**
	 * <p>
	 * Lee las propiedades adecuadas, como color, tamaño, tipo... y construye un
	 * objeto Font para la fuente de las métricas
	 * </p>
	 * <p>
	 * Si no se ha definido una fuente para las métricas se debe devolver la fuente
	 * por defecto
	 * </p>
	 * 
	 * @return la fuente para las métricas
	 */
	public Font getMetricFont() {
		log.info("Busca la información de configuración de la fuente, para las métricas");

		String type = properties.getProperty("Font.metric.type");
		String height = properties.getProperty("Font.metric.height");
		String color = properties.getProperty("Font.metric.color");

		if (type == null) {
			type = getDefaultParam("type");
			log.info("El tipo de la fuente de las metricas es el valor por defecto");
		}
		if (height == null) {
			height = getDefaultParam("height");
			log.info("El tamaño de la fuente de las metricas es el valor por defecto");
		}
		if (color == null) {
			color = getDefaultParam("color");
			log.info("El color de la fuente de las metricas es el valor por defecto");
		}

		log.info("Llamo a newFont con los datos color: " + color + " height: " + height + " type: " + type);

		return new Font(type, Integer.valueOf(height), color);

	}

	/**
	 * <p>
	 * Deberá leer las propiedades adecuadas, como color, tamaño, tipo... y
	 * construir un objeto Font para la fuente del indicador en dicho estado
	 * </p>
	 * 
	 * @param state Estado para el que se solicita el color de fuente
	 * @return La fuente para el indicador cuando el estado es el parametro pasado
	 * @throws IOException problema al leer el fichero
	 */

	public Font getIndicatorFont(IndicatorI.IndicatorState state) throws IOException {
		/**
		 * He eliminado el static, así si funciona Hay que comprobar si el static estaba
		 * puesto con sentido desde un primer momento
		 **/
		Font font = null;
		// TODO:
		String propertyState = "Font." + state.toString();
		log.info("Raiz que uso para buscar los datos del indicador en estado " + state + " " + propertyState);

		String color = properties.getProperty(propertyState + ".color");
		String height = properties.getProperty(propertyState + ".height");
		String type = properties.getProperty(propertyState + ".type");

		log.info("Los datos son, color: " + color + " height: " + height + " type: " + type);
		log.info("Intento crear la fuente");

		if (color == null) {
			color = properties.getProperty("Font.default.color");
		}
		if (height == null) {
			height = properties.getProperty("Font.default.height");
		}
		if (type == null) {
			type = properties.getProperty("Font.default.type");
		}

		font = new Font(type, Integer.valueOf(height), color);
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
