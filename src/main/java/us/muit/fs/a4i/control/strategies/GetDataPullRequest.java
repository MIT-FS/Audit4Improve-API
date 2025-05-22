package us.muit.fs.a4i.control.strategies;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;

public class GetDataPullRequest {

    private final HttpClient httpClient;

    public GetDataPullRequest() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public int getTotalPullRequests(String owner, String repo) throws IOException, InterruptedException {
        JSONArray prArray = fetchPullRequests(owner, repo, "all");
        return prArray.length();
    }

    public int getClosedPullRequests(String owner, String repo) throws IOException, InterruptedException {
        JSONArray prArray = fetchPullRequests(owner, repo, "closed");
        return prArray.length();
    }

    private JSONArray fetchPullRequests(String owner, String repo, String state) throws IOException, InterruptedException {
        String url = String.format("https://api.github.com/repos/%s/%s/pulls?state=%s&per_page=100", owner, repo, state);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/vnd.github.v3+json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("GitHub API error: " + response.statusCode());
        }

        return new JSONArray(response.body());
    }
}
