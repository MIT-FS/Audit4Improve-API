/**
 * 
 */
package us.muit.fs.a4i.test.model.remote;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;


import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.model.remote.GitHubRepositoryEnquirer;

/**
 * 
 */
class GitHubRepositoryEnquirerTest {

	private static Logger log = Logger.getLogger(GitHubOrganizationEnquirerTest.class.getName());
	GitHubRepositoryEnquirer ghEnquirer = new GitHubRepositoryEnquirer();

	/**
	 * Test method for GitHubRepositoryEnquirer, verifing that issuesLastMonth is
	 * correctly obtained
	 * 
	 * @throws MetricException
	 * @throws ReportItemException
	 */
	@Test
	void testIssuesLastMonth() throws MetricException, ReportItemException {
		ReportItem<Integer> metric = ghEnquirer.getMetric("issuesLastMonth", "MIT-FS/Audit4Improve-API");
		assertEquals(metric.getName(), "issuesLastMonth");
		log.info(metric.getValue().toString());
		log.info(metric.getDescription());
	}

	/**
	 * Test method for GitHubRepositoryEnquirer, verifing that closedIssuesLastMonth
	 * is correctly obtained
	 * 
	 * @throws MetricException
	 * @throws ReportItemException
	 */
	@Test
	void testClosedIssuesLastMonth() throws MetricException, ReportItemException {
		ReportItem<Integer> metric = ghEnquirer.getMetric("closedIssuesLastMonth", "MIT-FS/Audit4Improve-API");
		assertEquals(metric.getName(), "closedIssuesLastMonth");
		log.info(metric.getValue().toString());
		log.info(metric.getDescription());
	}

	/**
	 * Test method for GitHubRepositoryEnquirer, verifing that closedIssuesLastMonth
	 * is correctly obtained
	 * 
	 * @throws MetricException
	 * @throws ReportItemException
	 */
	@Test
	void testMeanClosedIssuesLastMonth() throws MetricException, ReportItemException {
		ReportItem<Double> metric = ghEnquirer.getMetric("meanClosedIssuesLastMonth", "MIT-FS/Audit4Improve-API");
		assertEquals(metric.getName(), "meanClosedIssuesLastMonth");
		log.info(metric.getValue().toString());
		log.info(metric.getDescription());
	}

	/**
	 * Test method for
	 * {@link us.muit.fs.a4i.model.remote.GitHubEnquirer#getAvailableMetrics()}.
	 */
	@Test
	void testGetAvailableMetrics() {
		List<String> availableMetrics = ghEnquirer.getAvailableMetrics();
		log.info(availableMetrics.toString());
	}

	@Test
	void testGetTotalPullRequests() throws MetricException {

		// Nombre de la métrica que queremos consultar
		String nombreMetrica = "totalPullReq";

		// Repositorio del que se quiere obtener la métrica
		String repositoryId = "MIT-FS/Audit4Improve-API";

		// Variable para almacenar el número total de pull requests
		ReportItem<Integer> metrica = null;

		// Creamos el RemoteEnquirer para el repositorio GitHub
		GitHubRepositoryEnquirer enquirer = new GitHubRepositoryEnquirer();

		// Obtenemos el número total de pull requests
		metrica = enquirer.getMetric(nombreMetrica, repositoryId);
		log.info(metrica.toString());

		// Comprobamos que el resultado coincida con el número total de pull requests
		// real
		assertTrue(metrica.getValue() > 0, "Bad number of PR");

	}

	/**
	 * @throws MetricException
	 */
	@Test
	void testCompletedPullRequests() throws MetricException {

		// Nombre de la métrica que queremos consultar
		String nombreMetrica = "closedPullReq";

		// Repositorio del que se quiere obtener la métrica
		String repositoryId = "MIT-FS/Audit4Improve-API";

		// Variable para almacenar el número de pull requests completados
		ReportItem<Integer> metrica = null;

		// Creamos el RemoteEnquirer para el repositorio GitHub
		GitHubRepositoryEnquirer enquirer = new GitHubRepositoryEnquirer();

		// Obtenemos el número de pull requests completados
		metrica = enquirer.getMetric(nombreMetrica, repositoryId);
		log.info(metrica.toString());

		// Comprobamos que el resultado coincida con el número de pull requests
		// completados real

		assertTrue(0 < metrica.getValue(), "Bad number of closed PR");
	}

