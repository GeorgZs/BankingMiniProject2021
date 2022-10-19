package crushers.common.httpExceptions;

/**
 * The requested resource could not be found.
 */
public class NotFoundException extends HttpException {

  /**
   * Empty constructor for Jackson.
   */
  public NotFoundException() {
    super(404, "Not Found!");
  }

  public NotFoundException(String message) {
    super(404, message);
  }
}
