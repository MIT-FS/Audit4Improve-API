/**
 * 
 */
package us.muit.fs.a4i.control;

import java.util.logging.Logger;

import us.muit.fs.a4i.exceptions.ReportNotDefinedException;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.remote.RemoteEnquirer;
import us.muit.fs.a4i.persistence.PersistenceManager;
import us.muit.fs.a4i.persistence.ReportFormaterI;

/**
 * @author isa
 *
 */
public class ReportManager implements ReportManagerI {
	private static Logger log=Logger.getLogger(ReportManager.class.getName());
	private ReportI report;
	private PersistenceManager persister;
	private RemoteEnquirer enquirer;
	private ReportFormaterI formater;
	private IndicatorsCalculator calc;
	private String entityId;

	

	@Override
	public void setRemoteEnquirer(RemoteEnquirer remote) {
		this.enquirer=remote;

	}

	@Override
	public void setPersistenceManager(PersistenceManager persistence) {
		this.persister=persistence;

	}

	@Override
	public void setFormater(ReportFormaterI formater) {
		this.formater=formater;

	}

	@Override
	public void setIndicatorCalc(IndicatorsCalculator calc) {
		this.calc=calc;

	}

	@Override
	public void saveReport(ReportI report) {
		persister.setReport(report);
		persister.setFormater(formater);
		try {
			persister.saveReport();
		} catch (ReportNotDefinedException e) {
			log.info("No debería entrar aquí porque se acaba de establecer el informe");
			e.printStackTrace();
		}
	}

	@Override
	public void save() throws ReportNotDefinedException {
		if(report!=null) {
			saveReport(report);
		}else throw new ReportNotDefinedException();
		
	}
   
	@Override
	public ReportI createReport(String entityId) {
		report=enquirer.buildReport(entityId);
		return report;
	}

	@Override
	public void deleteReport(ReportI report) {
		// TODO Auto-generated method stub

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

}
