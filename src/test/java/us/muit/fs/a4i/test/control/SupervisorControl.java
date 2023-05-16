package us.muit.fs.a4i.test.control;

import java.util.HashMap;
//import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.kohsuke.github.GHMyself;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHPersonSet;
import org.kohsuke.github.GHProject;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHRepositoryStatistics;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.PagedIterable;

/**
 * @author Isabel Rom�n Mart�nez
 * @version 0.2 Esta clase se crea para poder probar algunas de las capacidades
 *          que ofrece la api github Serádescartada posteriormente No usa
 *          Junit, sino que crea un main, no tiene verificaciones automáticas,
 *          la automatización no es posible
 *
 */
public class SupervisorControl {
	private static Logger log = Logger.getLogger(SupervisorControl.class.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GitHub github = GitHubBuilder.fromEnvironment().build();
			GHMyself myinfo = github.getMyself();
			// GHRepository unrepo=github.getRepository("MIT-FS/ShopManager");
			// PagedIterable<GHRepository> myrepos=myinfo.listRepositories();
			PagedIterable<GHRepository> myOwnRepos = myinfo.listRepositories(10, GHMyself.RepositoryListFilter.OWNER);
			int count = 1;
			for (GHRepository repo : myOwnRepos.toList()) {
				System.out.println("Nombre de mi repositorio número " + count + " " + repo.getFullName() + " con id " + repo.getId());
				List<GHProject> proyectos = repo.listProjects().toList();
				int i = 1;
				for (GHProject project : proyectos) {
					System.out.println(
							"Con proyecto " + i + " llamado " + project.getName() + " con id " + project.getId());
					GHProject audit = github.getProject(project.getId());
					System.out.println(audit);
					i++;
				}
				if (repo.isFork()) {
					System.out.println("Que es un fork de " + repo.fork().getName());
				}
				count++;
			}
			GHPersonSet<GHOrganization> misOrganizaciones = myinfo.getAllOrganizations();
			System.out.println("Pertenezco a las siguientes organizaciones: ");
			// Iterator<GHOrganization> iteradorOrganizaciones =
			// misOrganizaciones.iterator();
			int i = 1;
			for (GHOrganization organizacion : misOrganizaciones) {
				System.out.println(i + " Organización " + organizacion.getId() + " : " + organizacion);
				PagedIterable<GHRepository> repos = organizacion.listRepositories();
				System.out.println(repos);
				i++;
			}
			/*
			 * log.info("Mis datos "+myinfo); log.info("Un repositorio "+unrepo);
			 * log.info("N�mero de repositorios "+myrepos.toList().size());
			 * log.info("Detalles de mis repositorios "+myrepos.toList());
			 */
			GHOrganization unaOrg = github.getOrganization("MIT-FS");
			// PagedIterable<GHRepository> repos=unaOrg.listRepositories();
			System.out.println("Recupero la organización " + unaOrg.getId());

			GHRepository githubrepo = github.getRepository("hub4j/github-api");
			System.out.println("Este repositorio es de " + githubrepo.getOwnerName() + " Y su descripción es "
					+ githubrepo.getDescription());

			GHRepositoryStatistics estadisticas = githubrepo.getStatistics();
			log.info("Estadisticas recogidas");

			// List<GHProject> proyectos=githubrepo.listProjects().toList();
			PagedIterable<GHRepositoryStatistics.ContributorStats> estDes = estadisticas.getContributorStats();
			log.info("Desarrolladores recogidos");
			List<GHRepositoryStatistics.ContributorStats> listaDesarrolladores = estDes.toList();
			System.out.println("Número de desarrolladores " + listaDesarrolladores.size());

			i = 1;

			HashMap<String, GHRepositoryStatistics.ContributorStats> mapaEstadisticasUsuario = new HashMap<String, GHRepositoryStatistics.ContributorStats>();
			for (GHRepositoryStatistics.ContributorStats desarrollador : listaDesarrolladores) {
				// System.out.println(i+" Desarrollador "+desarrollador.getAuthor().getName()+"
				// mail "+desarrollador.getAuthor().getEmail()+ " login
				// "+desarrollador.getAuthor().getLogin());
//        		GHUser usuario=github.getUser(desarrollador.getAuthor().getLogin());
				mapaEstadisticasUsuario.put(desarrollador.getAuthor().getLogin(), desarrollador);
				i++;
			}
			System.out.println("Datos del usuario rtyley " + mapaEstadisticasUsuario.get("rtyley"));
			System.out.println("Semanas " + mapaEstadisticasUsuario.get("rtyley").getWeeks());

			/*
			 * for(GHProject project:proyectos){
			 * System.out.println("Con proyecto "+i+" llamado "+project.getName()+" con id "
			 * +project.getId()); GHProject audit= github.getProject(project.getId());
			 * System.out.println(audit); i++; }
			 */
		} catch (Exception e) {
			log.info(e + " No se puede crear la instancia GitHub\n");
			log.info(
					"Recuerde que debe configurar las variables de entorno GITHUB_LOGIN y GITHUB_OAUTH con su nombre de usuario y token respectivamente");
			e.printStackTrace();
		}
	}

}
