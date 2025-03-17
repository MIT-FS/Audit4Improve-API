/**
 * 
 */
package us.muit.fs.a4i.model.remote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHOrganization;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.GHProject;
import org.kohsuke.github.GHRepository;

import us.muit.fs.a4i.exceptions.MetricException;

import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.Report;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItem.ReportItemBuilder;

/**
 * <p>
 * Esta clase permite consultar métricas sobre una organización GitHub
 * </p>
 * <p>
 * Deuda técnica: sería necesario verificar mejor el funcionamiento de las
 * consultas de proyectos cerrados y abiertos, no parece hacer lo esperado
 * Habría que incluir más métricas y algún indicador RECUERDA: las métricas
 * tienen que estar incluidas en el fichero de configuración a4iDefault.json
 * </p>
 * 
 * @author Isabel Román
 *
 */

public class GitHubOrganizationEnquirer extends GitHubEnquirer<GHOrganization> {
	private static Logger log = Logger.getLogger(GitHubOrganizationEnquirer.class.getName());
	/**
	 * <p>
	 * Identificador unívoco de la entidad a la que se refire el informe en el
	 * servidor remoto que se va a utilizar
	 * </p>
	 */
	private String entityId;

	public GitHubOrganizationEnquirer() {
		super();
		myQueries.put("repositoriesWithOpenPullRequest",GitHubOrganizationEnquirer::getRepositoriesWithOpenPullRequest);
		myQueries.put("repositories",GitHubOrganizationEnquirer::getRepositories);
		myQueries.put("pullRequests",GitHubOrganizationEnquirer::getPullRequests);
		myQueries.put("members",GitHubOrganizationEnquirer::getMembers);
		myQueries.put("teams",GitHubOrganizationEnquirer::getTeams);
		myQueries.put("openProjects",GitHubOrganizationEnquirer::getOpenProjects);
		myQueries.put("closedProjects",GitHubOrganizationEnquirer::getClosedProjects);
		myQueries.put("followers",GitHubOrganizationEnquirer::getFollowers);
		
		//Equipo 7 23/24
		myQueries.put("teamsBalance",GitHubOrganizationEnquirer::getTeamsBalance);
	
		log.info("Incluidas métricas en Enquirer");
	}

	@Override
	public ReportI buildReport(String organizationId) {
		ReportI report = null;
		log.info("Invocado el metodo que construye un informe de organización, para la organizacion " + organizationId);
		/**
		 * <p>
		 * Información sobre la organizacion de GitHub
		 * </p>
		 */
		GHOrganization organization;
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
			log.info("Nombre organizacion = " + organizationId);

			GitHub gb = getConnection();
			organization = gb.getOrganization(organizationId);

			log.info("La organizacion es de la empresa " + organization.getCompany() + " fue creada en "
					+ organization.getCreatedAt() + " se puede contactar en " + organization.getEmail());
			log.info("leidos datos de la " + organization);
			report = new Report(organizationId);

			/**
			 * Métricas directas de tipo conteo
			 */

			report.addMetric(getMembers(organization));
			log.info("Incluida metrica members ");

			report.addMetric(getTeams(organization));
			log.info("Incluida metrica teams ");

			report.addMetric(getFollowers(organization));
			log.info("Incluida metrica followers ");

			report.addMetric(getPullRequests(organization));
			log.info("Incluida metrica pullRequests ");

			report.addMetric(getRepositories(organization));
			log.info("Incluida metrica repositories ");

			report.addMetric(getRepositoriesWithOpenPullRequest(organization));
			log.info("Incluida metrica repositoriesWithPullRequest ");

			report.addMetric(getOpenProjects(organization));
			log.info("Incluida metrica openProjects ");

			report.addMetric(getClosedProjects(organization));
			log.info("Incluida metrica closedProjects ");

		} catch (Exception e) {
			log.severe("Problemas en la conexión " + e);
		}

