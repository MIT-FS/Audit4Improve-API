package us.muit.fs.a4i.control;

import java.util.Arrays;
import java.util.List;

import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.model.entities.impl.SimpleReportItem;

public class IndicatorStrategyEquipo4 implements IndicatorStrategy<Double> {

    private static final String M1 = "issue_response_time";
    private static final String M2 = "issues_without_response_ratio";
    private static final String M3 = "resolved_under_48h_ratio";
    private static final String INDICATOR_NAME = "issue_management_speed";

    @Override
    public ReportItemI<Double> calcIndicator(List<ReportItemI<Double>> metrics) throws NotAvailableMetricException {
        Double m1 = null, m2 = null, m3 = null;

        for (ReportItemI<Double> metric : metrics) {
            switch (metric.getName()) {
                case M1:
                    m1 = metric.getValue();
                    break;
                case M2:
                    m2 = metric.getValue();
                    break;
                case M3:
                    m3 = metric.getValue();
                    break;
            }
        }

        if (m1 == null || m2 == null || m3 == null) {
            throw new NotAvailableMetricException("Faltan métricas necesarias para calcular el indicador.");
        }

        double resultado = (m1 + m2 + m3) / 3.0;
        return new SimpleReportItem(INDICATOR_NAME, resultado);
    }

    @Override
    public List<String> requiredMetrics() {
        return Arrays.asList(M1, M2, M3);
    }
}