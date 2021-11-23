package crushers.server.httpExceptions;

public class ForbiddenException extends HttpException {
  public ForbiddenException() {
    super(403, "You do not have the right permissions to do this action.");
  }
}
