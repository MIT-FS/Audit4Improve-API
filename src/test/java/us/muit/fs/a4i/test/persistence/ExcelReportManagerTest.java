/**
 * <p>
 * Test para probar la clase ExcelReportManager
 * </p>
 * 
 * @author Iván Matas González
 *
 */
package us.muit.fs.a4i.test.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;


import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.model.entities.Metric;
import us.muit.fs.a4i.persistence.ReportFormaterI;
import us.muit.fs.a4i.model.entities.Indicator;
import us.muit.fs.a4i.persistence.*; 

@ExtendWith(MockitoExtension.class)

class ExcelReportManager {
	private static Logger log = Logger.getLogger(ExcelReportManager.class.getName());

	@Captor
	private ArgumentCaptor<Integer> intCaptor;
	@Captor
	private ArgumentCaptor<String> strCaptor;
	@Captor
	private ArgumentCaptor<ReportFormaterI> FormaterCaptor;
	
	@Mock(serializable = true)
	private static Metric<Integer> metricIntMock= Mockito.mock(Metric.class);
	@Mock(serializable = true)
	private static Metric<String> metricStrMock= Mockito.mock(Metric.class);
	
	@Test
	void ExcelCreation() {
		
	}


}