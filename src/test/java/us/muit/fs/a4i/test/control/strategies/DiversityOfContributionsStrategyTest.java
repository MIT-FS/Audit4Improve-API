package us.muit.fs.a4i.test.control.strategies;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import us.muit.fs.a4i.control.strategies.DiversityOfContributionsStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.model.entities.ReportItemI;

class DiversityOfContributionsStrategyTest {

	@Test
	public void testCalcIndicator() throws NotAvailableMetricException {
		// Creamos los mocks necesarios
		//ReportItemI<Integer> mockNum_commits = Mockito.mock(ReportItemI.class);
		ReportItemI<HashMap<String,Double>> mockNum_commits_per_user = Mockito.mock(ReportItemI.class);
		//ReportItemI<Integer> mockNum_lines = Mockito.mock(ReportItemI.class);
		ReportItemI<HashMap<String,Double>> mockNum_lines_per_user = Mockito.mock(ReportItemI.class);

		// Configuramos los mocks para devolver valores predefinidos
		//Mockito.when(mockNum_commits.getName()).thenReturn("totalCommitsLastMonth");
		//Mockito.when(mockNum_commits.getValue()).thenReturn(200);

		HashMap<String,Double> commits_per_user = new HashMap<>();
		commits_per_user.put("Antonio", 200.0);
		commits_per_user.put("Manolo", 3.0);
		
		Mockito.when(mockNum_commits_per_user.getName()).thenReturn("totalCommitsPerUserLastMonth");
		Mockito.when(mockNum_commits_per_user.getValue()).thenReturn(commits_per_user);

		//Mockito.when(mockNum_lines.getName()).thenReturn("totalLinesLastMonth");
		//Mockito.when(mockNum_lines.getValue()).thenReturn(20000);
		
		HashMap<String,Double> lines_per_user = new HashMap<>();
		lines_per_user.put("Antonio", 10000.0);
		lines_per_user.put("Manolo", 10000.0);
		
		Mockito.when(mockNum_lines_per_user.getName()).thenReturn("totalLinesPerUserLastMonth");
		Mockito.when(mockNum_lines_per_user.getValue()).thenReturn(lines_per_user);

		// Creamos una instancia de la estrategia
		DiversityOfContributionsStrategy strategy = new DiversityOfContributionsStrategy();

		// Ejecutamos el método que queremos probar con los mocks como argumentos
		List<ReportItemI<HashMap<String, Double>>> metrics = Arrays.asList(mockNum_commits_per_user, mockNum_lines_per_user);
		ReportItemI<HashMap<String, Double>> result = strategy.calcIndicator(metrics);

		// Comprobamos que el resultado es el esperado
		Assertions.assertEquals("diversityOfContributions", result.getName());
		Assertions.assertEquals(0.112, result.getValue().get("entropyValue"), 0.01);
		Assertions.assertDoesNotThrow(() -> strategy.calcIndicator(metrics));

	}

	/*
	@Test
	public void testCalcIndicatorThrowsNotAvailableMetricException() {
		// Creamos los mocks necesarios
		ReportItemI<HashMap<String, Double>> mockNum_commits = Mockito.mock(ReportItemI.class);

		// Configuramos los mocks para devolver valores predefinidos
		//Mockito.when(mockNum_commits.getName()).thenReturn("totalCommitsLastMonth");
		//Mockito.when(mockNum_commits.getValue()).thenReturn(200.0);

		// Creamos una instancia de la estrategia
		DiversityOfContributionsStrategy strategy = new DiversityOfContributionsStrategy();

		// Ejecutamos el método que queremos probar con una sola métrica
		List<ReportItemI<?>> metrics = Arrays.asList(mockNum_commits);
		// Comprobamos que se lanza la excepción adecuada
		NotAvailableMetricException exception = Assertions.assertThrows(NotAvailableMetricException.class,
				() -> strategy.calcIndicator(metrics));

	}
	*/

	/*
	@Test
	public void testRequiredMetrics() {

		// Creamos una instancia de la estrategia
		DiversityOfContributionsStrategy strategy = new DiversityOfContributionsStrategy();

		// Ejecutamos el método que queremos probar
		List<String> requiredMetrics = strategy.requiredMetrics();

		// Comprobamos que el resultado es el esperado
		List<String> expectedMetrics = Arrays.asList("totalCommitsLastMonth", "commitsPerUserLastMonth", "totalLinesLastMonth", "linesPerUserLastMonth");
		Assertions.assertEquals(expectedMetrics, requiredMetrics);
	}
	*/
}