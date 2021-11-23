package crushers.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.sun.net.httpserver.*;

import crushers.server.httpExceptions.HttpException;
import crushers.server.httpExceptions.MethodNotAllowedException;
import crushers.utils.Json;

/**
 * This is a base router which handles some of the wiring for differentiating between common crud 
 * endpoints. This allows us to overwrite some simplified methods without having to worry about the 
 * wiring itself. It also allows for a base path to be configured under which all the endpoints are 
 * available.
 */
public class Router<Type> {

  private String basePath;
  
  public Router(String basePath) {
    this.basePath = basePath;
  }
  
  /**
   * Adds all the endpoints to the given http server. It can be overwritten to add more customised 
   * endpoints in addition to the prewired crud endpoints.
   */
  public void addEndpoints(HttpServer server) {
    addCrudEndpoints(server);
  }

  /**
   * A prewired crud endpoint for getting a single item.
   */
  protected void get(HttpExchange exchange, int id) throws Exception {
    throw new MethodNotAllowedException();
  }

  /**
   * A prewired crud endpoint for getting all items.
   */
  protected void getAll(HttpExchange exchange) throws Exception {
    throw new MethodNotAllowedException();
  }

  /**
   * A prewired crud endpoint for creating a new item.
   */
  protected void post(HttpExchange exchange) throws Exception {
    throw new MethodNotAllowedException();
  }

  /**
   * A prewired crud endpoint for updating an existing item.
   */
  protected void put(HttpExchange exchange, int id) throws Exception {
    throw new MethodNotAllowedException();
  }

  /**
   * A prewired crud endpoint for deleting an existing item.
   */
  protected void delete(HttpExchange exchange, int id) throws Exception {
    throw new MethodNotAllowedException();
  }

  /**
   * Adds the wiring for the crud endpoints to the given http server.
   */
  private void addCrudEndpoints(HttpServer server) {
    HttpContext collectionEndpoint = server.createContext(this.basePath);
    collectionEndpoint.setHandler(this::collectionEndpointHandler);

    HttpContext itemEndpoint = server.createContext(this.basePath + "/");
    itemEndpoint.setHandler(this::itemEndpointHandler);
  }

  /**
   * Sends a json response with the default success status code depending on the request method.
   */
  final protected void sendJsonResponse(HttpExchange exchange, Object responseData) throws IOException {
    byte[] response = Json.instance.stringify(responseData).getBytes();
    int statusCode = 200;

    if (exchange.getRequestMethod().equals("POST")) {
      statusCode = 201;
    }
    else if (exchange.getRequestMethod().equals("DELETE")) {
      statusCode = 204;
    }

    sendResponse(exchange, statusCode, response);
  }

  /**
   * Sets the status code and sends a response.
   */
  final protected void sendResponse(HttpExchange exchange, int statusCode, byte[] response) throws IOException {
    exchange.getResponseHeaders().add("Content-Type", "application/json");
    exchange.sendResponseHeaders(statusCode, response.length);
    exchange.getResponseBody().write(response);
    exchange.close();
  }

  /**
   * Tries to parse the request body as json.
   */
  final protected <T> T getJsonBodyData(HttpExchange exchange, Class<T> type) throws IOException {
    final List<String> contentType = exchange.getRequestHeaders().get("Content-Type");

    if (contentType == null || !contentType.contains("application/json")) {
      throw new IllegalArgumentException("The request is missing a valid json body.");
    }

    byte[] body = exchange.getRequestBody().readAllBytes();
    return Json.instance.parse(new String(body), type);
  }

  /**
   * The wiring for the collection endpoints.
   */
  private void collectionEndpointHandler(HttpExchange exchange) throws IOException {
    try {
      switch (exchange.getRequestMethod()) {
        case "GET":
          getAll(exchange);
          break;
      
        case "POST":
          post(exchange);
          break;
  
        default:
          throw new MethodNotAllowedException();
      }
    }
    catch (HttpException ex) {
      sendResponse(exchange, ex.statusCode, String.format("{\"error\":\"%s\"}", ex.getMessage()).getBytes());
    }
    catch (Exception ex) {
      sendResponse(exchange, 500, String.format("{\"error\":\"Internal server error, try again later.\"}").getBytes());
      ex.printStackTrace();
    }
  }

  /**
   * The wiring for the item endpoints.
   */
  private void itemEndpointHandler(HttpExchange exchange) throws IOException {
    try {
      final String[] pathParams = exchange.getRequestURI().getPath().split("/");
      final int id = Integer.parseInt(pathParams[pathParams.length - 1]);

      switch (exchange.getRequestMethod()) {
        case "GET":
          get(exchange, id);
          break;

        case "PUT":
          put(exchange, id);
          break;

        case "DELETE":
          delete(exchange, id);
          break;

        default:
          throw new MethodNotAllowedException();
      }
    }
    catch (HttpException ex) {
      sendResponse(exchange, ex.statusCode, String.format("{\"error\":\"%s\"}", ex.getMessage()).getBytes());
    }
    catch (Exception ex) {
      sendResponse(exchange, 500, String.format("{\"error\":\"Internal server error, try again later.\"}").getBytes());
      ex.printStackTrace();
    }
  }

}
