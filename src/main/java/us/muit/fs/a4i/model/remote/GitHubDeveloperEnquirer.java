/**
 * 
 */
package us.muit.fs.a4i.model.remote;

import java.util.List;
import java.util.logging.Logger;

import org.kohsuke.github.GHEvent;
import org.kohsuke.github.GHEventInfo;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHProject;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItem;

import us.muit.fs.a4i.model.entities.ReportItem.ReportItemBuilder;

/**
 * Deuda técnica Esta clase debe consultar datos sobre un desarrollador concreto
 * de github Ahora mismo está en estado lamentable Simplemente busca los eventos
 * de un desarrollador No localiza eventos de tipo ISSUE, que son los que se
 * quería RECUERDA: las métricas tienen que estar incluidas en el fichero de
 * configuración a4iDefault.json
 */
public class GitHubDeveloperEnquirer extends GitHubEnquirer<GHUser> {
	public GitHubDeveloperEnquirer() {
		super();
		myQueries.put("closedIssuesLastMonth",GitHubDeveloperEnquirer::getClosedIssuesLastMonth);
		myQueries.put("assignedIssuesLastMonth",GitHubDeveloperEnquirer::getAssignedIssuesLastMonth);
		log.info("Incluidos nombres metricas en Enquirer");
	}

	private static Logger log = Logger.getLogger(GitHubDeveloperEnquirer.class.getName());
	/**
	 * <p>
	 * Identificador unívoco de la entidad a la que se refire el informe en el
	 * servidor remoto que se va a utilizar
	 * </p>
	 */
	private String entityId;

	@Override
	public ReportI buildReport(String developerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReportItem getMetric(String metricName, String developerId) throws MetricException {
		GHUser developer;

		GitHub gb = getConnection();
		try {
			developer = gb.getUser(developerId);
			log.info("Localizado el desarrollador " + developer.getName());
		} catch (Exception e) {
			e.printStackTrace();
			throw new MetricException("No se puede acceder al desarrollador " + developerId + " para recuperarlo");
		}

		return getMetric(metricName, developer);
	}

	private ReportItem getMetric(String metricName, GHUser developer) throws MetricException {
		log.info("Localizando la metrica " + metricName);
		ReportItem metric;
		if (developer == null) {
			throw new MetricException(
					"Intenta obtener una métrica de desarrollador sin haber obtenido el desarrollador");
		}
		/*
		switch (metricName) {
		case "closedIssuesLastMonth":
			metric = getClosedIssuesLastMonth(developer);
			break;
		case "assignedIssuesLastMonth":
			metric = getAssignedIssuesLastMonth(developer);
			break;
		default:
			throw new MetricException("La métrica " + metricName + " no está definida para un repositorio");
		}
		*/

		return myQueries.get(metricName).apply(developer);
	}

	static private ReportItem getClosedIssuesLastMonth(GHUser developer) {
		log.info("Consultando los issues asignados a un desarrollador");
		ReportItemBuilder<Integer> builder = null;
		int issues = 0;

		try {
			PagedIterable<GHEventInfo> events = developer.listEvents();
			for (GHEventInfo event : events) {
				log.info("Evento tipo" + event.getType() + " en la fecha " + event.getCreatedAt());
				if (event.getType() == GHEvent.ISSUES) {

					GHEventPayload.Issue payload = event.getPayload(GHEventPayload.Issue.class);
					log.info(payload.getAction());
					issues++;

				}

			}
			builder = new ReportItem.ReportItemBuilder<Integer>("closedIssuesLastMonth", issues);
			builder.source("GitHub");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	static private ReportItem getAssignedIssuesLastMonth(GHUser developer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RemoteType getRemoteType() {
		// TODO Auto-generated method stub
		return null;
	}

}
