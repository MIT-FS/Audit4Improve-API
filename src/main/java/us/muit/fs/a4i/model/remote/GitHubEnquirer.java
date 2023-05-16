/**
 * 
 */
package us.muit.fs.a4i.model.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

/**
 * <p>
 * Clase abstracta con los métodos comunes a los constructores que recogen la
 * información del servicio GitHub
 * </p>
 * <p>
 * Para las consultas a github se recurre a la API github-API
 * </p>
 * <p>
 * Actualmente sólo incluye el establecimiento del identificador de entidad y la
 * obtención del objeto GitHub para las consultas
 * </p>
 * 
 * @author Isabel Román
 *
 */
public abstract class GitHubEnquirer implements RemoteEnquirer {
	private static Logger log = Logger.getLogger(GitHubEnquirer.class.getName());
	protected List<String> metricNames;

	/**
	 * <p>
	 * Referencia al objeto GitHub que permite hacer consultas al servidor Github
	 * </p>
	 * <p>
	 * Se crea al invocar por primera vez getConnectiony se mantiene mientras el
	 * GHBuilder está vivo
	 * </p>
	 */
	private GitHub github = null;

	public GitHubEnquirer() {
		metricNames = new ArrayList<String>();
	}

	/**
	 * <p>
	 * El objeto para contectarse al GitHub se crea la primera vez que se invoca
	 * getConnection
	 * </p>
	 * 
	 * @return devuelve un objeto GitHub que permite la consulta al remoto
	 */
	protected GitHub getConnection() {

		if (github == null)
			try {
				github = GitHubBuilder.fromEnvironment().build();
				log.info("Creado el objeto GitHub");

			} catch (Exception e) {
				log.info(e + " No se puede crear la instancia GitHub\n");
				log.info(
						"Recuerde que debe configurar las variables de entorno GITHUB_LOGIN y GITHUB_OAUTH con su nombre de usuario y token respectivamente");
				e.printStackTrace();
			}
		return github;
	}

	protected void setMetric(String newMetric) {
		metricNames.add(newMetric);
	}

	public List<String> getAvailableMetrics() {
		return metricNames;
	}

}
