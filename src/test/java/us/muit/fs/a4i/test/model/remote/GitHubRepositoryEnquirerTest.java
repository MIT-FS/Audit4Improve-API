/**
 * 
 */
package us.muit.fs.a4i.test.model.remote;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.remote.GitHubRepositoryEnquirer;
/**
 * 
 */
class GitHubRepositoryEnquirerTest {
	
	private static Logger log = Logger.getLogger(GitHubOrganizationEnquirerTest.class.getName());
	GitHubRepositoryEnquirer ghEnquirer = new GitHubRepositoryEnquirer();

	/**
	 * Test method for
	 * GitHubRepositoryEnquirer, verifing that issuesLastMonth is correctly obtained
	 * @throws MetricException 
	 * @throws ReportItemException 
	 */
	@Test
	void testIssuesLastMonth() throws MetricException, ReportItemException {		
		ReportItem<Integer> metric = ghEnquirer.getMetric("issuesLastMonth", "MIT-FS/Audit4Improve-API");		
		assertEquals(metric.getName(),"issuesLastMonth");
		log.info(metric.getValue().toString());
		log.info(metric.getDescription());
	}
	
	/**
	 * Test method for
	 * GitHubRepositoryEnquirer, verifing that closedIssuesLastMonth is correctly obtained
	 * @throws MetricException 
	 * @throws ReportItemException 
	 */
	@Test
	void testClosedIssuesLastMonth() throws MetricException, ReportItemException {		
		ReportItem<Integer> metric = ghEnquirer.getMetric("closedIssuesLastMonth", "MIT-FS/Audit4Improve-API");		
		assertEquals(metric.getName(),"closedIssuesLastMonth");
		log.info(metric.getValue().toString());
		log.info(metric.getDescription());
	}
	/**
	 * Test method for
	 * GitHubRepositoryEnquirer, verifing that closedIssuesLastMonth is correctly obtained
	 * @throws MetricException 
	 * @throws ReportItemException 
	 */
	@Test
	void testMeanClosedIssuesLastMonth() throws MetricException, ReportItemException {		
		ReportItem<Double> metric = ghEnquirer.getMetric("meanClosedIssuesLastMonth", "MIT-FS/Audit4Improve-API");		
		assertEquals(metric.getName(),"meanClosedIssuesLastMonth");
		log.info(metric.getValue().toString());
		log.info(metric.getDescription());
	}
	

	/**
	 * Test method for {@link us.muit.fs.a4i.model.remote.GitHubEnquirer#getAvailableMetrics()}.
	 */
	@Test
	void testGetAvailableMetrics() {
		List<String> availableMetrics=ghEnquirer.getAvailableMetrics();
		log.info(availableMetrics.toString());
	}
	
	@Test
	void testGetTotalPullRequests() throws MetricException {
		
		// Nombre de la métrica que queremos consultar
		String nombreMetrica = "totalPullReq";
		
		// Repositorio del que se quiere obtener la métrica
		String repositoryId = "MIT-FS/Audit4Improve-API";
		
		// Variable para almacenar el número total de pull requests 
		ReportItem metrica = null;
		
		// Número total de pull requests que habrá en el repositorio
		int numPullRequests = 61; 
		
        // Creamos el RemoteEnquirer para el repositorio GitHub
        GitHubRepositoryEnquirer enquirer = new GitHubRepositoryEnquirer();

        // Obtenemos el número total de pull requests
        metrica = enquirer.getMetric(nombreMetrica, repositoryId);
        log.info(metrica.toString());

        // Comprobamos que el resultado coincida con el número total de pull requests real
        assertEquals(numPullRequests, metrica.getValue());
		
	}
	
	/**
	 * @throws MetricException
	 */
	@Test
	void testCompletedPullRequests() throws MetricException {
		
		// Nombre de la métrica que queremos consultar
		String nombreMetrica = "completedPullReq";
				
		// Repositorio del que se quiere obtener la métrica
		String repositoryId = "MIT-FS/Audit4Improve-API";
				
		// Variable para almacenar el número de pull requests completados
		ReportItem metrica = null;
				
		// Número de pull requests completados que habrá en el repositorio
		int numCompletedPull = 51; 
				
		// Creamos el RemoteEnquirer para el repositorio GitHub
		GitHubRepositoryEnquirer enquirer = new GitHubRepositoryEnquirer();

		// Obtenemos el número de pull requests completados
		metrica = enquirer.getMetric(nombreMetrica, repositoryId);
		log.info(metrica.toString());

		// Comprobamos que el resultado coincida con el número de pull requests completados real
		assertEquals(numCompletedPull, metrica.getValue());	
	}

}
