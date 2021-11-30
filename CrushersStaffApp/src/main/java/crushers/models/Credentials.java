package crushers.models;

public class Credentials {
  public String email;
  public String password;


  public Credentials(){

  }

  public Credentials(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }
}
