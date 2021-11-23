package crushers.server.httpExceptions;

public class BadRequestException extends HttpException {
  public BadRequestException(String message) {
    super(400, message);
  }
}
