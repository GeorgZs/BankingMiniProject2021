package crushers.server;

import com.sun.net.httpserver.HttpExchange;

import java.time.LocalDateTime;
import java.util.*;

import crushers.common.httpExceptions.*;
import crushers.common.utils.Json;
import crushers.common.models.*;

/**
 * Singleton class for authentication related functionalities.
 */
public class Authenticator {
  public final static Authenticator instance = new Authenticator();
  private final Map<String, User> users = new LinkedHashMap<>();
  private final Map<String, User> activeTokens = new LinkedHashMap<>();

  private Authenticator() {
    // singleton pattern
  }

  /**
   * Register a user to the authenticator for logging in.
   */
  public void register(User user) throws Exception {
    if (users.containsKey(user.getEmail())) {
      throw new BadRequestException("This email is already registered to a user.");
    }

    users.put(user.getEmail(), user);
  }

  /**
   * Try to log a user in with the sent credentials.
   * @return the generated token for the user.
   * @throws Exception if the credentials are invalid.
   */
  public String login(HttpExchange exchange) throws Exception {
    final List<String> contentType = exchange.getRequestHeaders().get("Content-Type");

    if (contentType == null || !contentType.contains("application/json")) {
      throw new BadRequestException("The request is missing a valid json body.");
    }

    byte[] body = exchange.getRequestBody().readAllBytes();
    Credentials credentials = Json.instance.parse(new String(body), Credentials.class);

    if (credentials == null) {
      throw new UnauthorizedException();
    }

    User user = users.get(credentials.getEmail());

    if (user == null) {
      throw new UnauthorizedException();
    }

    if (!user.getPassword().equals(credentials.getPassword())) {
      throw new UnauthorizedException();
    }

    String token = generateToken();
    activeTokens.put(token, user);
    return token;
  }

  /**
   * Logs a user out and invalidates their auth token.
   */
  public void logout(HttpExchange exchange) throws Exception {
    final String authorization = exchange.getRequestHeaders().getFirst("Authorization");

    if (authorization == null || !authorization.startsWith("Bearer ")) {
      throw new UnauthorizedException();
    }

    String token = authorization.split(" ")[1];
    activeTokens.remove(token);
  }

  /**
   * Validate user authentication.
   * @return the logged in user.
   * @throws Exception if the token is invalid.
   */
  public User authUser(HttpExchange exchange) throws Exception {
    final String token = getToken(exchange);
    final User user = activeTokens.get(token);

    if (user == null) {
      throw new UnauthorizedException();
    }

    return user;
  }

  /**
   * Validate customer authentication.
   * @return the logged in customer.
   * @throws Exception if the token is invalid or belongs to a non-customer user.
   */
  public User authCustomer(HttpExchange exchange) throws Exception {
    final String token = getToken(exchange);
    final User user = activeTokens.get(token);

    if (user == null) {
      throw new UnauthorizedException();
    }

    if (!user.isCustomer()) {
      throw new ForbiddenException();
    }

    return user;
  }

  /**
   * Validate clerk authentication.
   * @return the logged in clerk.
   * @throws Exception if the token is invalid or belongs to a non-clerk user.
   */
  public User authClerk(HttpExchange exchange) throws Exception {
    final String token = getToken(exchange);
    final User user = activeTokens.get(token);

    if (user == null) {
      throw new UnauthorizedException();
    }

    if (!user.isClerk()) {
      throw new ForbiddenException();
    }

    return user;
  }

  /**
   * Validate manager authentication.
   * @return the logged in manager.
   * @throws Exception if the token is invalid or belongs to a non-manager user.
   */
  public User authManager(HttpExchange exchange) throws Exception {
    final String token = getToken(exchange);
    final User user = activeTokens.get(token);

    if (user == null) {
      throw new UnauthorizedException();
    }

    if (!user.isManager()) {
      throw new ForbiddenException();
    }

    return user;
  }

  /**
   * Gets the auth token from a http request.
   * @return the token.
   * @throws Exception if there is no token.
   */
  private String getToken(HttpExchange exchange) throws Exception {
    final String authorization = exchange.getRequestHeaders().getFirst("Authorization");

    if (authorization == null || !authorization.startsWith("Bearer ")) {
      throw new UnauthorizedException();
    }

    return authorization.split(" ")[1];
  }

  /**
   * Generates a unique token which can be used to identify a logged in user.
   */
  private String generateToken() {
    return LocalDateTime.now().toString() + "#" + (int)(1000 + Math.random() * 9000);
  }
}
