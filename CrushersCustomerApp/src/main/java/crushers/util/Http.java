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
    public static HttpResponse<String> auth(String source, String key, String value) throws IOException, InterruptedException{
        String plainCredentials = key + ":" + value;
        String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
        String authorizationHeader = "Basic " + base64Credentials;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + source))
                .GET()
                .header("Authorization", authorizationHeader)
                .header("Content-Type", "application/json")
                .build();
        // Send HTTP request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

}
