package crushers.server.httpExceptions;

public class UnauthorizedException extends HttpException {
  public UnauthorizedException() {
    super(401, "Log in failed! You are not logged in!");
  }
}
