package us.muit.fs.a4i.model.entities.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItemI;

public class SimpleReport implements ReportI {

    private String entityId;
    private ReportType type;

    private Map<String, ReportItemI> metrics = new HashMap<>();
    private Map<String, ReportItemI> indicators = new HashMap<>();

    public SimpleReport(String entityId, ReportType type) {
        this.entityId = entityId;
        this.type = type;
    }

    @Override
    public ReportType getType() {
        return type;
    }

    @Override
    public String getEntityId() {
        return entityId;
    }

    @Override
    public ReportItemI getMetricByName(String name) {
        return metrics.get(name);
    }

    @Override
    public Collection<ReportItemI> getAllMetrics() {
        return metrics.values();
    }

    @Override
    public void addMetric(ReportItemI metric) {
    	metrics.put(metric.getName(), metric);
    }

    @Override
    public ReportItemI getIndicatorByName(String name) {
        return indicators.get(name);
    }

    @Override
    public Collection<ReportItemI> getAllIndicators() {
        return indicators.values();
    }

    @Override
    public void addIndicator(ReportItemI newIndicator) {
    	indicators.put(newIndicator.getName(), newIndicator);
    }

}