/**
 * 
 */
package us.muit.fs.a4i.persistence;

import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;

import us.muit.fs.a4i.config.Context;
import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.model.entities.Indicator.State;

/**
 * @author Isabel Román
 *
 */
public class ReportFormater implements ReportFormaterI {
	/**
	 * Formato de fuente usado si no se ha definido ninguna otra
	 */
	private Font defaultFont;
	/**
	 * Formato de fuente para las métricas
	 */
	private Font metricFont;
	/**
	 * Fomatos de fuente en función del estado del indicador
	 */
	private HashMap<Indicator.State,Font> indicatorsFont;
	
    ReportFormater(){
    	 	this.indicatorsFont=new HashMap<Indicator.State,Font>();    	 	
    	 	//Sólo se construye el mapa, conforme se vayan solicitando se irán rellenando
    }
	@Override
	public Font getMetricFont() {
		if (metricFont==null) {
			metricFont=Context.getMetricFont();
		}
		return metricFont;
	}

	@Override
	public void setMetricFont(Font font) {
		metricFont=font;

	}

	@Override
	public Font getIndicatorFont(Indicator.State state) throws IOException {	
		if (!indicatorsFont.containsKey(state)){
			try {
				indicatorsFont.put(state, Context.getIndicatorFont(state));
			} catch (IOException e) {
				indicatorsFont.put(state, Context.getContext().getDefaultFont());
			}
		}	
	
		return indicatorsFont.get(state);
	}

	@Override
	public void setIndicatorFont(State state, Font font) {
		indicatorsFont.put(state, font);

	}

	@Override
	public void setDefaultFont(Font font) {
		defaultFont=font;
	}
	
}
