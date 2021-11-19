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
  protected void get(HttpExchange exchange, int id) throws IOException {
    final int statusCode = 200;

    // temporary json solution as an example for the server
    final String json = duckService.get(id).toString();
    final byte[] response = json.getBytes();

    sendResponse(exchange, statusCode, response);
  }

  @Override
  protected void getAll(HttpExchange exchange) throws IOException {
    final int statusCode = 200;

    // temporary json solution as an example for the server
    final String json = "[" + duckService.getAll().stream().map(duck -> duck.toString()).reduce((a, b) -> a + "," + b).get() + "]";
    final byte[] response = json.getBytes();

    sendResponse(exchange, statusCode, response);
  }

}
