/**
 * 
 */
package us.muit.fs.a4i.control;

import java.util.logging.Logger;

import us.muit.fs.a4i.exceptions.IndicatorException;
import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.model.entities.ReportI;

/**
 * <p>
 * Implementa los métodos para calcular indicadores referidos a un repositorio
 * repositorio
 * </p>
 * <p>
 * Puede hacerse uno a uno o todos a la vez
 * </p>
 * 
 * @author Isabel Román
 *
 */
public class RepositoryCalculator implements IndicatorsCalculator {
	private static Logger log = Logger.getLogger(RepositoryCalculator.class.getName());
	private static ReportI.ReportType reportType = ReportI.ReportType.REPOSITORY;

	@Override
	public void calcIndicator(String indicatorName, ReportManagerI reportManager) throws IndicatorException {
		log.info("Calcula el indicador de nombre " + indicatorName);
		/**
		 * Tiene que mirar si están ya las métricas que necesita Si están lo calcula Si
		 * no están busca las métricas, las añade al informe y lo calcula
		 * 
		 */

	}

	/**
	 * Calcula todos los indicadores definidos para un repositorio Recupera todas
	 * las métricas que necesite y que no estén en el informe y las añade al mismo
	 * 
	 */
	@Override
	public void calcAllIndicators(ReportManagerI reportManager) throws IndicatorException {
		log.info("Calcula todos los indicadores del repositorio y los incluye en el informe");
	}

	private Indicator commitsPerUser(ReportI report) {
		Indicator indicator = null;

		return indicator;
	}

	@Override
	public ReportI.ReportType getReportType() {
		return reportType;
	}

}