		return report;
	}

	/**
	 * Permite consultar desde fuera una única métrica de la organización con el id
	 * que se pase como parámetro
	 */
	@Override
	public ReportItem<Integer> getMetric(String metricName, String organizationId) throws MetricException {
		log.info("Invocado getMetric para buscar " + metricName);
		GHOrganization organization;

		GitHub gb = getConnection();
		try {
			organization = gb.getOrganization(organizationId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MetricException(
					"No se puede acceder a la organizacion remota " + organizationId + " para recuperarla");
		}

		return getMetric(metricName, organization);
	}

	/**
	 * <p>
	 * Crea la métrica solicitada consultando la organizacion que se pasa como
	 * parámetro
	 * </p>
	 * 
	 * @param metricName   Métrica solicitada
	 * @param organization Organizacion
	 * @return La métrica creada
	 * @throws MetricException Si la métrica no está definida se lanzará una
	 *                         excepción
	 */
	private ReportItem getMetric(String metricName, GHOrganization organization) throws MetricException {
		ReportItem metric = null;
		if (organization == null) {
			throw new MetricException("Intenta obtener una métrica sin haber obtenido los datos de la organizacion");
		}
		switch (metricName) {
		case "repositoriesWithOpenPullRequest":
			metric = getRepositoriesWithOpenPullRequest(organization);
			break;
		case "repositories":
			metric = getRepositories(organization);
			break;
		case "pullRequests":
			metric = getPullRequests(organization);
			break;
		case "members":
			metric = getMembers(organization);
			break;
		case "teams":
			metric = getTeams(organization);
			break;
		case "openProjects":
			metric = getOpenProjects(organization);
			break;
		case "closedProjects":
			metric = getClosedProjects(organization);
			break;
		case "followers":
			metric = getFollowers(organization);
			break;
		case "teamsBalance":
            metric=getTeamsBalance(organization);
            break;
		default:
			throw new MetricException("La métrica " + metricName + " no está definida para un repositorio");
		}

		return metric;
	}

	static private ReportItem getRepositoriesWithOpenPullRequest(GHOrganization organization) {
		log.info("Consultando los repositorios con pull requests abiertos");
		ReportItemBuilder<Integer> builder = null;
		try {
			builder = new ReportItem.ReportItemBuilder<Integer>("repositoriesWithOpenPullRequest",
					organization.getRepositoriesWithOpenPullRequests().size());
			builder.source("GitHub");
		} catch (ReportItemException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	static private ReportItem getRepositories(GHOrganization organization) {
		log.info("Consultando los repositorios");
		ReportItemBuilder<Integer> builder = null;
		try {
			builder = new ReportItem.ReportItemBuilder<Integer>("repositories", organization.getPublicRepoCount());
			builder.source("GitHub");
		} catch (ReportItemException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	static private ReportItem getMembers(GHOrganization organization) {
		log.info("Consultando los miembros");
		ReportItemBuilder<Integer> builder = null;
		try {
			builder = new ReportItem.ReportItemBuilder<Integer>("members", organization.listMembers().toList().size());
			builder.source("GitHub");
		} catch (ReportItemException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	static private ReportItem getTeams(GHOrganization organization) {
		log.info("Consultando los equipos");
		ReportItemBuilder<Integer> builder = null;
		try {
			int size = organization.getTeams().size();
			log.info("Numero de equipos" + size);
			builder = new ReportItem.ReportItemBuilder<Integer>("teams", organization.getTeams().size());
			builder.source("GitHub");
		} catch (ReportItemException | IOException e) {
			log.fine("unable to retry teams");
			e.printStackTrace();
		}
		return builder.build();
	}

	static private ReportItem getFollowers(GHOrganization organization) {
		log.info("Consultando los seguidores");
		ReportItemBuilder<Integer> builder = null;
		try {
			builder = new ReportItem.ReportItemBuilder<Integer>("followers", organization.getFollowersCount());
			builder.source("GitHub");
		} catch (ReportItemException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	static private ReportItem getPullRequests(GHOrganization organization) {
		log.info("Consultando los pull requests");
		ReportItemBuilder<Integer> builder = null;
		try {
			builder = new ReportItem.ReportItemBuilder<Integer>("pullRequests", organization.getPullRequests().size());
			builder.source("GitHub");
		} catch (ReportItemException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	static private ReportItem getOpenProjects(GHOrganization organization) {

		ReportItemBuilder<Integer> builder = null;
		try {
			log.info("Consultando los proyectos abiertos en " + organization.getUrl());
			int number = 0;
			// first we look for projects associated with the organization
			PagedIterable<GHProject> pagina = organization.listProjects(GHProject.ProjectStateFilter.OPEN);
			List<GHProject> proyectos = pagina.toList();
			number = number + proyectos.size();
			// second we look for projects associated with the repos
			PagedIterable<GHRepository> repositories = organization.listRepositories();

			for (GHRepository repo : repositories) {
				PagedIterable<GHProject> repoproyects = repo.listProjects(GHProject.ProjectStateFilter.OPEN);
				number = number + repoproyects.toList().size();
			}

			log.info("Open projects " + number);
			builder = new ReportItem.ReportItemBuilder<Integer>("openProjects", number);

			for (GHProject pro : proyectos) {
				log.info("Proyecto " + pro.getName() + " en estado " + pro.getState());
			}
			builder.source("GitHub");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}

	static private ReportItem getClosedProjects(GHOrganization organization) {
		ReportItemBuilder<Integer> builder = null;
		try {
			log.info("Consultando los proyectos cerrados en " + organization.getUrl());
			int number = 0;
			// first we look for projects associated with the organization
			PagedIterable<GHProject> pagina = organization.listProjects(GHProject.ProjectStateFilter.CLOSED);
			List<GHProject> proyectos = pagina.toList();
			number = number + proyectos.size();
			// second we look for projects associated with the repos
			PagedIterable<GHRepository> repositories = organization.listRepositories();

			for (GHRepository repo : repositories) {
				PagedIterable<GHProject> repoproyects = repo.listProjects(GHProject.ProjectStateFilter.CLOSED);
				number = number + repoproyects.toList().size();
			}

			log.info("Closed projects " + number);
			builder = new ReportItem.ReportItemBuilder<Integer>("closedProjects", number);

			for (GHProject pro : proyectos) {
				log.info("Proyecto " + pro.getName() + " en estado " + pro.getState());
			}
			builder.source("GitHub");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();

	}
	static private Map <GHRepository,Integer> getIssuesPerRepository(GHOrganization organization) {
        log.info("Consultando los issues de cada uno de los repositorios de la organización");
		Map <GHRepository,Integer> mapa = new HashMap<>();
        try {

			PagedIterable<GHRepository> repositorios = organization.listRepositories();
			for (GHRepository repo : repositorios) {
				mapa.put(repo,repo.getIssues(GHIssueState.OPEN).size());				
			}
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mapa;
    }

	static private Map <GHRepository,Integer> getTeamsPerRepository(GHOrganization organization) {
		log.info("Consultando el número de equipos por repositorio");
		ReportItemBuilder<Map<GHRepository,Integer>> builder=null;
		Map <GHRepository,Integer> mapa = new HashMap<>();
		try {
			PagedIterable<GHRepository> repositorios = organization.listRepositories();
			for (GHRepository repo : repositorios) {
				mapa.put(repo,repo.getTeams().size());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapa;
	}

	static private ReportItem getTeamsBalance(GHOrganization organization) {

		log.info("Consultando el equilibrio entre equipos e issues de los repositorios");
		Integer issuesTotales= getIssuesPerRepository(organization).size();
        Integer desarrolladoresTotales=(Integer) getTeams(organization).getValue();
		Integer numProjects = (Integer) getRepositories(organization).getValue();
		Map <GHRepository,Integer> issuesPerProject =getIssuesPerRepository(organization);
		Map <GHRepository,Integer> teamsPerProject = getTeamsPerRepository(organization);
		
        Double desajustePromedioOrganizacion=0.0;
        List<Double> desajustePromedioProyecto = new ArrayList<Double>();

		//Es raro que sea Integer y en la declaración de a4iDefault esta(ba) como Double
		ReportItemBuilder<Integer> builder=null;
		try {
			//Calcular el desajuste promedio de cada proyecto y guardarlo en desajustePromedioProyecto
			issuesPerProject.forEach((repo,numIssues)->
				desajustePromedioProyecto.add((double) (Math.abs(numIssues/(issuesTotales)-(teamsPerProject.get(repo)/desarrolladoresTotales))))
			);

			//Sumar todos los desajustes y guardarlos en desajustePromedioOrganizacion
			for (Double desajuste: desajustePromedioProyecto){
				desajustePromedioOrganizacion+=desajuste;
			}

			builder = new ReportItem.ReportItemBuilder<Integer>("teamsBalance", (int) Math.round(desajustePromedioOrganizacion));
			builder.source("GitHub");
		} catch (ReportItemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.build();
	}
	

}
