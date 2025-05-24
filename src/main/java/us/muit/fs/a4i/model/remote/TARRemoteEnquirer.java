package us.muit.fs.a4i.model.remote;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import us.muit.fs.a4i.exceptions.MetricException;
import us.muit.fs.a4i.exceptions.ReportItemException;
import us.muit.fs.a4i.model.entities.ReportI;
import us.muit.fs.a4i.model.entities.ReportItem;

public class TARRemoteEnquirer extends GitHubEnquirer<String> {

    private static final Logger log = Logger.getLogger(TARRemoteEnquirer.class.getName());
    private static final String GITHUB_API_BASE = "https://api.github.com";

    public TARRemoteEnquirer() {
        super();
    }

    @Override
    public ReportItem getMetric(String metricName, String repoFullName) throws MetricException {
        if ("uniqueVisitorsLastDay".equals(metricName)) {
            return getUniqueVisitors(repoFullName);
        } else if ("uniqueClonesLastDay".equals(metricName)) {
            return getUniqueClones(repoFullName);
        } else if ("cloneConversionRate".equals(metricName)) {
            return getCloneConversionRate(repoFullName);
        } else {
            throw new MetricException("Métrica desconocida: " + metricName);
        }
    }

    private ReportItem getUniqueVisitors(String repoFullName) {
        try {
            String json = callGitHubAPI("/repos/" + repoFullName + "/traffic/visitors");
            int uniques = extractIntFromJson(json, "\"uniques\":");
            ReportItem.ReportItemBuilder builder = new ReportItem.ReportItemBuilder("uniqueVisitorsLastDay", Integer.valueOf(uniques));
            builder.source("GitHub REST API");
            builder.unit("Visitantes/día");
            return builder.build();
        } catch (Exception e) {
            log.severe("Error obteniendo visitantes: " + e.getMessage());
            throw new MetricException("No se pudo obtener visitantes únicos.");
        }
    }

    private ReportItem getUniqueClones(String repoFullName) {
        try {
            String json = callGitHubAPI("/repos/" + repoFullName + "/traffic/clones");
            int uniques = extractIntFromJson(json, "\"uniques\":");
            ReportItem.ReportItemBuilder builder = new ReportItem.ReportItemBuilder("uniqueClonesLastDay", Integer.valueOf(uniques));
            builder.source("GitHub REST API");
            builder.unit("Clones/día");
            return builder.build();
        } catch (Exception e) {
            log.severe("Error obteniendo clones: " + e.getMessage());
            throw new MetricException("No se pudo obtener clones únicos.");
        }
    }

    private ReportItem getCloneConversionRate(String repoFullName) {
        try {
            int visitors = ((Integer) getUniqueVisitors(repoFullName).getValue()).intValue();
            int clones = ((Integer) getUniqueClones(repoFullName).getValue()).intValue();
            double rate = (visitors > 0) ? ((double) clones / visitors) * 100.0 : 0.0;

            ReportItem.ReportItemBuilder builder = new ReportItem.ReportItemBuilder("cloneConversionRate", Double.valueOf(rate));
            builder.source("GitHub REST API");
            builder.unit("%");
            return builder.build();
        } catch (Exception e) {
            throw new MetricException("No se pudo calcular la tasa de conversión.");
        }
    }

    private String callGitHubAPI(String endpoint) throws Exception {
        String token = System.getProperty("github.token");
        if (token == null || token.isEmpty()) {
            throw new MetricException("Token de GitHub no encontrado. Asegúrate de pasarlo con -Dgithub.token=...");
        }

        URL url = new URL(GITHUB_API_BASE + endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "token " + token);
        con.setRequestProperty("Accept", "application/vnd.github.v3+json");
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);

        int status = con.getResponseCode();
        if (status != 200) {
            throw new MetricException("Error en la conexión con GitHub. Código: " + status);
        }

        InputStream in = con.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        con.disconnect();

        return response.toString();
    }

    private int extractIntFromJson(String json, String key) {
        int index = json.indexOf(key);
        if (index == -1) return 0;
        int start = index + key.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        String number = json.substring(start, end).trim();
        return Integer.parseInt(number);
    }

    @Override
    public ReportI buildReport(String entityId) {
        return null; // opcional
    }

    @Override
    public RemoteType getRemoteType() {
        return RemoteType.GITHUB;
    }
}
