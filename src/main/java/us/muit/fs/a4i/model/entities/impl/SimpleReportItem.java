package us.muit.fs.a4i.model.entities.impl;

import us.muit.fs.a4i.model.entities.ReportItemI;
import us.muit.fs.a4i.model.entities.IndicatorI;

import java.util.Date;

public class SimpleReportItem implements ReportItemI<Double> {

    private String name;
    private Double value;
    private Date date;
    private String description;
    private String source;
    private String unit;
    private IndicatorI indicator;

    public SimpleReportItem(String name, Double value) {
        this.name = name;
        this.value = value;
        this.date = new Date(); // asignamos la fecha actual por defecto
    }

    public SimpleReportItem(String name, Double value, String description, String source, String unit) {
        this.name = name;
        this.value = value;
        this.description = description;
        this.source = source;
        this.unit = unit;
        this.date = new Date();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public IndicatorI getIndicator() {
        return indicator;
    }

    // Setter si necesitamos añadir el indicador más tarde
    public void setIndicator(IndicatorI indicator) {
        this.indicator = indicator;
    }
}