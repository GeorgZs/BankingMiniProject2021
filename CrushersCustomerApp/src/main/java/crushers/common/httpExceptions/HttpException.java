package crushers.common.httpExceptions;

/**
 * The base class for all custom server exceptions.
 */
public class HttpException extends Exception {
  public int statusCode;
  public String error;

  /**
   * Empty constructor for Jackson.
   */
  public HttpException() {
    super("Oops, something went wrong!");
    this.statusCode = 500; // Internal server error
    this.error = "Oops, something went wrong!";
  }

  public HttpException(int statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
    this.error = message;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
