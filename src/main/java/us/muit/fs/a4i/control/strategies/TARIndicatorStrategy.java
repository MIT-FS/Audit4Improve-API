package us.muit.fs.a4i.control.strategies;

import us.muit.fs.a4i.control.IndicatorStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.Collection;

/**
 * Estrategia para calcular el indicador de tráfico de acceso a repositorios de GitHub.
 */
public class TARIndicatorStrategy implements IndicatorStrategy<Double> {
    private static final Logger log = Logger.getLogger(TARIndicatorStrategy.class.getName());

    private static final List<String> REQUIRED = List.of(
            "uniqueVisitors", "uniqueClones"
    );

    @Override
    public ReportItemI<Double> calcIndicator(List<ReportItemI<Double>> metrics)
            throws NotAvailableMetricException {
        log.info("Calculando indicador de tráfico GitHub con métricas: " + metrics);
        Map<String, Double> m = metrics.stream()
                .collect(Collectors.toMap(ReportItemI::getName, ReportItemI::getValue));

        for (String req : REQUIRED) {
            if (!m.containsKey(req)) {
                throw new NotAvailableMetricException("Falta métrica " + req);
            }
        }

        double visitors = m.get("uniqueVisitors");
        double clones = m.get("uniqueClones");

        if (visitors <= 0) {
            throw new NotAvailableMetricException("Número de visitantes debe ser mayor que cero.");
        }

        double conversionRate = clones / visitors;

        IndicatorState state = classify(visitors);

        try {
            return new ReportItem.ReportItemBuilder<Double>("GithubTraffic", conversionRate)
                    .metrics((Collection) metrics)
                    .indicator(state)
                    .build();
        } catch (ReportItemException e) {
            throw new NotAvailableMetricException("Error construyendo ReportItem: " + e.getMessage());
        }
    }

    @Override
    public List<String> requiredMetrics() {
        return REQUIRED;
    }

    private IndicatorState classify(double visitors) {
        if (visitors < 10) return IndicatorState.CRITICAL;
        if (visitors < 50) return IndicatorState.WARNING;
        return IndicatorState.OK;
    }
}
