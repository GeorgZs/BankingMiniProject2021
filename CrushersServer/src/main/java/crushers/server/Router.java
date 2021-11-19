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
  protected void get(HttpExchange exchange) throws IOException {
    disallowMethod(exchange);
  }

  /**
   * A prewired crud endpoint for getting all items.
   */
  protected void getAll(HttpExchange exchange) throws IOException {
    disallowMethod(exchange);
  }

  /**
   * A prewired crud endpoint for creating a new item.
   */
  protected void post(HttpExchange exchange) throws IOException {
    disallowMethod(exchange);
  }

  /**
   * A prewired crud endpoint for updating an existing item.
   */
  protected void put(HttpExchange exchange) throws IOException {
    disallowMethod(exchange);
  }

  /**
   * A prewired crud endpoint for deleting an existing item.
   */
  protected void delete(HttpExchange exchange) throws IOException {
    disallowMethod(exchange);
  }

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
            disallowMethod(exchange);
            break;
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    });

    HttpContext itemEndpoint = server.createContext(this.basePath + "/");
    itemEndpoint.setHandler(exchange -> {
      try {
        switch (exchange.getRequestMethod()) {
          case "GET":
            get(exchange);
            break;
  
          case "PUT":
            put(exchange);
            break;
  
          case "DELETE":
            delete(exchange);
            break;
  
          default:
            disallowMethod(exchange);
            break;
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    });
  }

  /**
   * Closes the http exchange after responding that the requested http method is not allowed for 
   * this endpoint.
   */
  private void disallowMethod(HttpExchange exchange) throws IOException {
    exchange.sendResponseHeaders(405, 0);
    exchange.close();
  }

}
