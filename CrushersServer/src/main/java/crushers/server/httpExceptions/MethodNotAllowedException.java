package crushers.server.httpExceptions;

public class MethodNotAllowedException extends HttpException {
  public MethodNotAllowedException() {
    super(405, "The requested http method is not allowed for this endpoint.");
  }  
}
