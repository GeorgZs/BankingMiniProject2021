package crushers.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;


public class Http {
    
    private static final String BASE_URL = "http://localhost:8080/";

    public static HttpClient client = HttpClient.newHttpClient();

    public static String get(String source) throws IOException, InterruptedException{
        HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .header("accept", "application/json")
        .uri(URI.create(BASE_URL + source))
        .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String post(String source, Object body) throws IOException, InterruptedException{
        HttpRequest request = HttpRequest
        .newBuilder(URI.create(BASE_URL + source))
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(Json.stringify(body)))
        .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static String authGet(String source, String token) throws IOException, InterruptedException{
        HttpRequest request = HttpRequest
        .newBuilder(URI.create(BASE_URL + source))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + token)
        .GET()
        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static String authPost(String source, String token, Object body) throws IOException, InterruptedException{
        HttpRequest request = HttpRequest
        .newBuilder(URI.create(BASE_URL + source))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + token)
        .POST(BodyPublishers.ofString(Json.stringify(body)))
        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public <Type> Type put(String endpoint, Object body, Class<Type> type) throws Exception {
        final HttpRequest req = HttpRequest
                .newBuilder(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .PUT(BodyPublishers.ofString(Json.stringify(body)))
                .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return Json.parse(res.body(), type);
    }

    public <Type> Type authPut(String endpoint, Object body, Class<Type> type, String authToken) throws Exception {
        final HttpRequest req = HttpRequest
                .newBuilder(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .PUT(BodyPublishers.ofString(Json.stringify(body)))
                .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return Json.parse(res.body(), type);
    }

}
