/**
 * 
 */
package us.muit.fs.a4i.control;

import java.util.logging.Logger;

import us.muit.fs.a4i.model.entities.Indicator;

import us.muit.fs.a4i.model.entities.ReportI;

/**
 * <p>Implementa los m�todos para calcular indicadores referidos a un repositorio repositorio</p>
 * <p>Puede hacerse uno a uno o todos a la vez</p>
 * @author Isabel Rom�n
 *
 */
public class RepositoryCalculator implements IndicatorsCalculator {
	private static Logger log=Logger.getLogger(RepositoryCalculator.class.getName());
	@Override
	public void calcIndicator(String name, ReportI report) {
		log.info("Calcula el indicador de nombre "+name);
		/**
		 * Tiene que mirar si est�n ya las m�tricas que necesita
		 * Si est�n lo calcula
		 * Si no est�n busca las m�tricas, las a�ade y lo calcula
		 * 
		 */
		
	}
/**
 * Calcula todos los indicadores definidos para un repositorio
 * Recupera todas las m�tricas que necesite y que no est�n en el informe y las a�ade al mismo
 * 
 */
	@Override
	public void calcAllIndicators(ReportI report) {
		log.info("Calcula todos los indicadores del repositorio y los incluye en el informe");
	}
    private Indicator commitsPerUser(ReportI report) {
    	Indicator indicator=null;
    	
    	return indicator;
    }
	@Override
	public ReportI.Type getReportType() {
		return ReportI.Type.REPOSITORY;
	}
}
