package crushers.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;


public class Http {
    
    private static final String BASE_URL = "http://localhost:8080/";

    public static HttpClient client = HttpClient.newHttpClient();

    public static HttpResponse<String> get(String source) throws IOException, InterruptedException{
        HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .header("accept", "application/json")
        .uri(URI.create(BASE_URL + source))
        .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public static HttpResponse<String> post(String source, Object body) throws IOException, InterruptedException{
        HttpRequest request = HttpRequest
        .newBuilder(URI.create(BASE_URL + source))
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(Json.stringify(body)))
        .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
    public static HttpResponse<String> authGet(String source, String token) throws IOException, InterruptedException{
        HttpRequest request = HttpRequest
        .newBuilder(URI.create(BASE_URL + source))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + token)
        .GET()
        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
    public static HttpResponse<String> authPost(String source, String token) throws IOException, InterruptedException{
        HttpRequest request = HttpRequest
        .newBuilder(URI.create(BASE_URL + source))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + token)
        .POST(BodyPublishers.ofString(""))
        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

}
