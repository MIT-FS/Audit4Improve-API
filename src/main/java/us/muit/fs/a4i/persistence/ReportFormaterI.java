/**
 * 
 */
package us.muit.fs.a4i.persistence;

import java.awt.Font;
import java.io.IOException;

import us.muit.fs.a4i.model.entities.Indicator;

/**
 * <p>Interfaz genérica para establecer el estilo de los informes si se persisten en un medio "visual"</p>
 * @author Isabel Román
 *
 */
public interface ReportFormaterI {
	/**
	 * <p>Devuelve el formato que debe tener la fuente de una métrica</p>
	 * @return Fuente de la métrica
	 */
	Font getMetricFont();
	/**
	 * <p>Establece la fuente para las métricas</p>
	 * @param font Fuente de la métrica
	 */
	void setMetricFont(Font font);
	/**
	 * <p>Recupera las fuente para un indicador con el estado indicado.</p>
	 * <p>Si no se ha configurado para ese estado una fuente específica se devuelve la fuente por defecto</p>
	 * @param state Estado para el que solicita la fuente
	 * @return La fuente a utilizar para los indicadores con el estado indicado
	 * @throws IOException Si no se puede leer la configuración
	 */
   
	Font getIndicatorFont(Indicator.State state) throws IOException;
	/**
	 * <p>Establece la fuente para un indicador en un estado determinado</p>
	 * @param state El estado correspondiente a la fuente que se está estableciendo
	 * @param font La fuente a usar cuando el estado del indicador es el señalado en la parámetro state
	 */
	void setIndicatorFont(Indicator.State state, Font font);
	/**
	 * <p>Establece la fuente por defecto para los indicadores y métricas</p>
	 * <p>Usada cuando el estado no está diferenciado o no se ha especificado una fuente para las métricas</p>
	 * @param font Fuente por defecto
	 */
	void setDefaultFont(Font font);
}
