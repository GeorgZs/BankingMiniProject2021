package crushers.common.httpExceptions;

/**
 * The requested action is forbidden for the authenticated user.
 */
public class ForbiddenException extends HttpException {
  /**
   * Empty constructor for Jackson.
   */
  public ForbiddenException() {
    super(403, "You do not have the right permissions to do this action.");
  }
}
