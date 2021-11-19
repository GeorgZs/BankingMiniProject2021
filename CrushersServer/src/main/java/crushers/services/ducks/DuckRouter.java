package crushers.services.ducks;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import crushers.server.Router;

/**
 * The router for the duck service, which is responsible for translating http talk to duck service 
 * calls and then translates the results back to http talk again. The service is injected via the
 * constructor.
 */
public class DuckRouter extends Router {

  private DuckService duckService;

  public DuckRouter(DuckService duckService) {
    super("/ducks");
    this.duckService = duckService;
  }

  @Override
  protected void get(HttpExchange exchange) throws IOException {
    final String[] pathParams = exchange.getRequestURI().getPath().split("/");
    final int id = Integer.parseInt(pathParams[pathParams.length - 1]);
    byte[] response = new byte[0];
    int statusCode = 200;

    try {
      // temporary json solution as an example for the server
      String json = duckService.get(id).toString();
      response = json.getBytes();
    }
    catch (Exception ex) {
      statusCode = 400;
      response = String.format("{\"error\":\"%s\"}", ex.getMessage()).getBytes();
    }

    exchange.getResponseHeaders().add("Content-Type", "application/json");
    exchange.sendResponseHeaders(statusCode, response.length);
    exchange.getResponseBody().write(response);
    exchange.close();
  }

  @Override
  protected void getAll(HttpExchange exchange) throws IOException {
    byte[] response = new byte[0];
    int statusCode = 200;

    try {
      // temporary json solution as an example for the server
      String json = "[" + duckService.getAll().stream().map(duck -> duck.toString()).reduce((a, b) -> a + "," + b).get() + "]";
      response = json.getBytes();
    }
    catch (Exception ex) {
      statusCode = 400;
      response = String.format("{\"error\":\"%s\"}", ex.getMessage()).getBytes();
    }

    exchange.getResponseHeaders().add("Content-Type", "application/json");
    exchange.sendResponseHeaders(statusCode, response.length);
    exchange.getResponseBody().write(response);
    exchange.close();
  }

}
