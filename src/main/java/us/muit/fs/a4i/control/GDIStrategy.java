package us.muit.fs.a4i.control;

import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GDIStrategy implements IndicatorStrategy<Double> {

    private static final String METRIC_TOTAL = "issues_total";
    private static final String METRIC_ETIQUETADOS = "issues_etiquetados";
    private static final String RESULT_NAME = "grado_documentacion_issues";

    @Override
    public ReportItemI<Double> calcIndicator(List<ReportItemI<Double>> metrics) throws NotAvailableMetricException {
        Optional<ReportItemI<Double>> totalOpt = metrics.stream()
            .filter(m -> METRIC_TOTAL.equals(m.getName()))
            .findFirst();

        Optional<ReportItemI<Double>> etiquetadosOpt = metrics.stream()
            .filter(m -> METRIC_ETIQUETADOS.equals(m.getName()))
            .findFirst();

        if (totalOpt.isEmpty() || etiquetadosOpt.isEmpty()) {
            throw new NotAvailableMetricException("Faltan métricas necesarias para calcular el indicador.");
        }

        double total = totalOpt.get().getValue();
        double etiquetados = etiquetadosOpt.get().getValue();

        if (total == 0) {
            throw new RuntimeException("El valor de 'issues_total' no puede ser cero.");
        }

        double resultado = (etiquetados / total) * 100.0;

        try {
            // Intentamos crear el ReportItem y si algo falla, envolvemos la excepción.
            return new ReportItem.ReportItemBuilder<>(RESULT_NAME, resultado).build();
        } catch (ReportItemException e) {
            // Envolvemos la ReportItemException en una RuntimeException
            throw new RuntimeException("Error al crear el ReportItem: " + e.getMessage(), e);
        }
    }


    @Override
    public List<String> requiredMetrics() {
        return Arrays.asList(METRIC_TOTAL, METRIC_ETIQUETADOS);
    }
}
