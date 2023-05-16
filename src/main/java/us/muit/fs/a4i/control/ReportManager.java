/**
 * 
 */
package us.muit.fs.a4i.control;

import java.io.IOException;
import java.util.logging.Logger;

import us.muit.fs.a4i.config.Context;
import us.muit.fs.a4i.exceptions.ReportNotDefinedException;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.model.remote.GitHubRepositoryEnquirer;
import us.muit.fs.a4i.model.remote.RemoteEnquirer;
import us.muit.fs.a4i.persistence.ExcelReportManager;
import us.muit.fs.a4i.persistence.PersistenceManager;
import us.muit.fs.a4i.persistence.ReportFormater;
import us.muit.fs.a4i.persistence.ReportFormaterI;

/**
 * @author Isabel Rom�n
 *
 */
public class ReportManager implements ReportManagerI {

	private static Logger log = Logger.getLogger(ReportManager.class.getName());
	private ReportI report;
	private PersistenceManager persister;
	private RemoteEnquirer enquirer;
	private ReportFormaterI formater;
	private IndicatorsCalculator calc;
	private String entityId;
	private ReportI.ReportType reportType;

	public ReportManager(ReportI.ReportType reportType) throws IOException {

		this.reportType = reportType;
		this.formater = new ReportFormater();
		// Selecting the RemoteEnquierer
		RemoteEnquirer.RemoteType remote = null;

		remote = RemoteEnquirer.RemoteType.valueOf(Context.getContext().getRemoteType());
		log.info("Tipo de remoto configurado: " + remote);
		switch (remote) {
		case GITHUB:
			setGHEnquierer();
			break;
		default:
			log.info("Tipo de persistencia no implementando");

		}
		// Selecting the persister
		PersistenceManager.PersistenceType persistence = null;
		persistence = PersistenceManager.PersistenceType.valueOf(Context.getContext().getPersistenceType());
		switch (persistence) {
		case DDBB:
			log.info("La persistencia para base de datos a�n no est� implemetnada");
			break;
		case EXCEL:
			this.persister = new ExcelReportManager();
			log.info("Se instancia el objeto para persistir informes en excel");
			break;
		default:
			break;

		}
		// Selecting the IndicatorCalculator
		switch (this.reportType) {
		case DEVELOPER:
			log.info("La calculadora para indicadores de desarrolladores no está implementada");
			break;
		case ORGANIZATION:
			log.info("La calculadora para indicadores de organización no está implementada");
			break;
		case PROJECT:
			log.info("La calculadora para indicadores de proyecto no está implementada");
			break;
		case REPOSITORY:
			this.calc = new RepositoryCalculator();
			log.info("Se instancia la calculadora para indicadores de respositorio");
			break;
		default:
			break;

		}

	}

	/**
	 * <p>
	 * Borra el informe pasado como parámetro, según las reglas establecidas por el
	 * gestor de persistencia
	 * </p>
	 * 
	 * @param report El informe que se quiere borrar
	 */
	public static void deleteReport(ReportI report) {
		log.info("deleteReport No implementado");

	}

	/**
	 * <p>
	 * Establece el objeto que se usará para consultar al servidor remoto y obtener
	 * las métricas
	 * </p>
	 * 
	 * @param remote Objeto RemoteEnquirer que consultará al servidor remoto
	 */
	public void setRemoteEnquirer(RemoteEnquirer remote) {
		this.enquirer = remote;

	}

	public void setPersistenceManager(PersistenceManager persistence) {
		this.persister = persistence;

	}

	@Override
	public void setFormater(ReportFormaterI formater) {
		this.formater = formater;

	}

	public void setIndicatorCalc(IndicatorsCalculator calc) {
		this.calc = calc;

	}

	/**
	 * <p>
	 * Persiste el informe que recibe como parámetro, según las reglas del gestor de
	 * persistencia y formateador establecidos
	 * </p>
	 * 
	 * @param report
	 *               <p>
	 *               El informe a persistir
	 *               </p>
	 */
	public void saveReport(ReportI report) {

		persister.setFormater(formater);
		try {
			persister.saveReport(report);
		} catch (ReportNotDefinedException e) {
			log.info("El informe que se quiere guardar no está definido");
			e.printStackTrace();
		}
	}

	@Override
	public void saveReport() throws ReportNotDefinedException {
		if (report != null) {
			saveReport(report);
		} else
			throw new ReportNotDefinedException();

	}

	@Override
	public ReportI newReport(String entityId, ReportI.ReportType reportType) throws Exception {
		if (reportType != this.reportType) {
			log.info("Se está intentando crear un tipo de informe diferente al ReportManager usado");
			throw new Exception("El tipo de informe no coincide con el del ReportManager");
		}

		report = enquirer.buildReport(entityId);
		return report;
	}

	@Override
	public void deleteReport() {
		// TODO Auto-generated method stub

	}

	/**
	 * Devuelve el informe que está manejando este gestor
	 */
	@Override
	public ReportI getReport() {
		return report;
	}

	@Override
	public void addMetric(String metricName) {
		// TODO Auto-generated method stub

	}

	@Override
	public ReportItemI getMetric(String metricName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addIndicator(String indicatorName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getIndicator(String indicatorName) {
		// TODO Auto-generated method stub

	}

	private void setGHEnquierer() {
		switch (this.reportType) {
		case REPOSITORY:
			this.enquirer = new GitHubRepositoryEnquirer();
			log.info("Se instancia el enquierer para informes de repositorio con info de github");
			break;
		case DEVELOPER:
			log.info("El enquierer para los informes de desarrolladores con info de github aún no está implementado");
			break;
		case ORGANIZATION:
			log.info("El enquierer para los informes de la organización con info de github aún no está implementado");
			break;
		case PROJECT:
			log.info("El enquierer para los informes de proyecto con info de github aún no está implementado");
			break;
		default:
			log.info("El tipo de informe que se está solicitando no está implementado");
			break;
		}
	}

}
