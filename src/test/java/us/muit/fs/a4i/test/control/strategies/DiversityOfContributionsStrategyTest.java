package us.muit.fs.a4i.test.control.strategies;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import us.muit.fs.a4i.control.strategies.DiversityOfContributionsStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.model.entities.ReportItemI;

class DiversityOfContributionsStrategyTest {

	@Test
	void testCalcIndicatorWithBalancedContributions() throws NotAvailableMetricException {
		ReportItemI<HashMap<String, Double>> commitsMetric = createMetric(
				"totalCommitsPerUserLastYear",
				mapOf("Antonio", 10.0, "Manolo", 10.0)
		);

		ReportItemI<HashMap<String, Double>> linesMetric = createMetric(
				"totalLinesPerUserLastYear",
				mapOf("Antonio", 100.0, "Manolo", 100.0)
		);

		DiversityOfContributionsStrategy strategy = new DiversityOfContributionsStrategy();

		ReportItemI<HashMap<String, Double>> result = strategy.calcIndicator(Arrays.asList(commitsMetric, linesMetric));

		Assertions.assertEquals("diversityOfContributions", result.getName());
		Assertions.assertEquals(1.0, result.getValue().get("entropyValue"), 0.01);
	}

	@Test
	void testCalcIndicatorWithUnbalancedContributions() throws NotAvailableMetricException {
		ReportItemI<HashMap<String, Double>> commitsMetric = createMetric(
				"totalCommitsPerUserLastYear",
				mapOf("Antonio", 200.0, "Manolo", 4.0)
		);

		ReportItemI<HashMap<String, Double>> linesMetric = createMetric(
				"totalLinesPerUserLastYear",
				mapOf("Antonio", 10000.0, "Manolo", 10000.0)
		);

		DiversityOfContributionsStrategy strategy = new DiversityOfContributionsStrategy();

		ReportItemI<HashMap<String, Double>> result = strategy.calcIndicator(Arrays.asList(commitsMetric, linesMetric));

		Assertions.assertEquals("diversityOfContributions", result.getName());
		Assertions.assertEquals(0.822, result.getValue().get("entropyValue"), 0.01);
	}

	@Test
	void testCalcIndicatorWithZeros() throws NotAvailableMetricException {
		ReportItemI<HashMap<String, Double>> commitsMetric = createMetric(
				"totalCommitsPerUserLastYear",
				mapOf("Antonio", 0.0, "Manolo", 0.0)
		);

		ReportItemI<HashMap<String, Double>> linesMetric = createMetric(
				"totalLinesPerUserLastYear",
				mapOf("Antonio", 0.0, "Manolo", 0.0)
		);

		DiversityOfContributionsStrategy strategy = new DiversityOfContributionsStrategy();

		ReportItemI<HashMap<String, Double>> result = strategy.calcIndicator(Arrays.asList(commitsMetric, linesMetric));

		Assertions.assertEquals("diversityOfContributions", result.getName());
		Assertions.assertEquals(0.0, result.getValue().get("entropyValue"), 0.01);
	}

	@Test
	void testCalcIndicatorWithOnlyOneUser() throws NotAvailableMetricException {
		ReportItemI<HashMap<String, Double>> commitsMetric = createMetric(
				"totalCommitsPerUserLastYear",
				mapOf("Antonio", 10.0)
		);

		ReportItemI<HashMap<String, Double>> linesMetric = createMetric(
				"totalLinesPerUserLastYear",
				mapOf("Antonio", 100.0)
		);

		DiversityOfContributionsStrategy strategy = new DiversityOfContributionsStrategy();

		ReportItemI<HashMap<String, Double>> result = strategy.calcIndicator(Arrays.asList(commitsMetric, linesMetric));

		Assertions.assertEquals("diversityOfContributions", result.getName());
		Assertions.assertEquals(0.0, result.getValue().get("entropyValue"), 0.01);
	}

	@Test
	void testCalcIndicatorWithDifferentUsersInMetrics() throws NotAvailableMetricException {
		ReportItemI<HashMap<String, Double>> commitsMetric = createMetric(
				"totalCommitsPerUserLastYear",
				mapOf("Antonio", 10.0, "Manolo", 10.0)
		);

		ReportItemI<HashMap<String, Double>> linesMetric = createMetric(
				"totalLinesPerUserLastYear",
				mapOf("Antonio", 100.0)
		);

		DiversityOfContributionsStrategy strategy = new DiversityOfContributionsStrategy();

		Assertions.assertDoesNotThrow(() -> strategy.calcIndicator(Arrays.asList(commitsMetric, linesMetric)));

		ReportItemI<HashMap<String, Double>> result = strategy.calcIndicator(Arrays.asList(commitsMetric, linesMetric));

		Assertions.assertEquals("diversityOfContributions", result.getName());
		Assertions.assertFalse(Double.isNaN(result.getValue().get("entropyValue")));
		Assertions.assertFalse(Double.isInfinite(result.getValue().get("entropyValue")));
	}

	@Test
	void testCalcIndicatorThrowsNotAvailableMetricException() {
		ReportItemI<HashMap<String, Double>> commitsMetric = createMetric(
				"totalCommitsPerUserLastYear",
				mapOf("Antonio", 0.0)
		);

		DiversityOfContributionsStrategy strategy = new DiversityOfContributionsStrategy();

		List<ReportItemI<HashMap<String, Double>>> metrics = Arrays.asList(commitsMetric);

		Assertions.assertThrows(NotAvailableMetricException.class, () -> strategy.calcIndicator(metrics));
	}

	@Test
	void testRequiredMetrics() {
		DiversityOfContributionsStrategy strategy = new DiversityOfContributionsStrategy();

		List<String> requiredMetrics = strategy.requiredMetrics();

		List<String> expectedMetrics = Arrays.asList("totalCommitsPerUserLastYear", "totalLinesPerUserLastYear");
		Assertions.assertEquals(expectedMetrics, requiredMetrics);
	}

	private ReportItemI<HashMap<String, Double>> createMetric(String name, HashMap<String, Double> value) {
		ReportItemI<HashMap<String, Double>> metric = Mockito.mock(ReportItemI.class);

		Mockito.when(metric.getName()).thenReturn(name);
		Mockito.when(metric.getValue()).thenReturn(value);

		return metric;
	}

	private HashMap<String, Double> mapOf(String user1, Double value1) {
		HashMap<String, Double> map = new HashMap<>();
		map.put(user1, value1);
		return map;
	}

	private HashMap<String, Double> mapOf(String user1, Double value1, String user2, Double value2) {
		HashMap<String, Double> map = new HashMap<>();
		map.put(user1, value1);
		map.put(user2, value2);
		return map;
	}
}