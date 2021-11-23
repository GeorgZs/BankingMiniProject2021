package crushers.server.httpExceptions;

public class aNotFoundException extends HttpException {
  public aNotFoundException(String message) {
    super(404, message);
  }
}