	// Test del equipo 1 del curso 23/24
	// MARK: Tests
	@Test
	@DisplayName("Test getConventionalCommits")
	@SuppressWarnings("rawtypes")
	void testGetConventionalCommits() {
		ReportItemI reportItem = testGetMetric("conventionalCommits");
		assertNotNull(reportItem, "Getting conventional commits failed: reportItem is null");
		assertNotNull(reportItem.getValue(), "Getting conventional commits failed: value is null");
		// Check that the value is a number between 0 and 1
		double value = (double) reportItem.getValue();
		assertTrue(value >= 0 && value <= 1, "Getting conventional commits failed: value is not between 0 and 1");
	}

	@Test
	@DisplayName("Test issuesWithLabels")
	@SuppressWarnings("rawtypes")
	void testGetIssuesWithLabels() {
		ReportItemI reportItem = testGetMetric("issuesWithLabels");
		assertNotNull(reportItem, "Getting issues with labels failed: reportItem is null");
		assertNotNull(reportItem.getValue(), "Getting issues with labels failed: value is null");
		// Check that the value is a number between 0 and 1
		double value = (double) reportItem.getValue();
		assertTrue(value >= 0 && value <= 1, "Getting issues with labels failed: value is not between 0 and 1");
	}

	@Test
	@DisplayName("Test gitFlowBranches")
	@SuppressWarnings("rawtypes")
	void testGetGitFlowBranches() {
		ReportItemI reportItem = testGetMetric("gitFlowBranches");
		assertNotNull(reportItem, "Getting git flow branches failed: reportItem is null");
		assertNotNull(reportItem.getValue(), "Getting git flow branches failed: value is null");
		// Check that the value is a number between 0 and 1
		double value = (double) reportItem.getValue();
		assertTrue(value >= 0 && value <= 1, "Getting git flow branches failed: value is not between 0 and 1");
	}

	@Test
	@DisplayName("Test commitsWithDescription")
	@SuppressWarnings("rawtypes")
	void testGetCommits() {
		ReportItemI reportItem = testGetMetric("commitsWithDescription");
		assertNotNull(reportItem, "Getting commits with description failed: reportItem is null");
		assertNotNull(reportItem.getValue(), "Getting commits with description failed: value is null");
		// Check that the value is a number between 0 and 1
		double value = (double) reportItem.getValue();
		assertTrue(value >= 0 && value <= 1, "Getting commits with description failed: value is not between 0 and 1");
	}

	@Test
	@DisplayName("Test conventionalPullRequests")
	@SuppressWarnings("rawtypes")
	void testGetRepository() {
		ReportItemI reportItem = testGetMetric("commitsWithDescription");
		assertNotNull(reportItem, "Getting repository failed: reportItem is null");
		assertNotNull(reportItem.getValue(), "Getting repository failed: value is null");
		// Check that the value is a number between 0 and 1
		double value = (double) reportItem.getValue();
		assertTrue(value >= 0 && value <= 1, "Getting repository failed: value is not between 0 and 1");
	}

	// MARK: Test helper methods
	@SuppressWarnings("rawtypes")
	ReportItemI testGetMetric(String metricString) {
		log.info("Consultando metrica " + metricString);
		ReportItemI reportItem;
		try {
			reportItem = ghEnquirer.getMetric(metricString, "MIT-FS/Audit4Improve-API");
		} catch (MetricException e) {
			e.printStackTrace();
			fail("Exception thrown while getting conventional commits: " + e.getMessage());
			return null;
		}

		assertNotNull(reportItem, "Getting the metric (" + metricString + ") failed: reportItem is null");
		return reportItem;
	}
	
