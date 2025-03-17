/**
 * 
 */
package us.muit.fs.a4i.model.remote;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHRepositoryStatistics;
import org.kohsuke.github.GHRepositoryStatistics.CodeFrequency;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

import us.muit.fs.a4i.config.GitFlow;
import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.Report;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItem.ReportItemBuilder;

/**
 * @author Isabel Román y alumnos de factoría software del MUIT de la ETSI (US)
 *         deuda técnica: las métricas tras la etiqueta //equipo 3 tienen problemas,
 *         no están acordes al indicador para el que fueron creadas RECUERDA:
 *         las métricas tienen que estar incluidas en el fichero de
 *         configuración a4iDefault.json
 *         
 *  TODO: esto está en periodo de limpieza, vamos a utilizar un mapa de punteros a funciones para quitarnos el case de enmedio, y ya que estamos
 *  podemos eliminar la lista de métricas y usar las keys del mapa (Esto falta)
 *  Debería haber un constructor para esto, para que la creación del mapa esté fuera
 *        
 */
public class GitHubRepositoryEnquirer extends GitHubEnquirer<GHRepository> {
	/**
	 * para trazar el código
	 */
	private static Logger log = Logger.getLogger(GitHubRepositoryEnquirer.class.getName());

	/**
	 * <p>
	 * Constructor
	 * </p>
	 */

	public GitHubRepositoryEnquirer() {
		super();
		//para cada métrica se añade en el mapa la función que la consulta y como clave el nombre de la métrica
		myQueries.put("subscribers", GitHubRepositoryEnquirer::getSubscribers);
		myQueries.put("forks", GitHubRepositoryEnquirer::getForks);
		myQueries.put("watchers", GitHubRepositoryEnquirer::getWatchers);
		myQueries.put("starts", GitHubRepositoryEnquirer::getStars);
		myQueries.put("issues", GitHubRepositoryEnquirer::getIssues);
		myQueries.put("closedIssues", GitHubRepositoryEnquirer::getClosedIssues);
		myQueries.put("openIssues", GitHubRepositoryEnquirer::getOpenIssues);
		myQueries.put("creation", GitHubRepositoryEnquirer::getCreation);
		myQueries.put("lastUpdated", GitHubRepositoryEnquirer::getLastUpdated);
		myQueries.put("lastPush", GitHubRepositoryEnquirer::getLastPush);		
		myQueries.put("totalAdditions", GitHubRepositoryEnquirer::getTotalAdditions);
		
		myQueries.put("totalDeletions", GitHubRepositoryEnquirer::getTotalDeletions);
		myQueries.put("collaborators", GitHubRepositoryEnquirer::getCollaborators);
		myQueries.put("ownerCommits", GitHubRepositoryEnquirer::getOwnerCommits);

		// equipo3
		myQueries.put("issuesLastMonth", GitHubRepositoryEnquirer::getIssuesLastMonth);
		myQueries.put("closedIssuesLastMonth", GitHubRepositoryEnquirer::getClosedIssuesLastMonth);
		myQueries.put("meanClosedIssuesLastMonth", GitHubRepositoryEnquirer::getMeanClosedIssuesLastMonth);
		myQueries.put("issues4DevLastMonth", GitHubRepositoryEnquirer::getIssues4DevLastMonth);
	
		// equipo 4
		myQueries.put("totalPullReq", GitHubRepositoryEnquirer::getTotalPullReq);
		myQueries.put("closedPullReq", GitHubRepositoryEnquirer::getClosedPullReq);
		
		// equipo 5
		myQueries.put("PRAcceptedLastYear", GitHubRepositoryEnquirer::getPRAcceptedLastYear);
		myQueries.put("PRAcceptedLastMonth", GitHubRepositoryEnquirer::getPRAcceptedLastMonth);
		myQueries.put("PRRejectedLastYear", GitHubRepositoryEnquirer::getPRRejectedLastYear);
		myQueries.put("PRRejectedLastMonth", GitHubRepositoryEnquirer::getPRRejectedLastMonth);

		// Equipo 1
		myQueries.put("conventionalCommits", GitHubRepositoryEnquirer::getConventionalCommits);
		myQueries.put("commitsWithDescription", GitHubRepositoryEnquirer::getCommitsWithDescription);
		myQueries.put("issuesWithLabels", GitHubRepositoryEnquirer::getIssuesWithLabels);
		myQueries.put("gitFlowBranches", GitHubRepositoryEnquirer::getGitFlowBranches);
		myQueries.put("conventionalPullRequests", GitHubRepositoryEnquirer::getConventionalPullRequests);
			
		log.info("Añadidas métricas al GHRepositoryEnquirer");
	}

