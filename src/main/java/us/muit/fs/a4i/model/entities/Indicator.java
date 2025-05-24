/**
 * 
 */
package us.muit.fs.a4i.model.entities;

import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @author Isabel Román
 *
 */
public class Indicator implements IndicatorI{
	private static Logger log = Logger.getLogger(Indicator.class.getName());
	private Collection<ReportItemI> metrics;
	private IndicatorState state;

	/**
	 * Constructor
	 */
	public Indicator() {
		this.state = IndicatorState.UNDEFINED;
	}

	/**
	 * @param state Estado del nuevo indicador
	 * @param metrics Colección de métricas en las que se basa el indicador
	 */
	public Indicator(IndicatorState state, Collection<ReportItemI> metrics) {
		this.metrics = metrics;
		this.state = state;
	}

	public void setMetrics(Collection<ReportItemI> metrics) {
		this.metrics = metrics;
	}

	public void setState(IndicatorState state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String info;
		info = "Indicador estado " + state + ", a partir de las metricas: " + metrics;
		return info;
	}

	@Override
	public IndicatorState getState() {

		return state;
	}

	@Override
	public Collection<ReportItemI> getMetrics() {

		return metrics;
	}

}