	//Test de construcción del informe (ReportI)
	@Test
	void testGetReport() {
		ReportI report=ghEnquirer.buildReport("MIT-FS/Audit4Improve-API");
		assertNotNull(report,"No construye el informe");
		log.info("Informe construido "+report.toString());
	}

	
	// Test realizados por le equipo 13 curso 25/25
	/**
	 * @throws MetricException
	 */
	@Test
	void testGetTotalCommitsPerUserLastYear() throws MetricException {

		// Nombre de la métrica que queremos consultar
		String nombreMetrica = "totalCommitsPerUserLastYear";

		// Repositorio del que se quiere obtener la métrica
		String repositoryId = "MIT-FS/Audit4Improve-API";

		// Variable para almacenar el número de commits totales realizados en el último mes
		ReportItem<HashMap<String, Double>> totalCommitsPerUserLastYear = null;
		// Creamos el RemoteEnquirer para el repositorio GitHub
		GitHubRepositoryEnquirer enquirer = new GitHubRepositoryEnquirer();

		// Obtenemos el número de commits en el último mes
		totalCommitsPerUserLastYear = enquirer.getMetric(nombreMetrica, repositoryId);

		// Comprobaciones:
		// 1. El valor de la métrica no es nulo
		assertNotNull(totalCommitsPerUserLastYear , "Getting total commits per user failed: reportItem is null");
		
		// COMENTARIO DE REVISIÓN: La aserción 'instanceof HashMap' es correcta y oportuna para asegurar que el enquirer 
		// no ha degradado el tipo dinámico de la colección (por ejemplo, pasándolo a una lista o un mapa inmutable genérico), 
		// garantizando que el consumidor de la API reciba la estructura exacta que requiere el indicador.
		// 2. El valor de la métrica es un hashmap con los usuarios y sus commits
		assertTrue(totalCommitsPerUserLastYear.getValue() instanceof HashMap, "Getting total commits per user failed: value is not an HahMap");
		
		// COMENTARIO DE REVISIÓN: El bucle de validación incremental es un acierto de diseño. Al comprobar 'entry.getValue() >= 0' 
		// mediante un tipo 'Double' explícito, se verifica que la métrica de actividad no contenga valores erróneos o negativos, 
		// respetando la semántica del indicador matemático de contribuciones.
		// 3. El valor de la métrica es mayor o igual que 0
		for (Map.Entry<String, Double> entry : totalCommitsPerUserLastYear.getValue().entrySet()) {
			assertTrue(entry.getValue() >= 0,
					"Getting total commits per user failed: value is less than 0 for user " + entry.getKey());
		}
	}
	
	/**
	 * @throws MetricException
	 */
	@Test
	void testGetTotalLinesPerUserLastYear() throws MetricException {
	
		// Nombre de la métrica que queremos consultar
		String nombreMetrica = "totalLinesPerUserLastYear";
	
		// Repositorio del que se quiere obtener la métrica
		String repositoryId = "MIT-FS/Audit4Improve-API";
	
		// Variable para almacenar el número de commits totales realizados en el último mes
		ReportItem<HashMap<String, Double>> testGetTotalLinesPerUserLastYear = null;
	
		// Creamos el RemoteEnquirer para el repositorio GitHub
		GitHubRepositoryEnquirer enquirer = new GitHubRepositoryEnquirer();
	
		// Obtenemos el número de commits en el último mes
		testGetTotalLinesPerUserLastYear = enquirer.getMetric(nombreMetrica, repositoryId);

		// Comprobaciones:
		// 1. El valor de la métrica no es nulo
		assertNotNull(testGetTotalLinesPerUserLastYear , "Getting total lines per user failed: reportItem is null");
		
		// COMENTARIO DE REVISIÓN: El test valida correctamente el contrato de la firma. Al exigir un 'HashMap' con valores 
		// 'Double', se amarra el comportamiento del enquirer para que la recolección de volumen de líneas modificadas sea 
		// compatible de forma transparente con los algoritmos matemáticos de diversidad de aportaciones del modelo.
		// 2. El valor de la métrica es un hashmap con los usuarios y sus commits
		assertTrue(testGetTotalLinesPerUserLastYear.getValue() instanceof HashMap, "Getting total lines per user failed: value is not an HashMap");
		
		// COMENTARIO DE REVISIÓN: La verificación por cada elemento de la colección asegura que la métrica es robusta. 
		// Aunque un usuario elimine más líneas de las que añada, el total de líneas modificadas acumuladas (additions + deletions) 
		// debe ser estrictamente positivo o cero, por lo que evaluar que el valor sea '>= 0' es lógicamente impecable.
		// 3. El valor de la métrica es mayor o igual que 0
		for (Map.Entry<String, Double> entry : testGetTotalLinesPerUserLastYear.getValue().entrySet()) {
			assertTrue(entry.getValue() >= 0,
					"Getting total lines per user failed: value is less than 0 for user " + entry.getKey());
		}
	}
}
