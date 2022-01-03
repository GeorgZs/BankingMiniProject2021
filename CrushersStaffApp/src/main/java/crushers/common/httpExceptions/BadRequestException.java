package crushers.common.httpExceptions;

/**
 * The request contained faulty data.
 */
public class BadRequestException extends HttpException {
  
  /**
   * Empty constructor for Jackson.
   */
  public BadRequestException() {
    super(400, "Bad Request!");
  }

  public BadRequestException(String message) {
    super(400, message);
  }
}
