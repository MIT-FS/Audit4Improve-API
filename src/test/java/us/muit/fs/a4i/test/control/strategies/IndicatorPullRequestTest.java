package us.muit.fs.a4i.test.control.strategies;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IndicatorPullRequestTest {

    IndicatorPullRequest indicator = new IndicatorPullRequest();

    @Test
    public void testEfficiencyCalculationSimple() {
        double efficiency = indicator.calculateEfficiency(75, 100);
        assertEquals(75.0, efficiency);
    }

    @Test
    public void testQualityLevelCorrecto() {
        String level = indicator.evaluateQualityLevel(80.0);
        assertEquals("Correcto", level);
    }

    @Test
    public void testQualityLevelPrecaucion() {
        String level = indicator.evaluateQualityLevel(60.0);
        assertEquals("Precaución", level);
    }

    @Test
    public void testQualityLevelCritico() {
        String level = indicator.evaluateQualityLevel(40.0);
        assertEquals("Crítico", level);
    }

    @Test
    public void testZeroTotalPRs() {
        double efficiency = indicator.calculateEfficiency(0, 0);
        assertEquals(0.0, efficiency);
    }

    @Test
    public void testMoreClosedThanTotal() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            indicator.calculateEfficiency(110, 100);
        });
        assertEquals("Closed PRs cannot exceed total PRs", exception.getMessage());
    }

    @Test
    public void testNegativeInputs() {
        assertThrows(IllegalArgumentException.class, () -> {
            indicator.calculateEfficiency(-1, 100);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            indicator.calculateEfficiency(10, -100);
        });
    }

    @Test
    public void testPerfectScore() {
        double efficiency = indicator.calculateEfficiency(10, 10);
        assertEquals(100.0, efficiency);
        assertEquals("Correcto", indicator.evaluateQualityLevel(efficiency));
    }
}
