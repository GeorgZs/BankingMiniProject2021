package crushers.server.httpExceptions;

public class HttpException extends Exception {
  public final int statusCode;
  
  public HttpException(int statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }

}
