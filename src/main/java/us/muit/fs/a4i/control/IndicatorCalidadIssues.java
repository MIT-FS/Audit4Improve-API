package us.muit.fs.a4i.control;

import java.util.List;
import java.util.Arrays;

import us.muit.fs.a4i.exceptions.NotAvailableMetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.ReportItem;
import us.muit.fs.a4i.model.entities.ReportItemI;

/**
 * Estrategia para calcular el indicador de calidad de resolución.
 */
public class IndicatorCalidadIssues {

    private static final String MRI = "reopenedIssuesAvg";
    private static final String TRPI = "firstTryResolutionRate";
    private static final String IAPC = "postClosureActivityRate";
    private static final String RESULT_NAME = "calidadResolucion";

    /**
     * Calcula el indicador a partir de una lista de métricas.
     * 
     * @param metrics Lista de métricas
     * @return ReportItemI con el valor del indicador
     * @throws NotAvailableMetricException si faltan métricas necesarias
     */
    public ReportItemI<Double> calcIndicator(List<ReportItemI<Double>> metrics) throws NotAvailableMetricException {
        Double mriValue = null;
        Double trpiValue = null;
        Double iapcValue = null;

        for (ReportItemI<Double> item : metrics) {
            switch (item.getName()) {
                case MRI:
                    mriValue = item.getValue();
                    break;
                case TRPI:
                    trpiValue = item.getValue();
                    break;
                case IAPC:
                    iapcValue = item.getValue();
                    break;
            }
        }

        if (mriValue == null || trpiValue == null || iapcValue == null) {
            throw new NotAvailableMetricException("Faltan métricas requeridas para calcular el indicador.");
        }

        // Normalización del valor MRI
        double percMRI;
        if (mriValue <= 0.3) {
            percMRI = 0.0;
        } else if (mriValue >= 2.0) {
            percMRI = 100.0;
        } else {
            percMRI = (mriValue - 0.3) / 1.7 * 100.0;
        }

        // Cálculo de la calidad
        double quality = 0.3 * (100-percMRI) + 0.4 * trpiValue + 0.3 * (100.0 - iapcValue);
        

        try {
            return new ReportItem.ReportItemBuilder<>(RESULT_NAME, quality)
                    .source("auto")
                    .unit("%")
                    .build();
        } catch (ReportItemException e) {
            throw new RuntimeException("Error al construir ReportItem: " + e.getMessage(), e);
        }
    }

    /**
     * Devuelve la lista de métricas requeridas por este indicador.
     * 
     * @return lista de nombres de métricas requeridas
     */
    public List<String> requiredMetrics() {
        return Arrays.asList(MRI, TRPI, IAPC);
    }
}
