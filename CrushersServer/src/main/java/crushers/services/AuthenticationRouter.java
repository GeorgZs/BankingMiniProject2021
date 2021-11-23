package crushers.services;

import java.io.IOException;

import com.sun.net.httpserver.*;

import crushers.server.Authenticator;
import crushers.server.httpExceptions.HttpException;
import crushers.server.httpExceptions.MethodNotAllowedException;

public class AuthenticationRouter {

  /**
   * Adds all the endpoints to the given http server. It can be overwritten to add more customised 
   * endpoints in addition to the prewired crud endpoints.
   */
  public void addEndpoints(HttpServer server) throws Exception {
    server.createContext("/auth/login").setHandler((HttpExchange exchange) -> {
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

    server.createContext("/auth/logout").setHandler((HttpExchange exchange) -> {
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
  
    /**
   * Sets the status code and sends a response.
   */
  final protected void sendResponse(HttpExchange exchange, int statusCode, byte[] response) throws IOException {
    exchange.getResponseHeaders().add("Content-Type", "application/json");
    exchange.sendResponseHeaders(statusCode, response.length);
    exchange.getResponseBody().write(response);
    exchange.close();
  }
  
}
