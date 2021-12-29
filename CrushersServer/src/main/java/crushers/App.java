package crushers;

import crushers.server.Server;

/**
 * Entry point for the application.
 * Starts server at http://localhost:8080
 */
public class App {
  public static void main(String[] args) {
    try {
      final Server server = new Server(8080);
      server.start();
    }
    catch (Exception es) {
      es.printStackTrace();
    }
  }
}
