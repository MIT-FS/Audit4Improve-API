package us.muit.fs.a4i.config;

import java.util.logging.Logger;

/**
 * <p>
 * Esta clase permite acceder a las interfaces para configurar y verificar
 * métricas e indicadores Se mantiene separada la lógica de la gestión de
 * configuración de métricas e indicadores porque en el futuro van a ser
 * bastante diferentes. En la versión actual son muy similares y por tanto el
 * diseño no es bueno ya que no identifica bien la reutilización
 * </p>
 * 
 * @author Isabel Román
 *
 */
public class Checker {
	private static Logger log = Logger.getLogger(Checker.class.getName());

	private MetricConfigurationI metricConf;
	private IndicatorConfigurationI indiConf;
/**
 * El constructor recibe los objetos configuradores, que podrán ser diferentes según el contexto.
 */
	Checker(MetricConfigurationI metricConf, IndicatorConfigurationI indiConf) {
		this.metricConf = metricConf;
		this.indiConf = indiConf;
	}

	/**
	 * @return interfaz para la configuración de métricas
	 */
	public MetricConfigurationI getMetricConfiguration() {
		return this.metricConf;
	}

	/**
	 * @return interfaz para la configuración de indicadores
	 */
	public IndicatorConfigurationI getIndicatorConfiguration() {
		return this.indiConf;
	}
}
