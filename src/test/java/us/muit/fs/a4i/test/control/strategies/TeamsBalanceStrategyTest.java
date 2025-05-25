package us.muit.fs.a4i.test.control.strategies;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;

import us.muit.fs.a4i.control.strategies.TeamsBalanceStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;

import us.muit.fs.a4i.model.entities.ReportItemI;

public class TeamsBalanceStrategyTest {
    private static Logger log = Logger.getLogger(TeamsBalanceStrategyTest.class.getName());

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

    /**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}


	    @Test
	    public void testTeamsBalance() throws NotAvailableMetricException {
	        // Creamos los mocks necesarios
	        ReportItemI<Double> mockTeamBalance = Mockito.mock(ReportItemI.class);
	        ReportItemI<Double> mockRepositories = Mockito.mock(ReportItemI.class);
			
	        // Configuramos los mocks para devolver valores predefinidos
	        Mockito.when(mockTeamBalance.getName()).thenReturn("teamsBalance");
	        Mockito.when(mockTeamBalance.getValue()).thenReturn(1.0);

	        Mockito.when(mockRepositories.getName()).thenReturn("repositories");
	        Mockito.when(mockRepositories.getValue()).thenReturn(5.0);

	        // Creamos una instancia de IssuesRatioIndicator
	        TeamsBalanceStrategy indicator = new TeamsBalanceStrategy();

	        // Ejecutamos el método que queremos probar con los mocks como argumentos
	        List<ReportItemI<Double>> metrics = Arrays.asList(mockTeamBalance, mockRepositories);
			log.info("MockteamBalance: " + mockTeamBalance);
			log.info("MockRepositories: " + mockRepositories);
			log.info("Metrics: " + metrics);
	        ReportItemI<Double> result = indicator.calcIndicator(metrics);

	        // Comprobamos que el resultado es el esperado
			log.info("Result: " + result);
	        Assertions.assertEquals("teamsBalance", result.getName());
	        Assertions.assertEquals(20.0, result.getValue());
	        Assertions.assertDoesNotThrow(()->indicator.calcIndicator(metrics));
	    }




}

