package us.muit.fs.a4i.test.model.remote;

import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.remote.GitHubDeveloperEnquirer;

class GitHubDeveloperEnquirerTest {
	private static Logger log = Logger.getLogger(GitHubOrganizationEnquirerTest.class.getName());
	GitHubDeveloperEnquirer ghEnquirer = new GitHubDeveloperEnquirer();

	/**
	 * Test method for GitHubOrganizationEnquirer
	 * 
	 * @throws MetricException
	 * @throws ReportItemException
	 */
	@Test
	void testAssignedIssuesLastMonth() throws MetricException {
		ReportItem<Integer> metric = ghEnquirer.getMetric("closedIssuesLastMonth", "Isabel-Roman");
		assertEquals(metric.getName(), "closedIssuesLastMonth");
		log.info(metric.getValue().toString());
		log.info(metric.getDescription());
	}

}
