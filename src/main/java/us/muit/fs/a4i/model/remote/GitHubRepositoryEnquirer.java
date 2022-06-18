/**
 * 
 */
package us.muit.fs.a4i.model.remote;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHRepositoryStatistics;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GHRepositoryStatistics.CodeFrequency;

import us.muit.fs.a4i.exceptions.MetricException;

import us.muit.fs.a4i.model.entities.Metric;
import us.muit.fs.a4i.model.entities.Metric.MetricBuilder;
import us.muit.fs.a4i.model.entities.Report;
import us.muit.fs.a4i.model.entities.ReportI;

/**
 * @author Isabel Román
 *
 */
public class GitHubRepositoryEnquirer extends GitHubEnquirer {
	private static Logger log = Logger.getLogger(GitHubRepositoryEnquirer.class.getName());
	/**
	 * <p>Constructor</p>
	 */

	public GitHubRepositoryEnquirer() {
		super();
		metricNames.add("subscribers");
		metricNames.add("forks");
		metricNames.add("watchers");
	}

	@Override
	public ReportI buildReport(String repositoryId) {
		ReportI myRepo = null;
		log.info("Invocado el método que construye un objeto RepositoryReport");
		/**
		 * <p>
		 * Información sobre el repositorio obtenida de GitHub
		 * </p>
		 */
		GHRepository remoteRepo;
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

			GitHub gb = getConnection();
			remoteRepo = gb.getRepository(repositoryId);
			log.info("leído " + remoteRepo);
			myRepo = new Report(repositoryId);

			/**
			 * Métricas directas de tipo conteo
			 */

			MetricBuilder<Integer> subscribers = new Metric.MetricBuilder<Integer>("subscribers",
					remoteRepo.getSubscribersCount());
			subscribers.source("GitHub");
			myRepo.addMetric(subscribers.build());
			log.info("Añadida métrica suscriptores " + subscribers);

			MetricBuilder<Integer> forks = new Metric.MetricBuilder<Integer>("forks", remoteRepo.getForksCount());
			forks.source("GitHub");
			myRepo.addMetric(forks.build());
			log.info("Añadida métrica forks " + forks);

			MetricBuilder<Integer> watchers = new Metric.MetricBuilder<Integer>("watchers",
					remoteRepo.getWatchersCount());
			watchers.source("GitHub");
			myRepo.addMetric(watchers.build());

			MetricBuilder<Integer> stars = new Metric.MetricBuilder<Integer>("stars", remoteRepo.getStargazersCount());
			stars.source("GitHub");
			myRepo.addMetric(stars.build());

			MetricBuilder<Integer> issues = new Metric.MetricBuilder<Integer>("issues", remoteRepo.getOpenIssueCount());
			issues.source("GitHub");
			myRepo.addMetric(issues.build());
			/**
			 * Métricas directas de tipo fecha
			 */

			MetricBuilder<Date> creation = new Metric.MetricBuilder<Date>("creation", remoteRepo.getCreatedAt());
			creation.source("GitHub");
			myRepo.addMetric(creation.build());

			MetricBuilder<Date> push = new Metric.MetricBuilder<Date>("lastPush", remoteRepo.getPushedAt());
			push.description("Último push realizado en el repositorio").source("GitHub");
			myRepo.addMetric(push.build());

			MetricBuilder<Date> updated = new Metric.MetricBuilder<Date>("lastUpdated", remoteRepo.getUpdatedAt());
			push.description("Última actualización").source("GitHub");
			myRepo.addMetric(updated.build());
			/**
			 * Métricas más elaboradas, requieren más "esfuerzo"
			 */

			GHRepositoryStatistics data = remoteRepo.getStatistics();
			List<CodeFrequency> codeFreq = data.getCodeFrequency();
			int additions = 0;
			int deletions = 0;
			for (CodeFrequency freq : codeFreq) {

				if ((freq.getAdditions() != 0) || (freq.getDeletions() != 0)) {
					Date fecha = new Date((long) freq.getWeekTimestamp() * 1000);
					log.info("Fecha modificaciones " + fecha);
					additions += freq.getAdditions();
					deletions += freq.getDeletions();
				}

			}
			MetricBuilder<Integer> totalAdditions = new Metric.MetricBuilder<Integer>("totalAdditions", additions);
			totalAdditions.source("GitHub, calculada")
					.description("Suma el total de adiciones desde que el repositorio se creó");
			myRepo.addMetric(totalAdditions.build());

			MetricBuilder<Integer> totalDeletions = new Metric.MetricBuilder<Integer>("totalDeletions", deletions);
			totalDeletions.source("GitHub, calculada")
					.description("Suma el total de borrados desde que el repositorio se creó");
			myRepo.addMetric(totalDeletions.build());

		} catch (Exception e) {
			log.severe("Problemas en la conexión " + e);
		}

		return myRepo;
	}

	/**
	 * Permite consultar desde fuera una métrica del repositorio indicado
	 */

	@Override
	public Metric getMetric(String metricName, String repositoryId) throws MetricException {
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
 * <p>Crea la métrica solicitada consultando el repositorio remoto que se pasa como parámetro</p>
 * @param metricName Métrica solicitada
 * @param remoteRepo Repositorio remoto
 * @return La métrica creada
 * @throws MetricException Si la métrica no está definida se lanzará una excepción
 */
	private Metric getMetric(String metricName, GHRepository remoteRepo) throws MetricException {
		Metric metric;
		if (remoteRepo == null) {
			throw new MetricException("Intenta obtener una métrica sin haber obtenido los datos del repositorio");
		}
		switch (metricName) {
		case "totalAdditions":
			metric = getTotalAdditions(remoteRepo);
			break;
		case "totalDeletions":
			metric = getTotalDeletions(remoteRepo);
			break;
		default:
			throw new MetricException("La métrica " + metricName + " no está definida para un repositorio");
		}

		return metric;
	}

	/*
	 * A partir de aquí los algoritmos específicoso para hacer las consultas de cada
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
	private Metric getTotalAdditions(GHRepository remoteRepo) throws MetricException {
		Metric metric = null;

		GHRepositoryStatistics data = remoteRepo.getStatistics();
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
			MetricBuilder<Integer> totalAdditions = new Metric.MetricBuilder<Integer>("totalAdditions", additions);
			totalAdditions.source("GitHub, calculada")
					.description("Suma el total de adiciones desde que el repositorio se creó");
			metric = totalAdditions.build();

		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	 * @return la métrica con el número total de eliminaciones desde el inicio
	 * @throws MetricException Intenta crear una métrica no definida
	 */
	private Metric getTotalDeletions(GHRepository remoteRepo) throws MetricException {
		Metric metric = null;

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
			MetricBuilder<Integer> totalDeletions = new Metric.MetricBuilder<Integer>("totalDeletions", deletions);
			totalDeletions.source("GitHub, calculada")
					.description("Suma el total de eliminaciones desde que el repositorio se creó");
			metric = totalDeletions.build();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return metric;

	}

}
