package crushers.api;

import java.net.URI;
import java.util.List;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class Http {
  // Singleton pattern
  public final static Http instance = new Http();

  public final String BASE_URL = "http://localhost:8080";
  private final String APPLICATION_JSON = "application/json";
  private final String AUTH_TYPE = "Bearer ";
  private HttpClient http;
  
  private Http() {
    http = HttpClient.newHttpClient();
  }

  /**
   * For http GET requests - parses objects
   */
  public <Type> Type get(String endpoint, Class<Type> responseType) throws Exception{
    final HttpRequest req = HttpRequest
      .newBuilder(URI.create(BASE_URL + endpoint))
      .GET()
      .build();

    HttpResponse<String> res = http.send(req, BodyHandlers.ofString());

    if (res.statusCode() < 200 || res.statusCode() >= 300) {
      HttpError error = Json.instance.parse(res.body(), HttpError.class);
      error.setStatusCode(res.statusCode());
      throw error;
    }

    return Json.instance.parse(res.body(), responseType);
  }

  /**
   * For http GET requests - parses lists
   */
  public <Type> List<Type> getList(String endpoint, Class<Type> responseType) throws Exception{
    final HttpRequest req = HttpRequest
      .newBuilder(URI.create(BASE_URL + endpoint))
      .GET()
      .build();

    HttpResponse<String> res = http.send(req, BodyHandlers.ofString());

    if (res.statusCode() < 200 || res.statusCode() >= 300) {
      HttpError error = Json.instance.parse(res.body(), HttpError.class);
      error.setStatusCode(res.statusCode());
      throw error;
    }

    return Json.instance.parseList(res.body(), responseType);
  }

  /**
   * For http POST requests - parses objects with their id
   * 
   * creates new data
   */
  public <Type> Type post(String endpoint, Object body, Class<Type> type) throws Exception {
    final HttpRequest req = HttpRequest
      .newBuilder(URI.create(BASE_URL + endpoint))
      .header("Content-Type", APPLICATION_JSON)
      .POST(BodyPublishers.ofString(Json.instance.stringify(body, type)))
      .build();
      
    HttpResponse<String> res = http.send(req, BodyHandlers.ofString());

    if (res.statusCode() < 200 || res.statusCode() >= 300) {
      HttpError error = Json.instance.parse(res.body(), HttpError.class);
      error.setStatusCode(res.statusCode());
      throw error;
    }

    return Json.instance.parse(res.body(), type);
  }

  /**
   * For http POST requests - parses objects with their id
   * 
   * @param requestType - type of the data we send to the server
   * @param responseType - type of the data we get back from the server
   * 
   * creates new data
   */
  public <ReqType, ResType> ResType post(String endpoint, Object body, Class<ReqType> requestType, Class<ResType> responseType) throws Exception {
    final HttpRequest req = HttpRequest
      .newBuilder(URI.create(BASE_URL + endpoint))
      .header("Content-Type", APPLICATION_JSON)
      .POST(BodyPublishers.ofString(Json.instance.stringify(body, requestType)))
      .build();

    HttpResponse<String> res = http.send(req, BodyHandlers.ofString());

    if (res.statusCode() < 200 || res.statusCode() >= 300) {
      HttpError error = Json.instance.parse(res.body(), HttpError.class);
      error.setStatusCode(res.statusCode());
      throw error;
    }

    return Json.instance.parse(res.body(), responseType);
  }

  /**
   * For http PUT requests - parses objects with their id
   * 
   * updates existing data
   */
  public <Type> Type put(String endpoint, Object body, Class<Type> type) throws Exception {
    final HttpRequest req = HttpRequest
      .newBuilder(URI.create(BASE_URL + endpoint))
      .header("Content-Type", APPLICATION_JSON)
      .PUT(BodyPublishers.ofString(Json.instance.stringify(body, type)))
      .build();

    HttpResponse<String> res = http.send(req, BodyHandlers.ofString());
    return Json.instance.parse(res.body(), type);
  }



  /**
   * For http GET requests - parses objects - logged in
   */
  public <Type> Type get(String endpoint, Class<Type> responseType, String authToken) throws Exception{
    final HttpRequest req = HttpRequest
      .newBuilder(URI.create(BASE_URL + endpoint))
      .header("Authorization", AUTH_TYPE + authToken)
      .GET()
      .build();

    HttpResponse<String> res = http.send(req, BodyHandlers.ofString());

    if (res.statusCode() < 200 || res.statusCode() >= 300) {
      HttpError error = Json.instance.parse(res.body(), HttpError.class);
      error.setStatusCode(res.statusCode());
      throw error;
    }

    return Json.instance.parse(res.body(), responseType);
  }


  /**
   * For http GET requests - parses lists - logged in
   */
  public <Type> List<Type> getList(String endpoint, Class<Type> responseType, String authToken) throws Exception{
    final HttpRequest req = HttpRequest
      .newBuilder(URI.create(BASE_URL + endpoint))
      .header("Authorization", AUTH_TYPE + authToken)
      .GET()
      .build();

    HttpResponse<String> res = http.send(req, BodyHandlers.ofString());

    if (res.statusCode() < 200 || res.statusCode() >= 300) {
      HttpError error = Json.instance.parse(res.body(), HttpError.class);
      error.setStatusCode(res.statusCode());
      throw error;
    }

    return Json.instance.parseList(res.body(), responseType);
  }

  /**
   * For http POST requests - parses objects with their id - logged in
   */
  public <Type> Type post(String endpoint, Object body, Class<Type> type, String authToken) throws Exception {
    final HttpRequest req = HttpRequest
      .newBuilder(URI.create(BASE_URL + endpoint))
      .header("Content-Type", APPLICATION_JSON)
      .header("Authorization", AUTH_TYPE + authToken)
      .POST(BodyPublishers.ofString(Json.instance.stringify(body, type)))
      .build();

    HttpResponse<String> res = http.send(req, BodyHandlers.ofString());

    if (res.statusCode() < 200 || res.statusCode() >= 300) {
      HttpError error = Json.instance.parse(res.body(), HttpError.class);
      error.setStatusCode(res.statusCode());
      throw error;
    }

    return Json.instance.parse(res.body(), type);
  }

  /**
   * For http PUT requests - parses objects with their id - logged in
   * 
   * updates existing data
   */
  public <Type> Type put(String endpoint, Object body, Class<Type> type, String authToken) throws Exception {
    final HttpRequest req = HttpRequest
      .newBuilder(URI.create(BASE_URL + endpoint))
      .header("Content-Type", APPLICATION_JSON)
      .header("Authorization", AUTH_TYPE + authToken)
      .PUT(BodyPublishers.ofString(Json.instance.stringify(body, type)))
      .build();

    HttpResponse<String> res = http.send(req, BodyHandlers.ofString());

    if (res.statusCode() < 200 || res.statusCode() >= 300) {
      HttpError error = Json.instance.parse(res.body(), HttpError.class);
      error.setStatusCode(res.statusCode());
      throw error;
    }
    
    return Json.instance.parse(res.body(), type);
  }

}
