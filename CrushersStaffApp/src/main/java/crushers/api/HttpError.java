package crushers.api;

public class HttpError extends Exception {
  private int statusCode;
  private String error;

  public HttpError() {
    // for jackson - the json thingy

    // when jackson parses a json string, it does the following:
    // 1. it creates an instance of the class it is parsing with the EMPTY CONSTRUCTOR
    // 2. it calls the setters on that instance with the values from the json string
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

  @Override
  public String toString() {
    return "HTTP ERROR! [" + statusCode + "] " + error;
  }
}
