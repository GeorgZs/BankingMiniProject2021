package crushers.server.httpExceptions;

public class UnauthorizedException extends HttpException {
  public UnauthorizedException() {
    super(401, "You need to be logged in to do this action.");
  }
}
