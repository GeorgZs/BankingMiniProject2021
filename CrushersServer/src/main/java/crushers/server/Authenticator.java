package crushers.server;

import com.sun.net.httpserver.HttpExchange;

import java.time.LocalDateTime;
import java.util.*;

import crushers.models.Credentials;
import crushers.models.ResetPasswordClass;
import crushers.models.users.*;
import crushers.server.httpExceptions.*;
import crushers.services.staff.JsonClerkStorage;
import crushers.utils.Json;
import crushers.utils.Security;

public class Authenticator {
  public final static Authenticator instance = new Authenticator();
  private final Security security = new Security();

  private final Map<String, User> users = new LinkedHashMap<>();
  private final Map<String, User> activeTokens = new LinkedHashMap<>();

  private Authenticator() {
    // singleton pattern - private constructor
  }

  public void register(User user) throws Exception {
    if (users.containsKey(user.getEmail())) {
      throw new BadRequestException("This email is already registered to a user.");
    }

    // Optionally encrypt/hash the password

    users.put(user.getEmail(), user);
  }


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

    User user = users.get(credentials.email);

    if (user == null) {
      throw new UnauthorizedException();
    }

    // encrypt the credential password as well if you encrypt/hash the password
    //.//.// for resetting password it is commented out
    //credentials.password = security.passwordEncryption(credentials.password, "MD5");

    if (!user.getPassword().equals(credentials.password)) {
      throw new UnauthorizedException();
    }

    String token = generateToken();
    activeTokens.put(token, user);
    return token;
  }

  public void logout(HttpExchange exchange) throws Exception {
    final String authorization = exchange.getRequestHeaders().getFirst("Authorization");

    if (authorization == null || !authorization.startsWith("Bearer ")) {
      throw new UnauthorizedException();
    }

    String token = authorization.split(" ")[1];
    activeTokens.remove(token);
  }


  public User authUser(HttpExchange exchange) throws Exception {
    final String token = getToken(exchange);
    final User user = activeTokens.get(token);

    if (user == null) {
      throw new UnauthorizedException();
    }

    return user;
  }

  public Customer authCustomer(HttpExchange exchange) throws Exception {
    final String token = getToken(exchange);
    final User user = activeTokens.get(token);

    if (user == null) {
      throw new UnauthorizedException();
    }

    if (!(user instanceof Customer)) {
      throw new ForbiddenException();
    }

    return (Customer) user;
  }

  public Clerk authClerk(HttpExchange exchange) throws Exception {
    final String token = getToken(exchange);
    final User user = activeTokens.get(token);

    if (user == null) {
      throw new UnauthorizedException();
    }

    if (!(user instanceof Clerk)) {
      throw new ForbiddenException();
    }

    return (Clerk) user;
  }

  public Manager authManager(HttpExchange exchange) throws Exception {
    final String token = getToken(exchange);
    final User user = activeTokens.get(token);

    if (user == null) {
      throw new UnauthorizedException();
    }

    if (!(user instanceof Manager)) {
      throw new ForbiddenException();
    }

    return (Manager) user;
  }

  private String getToken(HttpExchange exchange) throws Exception {
    final String authorization = exchange.getRequestHeaders().getFirst("Authorization");

    if (authorization == null || !authorization.startsWith("Bearer ")) {
      throw new UnauthorizedException();
    }

    return authorization.split(" ")[1];
  }

  private String generateToken() {
    return LocalDateTime.now().toString() + "#" + (int)(1000 + Math.random() * 9000);
  }


  public void resetPassword(ResetPasswordClass resetPasswordClass) throws Exception{
    for(String email : users.keySet()){
      if(resetPasswordClass.getEmail().equals(email)){
        if(Arrays.equals(resetPasswordClass.getSecurityQuestions(), resetPasswordClass.getSecurityQuestions())){
          User user = users.get(email);
          users.remove(email);
          user.setPassword(resetPasswordClass.getPassword());
          register(user);
        }
      }
    }
  }
}
