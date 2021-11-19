package crushers.server;

public class MethodNotAllowedException extends Exception {
  public MethodNotAllowedException() {
    super("The requested http method is not allowed for this endpoint.");
  }  
}
