package crushers;

import java.net.URI;
import java.util.List;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class HTTPUtils {
    public static String BASE_URL = "http://localhost:8080";
    protected static HttpClient http = HttpClient.newHttpClient();
    protected static String APPLICATION_JSON = "application/json";

    public static <Type>List<Type> getCollection(String endpoint, Class<Type> responseType) throws Exception{
        final HttpRequest req = HttpRequest
                .newBuilder(URI.create(BASE_URL + endpoint))
                .GET()
                .build();
        HttpResponse<String> res = http.send(req, BodyHandlers.ofString());
        return Json.parseList(res.body(), responseType);
    }

    public static <Type> Type get(String endpoint, Class<Type> responseType) throws Exception{
        final HttpRequest req = HttpRequest
                .newBuilder(URI.create(BASE_URL + endpoint))
                .GET()
                .build();
        HttpResponse<String> res = http.send(req, BodyHandlers.ofString());
        return Json.parse(res.body(), responseType);
    }

    public static <Type> Type post(String endpoint, Object body, Class<Type> responseType) throws Exception {
        final HttpRequest req = HttpRequest
                .newBuilder(URI.create(BASE_URL + endpoint))
                .header("Content-Type", APPLICATION_JSON)
                .POST(BodyPublishers.ofString(Json.stringify(body)))
                .build();
        HttpResponse<String> res = http.send(req, BodyHandlers.ofString());
        System.out.println(res.statusCode());
        System.out.println(res.body());
        return Json.parse(res.body(), responseType);
    }
}
