package us.muit.fs.a4i.control.strategies;

import us.muit.fs.a4i.control.IndicatorStrategy;
import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.IndicatorI.IndicatorState;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.Collection;


/**
 * Estrategia para calcular el Índice de Eficiencia del Flujo (IEF).
 */
public class IEFStrategy implements IndicatorStrategy<Double> {
    private static final Logger log = Logger.getLogger(IEFStrategy.class.getName());

    private static final List<String> REQUIRED = List.of(
            "cycleTime", "waitTime", "throughput", "WIP"
    );

    private final double w1 = 0.25, w2 = 0.25, w3 = 0.25, w4 = 0.25;
    private final double maxC = 160.0, maxW = 80.0, maxT = 50.0, maxP = 20.0;

    @Override
    public ReportItemI<Double> calcIndicator(List<ReportItemI<Double>> metrics)
            throws NotAvailableMetricException {
        log.info("Calculando IEF con métricas: " + metrics);
        Map<String, Double> m = metrics.stream()
                .collect(Collectors.toMap(ReportItemI::getName, ReportItemI::getValue));

        for (String req : REQUIRED) {
            if (!m.containsKey(req)) {
                throw new NotAvailableMetricException("Falta métrica " + req);
            }
        }

        double cycle = m.get("cycleTime"),
                wait  = m.get("waitTime"),
                thr   = m.get("throughput"),
                wip   = m.get("WIP");

        double normC = clamp(cycle / maxC),
                normW = clamp(wait  / maxW),
                normT = clamp(thr   / maxT),
                normP = clamp(wip   / maxP);

        double score = w1 * (1 - normC)
                + w2 * (1 - normW)
                + w3 * normT
                + w4 * (1 - normP);

        IndicatorState state = classify(score);

        try {
            return new ReportItem.ReportItemBuilder<Double>("IEF", score)
                .metrics((Collection) metrics)
                .indicator(state)
                .build();
        } catch (ReportItemException e) {
            throw new NotAvailableMetricException("Error building IEF ReportItem: " + e.getMessage());
        }
    }

    @Override
    public List<String> requiredMetrics() {
        return REQUIRED;
    }

    private double clamp(double v) {
        return v < 0 ? 0 : (v > 1 ? 1 : v);
    }

    private IndicatorState classify(double x) {
        if (x >= 0.8)      return IndicatorState.OK;
        if (x >= 0.6)      return IndicatorState.OK;
        if (x >= 0.4)      return IndicatorState.WARNING;
        return IndicatorState.CRITICAL;
    }
}
