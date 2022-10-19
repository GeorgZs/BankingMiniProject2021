package crushers;

import crushers.server.Server;

/**
 * Entry point for the application.
 * Starts the server at http://localhost:8080
 */
public class App {
  public static void main(String[] args) {
    try {
      final Server server = new Server(8080);
      server.start();
    }
    catch (Exception ex) {
      System.err.println("The server seems to have crashed, please restart the server.");
      System.err.println("If this keeps happening feel free to contact us and we will help you out.");
      System.err.println("- Team 2, Crushers (support@crushers.se)");
      System.err.println();
      System.err.println("Information about the crash for our developers:");
      ex.printStackTrace();
    }
  }
}