	/**
	 * Devuelve el informe para el repositorio cuyo id se pasa como parámetro
	 */
	@Override
	public ReportI buildReport(String repositoryId) {
		ReportI report = null;
		log.info("Invocado el método que construye un objeto RepositoryReport");
		/**
		 * <p>
		 * Información sobre el repositorio obtenida de GitHub
		 * </p>
		 */
		GHRepository repo;
		/**
		 * <p>
		 * En estos momentos cada vez que se invoca construyeObjeto se crea y rellena
		 * uno nuevo
		 * </p>
		 * <p>
		 * Deuda técnica: se puede optimizar consultando sólo las diferencias respecto a
		 * la fecha de la última representación local
		 * </p>
		 */

		try {
			log.info("Nombre repo = " + repositoryId);
/*
			GitHub gb = getConnection();
			repo = gb.getRepository(repositoryId);
			log.info("El repositorio es de " + repo.getOwnerName() + " Y su descripción es " + repo.getDescription());
			log.info("leído " + repo);
			*/
			report = new Report(repositoryId);

			/**
			 * Métricas más elaboradas, requieren más "esfuerzo"
			 */

			report.addMetric(getMetric("totalAdditions", repositoryId));
			log.info("Incluida metrica totalAdditions ");

			report.addMetric(getMetric("totalDeletions", repositoryId));
			log.info("Incluida metrica totalDeletions ");

			/**
			 * Métricas directas de tipo conteo
			 */

			report.addMetric(getMetric("subscribers", repositoryId));
			log.info("Incluida metrica suscribers ");

				
			/**
			 * Métricas directas de tipo fecha
			 */
			report.addMetric(getMetric("creation", repositoryId));
			log.info("Incluida metrica creation ");

			report.addMetric(getMetric("lastPush", repositoryId));
			log.info("Incluida metrica lastPush ");
		
		} catch (Exception e) {
			log.severe("Problemas en la conexión " + e);
		}

		return report;
	}

	/**
	 * Permite consultar desde fuera una única métrica del repositorio indicado
	 * 
	 * @param metricName   el nombre de la métrica
	 * @param repositoryId el id del repositorio
	 * @return el item para incluir en el informe del repositorio
	 */

