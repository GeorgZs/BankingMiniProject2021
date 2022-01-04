package crushers.common.httpExceptions;

/**
 * The user is not successfully logged in.
 */
public class UnauthorizedException extends HttpException {
  /**
   * Empty constructor for Jackson.
   */
  public UnauthorizedException() {
    super(401, "Log in failed! You are not logged in!");
  }
}
