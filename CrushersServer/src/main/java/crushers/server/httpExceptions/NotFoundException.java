package crushers.server.httpExceptions;

public class NotFoundException extends HttpException {
  public NotFoundException(String message) {
    super(404, message);
  }
}
