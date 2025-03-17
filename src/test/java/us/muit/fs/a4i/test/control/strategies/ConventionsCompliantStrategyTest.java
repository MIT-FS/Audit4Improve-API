package us.muit.fs.a4i.test.control.strategies;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.jupiter.api.Assertions;

import us.muit.fs.a4i.control.strategies.ConventionsCompliantStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.model.entities.ReportItemI;

public class ConventionsCompliantStrategyTest {
    // MARK: - Attributes
    // The indicator strategy to be tested
    static ConventionsCompliantStrategy repositoryIndicatorStrategy;
    // The expected calculation of the indicator
    static Double calculation;

    // MARK: - Setup methods
    /**
	 * @throws java.lang.Exception
	 */
    @SuppressWarnings("unchecked")
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
        // Create the indicator strategy, here we use the default weights
        repositoryIndicatorStrategy = new ConventionsCompliantStrategy();

        // Based on the provided metrics, the indicator should be calculated as follows:
        calculation = (0.2 * 0.86) + (0.2 * 0) + (0.2 * 0.92) + (0.2 * 0) + (0.2 * 0.98);
	}

    // MARK: - Test methods
    /**
     * The indicator is calculated based on the following metrics:
        * Metric 1: Ratio of commits complying with summary naming conventions (conventional commits). Must be above 85%.

        * Metric 2: Ratio of commits that have both a summary and a description. Must be above 85%.
        * Metric 3: Ratio of issues that have at least one label, compared to the total amount of issues. Must be above 85%.
        * Metric 4: The ratio of all branches that follow the correct naming conventions. Must be a 100% compliance.
        * Metric 5: Ratio of pull requests following correct naming and description conventions. Must be above 85%.
        * 
        * Where RepoIndicator = w1 * C1 + w2 * C2 + w3 * C3 + w4 * C4 + w5 * C5, and:
        * C_i = M_i if M_i >= 0.85 else 0 when i in {1, 2, 3, 5}
        * C_4 = 1 if M_4 == 1 else 0
     * @throws NotAvailableMetricException
     */
    @Test
    public void testCalcIndicator() throws NotAvailableMetricException {
        // Mock the required metrics
        ReportItemI<Double> mockConventionalCommits = Mockito.mock(ReportItemI.class);
        ReportItemI<Double> mockCommitsWithDescription = Mockito.mock(ReportItemI.class);
        ReportItemI<Double> mockIssuesWithLabels = Mockito.mock(ReportItemI.class);
        ReportItemI<Double> mockGitFlowBranches = Mockito.mock(ReportItemI.class);
        ReportItemI<Double> mockConventionalPullRequests = Mockito.mock(ReportItemI.class);

        // Mock the metric values
        when(mockConventionalCommits.getValue()).thenReturn(0.86);
        when(mockCommitsWithDescription.getValue()).thenReturn(0.84);
        when(mockIssuesWithLabels.getValue()).thenReturn(0.92);
        when(mockGitFlowBranches.getValue()).thenReturn(0.85);
        when(mockConventionalPullRequests.getValue()).thenReturn(0.98);
        // Mock the metric names
        when(mockConventionalCommits.getName()).thenReturn("conventionalCommits");
        when(mockCommitsWithDescription.getName()).thenReturn("commitsWithDescription");
        when(mockIssuesWithLabels.getName()).thenReturn("issuesWithLabels");
        when(mockGitFlowBranches.getName()).thenReturn("gitFlowBranches");
        when(mockConventionalPullRequests.getName()).thenReturn("conventionalPullRequests");
        // Set up the list of metrics
        List<ReportItemI<Double>> metrics = Arrays.asList(
            mockConventionalCommits,
            mockCommitsWithDescription,
            mockIssuesWithLabels,
            mockGitFlowBranches,
            mockConventionalPullRequests
        );

        // Calculate the indicator output
        assertDoesNotThrow(() -> {
            repositoryIndicatorStrategy.calcIndicator(metrics);
        }, "The calculation threw an exception, which it should not.");

        ReportItemI<Double> result = repositoryIndicatorStrategy.calcIndicator(metrics);

        assertNotNull(result, "The indicator result is null.");

        // Check the result
        Assertions.assertEquals(ConventionsCompliantStrategy.ID, 
        result.getName(), 
        "The indicator name is not correct, expected: " + ConventionsCompliantStrategy.ID + ", but was: " + result.getName());
        
        Assertions.assertEquals(calculation, result.getValue(), "The indicator value from the calculation is not correct.");
    }

    /**
     * Test the calculation of the indicator when the required metrics are not available
     */
    @Test
    public void testCalcIndicatorThrowsNotAvailableMetricException() {
        // Mock the required metrics
        ReportItemI<Double> mockConventionalCommits = Mockito.mock(ReportItemI.class);
        ReportItemI<Double> mockCommitsWithDescription = Mockito.mock(ReportItemI.class);
        ReportItemI<Double> mockIssuesWithLabels = Mockito.mock(ReportItemI.class);
        ReportItemI<Double> mockConventionalPullRequests = Mockito.mock(ReportItemI.class);

        // Mock the metric values
        when(mockConventionalCommits.getValue()).thenReturn(0.86);
        when(mockCommitsWithDescription.getValue()).thenReturn(0.84);
        when(mockIssuesWithLabels.getValue()).thenReturn(0.92);
        when(mockConventionalPullRequests.getValue()).thenReturn(0.98);
        // Mock the metric names
        when(mockConventionalCommits.getName()).thenReturn("conventionalCommits");
        when(mockCommitsWithDescription.getName()).thenReturn("commitsWithDescription");
        when(mockIssuesWithLabels.getName()).thenReturn("issuesWithLabels");
        when(mockConventionalPullRequests.getName()).thenReturn("conventionalPullRequests");
        // Set up the list of metrics
        List<ReportItemI<Double>> metrics = Arrays.asList(
            mockConventionalCommits,
            mockCommitsWithDescription,
            mockIssuesWithLabels,
            mockConventionalPullRequests
        );

        // Check that the method throws the expected exception
        Assertions.assertThrows(NotAvailableMetricException.class, () -> {
            repositoryIndicatorStrategy.calcIndicator(metrics);
        });
    }

    /**
     * Test the required metrics for the indicator calculation
     */
    @Test
    public void testRequiredMetrics() {
        List<String> expectedRequirements = Arrays.asList("conventionalCommits", "commitsWithDescription", "issuesWithLabels", "gitFlowBranches", "conventionalPullRequests");

        for (String requirement : expectedRequirements) {
            Assertions.assertTrue(repositoryIndicatorStrategy.requiredMetrics().contains(requirement),
                    "The required metrics for RepositoryIndicatorStrategyTest did not contain the following expected metric: " + requirement);
        }
    }
}
