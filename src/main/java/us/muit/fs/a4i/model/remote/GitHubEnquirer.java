/**
 * 
 */
package us.muit.fs.a4i.model.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.function.*;
import java.util.Map;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.remote.RemoteEnquirer.RemoteType;

/**
 * <p>
 * Clase abstracta con los métodos comunes a los constructores que consultan la
 * información del servicio GitHub. Usan Github como backend
 * </p>
 * <p>
 * Para las consultas a github se recurre a la API github-API
 * </p>
 * <p>
 * Actualmente sólo incluye el establecimiento del identificador de entidad y la
 * obtención del objeto GitHub para las consultas.
 * </p>
 * protected Map<String,Function<T,ReportItem>> myQueries; será un mapa de
 * funciones, en el que la clave es el nombre de la métrica y el valor es la
 * referencia a la función que permite recuperar esa métrica
 * <p>
 * </p>
 * 
 * @author Isabel Román
 * @param <T>
 *
 */
public abstract class GitHubEnquirer<T> implements RemoteEnquirer {
	private static Logger log = Logger.getLogger(GitHubEnquirer.class.getName());
	protected Map<String, Function<T, ReportItem>> myQueries;
	private RemoteEnquirer.RemoteType type = RemoteEnquirer.RemoteType.GITHUB;

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
		myQueries = new HashMap<String, Function<T, ReportItem>>();
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

	/**
	 * Permite añadir una nueva función de búsqueda de métrica al mapa
	 * 
	 * @param newMetric       Nombre de la métrica nueva
	 * @param functionPointer Referencia a la función que implementa el algoritmo
	 *                        para recuperar la métrica
	 */
	protected void setMetric(String newMetric, Function<T, ReportItem> functionPointer) {
		myQueries.put(newMetric, functionPointer);
	}

	public List<String> getAvailableMetrics() {
		List<String> metrics = new ArrayList<String>(myQueries.keySet());
		return metrics;
	}

	public RemoteType getRemoteType() {
		return type;
	}

}
