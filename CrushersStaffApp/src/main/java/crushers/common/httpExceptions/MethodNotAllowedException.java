package crushers.common.httpExceptions;

/**
 * The requessted method is not allowed for the requested resource.
 */
public class MethodNotAllowedException extends HttpException {
  /**
   * Empty constructor for Jackson.
   */
  public MethodNotAllowedException() {
    super(405, "The requested http method is not allowed for this endpoint.");
  }  
}
