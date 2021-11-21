package crushers.services.ducks;

import java.io.IOException;
import java.util.Collection;

import com.sun.net.httpserver.HttpExchange;

import crushers.models.Duck;
import crushers.server.Router;

/**
 * The router for the duck service, which is responsible for translating http talk to duck service 
 * calls and then translates the results back to http talk again. The service is injected via the
 * constructor.
 */
public class DuckRouter extends Router<Duck> {

  private DuckService duckService;

  public DuckRouter(DuckService duckService) {
    super("/ducks");
    this.duckService = duckService;
  }

  @Override
  protected void get(HttpExchange exchange, int id) throws Exception {
    final Duck responseData = duckService.get(id);
    sendJsonResponse(exchange, responseData);
  }

  @Override
  protected void getAll(HttpExchange exchange) throws Exception {
    final Collection<Duck> responseData = duckService.getAll();
    sendJsonResponse(exchange, responseData);
  }

  @Override
  protected void post(HttpExchange exchange) throws Exception {
    final Duck requestData = getJsonBodyData(exchange, Duck.class);
    final Duck responseData = duckService.create(requestData);
    sendJsonResponse(exchange, responseData);
  }

}
