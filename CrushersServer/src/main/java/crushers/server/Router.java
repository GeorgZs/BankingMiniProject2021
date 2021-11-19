package crushers.server;

import java.io.IOException;

import com.sun.net.httpserver.*;

/**
 * This is a base router which handles some of the wiring for differentiating between common crud 
 * endpoints. This allows us to overwrite some simplified methods without having to worry about the 
 * wiring itself. It also allows for a base path to be configured under which all the endpoints are 
 * available.
 */
public class Router {

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
    collectionEndpoint.setHandler(exchange -> {
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
      catch (MethodNotAllowedException ex) {
        exchange.sendResponseHeaders(405, 0);
      }
      catch (IllegalArgumentException ex) {
        final int statusCode = 400;
        final byte[] response = String.format("{\"error\":\"%s\"}", ex.getMessage()).getBytes();
        sendResponse(exchange, statusCode, response);
      }
      catch (Exception ex) {
        final int statusCode = 500;
        final byte[] response = String.format("{\"error\":\"Internal server error, try again later.\"}").getBytes();
        sendResponse(exchange, statusCode, response);
        ex.printStackTrace();
      }
    });

    HttpContext itemEndpoint = server.createContext(this.basePath + "/");
    itemEndpoint.setHandler(exchange -> {
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
      catch (MethodNotAllowedException ex) {
        exchange.sendResponseHeaders(405, 0);
      }
      catch (IllegalArgumentException ex) {
        final int statusCode = 400;
        final byte[] response = String.format("{\"error\":\"%s\"}", ex.getMessage()).getBytes();
        sendResponse(exchange, statusCode, response);
      }
      catch (Exception ex) {
        final int statusCode = 500;
        final byte[] response = String.format("{\"error\":\"Internal server error, try again later.\"}").getBytes();
        sendResponse(exchange, statusCode, response);
        ex.printStackTrace();
      }
    });
  }

  final protected void sendResponse(HttpExchange exchange, int statusCode, byte[] response) throws IOException {
    exchange.getResponseHeaders().add("Content-Type", "application/json");
    exchange.sendResponseHeaders(statusCode, response.length);
    exchange.getResponseBody().write(response);
    exchange.close();
  }

}
