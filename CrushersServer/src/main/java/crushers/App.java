package crushers;

import java.io.IOException;

import crushers.server.Server;

/**
 * Entry point for the application.
 */
public class App {
  public static void main(String[] args) throws IOException {
    final Server server = new Server(8080);
    server.start();
  }
}
