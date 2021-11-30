package crushers.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Http {
    
    private static final String API_URL = "http://localhost:8080/";

    public static HttpClient client = HttpClient.newHttpClient();

    public static HttpResponse<String> get(String resource) throws IOException, InterruptedException{
        HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .header("accept", "application/json")
        .uri(URI.create(API_URL + resource))
        .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
    
    // public static <T> parse(){
    //     ObjectMapper mapper = new ObjectMapper();
    //     List<T> objects = 
    // }

}
