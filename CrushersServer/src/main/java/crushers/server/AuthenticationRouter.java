package crushers.server;

import com.sun.net.httpserver.*;

import crushers.common.httpExceptions.HttpException;
import crushers.common.httpExceptions.MethodNotAllowedException;

/**
 * The router for our authenticator.
 */
public class AuthenticationRouter extends Router {
  public AuthenticationRouter() {
    super("/auth");
  }

  /**
   * Adds all the endpoints to the given http server.
   */
  public void addEndpoints(HttpServer server) throws Exception {
    // no super call here, since we don't need the prewired crud endpoints here

    server.createContext(this.basePath + "/login").setHandler((HttpExchange exchange) -> {
      try {
        switch (exchange.getRequestMethod()) {
          case "POST":
            final String token = Authenticator.instance.login(exchange);
            sendResponse(exchange, 200, String.format("{\"token\":\"%s\"}", token).getBytes());
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
    });

    server.createContext(this.basePath + "/logout").setHandler((HttpExchange exchange) -> {
      try {
        switch (exchange.getRequestMethod()) {
          case "POST":
            Authenticator.instance.logout(exchange);
            exchange.sendResponseHeaders(200, 0);
            exchange.close();
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
    });
  }
}
