package crushers.services;

import com.sun.net.httpserver.*;

import crushers.models.ResetPasswordClass;
import crushers.server.Authenticator;
import crushers.server.Router;
import crushers.server.httpExceptions.HttpException;
import crushers.server.httpExceptions.MethodNotAllowedException;

public class AuthenticationRouter extends Router<ResetPasswordClass> {
  public AuthenticationRouter() {
    super("/auth");
  }

  /**
   * Adds all the endpoints to the given http server. It can be overwritten to add more customised 
   * endpoints in addition to the prewired crud endpoints.
   */
  public void addEndpoints(HttpServer server) throws Exception {
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

    server.createContext(this.basePath + "/password").setHandler((HttpExchange exchange) -> {
      try {
        switch (exchange.getRequestMethod()) {
          case "PUT":
            resetPassword(exchange);
            sendResponse(exchange, 200, String.format("{\"Password successfully reset\"}").getBytes());
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

  final protected void resetPassword(HttpExchange exchange) throws Exception{
    ResetPasswordClass resetPassword = getJsonBodyData(exchange, ResetPasswordClass.class);
    Authenticator.instance.resetPassword(resetPassword);
  }
  
}
