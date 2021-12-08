package crushers.datamodels;

public class AuthToken {
  private String token;

  public AuthToken() {
    // empty for jackson
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
  
}
