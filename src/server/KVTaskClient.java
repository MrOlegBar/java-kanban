package server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String apiToken;
    HttpClient client = HttpClient.newHttpClient();
    URI url;
    KVServer kVServer = new KVServer();

    //URI url = URI.create("http://localhost:8081/register");

    KVTaskClient(URI url) throws IOException, InterruptedException {
        this.url = url;
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        this.apiToken = response.body();
    }

    public String getApiToken() {
        return apiToken;
    }

    void put(String key, String json) { //POST /save/<ключ>?API_TOKEN=
        URI url = URI.create(this.url.getHost() + "/save/" + key + "?API_TOKEN=" + apiToken);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(url)
                .build();
    }

    String load(String key) throws IOException, InterruptedException { //GET /load/<ключ>?API_TOKEN=.
        URI url = URI.create(this.url.getHost() + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
