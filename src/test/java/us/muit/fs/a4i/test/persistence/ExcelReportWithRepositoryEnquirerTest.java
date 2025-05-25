package us.muit.fs.a4i.test.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import us.muit.fs.a4i.model.remote.GitHubRepositoryEnquirer;
import us.muit.fs.a4i.persistence.ExcelReportManager;
import us.muit.fs.a4i.persistence.ReportFormater;

class ExcelReportWithRepositoryEnquirerTest {
	private static Logger log = Logger.getLogger(ExcelReportWithRepositoryEnquirerTest.class.getName());

	@Test
	void testSaveRepositoryReport() {
		String excelPath = new String("src" + File.separator + "test" + File.separator + "resources" + File.separator);
		String excelName = new String("excelTest.xlsx");
		String repoName = new String("MIT-FS/Audit4Improve-API");
		ExcelReportManager underTest = new ExcelReportManager(excelPath, excelName);

		underTest.setFormater(new ReportFormater());

		GitHubRepositoryEnquirer ghEnquirer = new GitHubRepositoryEnquirer();

		underTest.saveReport(ghEnquirer.buildReport(repoName));
	}

}