	@Override
	public ReportItem getMetric(String metricName, String repositoryId) throws MetricException {
		GHRepository remoteRepo;

		GitHub gb = getConnection();
		try {
			remoteRepo = gb.getRepository(repositoryId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MetricException(
					"No se puede acceder al repositorio remoto " + repositoryId + " para recuperarlo");
		}

		return getMetric(metricName, remoteRepo);
	}

	/**
	 * <p>
	 * Crea la métrica solicitada consultando el repositorio remoto que se pasa como
	 * parámetro
	 * </p>
	 * 
	 * @param metricName Métrica solicitada
	 * @param remoteRepo Repositorio remoto
	 * @return La métrica creada
	 * @throws MetricException Si la métrica no está definida se lanzará una
	 *                         excepción
	 */
	private ReportItem getMetric(String metricName, GHRepository remoteRepo) throws MetricException {
		ReportItem metric;
		if (remoteRepo == null) {
			throw new MetricException("Intenta obtener una métrica sin haber obtenido los datos del repositorio");
		}
		metric=myQueries.get(metricName).apply(remoteRepo);
		return metric;
	}

	/*
	 * A partir de aquí los algoritmos específicos para hacer las consultas de cada
	 * métrica
	 */

	/**
	 * <p>
	 * Obtención del número total de adiciones al repositorio
	 * </p>
	 * 
	 * @param remoteRepo el repositorio remoto sobre el que consultar
	 * @return la métrica con el número total de adiciones desde el inicio
	 * @throws MetricException Intenta crear una métrica no definida
	 */
	static private ReportItem getTotalAdditions(GHRepository remoteRepo) throws MetricException {
		log.fine("buscando totaladditions");
		ReportItem metric = null;

		GHRepositoryStatistics data = remoteRepo.getStatistics();
        log.fine("estadisticas "+data.toString());
		List<CodeFrequency> codeFreq;
		try {
			codeFreq = data.getCodeFrequency();

			int additions = 0;
             
			for (CodeFrequency freq : codeFreq) {

				if (freq.getAdditions() != 0) {
					Date fecha = new Date((long) freq.getWeekTimestamp() * 1000);
					log.info("Fecha modificaciones " + fecha);
					additions += freq.getAdditions();

				}
			}
			ReportItemBuilder<Integer> builder = new ReportItem.ReportItemBuilder<Integer>("totalAdditions", additions);
			builder.source("GitHub, calculada")
					.description("Suma el total de adiciones desde que el repositorio se creó");
			metric = builder.build();

		} catch (Exception e) {
		
			log.warning("Problemas al leer codefrequency en getTotalAdditions");
			e.printStackTrace();
		}
		return metric;

	}

	/**
	 * <p>
	 * Obtención del número total de eliminaciones del repositorio
	 * </p>
	 * 
	 * @param remoteRepo el repositorio remoto sobre el que consultar
	 * @return la métrica con el n�mero total de eliminaciones desde el inicio
	 * @throws MetricException Intenta crear una métrica no definida
	 */
	static private ReportItem getTotalDeletions(GHRepository remoteRepo) throws MetricException {
		ReportItem metric = null;

		GHRepositoryStatistics data = remoteRepo.getStatistics();
		List<CodeFrequency> codeFreq;
		try {
			codeFreq = data.getCodeFrequency();

			int deletions = 0;

			for (CodeFrequency freq : codeFreq) {

				if (freq.getDeletions() != 0) {
					Date fecha = new Date((long) freq.getWeekTimestamp() * 1000);
					log.info("Fecha modificaciones " + fecha);
					deletions += freq.getAdditions();

				}
			}
			ReportItemBuilder<Integer> totalDeletions = new ReportItem.ReportItemBuilder<Integer>("totalDeletions",
					deletions);
			totalDeletions.source("GitHub, calculada")
					.description("Suma el total de eliminaciones desde que el repositorio se cre�");
			metric = totalDeletions.build();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReportItemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return metric;

	}

	/**
	 * Devuelve el número de suscriptores
	 * 
	 * @param repo repositorio que se consulta
	 * @return item para el informe
	 */
	static private ReportItem getSubscribers(GHRepository repo) {
		log.info("Consultando los subscriptores");
		ReportItemBuilder<Integer> builder = null;
		try {
			builder = new ReportItem.ReportItemBuilder<Integer>("subscribers", repo.getSubscribersCount());
			builder.source("GitHub");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	/**
	 * Devuelve el número de forks
	 * 
	 * @param repo repositorio que se consulta
	 * @return item para el informe
	 */
	static private ReportItem getForks(GHRepository repo) {
		log.info("Consultando los forks");
		ReportItemBuilder<Integer> builder = null;
		try {
			builder = new ReportItem.ReportItemBuilder<Integer>("forks", repo.getForksCount());
			builder.source("GitHub");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	/**
	 * Devuelve los usuarios que observan el repositorio
	 * 
	 * @param repo repositorio que se consulta
	 * @return item para el informe
	 */
	static private ReportItem getWatchers(GHRepository repo) {
		log.info("Consultando los watchers");
		ReportItemBuilder<Integer> builder = null;
		try {
			builder = new ReportItem.ReportItemBuilder<Integer>("watchers", repo.getWatchersCount());
			builder.source("GitHub");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	/**
	 * Devuelve el número de estrellas
	 * 
	 * @param repo repositorio que se consulta
	 * @return item para el informe
	 */
	static private ReportItem getStars(GHRepository repo) {
		log.info("Consultando las starts");
		ReportItemBuilder<Integer> builder = null;
		try {
			builder = new ReportItem.ReportItemBuilder<Integer>("stars", repo.getStargazersCount());
			builder.description("Numero de estrellas").source("GitHub");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	/**
	 * Devuelve el número de commits que realiza el responsable del repositorio
	 * 
	 * @param repo repositorio que se consulta
	 * @return item para el informe
	 */
	static private ReportItem getOwnerCommits(GHRepository repo) {
		log.info("Consultando los commits del responsable del repositorio");
		ReportItemBuilder<Integer> builder = null;
		GHRepositoryStatistics data = repo.getStatistics();

		try {
			builder = new ReportItem.ReportItemBuilder<Integer>("ownerCommits",
					data.getParticipation().getOwnerCommits().size());

			builder.description("Commits del responsable").source("GitHub");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	/**
	 * Devuelve el número de tickets
	 * 
	 * @param repo repositorio que se consulta
	 * @return item para el informe
	 */
	static private ReportItem getIssues(GHRepository repo) {
		log.info("Consultando los issues");
		ReportItemBuilder<Integer> builder = null;
		try {
			builder = new ReportItem.ReportItemBuilder<Integer>("issues", repo.getIssues(GHIssueState.ALL).size());
			builder.description("Numero de asuntos totales").source("GitHub");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	/**
	 * Devuelve los issues abiertos en el repositorio que se pasa como parámetro
	 * 
	 * @param repo repositorio que se consulta
	 * @return item para incluir en el informe
	 */
	static private ReportItem getOpenIssues(GHRepository repo) {
		log.info("Consultando los issues abiertos");
		ReportItemBuilder<Integer> builder = null;
		try {
			builder = new ReportItem.ReportItemBuilder<Integer>("openIssues", repo.getOpenIssueCount());
			builder.description("Numero de asuntos abiertos").source("GitHub");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	/**
	 * Devuelve los issues cerrados del repositorio
	 * 
	 * @param repo repositorio que se consulta
	 * @return item para incluir en el informe
	 */
	static private ReportItem getClosedIssues(GHRepository repo) {
		log.info("Consultando los issues cerrados");
		ReportItemBuilder<Integer> builder = null;
		try {
			builder = new ReportItem.ReportItemBuilder<Integer>("closedIssues",
					repo.getIssues(GHIssueState.CLOSED).size());
			builder.description("Numero de asuntos cerrados").source("GitHub");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	/**
	 * Devuelve el número de colaboradores
	 * 
	 * @param repo repositorio que se consulta
	 * @return item para el informe
	 */
	static private ReportItem getCollaborators(GHRepository repo) {
		log.info("Consultando los colaboradores");
		ReportItemBuilder<Integer> builder = null;
		try {
			builder = new ReportItem.ReportItemBuilder<Integer>("collaborators", repo.getCollaborators().size());
			builder.description("Numero de colaboradores en el repositorio").source("GitHub");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	/**
	 * Devuelve la fecha de creación del repositorio
	 * 
	 * @param repo
	 * @return item para el informe
	 */
	static private ReportItem getCreation(GHRepository repo) {
		log.info("Consultando fecha de creación");
		ReportItemBuilder<Date> builder = null;
		try {
			builder = new ReportItem.ReportItemBuilder<Date>("creation", repo.getCreatedAt());
			builder.description("Fecha de creación del repositorio").source("GitHub");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	/**
	 * Devuelve la fecha del último push en el repositorio que se pase como
	 * argumento
	 * 
	 * @param repo repositorio que se consulta
	 * @return item para el informe
	 */
	static private ReportItem getLastPush(GHRepository repo) {
		log.info("Consultando el ultimo push");
		ReportItemBuilder<Date> builder = null;
		try {
			builder = new ReportItem.ReportItemBuilder<Date>("lastPush", repo.getPushedAt());
			builder.description("Último push realizado en el repositorio").source("GitHub");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	/**
	 * Fecha de la última actualización
	 * 
	 * @param repo repositorio que se consulta
	 * @return item para incluir en el informe del repositorio
	 */
	static private ReportItem getLastUpdated(GHRepository repo) {
		log.info("Consultando la ultima actualización");
		ReportItemBuilder<Date> builder = null;
		try {
			builder = new ReportItem.ReportItemBuilder<Date>("lastUpdated", repo.getUpdatedAt());
			builder.description("Última actualización").source("GitHub");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	// Métodos añadidos por el equipo 3
	/**
	 * 
	 * @param remoteRepo
	 * @return ReportItem with the number of issues created last month
	 * @throws MetricException
	 */
	static private ReportItem getIssuesLastMonth(GHRepository remoteRepo) throws MetricException {
		log.info("Consultando los issues abiertos un mes");
		int issuesLastMonth = 0;
		ReportItemBuilder<Integer> builder = null;
		try {
			// Calcular la fecha de hace un mes
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			Date lastMonth = cal.getTime();

			// Obtener todas las issues creadas desde la fecha de hace un mes
			PagedIterable<GHIssue> issues = remoteRepo.queryIssues().state(GHIssueState.ALL).since(lastMonth).list();

			// Contar el número de issues
			for (GHIssue issue : issues) {
				if (issue.getCreatedAt().after(lastMonth)) {
					issuesLastMonth++;
					log.finer("issue manejado por " + issue.getUser().getName());

				}
			}

		} catch (IOException e) {
			throw new MetricException(
					"Error al obtener el número de issues creadas en el último mes: " + e.getMessage());
		}

		// Construir y devolver la métrica
		try {
			builder = new ReportItem.ReportItemBuilder<Integer>("issuesLastMonth", issuesLastMonth);
			builder.description("Issues creadas en el mes").source("GitHub");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return builder.build();
	}

	/**
	 * 
	 * @param remoteRepo
	 * @return ReportItem with the issues closed last month
	 * @throws MetricException
	 */
	static private ReportItem getClosedIssuesLastMonth(GHRepository remoteRepo) throws MetricException {
		int closedIssuesLastMonth = 0;
		ReportItemBuilder<Integer> builder = null;
		try {
			// Calcular la fecha de hace un mes
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			Date lastMonth = cal.getTime();

			// Obtener todas las issues cerradas desde la fecha de hace un mes
			PagedIterable<GHIssue> issues = remoteRepo.queryIssues().state(GHIssueState.CLOSED).since(lastMonth).list();

			// Contar el número de issues cerradas
			for (GHIssue issue : issues) {
				if (issue.getClosedAt() != null && issue.getClosedAt().after(lastMonth)) {
					closedIssuesLastMonth++;
				}
			}

		} catch (Exception e) {
			throw new MetricException(
					"Error al obtener el número de issues cerradas en el último mes: " + e.getMessage());
		}

		// Construir y devolver la métrica
		try {
			builder = new ReportItem.ReportItemBuilder<Integer>("closedIssuesLastMonth", closedIssuesLastMonth);
			builder.description("Issues cerradas en el mes").source("GitHub");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return builder.build();
	}

	/**
	 * 
	 * @param remoteRepo
	 * @return ReportItem with the average of closed issues per member in a month
	 * @throws MetricException
	 */
	static private ReportItem getMeanClosedIssuesLastMonth(GHRepository remoteRepo) throws MetricException {
		int closedIssuesLastMonth = 0;
		int activeMembers = 0;
		Map<String, Integer> issuesClosedByMember = new HashMap<>();
		ReportItemBuilder<Double> builder = null;

		try {
			// Calcular la fecha de hace un mes
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			Date lastMonth = cal.getTime();

			// Obtener todas las issues cerradas desde la fecha de hace un mes
			PagedIterable<GHIssue> issues = remoteRepo.queryIssues().state(GHIssueState.CLOSED).since(lastMonth).list();

			// Contar el número de issues cerradas por cada miembro
			for (GHIssue issue : issues) {
				if (issue.getClosedAt() != null && issue.getClosedAt().after(lastMonth)) {
					GHUser closer = issue.getClosedBy();
					if (closer != null) {
						issuesClosedByMember.put(closer.getLogin(),
								issuesClosedByMember.getOrDefault(closer.getLogin(), 0) + 1);
						closedIssuesLastMonth++;
					}
				}
			}

			// Contar el número de miembros activos
			activeMembers = issuesClosedByMember.size();

		} catch (IOException e) {
			throw new MetricException(
					"Error al obtener el promedio de issues cerradas por miembro activo en el último mes: "
							+ e.getMessage());
		}

		// Calcular el promedio de issues que cierra un miembro
		double avgClosedIssuesPerMember = activeMembers > 0 ? (double) closedIssuesLastMonth / activeMembers : 0.0;

		// Construir y devolver la métrica

		try {
			builder = new ReportItem.ReportItemBuilder<Double>("meanClosedIssuesLastMonth", avgClosedIssuesPerMember);
			builder.description("Average of closed issues per member").source("GitHub");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return builder.build();
	}

	/**
	 * 
	 * @param remoteRepo
	 * @return ReportItem with a map indicating the issues assigned to each member
	 *         in a month
	 * @throws MetricException
	 * 
	 *                         Deuda técnica: esto no está bien porque aquí este
	 *                         reportitem es un mapa y luego lo tratan como si fuera
	 *                         un double... no es coherente una parte con la otra.
	 */
	static private ReportItem getIssues4DevLastMonth(GHRepository remoteRepo) throws MetricException {
		Map<String, Integer> issuesAssignedByMember = new HashMap<>();
		ReportItemBuilder<Map<String, Integer>> builder = null;

		try {
			// Calcular la fecha de hace un mes
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			Date lastMonth = cal.getTime();

			// Obtener todas las issues creadas desde la fecha de hace un mes
			PagedIterable<GHIssue> issues = remoteRepo.queryIssues().state(GHIssueState.ALL).since(lastMonth).list();

			// Contar el número de issues asignadas a cada miembro
			for (GHIssue issue : issues) {
				if (issue.getCreatedAt().after(lastMonth) && !issue.getAssignees().isEmpty()) {
					for (GHUser assignee : issue.getAssignees()) {
						issuesAssignedByMember.put(assignee.getLogin(),
								issuesAssignedByMember.getOrDefault(assignee.getLogin(), 0) + 1);
					}
				}
			}

		} catch (IOException e) {
			throw new MetricException("Error al obtener el número de issues asignadas a cada miembro en el último mes: "
					+ e.getMessage());
		}

		// Construir y devolver la métrica
		try {
			builder = new ReportItem.ReportItemBuilder<Map<String, Integer>>("issues4DevLastMonth",
					issuesAssignedByMember);
			builder.description("Issues assigned to a developer last month").source("GitHub");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return builder.build();
	}

	static private ReportItem getClosedPullReq(GHRepository repo) {
		log.info("Consultando los pull requests completados");
		ReportItemBuilder<Integer> builder = null;

		int completedPullRequests = 0;

		try {

			for (GHPullRequest pullRequest : repo.getPullRequests(GHIssueState.CLOSED)) {

				completedPullRequests++;

			}
			builder = new ReportItem.ReportItemBuilder<Integer>("closedPullReq", completedPullRequests);
			builder.description("Número de pull requests completados").source("GitHub");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder.build();
	}

	static private ReportItem getTotalPullReq(GHRepository repo) {
		log.info("Consultando los pull requests totales");
		ReportItemBuilder<Integer> builder = null;

		int totalPullRequests = 0;

		try {

			for (GHPullRequest pullRequest : repo.getPullRequests(GHIssueState.ALL)) {

				totalPullRequests++;

			}
			builder = new ReportItem.ReportItemBuilder<Integer>("totalPullReq", totalPullRequests);
			builder.description("Número de pull requests totales").source("GitHub");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder.build();
	}

	// Equipo 5

	/**
	 * <p>
	 * Filtra las solicitudes de extracción (pull requests) según la fecha de
	 * creación y el estado de aceptación.
	 * </p>
	 *
	 * @param pullRequests la lista de solicitudes de extracción a filtrar
	 * @param startDate    la fecha de inicio del intervalo de tiempo para filtrar
	 * @param endDate      la fecha de finalización del intervalo de tiempo para
	 *                     filtrar
	 * @param accepted     indica si se deben filtrar las solicitudes de extracción
	 *                     aceptadas (true) o rechazadas (false)
	 * @return la lista de solicitudes de extracción que cumplen con los criterios
	 *         de filtrado
	 */
	private static List<GHPullRequest> filterPullRequests(List<GHPullRequest> pullRequests, LocalDateTime startDate,
			LocalDateTime endDate, boolean accepted) {
		return pullRequests.stream().filter(pr -> {
			try {
				LocalDateTime createdDate = pr.getCreatedAt().toInstant().atZone(ZoneId.systemDefault())
						.toLocalDateTime();
				return createdDate.isAfter(startDate) && createdDate.isBefore(endDate)
						&& (accepted ? pr.isMerged() : !pr.isMerged());
			} catch (IOException e) {
				log.warning("Failed to get creation date for PR #" + pr.getNumber() + "\n" + e);
				return false;
			}
		}).collect(Collectors.toList());
	}

	/**
	 * <p>
	 * Obtención del número de solicitudes de extracción aceptadas en el último año.
	 * </p>
	 * 
	 * @param remoteRepo el repositorio remoto sobre el que consultar
	 * @return la métrica con el número de solicitudes de extracción aceptadas en el
	 *         último año
	 * @throws MetricException si ocurre un error al obtener las solicitudes de
	 *                         extracción
	 */
	static private ReportItem getPRAcceptedLastYear(GHRepository remoteRepo) throws MetricException {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime oneYearAgo = now.minusYears(1);

		try {
			List<GHPullRequest> pullRequests = remoteRepo.getPullRequests(GHIssueState.CLOSED);
			List<GHPullRequest> acceptedLastYear = filterPullRequests(pullRequests, oneYearAgo, now, true);

			ReportItemBuilder<Integer> acceptedLastYearMetric = new ReportItem.ReportItemBuilder<>("PRAcceptedLastYear",
					acceptedLastYear.size());
			acceptedLastYearMetric.source("GitHub, calculada")
					.description("Número de solicitudes de extracción aceptadas en el último año");

			return acceptedLastYearMetric.build();
		} catch (IOException | ReportItemException e) {
			throw new MetricException(
					"Error al obtener las solicitudes de extracción aceptadas en el último año\n" + e);
		}
	}

	/**
	 * <p>
	 * Obtención del número de solicitudes de extracción aceptadas en el último mes.
	 * </p>
	 * 
	 * @param remoteRepo el repositorio remoto sobre el que consultar
	 * @return la métrica con el número de solicitudes de extracción aceptadas en el
	 *         último mes
	 * @throws MetricException si ocurre un error al obtener las solicitudes de
	 *                         extracción
	 */
	static private ReportItem getPRAcceptedLastMonth(GHRepository remoteRepo) throws MetricException {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime oneMonthAgo = now.minusMonths(1);

		try {
			List<GHPullRequest> pullRequests = remoteRepo.getPullRequests(GHIssueState.CLOSED);
			List<GHPullRequest> acceptedLastMonth = filterPullRequests(pullRequests, oneMonthAgo, now, true);

			ReportItemBuilder<Integer> acceptedLastMonthMetric = new ReportItem.ReportItemBuilder<>(
					"PRAcceptedLastMonth", acceptedLastMonth.size());
			acceptedLastMonthMetric.source("GitHub, calculada")
					.description("Número de solicitudes de extracción aceptadas en el último mes");

			return acceptedLastMonthMetric.build();
		} catch (IOException | ReportItemException e) {
			throw new MetricException(
					"Error al obtener las solicitudes de extracción aceptadas en el último mes\n" + e);
		}
	}

	/**
	 * <p>
	 * Obtención del número de solicitudes de extracción rechazadas en el último
	 * año.
	 * </p>
	 * 
	 * @param remoteRepo el repositorio remoto sobre el que consultar
	 * @return la métrica con el número de solicitudes de extracción rechazadas en
	 *         el último año
	 * @throws MetricException si ocurre un error al obtener las solicitudes de
	 *                         extracción
	 */
	static private ReportItem getPRRejectedLastYear(GHRepository remoteRepo) throws MetricException {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime oneYearAgo = now.minusYears(1);

		try {
			List<GHPullRequest> pullRequests = remoteRepo.getPullRequests(GHIssueState.CLOSED);
			List<GHPullRequest> rejectedLastYear = filterPullRequests(pullRequests, oneYearAgo, now, false);

			ReportItemBuilder<Integer> rejectedLastYearMetric = new ReportItem.ReportItemBuilder<>("PRRejectedLastYear",
					rejectedLastYear.size());
			rejectedLastYearMetric.source("GitHub, calculada")
					.description("Número de solicitudes de extracción rechazadas en el último año");

			return rejectedLastYearMetric.build();
		} catch (IOException | ReportItemException e) {
			throw new MetricException(
					"Error al obtener las solicitudes de extracción rechazadas en el último año\n" + e);
		}
	}

	/**
	 * <p>
	 * Obtención del número de solicitudes de extracción rechazadas en el último
	 * mes.
	 * </p>
	 * 
	 * @param remoteRepo el repositorio remoto sobre el que consultar
	 * @return la métrica con el número de solicitudes de extracción rechazadas en
	 *         el último mes
	 * @throws MetricException si ocurre un error al obtener las solicitudes de
	 *                         extracción
	 */
	static private ReportItem getPRRejectedLastMonth(GHRepository remoteRepo) throws MetricException {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime oneMonthAgo = now.minusMonths(1);

		try {
			List<GHPullRequest> pullRequests = remoteRepo.getPullRequests(GHIssueState.CLOSED);
			List<GHPullRequest> rejectedLastMonth = filterPullRequests(pullRequests, oneMonthAgo, now, false);

			ReportItemBuilder<Integer> rejectedLastMonthMetric = new ReportItem.ReportItemBuilder<>(
					"PRRejectedLastMonth", rejectedLastMonth.size());
			rejectedLastMonthMetric.source("GitHub, calculada")
					.description("Número de solicitudes de extracción rechazadas en el último mes");

			return rejectedLastMonthMetric.build();
		} catch (IOException | ReportItemException e) {
			throw new MetricException(
					"Error al obtener las solicitudes de extracción rechazadas en el último mes\n" + e);
		}
	}

	// Metricas equipo 1 curso 23/24
	/**
	 * <p>
	 * Obtiene el ratio de commits convencionales en el último mes
	 * </p>
	 * 
	 * @param remoteRepo Repositorio remoto
	 * @return La métrica con el ratio de commits convencionales
	 * @throws MetricException Si se produce un error al consultar los commits o al
	 *                         crear la métrica
	 */
	static private ReportItem<Double> getConventionalCommits(GHRepository remoteRepo) throws MetricException {
		// Attributes
		ReportItem<Double> metric = null;
		List<GHCommit> commits;

		// Logic
		// Query the commits in the last month to check if they are conventional
		try {
			commits = remoteRepo.queryCommits().since(new Date(System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000))
					.list().toList();

			// Calculate the ratio of conventional commits
			Double conventionalRatio;
			if (commits.size() == 0) {
				conventionalRatio = 0.0;
			} else {
				int conventionalCommits = 0;
				for (GHCommit commit : commits) {
					if (commit.getCommitShortInfo().getMessage().matches(
							"^(revert: )?(feat|fix|docs|style|refactor|perf|test|chore)(\\(.+\\))?: .{1,50}")) {
						conventionalCommits++;
					}
				}
				conventionalRatio = (double) conventionalCommits / commits.size();
			}

			// Create the metric
			ReportItemBuilder<Double> conventionalCommitsMetric = new ReportItem.ReportItemBuilder<Double>(
					"conventionalCommits", conventionalRatio);
			conventionalCommitsMetric.source("GitHub, calculada")
					.description("Número de commits convencionales en el último mes");
			metric = conventionalCommitsMetric.build();
		} catch (IOException e) {
			throw new MetricException("Error al consultar los commits del repositorio");
		} catch (ReportItemException e) {
			throw new MetricException("Error al crear la métrica");
		}
		return metric;
	}

	/**
	 * <p>
	 * Obtiene el ratio de commits con descripción en el último mes
	 * </p>
	 * 
	 * @param remoteRepo Repositorio remoto
	 * @return La métrica con el ratio de commits con descripción
	 * @throws MetricException Si se produce un error al consultar los commits o al
	 *                         crear la métrica
	 */
	static private ReportItem<Double> getCommitsWithDescription(GHRepository remoteRepo) throws MetricException {
		// Attributes
		ReportItem<Double> metric = null;
		List<GHCommit> commits;

		// Logic
		// Query the commits in the last month to check if they have a description
		try {
			commits = remoteRepo.queryCommits().since(new Date(System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000))
					.list().toList();

			// Calculate the ratio of commits with description
			Double commitsWithDescriptionRatio;
			if (commits.size() == 0) {
				commitsWithDescriptionRatio = 0.0;
			} else {
				int commitsWithDescription = 0;
				for (GHCommit commit : commits) {
					if (commit.getCommitShortInfo().getMessage().matches(".*\\n\\n.*")) {
						commitsWithDescription++;
					}
				}
				commitsWithDescriptionRatio = (double) commitsWithDescription / commits.size();
			}

			// Create the metric
			ReportItemBuilder<Double> commitsWithDescriptionMetric = new ReportItem.ReportItemBuilder<Double>(
					"commitsWithDescription", commitsWithDescriptionRatio);
			commitsWithDescriptionMetric.source("GitHub, calculada")
					.description("Número de commits con descripción en el último mes");
			metric = commitsWithDescriptionMetric.build();
		} catch (IOException e) {
			throw new MetricException("Error al consultar los commits del repositorio");
		} catch (ReportItemException e) {
			throw new MetricException("Error al crear la métrica");
		}
		return metric;
	}

	/**
	 * <p>
	 * Obtiene el ratio de issues con etiquetas en el repositorio
	 * </p>
	 * 
	 * @param remoteRepo Repositorio remoto
	 * @return La métrica con el ratio de issues con etiquetas
	 * @throws MetricException Si se produce un error al consultar los issues o al
	 *                         crear la métrica
	 */
	static private ReportItem<Double> getIssuesWithLabels(GHRepository remoteRepo) throws MetricException {
		// Attributes
		ReportItem<Double> metric = null;
		List<GHIssue> issues;

		// Logic
		// Query the open issues to check if they have labels
		try {
			issues = remoteRepo.getIssues(GHIssueState.OPEN);

			// Calculate the ratio of issues with labels
			// By default, the ratio is 1.0 (100%) if there are no issues
			Double issuesWithLabelsRatio = 1.0;

			if (issues.size() > 0) {
				int issuesWithLabels = issues.stream().filter(issue -> issue.getLabels().size() > 0).toList().size();
				issuesWithLabelsRatio = (double) issuesWithLabels / issues.size();
			}

			ReportItemBuilder<Double> issuesWithLabelsMetric = new ReportItem.ReportItemBuilder<Double>(
					"issuesWithLabels", issuesWithLabelsRatio);
			issuesWithLabelsMetric.source("GitHub, calculada")
					.description("Número de issues con etiquetas en el repositorio");
			metric = issuesWithLabelsMetric.build();
		} catch (IOException e) {
			throw new MetricException("Error al consultar los issues del repositorio");
		} catch (ReportItemException e) {
			throw new MetricException("Error al crear la métrica");
		}
		return metric;
	}

	/**
	 * <p>
	 * Obtiene el ratio de ramas que siguen las convenciones de Git Flow en el
	 * repositorio
	 * </p>
	 * 
	 * @param remoteRepo Repositorio remoto
	 * @return La métrica con el ratio de ramas que siguen las convenciones de Git
	 *         Flow
	 * @throws MetricException Si se produce un error al consultar las ramas o al
	 *                         crear la métrica
	 */
	static private ReportItem<Double> getGitFlowBranches(GHRepository remoteRepo) throws MetricException {
		// Attributes
		ReportItem<Double> metric = null;
		List<GHBranch> branches;

		// Logic
		// Query the branches to check if they follow the Git Flow naming conventions
		try {
			branches = remoteRepo.getBranches().values().stream().toList();

			// Calculate the ratio of Git Flow branches
			Double gitFlowBranchesRatio;
			if (branches.size() == 0) {
				gitFlowBranchesRatio = 0.0;
			} else {
				gitFlowBranchesRatio = (double) branches.stream()
						.filter(branch -> GitFlow.isGitFlowBranch(branch.getName())).toList().size() / branches.size();
			}

			// Create the metric
			ReportItemBuilder<Double> gitFlowBranchesMetric = new ReportItem.ReportItemBuilder<Double>(
					"gitFlowBranches", gitFlowBranchesRatio);
			gitFlowBranchesMetric.source("GitHub, calculada")
					.description("Número de ramas que siguen las convenciones de Git Flow en el repositorio");
			metric = gitFlowBranchesMetric.build();
		} catch (IOException e) {
			throw new MetricException("Error al consultar las ramas del repositorio");
		} catch (ReportItemException e) {
			throw new MetricException("Error al crear la métrica");
		}
		return metric;
	}

	/**
	 * <p>
	 * Obtiene el ratio de pull requests convencionales en el último mes
	 * </p>
	 * 
	 * @param remoteRepo Repositorio remoto
	 * @return La métrica con el ratio de pull requests convencionales
	 * @throws MetricException Si se produce un error al consultar los pull requests
	 *                         o al crear la métrica
	 */
	static private ReportItem<Double> getConventionalPullRequests(GHRepository remoteRepo) throws MetricException {
		ReportItem<Double> metric = null;
		List<GHPullRequest> pullRequests;

		// Logic
		// Query the pull requests in the last month to check if they are conventional
		try {
			pullRequests = remoteRepo.queryPullRequests().state(GHIssueState.OPEN).list().toList();

			// Calculate the ratio of conventional pull requests
			Double conventionalPullRequestsRatio;
			if (pullRequests.size() == 0) {
				conventionalPullRequestsRatio = 0.0;
			} else {
				int conventionalPullRequests = 0;
				for (GHPullRequest pullRequest : pullRequests) {
					if (pullRequest.getTitle().matches(
							"^(revert: )?(feat|fix|docs|style|refactor|perf|test|chore)(\\(.+\\))?: .{1,50}")) {
						conventionalPullRequests++;
					}
				}
				conventionalPullRequestsRatio = (double) conventionalPullRequests / pullRequests.size();
			}

			// Create the metric
			ReportItemBuilder<Double> conventionalPullRequestsMetric = new ReportItem.ReportItemBuilder<Double>(
					"conventionalPullRequests", conventionalPullRequestsRatio);
			conventionalPullRequestsMetric.source("GitHub, calculada")
					.description("Número de pull requests convencionales en el último mes");
			metric = conventionalPullRequestsMetric.build();
		} catch (IOException e) {
			throw new MetricException("Error al consultar los pull requests del repositorio");
		} catch (ReportItemException e) {
			throw new MetricException("Error al crear la métrica");
		}

		return metric;
	}

}
